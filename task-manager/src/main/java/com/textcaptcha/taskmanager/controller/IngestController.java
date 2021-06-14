package com.textcaptcha.taskmanager.controller;

import com.textcaptcha.taskmanager.dto.IngestRequestBody;
import com.textcaptcha.taskmanager.dto.IngestResultDto;
import com.textcaptcha.taskmanager.model.AnnotatedToken;
import com.textcaptcha.taskmanager.service.NerAnnotatorService;
import com.textcaptcha.taskmanager.service.NerTaskGeneratorService;
import com.textcaptcha.taskmanager.util.HashUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/ingest")
public class IngestController {

    private final Logger logger = LoggerFactory.getLogger(IngestController.class);

    private final NerAnnotatorService nerAnnotatorService;
    private final NerTaskGeneratorService nerTaskGeneratorService;

    @Autowired
    public IngestController(NerAnnotatorService nerAnnotatorService, NerTaskGeneratorService nerTaskGeneratorService) {
        this.nerAnnotatorService = nerAnnotatorService;
        this.nerTaskGeneratorService = nerTaskGeneratorService;
    }

    @Operation(summary = "Ingest article text", description = "Processes received text and generates tasks for it. " +
            "Returns data, used later on for making CAPTCHA task requests for the received text.")
    @PostMapping
    public IngestResultDto ingestText(@RequestBody IngestRequestBody body) {
        logger.debug("Received ingest request: " + body.toString());

        String articleUrlHash = HashUtils.SHA256(body.getArticleUrl());
        String articleTextHash = HashUtils.SHA256(body.getArticleText());

        // TODO this is NER-specific.

        if (nerTaskGeneratorService.areTasksGenerated(articleUrlHash, articleTextHash)) {
            logger.debug("Tasks for " + body.getArticleUrl() + " (" + articleUrlHash + ", " + articleTextHash + ") already generated.");
            return new IngestResultDto(articleUrlHash, articleTextHash);
        }

        String decodedText = URLDecoder.decode(body.getArticleText(), StandardCharsets.UTF_8);
        List<AnnotatedToken> tokens = nerAnnotatorService.annotate(decodedText);
        int generatedTaskCount = nerTaskGeneratorService.generateTasks(body.getArticleUrl(), articleUrlHash, articleTextHash, tokens);

        logger.debug("Generated " + generatedTaskCount + " tasks for " + body.getArticleUrl() + " (" + articleUrlHash + ", " + articleTextHash + ").");

        return new IngestResultDto(articleUrlHash, articleTextHash);
    }

}
