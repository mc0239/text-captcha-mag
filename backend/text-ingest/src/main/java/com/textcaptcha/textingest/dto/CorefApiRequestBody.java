package com.textcaptcha.textingest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CorefApiRequestBody {

    List<Sentence> sentences = new ArrayList<>();

    public List<Sentence> getSentences() {
        return sentences;
    }

    public void setSentences(List<Sentence> sentences) {
        this.sentences = sentences;
    }

    public static class Sentence {

        List<Token> tokens = new ArrayList<>();

        public List<Token> getTokens() {
            return tokens;
        }

        public void setTokens(List<Token> tokens) {
            this.tokens = tokens;
        }

        @Override
        public String toString() {
            return tokens.stream().map(Token::toString).collect(Collectors.joining(" "));
        }
    }

    public static class Token {

        private String text;

        @JsonProperty("mention")
        private Integer mentionId;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Integer getMentionId() {
            return mentionId;
        }

        public void setMentionId(Integer mentionId) {
            this.mentionId = mentionId;
        }

        @Override
        public String toString() {
            return text + (mentionId != null ? "[" + mentionId + "]" : "");
        }
    }

}
