package com.textcaptcha.textingest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@Component
@ApplicationScope
public class TextIngestConfigProvider {

    private static final String PREFIX = "task-manager";

    private static final String NER_URL = PREFIX + ".ner-api";

    private final Environment environment;

    @Autowired
    public TextIngestConfigProvider(Environment environment) {
        this.environment = environment;
    }

    public String getNerUrl() {
        return environment.getProperty("task-manager.ner-api");
    }

    public String getClasslaUrl() {
        return environment.getProperty("task-manager.classla-api");
    }

}
