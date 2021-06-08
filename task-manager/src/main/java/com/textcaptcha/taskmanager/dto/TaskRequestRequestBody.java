package com.textcaptcha.taskmanager.dto;

public class TaskRequestRequestBody {

    private String articleUrl;
    private String articleUid;

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public String getArticleUid() {
        return articleUid;
    }

    public void setArticleUid(String articleUid) {
        this.articleUid = articleUid;
    }

    @Override
    public String toString() {
        return "TaskRequestRequestBody{" +
                "articleUrl='" + articleUrl + '\'' +
                ", articleUid='" + articleUid + '\'' +
                '}';
    }
}
