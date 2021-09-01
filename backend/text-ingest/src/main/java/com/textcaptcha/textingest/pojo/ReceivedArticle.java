package com.textcaptcha.textingest.pojo;

import com.textcaptcha.dto.ArticleHashPairDto;
import com.textcaptcha.util.HashUtils;

public class ReceivedArticle extends ArticleHashPairDto {

    private final String url;
    private final String text;

    public ReceivedArticle(String url, String text) {
        super(HashUtils.SHA256(url), HashUtils.SHA256(text));
        this.url = url;
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "ReceivedArticle{" +
                "url='" + url + '\'' +
                ", text.length='" + text.length() + '\'' +
                "} " + super.toString();
    }
}
