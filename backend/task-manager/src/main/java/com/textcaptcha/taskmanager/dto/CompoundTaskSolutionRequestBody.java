package com.textcaptcha.taskmanager.dto;

import java.util.List;

public class CompoundTaskSolutionRequestBody {

    private List<TaskSolutionRequestBody> taskSolutions;

    public void setTaskSolutions(List<TaskSolutionRequestBody> taskSolutions) {
        this.taskSolutions = taskSolutions;
    }

    public List<TaskSolutionRequestBody> getTaskSolutions() {
        return taskSolutions;
    }

}
