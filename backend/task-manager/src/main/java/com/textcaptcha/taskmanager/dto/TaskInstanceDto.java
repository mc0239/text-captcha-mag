package com.textcaptcha.taskmanager.dto;

import com.textcaptcha.data.model.task.CaptchaTask;
import com.textcaptcha.data.model.task.CorefCaptchaTask;
import com.textcaptcha.data.model.task.NerCaptchaTask;
import com.textcaptcha.data.model.task.TaskType;
import com.textcaptcha.taskmanager.exception.InvalidTaskTypeException;

import java.util.UUID;

public abstract class TaskInstanceDto<C> {

    private final TaskType taskType;
    private UUID id;

    public TaskInstanceDto(TaskType type, UUID taskInstanceId) {
        this.taskType = type;
        this.id = taskInstanceId;
    }

    public static TaskInstanceDto fromIssuedTaskInstance(UUID taskInstanceId, CaptchaTask captchaTask) {
        TaskType taskType = captchaTask.getTaskType();
        switch (taskType) {
            case NER:
                return new NerTaskInstanceDto(taskInstanceId, (NerCaptchaTask) captchaTask);
            case COREF:
                return new CorefTaskInstanceDto(taskInstanceId, (CorefCaptchaTask) captchaTask);
            default:
                // TaskType is null?
                throw new InvalidTaskTypeException(new RuntimeException("TaskType is " + taskType));
        }
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
