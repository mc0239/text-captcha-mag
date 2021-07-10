package com.textcaptcha.taskmanager.dto;

import java.util.ArrayList;
import java.util.List;

public class NerTaskSolutionRequestBody extends TaskSolutionRequestBody {

    private List<Integer> indexes = new ArrayList<>();

    public List<Integer> getIndexes() {
        return indexes;
    }

    public void setIndexes(List<Integer> indexes) {
        this.indexes = indexes;
    }

}
