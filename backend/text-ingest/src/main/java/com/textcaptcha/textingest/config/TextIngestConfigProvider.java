package com.textcaptcha.textingest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@Component
@ApplicationScope
public class TextIngestConfigProvider {

    private static final String PREFIX = "text-ingest";

    private static final String NER_URL = PREFIX + ".ner-url";
    private static final String CLASSLA_URL = PREFIX + ".classla-url";
    private static final String COREF_URL = PREFIX + ".coref-url";

    private final Environment environment;

    @Autowired
    public TextIngestConfigProvider(Environment environment) {
        this.environment = environment;
    }

    public String getNerUrl() {
        return environment.getProperty(NER_URL);
    }

    public String getClasslaUrl() {
        return environment.getProperty(CLASSLA_URL);
    }

    public String getCorefUrl() {
        return environment.getProperty(COREF_URL);
    }
}
