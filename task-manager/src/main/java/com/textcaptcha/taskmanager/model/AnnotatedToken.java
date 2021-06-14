package com.textcaptcha.taskmanager.model;

import com.textcaptcha.taskmanager.dto.NerApiResponse;

public class AnnotatedToken {

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

    public static AnnotatedToken fromNerToken(NerApiResponse.NerToken nerToken) {
        AnnotatedToken at = new AnnotatedToken();
        at.setWord(nerToken.getWord());
        at.setAnnotation(nerToken.getEntity());
        at.setScore(nerToken.getScore());
        return at;
    }

}
