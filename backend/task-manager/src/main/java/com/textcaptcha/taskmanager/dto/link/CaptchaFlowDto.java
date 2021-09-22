package com.textcaptcha.taskmanager.dto.link;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.textcaptcha.taskmanager.dto.TaskInstanceDto;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CaptchaFlowDto extends CaptchaLinkDto {

    private final TaskInstanceDto task;

    public CaptchaFlowDto(TaskInstanceDto task) {
        super(null, null);
        this.task = task;
    }

    public CaptchaFlowDto(UUID linkId, Boolean complete) {
        super(linkId, complete);
        this.task = null;
    }

    public TaskInstanceDto getTask() {
        return task;
    }

}
