package com.textcaptcha.taskmanager.controller;

import com.textcaptcha.taskmanager.config.TaskManagerConfigProvider;
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

    private final Logger logger = LoggerFactory.getLogger(HealthController.class);

    private final TaskManagerConfigProvider config;

    @Autowired
    public HealthController(TaskManagerConfigProvider config) {
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
