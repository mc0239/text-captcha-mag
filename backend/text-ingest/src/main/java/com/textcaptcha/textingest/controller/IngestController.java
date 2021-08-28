package com.textcaptcha.textingest.controller;

import com.textcaptcha.annotation.Loggable;
import com.textcaptcha.data.model.IngestedArticle;
import com.textcaptcha.data.repository.IngestedArticleRepository;
import com.textcaptcha.textingest.dto.IngestRequestBody;
import com.textcaptcha.textingest.dto.IngestResultDto;
import com.textcaptcha.textingest.exception.IngestException;
import com.textcaptcha.textingest.pojo.ReceivedArticle;
import com.textcaptcha.textingest.service.ingest.CorefIngestService;
import com.textcaptcha.textingest.service.ingest.NerIngestService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/ingest")
public class IngestController {

    @Loggable
    private Logger logger;

    private final IngestedArticleRepository articleRepository;

    private final NerIngestService nerIngestService;
    private final CorefIngestService corefIngestService;

    @Autowired
    public IngestController(
            IngestedArticleRepository articleRepository,
            NerIngestService nerIngestService,
            CorefIngestService corefIngestService
    ) {
        this.articleRepository = articleRepository;
        this.nerIngestService = nerIngestService;
        this.corefIngestService = corefIngestService;
    }

    @Operation(summary = "Ingest article text", description = "Processes received text and generates tasks for it. " +
            "Returns data, used later on for making CAPTCHA task requests for the received text.")
    @PostMapping
    public IngestResultDto ingestText(@RequestBody IngestRequestBody body) {
        logger.debug("Received ingest request: " + body.toString());

        String decodedText = URLDecoder.decode(body.getArticleText(), StandardCharsets.UTF_8);
        ReceivedArticle article = new ReceivedArticle(body.getArticleUrl(), decodedText);

        boolean articleAlreadyIngested = articleRepository.existsByArticleUrlHashAndArticleTextHash(article.getUrlHash(), article.getTextHash());
        if (articleAlreadyIngested) {
            logger.debug("Article " + article + " already ingested.");
            return new IngestResultDto(article.getUrlHash(), article.getTextHash());
        }

        try {
            nerIngestService.ingest(article);
            corefIngestService.ingest(article);
        } catch (IngestException ex) {
            logger.error("Article ingest failed. ", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Article ingest failed.");
        }

        // Both ingests were successful, write to ingested articles.
        IngestedArticle ingestedArticle = new IngestedArticle();
        ingestedArticle.setArticleUrl(article.getUrl());
        ingestedArticle.setArticleText(article.getText());
        ingestedArticle.setArticleHashes(article.getUrlHash(), article.getTextHash());
        articleRepository.save(ingestedArticle);

        return new IngestResultDto(article.getUrlHash(), article.getTextHash());
    }

}
