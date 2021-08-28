package com.textcaptcha.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ArticleHashPairDto {

    @JsonProperty("articleUrlHash")
    private final String urlHash;

    @JsonProperty("articleTextHash")
    private final String textHash;

    public ArticleHashPairDto(String urlHash, String textHash) {
        this.urlHash = urlHash;
        this.textHash = textHash;
    }

    public String getUrlHash() {
        return urlHash;
    }

    public String getTextHash() {
        return textHash;
    }

    @JsonIgnore
    public ArticleHashPairDto getHashes() {
        return this;
    }

    @JsonIgnore
    public boolean hasHashes() {
        return this.urlHash != null && this.textHash != null;
    }

    @Override
    public String toString() {
        return "ArticleHashPairDto{" +
                "urlHash='" + urlHash + '\'' +
                ", textHash='" + textHash + '\'' +
                '}';
    }
}
