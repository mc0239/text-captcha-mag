package com.textcaptcha.taskmanager.dto;

public class IngestRequestBody {

    private String articleUrl;
    private String articleUid;
    private String text;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "IngestRequestBody{" +
                "articleUrl='" + articleUrl + '\'' +
                ", articleUid='" + articleUid + '\'' +
                ", text.length()='" + text.length() + '\'' +
                '}';
    }
}
