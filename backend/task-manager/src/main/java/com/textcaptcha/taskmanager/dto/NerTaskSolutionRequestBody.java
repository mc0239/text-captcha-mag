package com.textcaptcha.taskmanager.dto;

import com.textcaptcha.data.model.task.TaskType;

import java.util.List;
import java.util.UUID;

public class NerTaskSolutionRequestBody extends TaskSolutionRequestBody<List<Integer>> {

    private final List<Integer> indexes;

    public NerTaskSolutionRequestBody(TaskType taskType, UUID instanceId, List<Integer> indexes) {
        super(taskType, instanceId);
        this.indexes = indexes;
    }

    public List<Integer> getIndexes() {
        return indexes;
    }

    @Override
    public List<Integer> getContent() {
        return indexes;
    }
}
