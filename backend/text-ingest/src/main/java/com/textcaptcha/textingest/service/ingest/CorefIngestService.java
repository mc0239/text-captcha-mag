package com.textcaptcha.textingest.service.ingest;

import com.textcaptcha.annotation.Loggable;
import com.textcaptcha.textingest.dto.ClasslaApiResponse;
import com.textcaptcha.textingest.exception.AnnotatorException;
import com.textcaptcha.textingest.exception.IngestException;
import com.textcaptcha.textingest.pojo.ReceivedArticle;
import com.textcaptcha.textingest.service.annotator.CorefAnnotatorService;
import com.textcaptcha.textingest.service.annotator.ClasslaAnnotatorService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CorefIngestService implements IngestService {

    @Loggable
    private Logger logger;

    private final ClasslaAnnotatorService classlaAnnotatorService;
    private final CorefAnnotatorService corefAnnotatorService;

    public CorefIngestService(
            ClasslaAnnotatorService classlaAnnotatorService,
            CorefAnnotatorService corefAnnotatorService
    ) {
        this.classlaAnnotatorService = classlaAnnotatorService;
        this.corefAnnotatorService = corefAnnotatorService;
    }

    @Override
    public void ingest(ReceivedArticle article) throws IngestException {
        try {
            List<ClasslaApiResponse.Sentence> sentences = classlaAnnotatorService.annotate(article.getText());

            // TODO these tokens now go to coref service
            corefAnnotatorService.annotate(sentences);
        } catch (AnnotatorException e) {
            throw new IngestException(e);
        }
    }

}
