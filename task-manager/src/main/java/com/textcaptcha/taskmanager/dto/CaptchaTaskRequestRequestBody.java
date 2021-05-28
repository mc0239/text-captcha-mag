package com.textcaptcha.taskmanager.dto;

public class CaptchaTaskRequestRequestBody {

    private String articleUrl;

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    @Override
    public String toString() {
        return "CaptchaTaskRequestRequestBody{" +
                "articleUrl='" + articleUrl + '\'' +
                '}';
    }

}
