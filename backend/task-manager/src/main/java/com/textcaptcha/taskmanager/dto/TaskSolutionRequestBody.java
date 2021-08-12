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
public abstract class TaskSolutionRequestBody {

    private TaskType taskType;

    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }
}
