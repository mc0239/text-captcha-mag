package com.textcaptcha.textingest.service.annotator;

import com.textcaptcha.textingest.config.TextIngestConfigProvider;
import com.textcaptcha.textingest.dto.ClasslaApiResponse;
import com.textcaptcha.textingest.exception.AnnotatorException;
import com.textcaptcha.textingest.service.annotator.AnnotatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class CorefAnnotatorService implements AnnotatorService<List<ClasslaApiResponse.Sentence>, Integer> {

    private final TextIngestConfigProvider config;
    private final RestTemplate rest;

    @Autowired
    public CorefAnnotatorService(TextIngestConfigProvider config) {
        this.config = config;

        rest = new RestTemplate();
        rest.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

    @Override
    public Integer annotate(List<ClasslaApiResponse.Sentence> sentences) throws AnnotatorException {
        // TODO
        return 0;
    }

}
