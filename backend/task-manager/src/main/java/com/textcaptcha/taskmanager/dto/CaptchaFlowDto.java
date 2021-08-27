package com.textcaptcha.taskmanager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CaptchaFlowDto {

    private final TaskInstanceDto task;
    private final Boolean flowComplete;

    public CaptchaFlowDto(TaskInstanceDto task) {
        this.task = task;
        this.flowComplete = null;
    }

    public CaptchaFlowDto(Boolean flowComplete) {
        this.flowComplete = flowComplete;
        this.task = null;
    }

    public TaskInstanceDto getTask() {
        return task;
    }

    public Boolean getFlowComplete() {
        return flowComplete;
    }
}
