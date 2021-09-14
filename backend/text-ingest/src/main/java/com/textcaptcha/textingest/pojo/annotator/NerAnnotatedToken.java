package com.textcaptcha.textingest.pojo.annotator;

import com.textcaptcha.data.model.task.content.NerCaptchaTaskContent;

public class NerAnnotatedToken extends AnnotatedToken {

    private String annotation;

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public static NerCaptchaTaskContent.Token toContentToken(NerAnnotatedToken in) {
        NerCaptchaTaskContent.Token out = new NerCaptchaTaskContent.Token();
        out.setAnnotation(in.annotation);
        out.setScore(in.score);
        out.setWord(in.word);
        return out;
    }
}
