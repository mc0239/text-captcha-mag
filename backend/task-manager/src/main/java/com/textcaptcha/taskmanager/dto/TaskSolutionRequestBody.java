package com.textcaptcha.taskmanager.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.textcaptcha.data.model.task.TaskType;

import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "taskType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = NerTaskSolutionRequestBody.class, name = TaskType.Name.NER),
        @JsonSubTypes.Type(value = CorefTaskSolutionRequestBody.class, name = TaskType.Name.COREF)
})
public abstract class TaskSolutionRequestBody<C> {

    private final TaskType taskType;
    private final UUID id;

    protected TaskSolutionRequestBody(TaskType taskType, UUID instanceId) {
        this.taskType = taskType;
        this.id = instanceId;
    }

    public UUID getId() {
        return id;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public abstract C getContent();
}
