package com.textcaptcha.taskmanager.dto;

public class TaskRequestRequestBody {

    private String articleUrl;

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    @Override
    public String toString() {
        return "TaskRequestRequestBody{" +
                "articleUrl='" + articleUrl + '\'' +
                '}';
    }

}
