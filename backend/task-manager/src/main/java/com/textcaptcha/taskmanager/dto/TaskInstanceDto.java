package com.textcaptcha.taskmanager.dto;

import com.textcaptcha.data.model.task.CaptchaTask;
import com.textcaptcha.data.model.task.TaskType;
import com.textcaptcha.data.model.task.content.CaptchaTaskContent;

import java.util.UUID;

public abstract class TaskInstanceDto<T extends CaptchaTask<? extends CaptchaTaskContent>, C> {

    private TaskType taskType;
    private UUID id;

    public TaskInstanceDto(TaskType type, UUID taskInstanceId) {
        this.taskType = type;
        this.id = taskInstanceId;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public abstract C getContent();

}
