package com.textcaptcha.data.model.response.content;

import java.util.Collections;
import java.util.List;

public class NerCaptchaTaskResponseContent extends CaptchaTaskResponseContent {

    private List<Integer> tokenIndexes = Collections.emptyList();

    public NerCaptchaTaskResponseContent(List<Integer> tokenIndexes) {
        this.tokenIndexes = tokenIndexes;
    }

    public NerCaptchaTaskResponseContent() { }

    public List<Integer> getTokenIndexes() {
        return tokenIndexes;
    }

    public void setTokenIndexes(List<Integer> tokenIndexes) {
        this.tokenIndexes = tokenIndexes;
    }

}
