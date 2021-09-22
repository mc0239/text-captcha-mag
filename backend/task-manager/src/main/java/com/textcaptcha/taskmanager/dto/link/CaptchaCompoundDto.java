package com.textcaptcha.taskmanager.dto.link;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.textcaptcha.taskmanager.dto.TaskInstanceDto;

import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CaptchaCompoundDto extends CaptchaLinkDto {

    private final List<TaskInstanceDto> tasks;

    public CaptchaCompoundDto(List<TaskInstanceDto> tasks) {
        super(null, null);
        this.tasks = tasks;
    }

    public CaptchaCompoundDto(UUID linkId, Boolean complete) {
        super(linkId, complete);
        this.tasks = null;
    }

    public List<TaskInstanceDto> getTasks() {
        return tasks;
    }

}
