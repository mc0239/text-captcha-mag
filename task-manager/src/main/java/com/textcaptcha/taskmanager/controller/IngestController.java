package com.textcaptcha.taskmanager.controller;

import com.textcaptcha.taskmanager.dto.IngestRequestBody;
import com.textcaptcha.taskmanager.model.AnnotatedToken;
import com.textcaptcha.taskmanager.service.NerAnnotatorService;
import com.textcaptcha.taskmanager.service.NerTaskGeneratorService;
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

    @PostMapping
    public void ingestText(@RequestBody IngestRequestBody body) {
        logger.debug("Received ingest request: " + body.toString());

        // TODO this is NER-specific.

        if (nerTaskGeneratorService.areTasksGenerated(body.getArticleUrl(), body.getArticleUid())) {
            logger.debug("Tasks for " + body.getArticleUrl() + " (" + body.getArticleUid() + ") already generated.");
            return;
        }

        String decodedText = URLDecoder.decode(body.getText(), StandardCharsets.UTF_8);
        List<AnnotatedToken> tokens = nerAnnotatorService.annotate(decodedText);
        int generatedTaskCount = nerTaskGeneratorService.generateTasks(body.getArticleUrl(), body.getArticleUid(), tokens);

        logger.debug("Generated " + generatedTaskCount + " tasks for " + body.getArticleUrl() + " (" + body.getArticleUid() + ").");
    }

}
