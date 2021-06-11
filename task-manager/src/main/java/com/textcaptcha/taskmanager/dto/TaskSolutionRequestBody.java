package com.textcaptcha.taskmanager.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskSolutionRequestBody {

    private UUID id;
    private List<Integer> indexes = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<Integer> getIndexes() {
        return indexes;
    }

    public void setIndexes(List<Integer> indexes) {
        this.indexes = indexes;
    }

}
