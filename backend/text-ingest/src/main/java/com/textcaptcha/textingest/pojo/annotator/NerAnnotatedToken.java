package com.textcaptcha.textingest.pojo.annotator;

import com.textcaptcha.data.model.task.content.NerCaptchaTaskContent;

public class NerAnnotatedToken extends AnnotatedToken {

    private String word;
    private String annotation;
    private Double score;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public static NerCaptchaTaskContent.Token toContentToken(NerAnnotatedToken in) {
        NerCaptchaTaskContent.Token out = new NerCaptchaTaskContent.Token();
        out.setAnnotation(in.annotation);
        out.setScore(in.score);
        out.setWord(in.word);
        return out;
    }
}
