package com.textcaptcha.data.model.task.content;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collections;
import java.util.List;

public class CorefCaptchaTaskContent extends CaptchaTaskContent {

    private List<Token> mentionOfInterest = Collections.emptyList();

    private List<List<Token>> suggestedMentions = Collections.emptyList();

    public CorefCaptchaTaskContent(List<Token> mentionOfInterest, List<List<Token>> suggestedMentions) {
        this.mentionOfInterest = mentionOfInterest;
        this.suggestedMentions = suggestedMentions;
    }

    public CorefCaptchaTaskContent() { }

    public List<Token> getMentionOfInterest() {
        return mentionOfInterest;
    }

    public void setMentionOfInterest(List<Token> mentionOfInterest) {
        this.mentionOfInterest = mentionOfInterest;
    }

    public List<List<Token>> getSuggestedMentions() {
        return suggestedMentions;
    }

    public void setSuggestedMentions(List<List<Token>> suggestedMentions) {
        this.suggestedMentions = suggestedMentions;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Token {

        private String word;
        private Integer mentionId;
        private Integer clusterId;
        private Double score;

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public Integer getMentionId() {
            return mentionId;
        }

        public void setMentionId(Integer mentionId) {
            this.mentionId = mentionId;
        }

        public Integer getClusterId() {
            return clusterId;
        }

        public void setClusterId(Integer clusterId) {
            this.clusterId = clusterId;
        }

        public Double getScore() {
            return score;
        }

        public void setScore(Double score) {
            this.score = score;
        }

    }

}
