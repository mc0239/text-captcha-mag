package com.textcaptcha.taskmanager.dto;

public class TaskRequestRequestBody {

    private String articleUrlHash;
    private String articleTextHash;
    private String taskType;

    public String getArticleUrlHash() {
        return articleUrlHash;
    }

    public void setArticleUrlHash(String articleUrlHash) {
        this.articleUrlHash = articleUrlHash;
    }

    public String getArticleTextHash() {
        return articleTextHash;
    }

    public void setArticleTextHash(String articleTextHash) {
        this.articleTextHash = articleTextHash;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    @Override
    public String toString() {
        return "TaskRequestRequestBody{" +
                "articleUrlHash='" + articleUrlHash + '\'' +
                ", articleTextHash='" + articleTextHash + '\'' +
                ", taskType='" + taskType + '\'' +
                '}';
    }
}
