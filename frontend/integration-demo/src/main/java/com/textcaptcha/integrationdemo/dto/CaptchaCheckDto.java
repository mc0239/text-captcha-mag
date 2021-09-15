package com.textcaptcha.integrationdemo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CaptchaCheckDto {

    private UUID flowId;
    private Boolean flowComplete;

    public CaptchaCheckDto() {
    }

    public UUID getFlowId() {
        return flowId;
    }

    public void setFlowId(UUID flowId) {
        this.flowId = flowId;
    }

    public Boolean getFlowComplete() {
        return flowComplete;
    }

    public void setFlowComplete(Boolean flowComplete) {
        this.flowComplete = flowComplete;
    }

}
