package com.textcaptcha.textingest.service.ingest;

import com.textcaptcha.textingest.exception.AnnotatorException;
import com.textcaptcha.textingest.exception.IngestException;
import com.textcaptcha.textingest.pojo.ReceivedArticle;
import com.textcaptcha.textingest.pojo.annotator.NerAnnotatedToken;
import com.textcaptcha.textingest.service.annotator.NerAnnotatorService;
import com.textcaptcha.textingest.service.taskgen.NerTaskGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NerIngestService implements IngestService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final NerAnnotatorService annotatorService;
    private final NerTaskGeneratorService taskGeneratorService;

    @Autowired
    public NerIngestService(
            NerAnnotatorService annotatorService,
            NerTaskGeneratorService taskGeneratorService
    ) {
        this.annotatorService = annotatorService;
        this.taskGeneratorService = taskGeneratorService;
    }

    @Override
    public void ingest(ReceivedArticle article) throws IngestException {
        if (taskGeneratorService.areTasksGenerated(article)) {
            logger.debug("Tasks for " + article + " already generated.");
            return;
        }

        try {
            List<NerAnnotatedToken> tokens = annotatorService.annotate(article.getText());
            int generatedTaskCount = taskGeneratorService.generateTasks(article, tokens);
            logger.debug("Generated " + generatedTaskCount + " tasks for " + article + ".");

        } catch (AnnotatorException e) {
            throw new IngestException(e);
        }
    }

}
