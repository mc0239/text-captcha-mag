package com.textcaptcha.taskmanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class NerApiResponse {

    @JsonProperty("named_entities")
    private List<NerToken> tokens = new ArrayList<>();

    public List<NerToken> getTokens() {
        return tokens;
    }

    public void setTokens(List<NerToken> tokens) {
        this.tokens = tokens;
    }

    public static class NerToken {

        private String entity;
        private Double score;
        private String word;

        public String getEntity() {
            return entity;
        }

        public void setEntity(String entity) {
            this.entity = entity;
        }

        public Double getScore() {
            return score;
        }

        public void setScore(Double score) {
            this.score = score;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        @Override
        public String toString() {
            return word + "(" + entity + ")";
        }

    }

}
