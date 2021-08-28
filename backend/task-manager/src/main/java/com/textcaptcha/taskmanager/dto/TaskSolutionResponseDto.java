package com.textcaptcha.taskmanager.dto;

public class TaskSolutionResponseDto {

    private final String content;

    public TaskSolutionResponseDto(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
