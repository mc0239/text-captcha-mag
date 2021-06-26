package com.textcaptcha.textingest.dto;

public class IngestRequestBody {

    private String articleUrl;
    private String articleText;

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public String getArticleText() {
        return articleText;
    }

    public void setArticleText(String articleText) {
        this.articleText = articleText;
    }

    @Override
    public String toString() {
        return "IngestRequestBody{" +
                "articleUrl='" + articleUrl + '\'' +
                ", articleText.length()='" + articleText.length() + '\'' +
                '}';
    }
}
