package com.textcaptcha.textingest.controller;

import com.textcaptcha.annotation.Loggable;
import com.textcaptcha.textingest.config.TextIngestConfigProvider;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
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

        health.put(config.getNerUrl(), getServiceStatus(config.getNerUrl(), "/predict/ner", "nič"));
        health.put(config.getClasslaUrl(), getServiceStatus(config.getClasslaUrl(), "/annotate", "nič"));
        health.put(config.getCorefUrl(), getServiceStatus(config.getCorefUrl(), "/predict/coref", "{'sentences':[{'tokens':[{'text':'nič','mention':0}]}]}"));

        return health;
    }

    private String getServiceStatus(String serviceUrl, String servicePath, String requestBody) {
        try {
            ResponseEntity<String> restResponse = new RestTemplate()
                    .postForEntity(serviceUrl + servicePath, requestBody, String.class);
            return String.valueOf(restResponse.getStatusCodeValue());
        } catch (RestClientResponseException e) {
            logger.warn("Health check of " + serviceUrl + servicePath + " resulted in " + e.getMessage() + ".");
            return String.valueOf(e.getRawStatusCode());
        } catch (ResourceAccessException e) {
            logger.warn("Health check of " + serviceUrl + servicePath + " resulted in " + e.getMessage() + ".");
            return "n/a";
        }
    }

}
