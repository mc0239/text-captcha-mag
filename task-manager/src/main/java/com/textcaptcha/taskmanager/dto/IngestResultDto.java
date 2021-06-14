package com.textcaptcha.taskmanager.dto;

public class IngestResultDto {

    private String articleUrlHash;
    private String articleTextHash;

    public IngestResultDto(String articleUrlHash, String articleTextHash) {
        this.articleUrlHash = articleUrlHash;
        this.articleTextHash = articleTextHash;
    }

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
}
