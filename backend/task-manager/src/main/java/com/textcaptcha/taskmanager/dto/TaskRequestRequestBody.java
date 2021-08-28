package com.textcaptcha.taskmanager.dto;

import com.textcaptcha.dto.ArticleHashPairDto;

public class TaskRequestRequestBody extends ArticleHashPairDto {

    private final String taskType;

    public TaskRequestRequestBody(String taskType, String urlHash, String textHash) {
        super(urlHash, textHash);
        this.taskType = taskType;
    }

    public String getTaskType() {
        return taskType;
    }

    @Override
    public String toString() {
        return "TaskRequestRequestBody{" +
                "taskType='" + taskType + '\'' +
                "} " + super.toString();
    }
}
