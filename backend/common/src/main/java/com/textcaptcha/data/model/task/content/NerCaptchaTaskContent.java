package com.textcaptcha.data.model.task.content;

import java.util.Collections;
import java.util.List;

public class NerCaptchaTaskContent extends CaptchaTaskContent {

    private List<Token> tokens = Collections.emptyList();

    public NerCaptchaTaskContent(List<Token> tokens) {
        this.tokens = tokens;
    }

    public NerCaptchaTaskContent() { }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    public static class Token {

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

    }

}
