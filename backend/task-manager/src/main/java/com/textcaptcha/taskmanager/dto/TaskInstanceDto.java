package com.textcaptcha.taskmanager.dto;

import com.textcaptcha.data.model.task.NerCaptchaTask;
import com.textcaptcha.data.pojo.AnnotatedToken;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TaskInstanceDto {

    private UUID id;
    private List<String> words;

    public TaskInstanceDto(UUID taskInstanceId, NerCaptchaTask captchaTask) {
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
