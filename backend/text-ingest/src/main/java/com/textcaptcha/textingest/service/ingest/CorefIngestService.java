package com.textcaptcha.textingest.service.ingest;

import com.textcaptcha.annotation.Loggable;
import com.textcaptcha.textingest.dto.ClasslaApiResponse;
import com.textcaptcha.textingest.exception.AnnotatorException;
import com.textcaptcha.textingest.exception.IngestException;
import com.textcaptcha.textingest.pojo.ReceivedArticle;
import com.textcaptcha.textingest.pojo.annotator.CorefAnnotatedToken;
import com.textcaptcha.textingest.service.annotator.ClasslaAnnotatorService;
import com.textcaptcha.textingest.service.annotator.CorefAnnotatorService;
import com.textcaptcha.textingest.service.taskgen.CorefTaskGeneratorService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CorefIngestService implements IngestService {

    @Loggable
    private Logger logger;

    private final ClasslaAnnotatorService classlaAnnotatorService;
    private final CorefAnnotatorService corefAnnotatorService;

    private final CorefTaskGeneratorService taskGeneratorService;

    public CorefIngestService(
            ClasslaAnnotatorService classlaAnnotatorService,
            CorefAnnotatorService corefAnnotatorService,
            CorefTaskGeneratorService taskGeneratorService
    ) {
        this.classlaAnnotatorService = classlaAnnotatorService;
        this.corefAnnotatorService = corefAnnotatorService;
        this.taskGeneratorService = taskGeneratorService;
    }

    @Override
    public void ingest(ReceivedArticle article) throws IngestException {
        try {
            ClasslaApiResponse sentences = classlaAnnotatorService.annotate(article.getText());
            List<CorefAnnotatedToken> corefs = corefAnnotatorService.annotate(sentences);

            int generatedTaskCount = taskGeneratorService.generateTasks(article, corefs);
            logger.debug("Generated " + generatedTaskCount + " tasks for " + article + ".");
        } catch (AnnotatorException e) {
            throw new IngestException(e);
        }
    }

}
