package com.textcaptcha.taskmanager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CaptchaFlowDto {

    private final TaskInstanceDto task;
    private final UUID flowId;
    private final Boolean flowComplete;

    public CaptchaFlowDto(TaskInstanceDto task) {
        this.task = task;
        this.flowId = null;
        this.flowComplete = null;
    }

    public CaptchaFlowDto(UUID flowId, Boolean flowComplete) {
        this.flowId = flowId;
        this.flowComplete = flowComplete;
        this.task = null;
    }

    public TaskInstanceDto getTask() {
        return task;
    }

    public UUID getFlowId() {
        return flowId;
    }

    public Boolean getFlowComplete() {
        return flowComplete;
    }
}
