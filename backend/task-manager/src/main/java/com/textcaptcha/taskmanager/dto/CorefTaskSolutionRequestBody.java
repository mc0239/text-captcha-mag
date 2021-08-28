package com.textcaptcha.taskmanager.dto;

import com.textcaptcha.data.model.task.TaskType;

import java.util.List;
import java.util.UUID;

public class CorefTaskSolutionRequestBody extends TaskSolutionRequestBody<List<Integer>> {

    private final List<Integer> indexes;

    public CorefTaskSolutionRequestBody(TaskType taskType, UUID instanceId, List<Integer> content) {
        super(taskType, instanceId);
        this.indexes = content;
    }

    public List<Integer> getIndexes() {
        return indexes;
    }

    @Override
    public List<Integer> getContent() {
        return indexes;
    }
}
