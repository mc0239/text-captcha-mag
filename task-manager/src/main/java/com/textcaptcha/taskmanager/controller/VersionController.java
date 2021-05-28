package com.textcaptcha.taskmanager.controller;

import com.textcaptcha.taskmanager.dto.VersionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/version")
public class VersionController {

    // If application fails to start because "bean of type ...BuildProperties could not be found", try running
    // maven package task manually and then running the application.

    private final BuildProperties buildProperties;

    @Autowired
    public VersionController(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @GetMapping
    public VersionDto getVersion() {
        return new VersionDto(buildProperties.getVersion());
    }

}
