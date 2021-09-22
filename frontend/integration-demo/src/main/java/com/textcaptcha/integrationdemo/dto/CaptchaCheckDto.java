package com.textcaptcha.integrationdemo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CaptchaCheckDto {

    private UUID linkId;
    private Boolean complete;

    public UUID getLinkId() {
        return linkId;
    }

    public void setLinkId(UUID linkId) {
        this.linkId = linkId;
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    public boolean isComplete() {
        return getComplete() != null && getComplete();
    }

}
