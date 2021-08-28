package com.textcaptcha.taskmanager.dto;

import com.textcaptcha.data.model.task.TaskType;

import java.util.List;
import java.util.UUID;

public class TaskSolutionRequestBody {

    private final TaskType taskType;
    private final UUID id;
    private final List<Integer> content;

    protected TaskSolutionRequestBody(TaskType taskType, UUID instanceId, List<Integer> content) {
        this.taskType = taskType;
        this.id = instanceId;
        this.content = content;
    }

    public UUID getId() {
        return id;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public List<Integer> getContent() {
        return content;
    }
}
