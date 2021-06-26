package com.textcaptcha.textingest.pojo;

import com.textcaptcha.util.HashUtils;

public class ReceivedArticle {

    private final String url;
    private final String text;

    private final String urlHash;
    private final String textHash;

    public ReceivedArticle(String url, String text) {
        this.url = url;
        this.text = text;
        this.urlHash = HashUtils.SHA256(url);
        this.textHash = HashUtils.SHA256(text);
    }

    public String getUrl() {
        return url;
    }

    public String getText() {
        return text;
    }

    public String getUrlHash() {
        return urlHash;
    }

    public String getTextHash() {
        return textHash;
    }

    @Override
    public String toString() {
        return "ReceivedArticle{" +
                "url='" + url + '\'' +
                ", text.length()='" + text.length() + '\'' +
                ", urlHash='" + urlHash + '\'' +
                ", textHash='" + textHash + '\'' +
                '}';
    }

}
