package com.textcaptcha.taskmanager.dto;

import com.textcaptcha.taskmanager.model.AnnotatedToken;
import com.textcaptcha.taskmanager.model.CaptchaTask;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TaskInstanceDto {

    private UUID id;
    private List<String> words;

    public TaskInstanceDto(UUID taskInstanceId, CaptchaTask captchaTask) {
        this.id = taskInstanceId;
        this.words = captchaTask.getTokens().stream().map(AnnotatedToken::getWord).collect(Collectors.toList());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }

}
