package com.textcaptcha.textingest.controller;

import com.textcaptcha.annotation.Loggable;
import com.textcaptcha.textingest.config.TextIngestConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthController {

    @Loggable
    private Logger logger;

    private final TextIngestConfigProvider config;

    @Autowired
    public HealthController(TextIngestConfigProvider config) {
        this.config = config;
    }

    @GetMapping
    public Map<String, String> checkServices() {
        HashMap<String, String> health = new HashMap<>();

        try {
            ResponseEntity<String> restResponse = new RestTemplate()
                    .postForEntity(config.getNerUrl() + "/predict/ner", "", String.class);
            health.put(config.getNerUrl(), String.valueOf(restResponse.getStatusCode()));
        } catch (ResourceAccessException e) {
            logger.warn("Health check of " + config.getNerUrl() + " resulted in " + e.getMessage() + ".");
            health.put(config.getNerUrl(), "Error: Resource not available");
        }

        return health;
    }

}
