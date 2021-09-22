package com.textcaptcha.taskmanager.dto.link;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CaptchaLinkDto {

    protected final UUID linkId;
    protected final Boolean complete;

    public CaptchaLinkDto(UUID linkId, Boolean complete) {
        this.linkId = linkId;
        this.complete = complete;
    }

    public UUID getLinkId() {
        return linkId;
    }

    public Boolean getComplete() {
        return complete;
    }

}
