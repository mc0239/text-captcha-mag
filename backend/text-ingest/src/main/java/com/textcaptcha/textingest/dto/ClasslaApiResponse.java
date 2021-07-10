package com.textcaptcha.textingest.dto;

import java.util.List;

public class ClasslaApiResponse {

    private List<Sentence> sentences;

    public List<Sentence> getSentences() {
        return sentences;
    }

    public void setSentences(List<Sentence> sentences) {
        this.sentences = sentences;
    }

    public static class Sentence {

        private List<Token> tokens;

        public List<Token> getTokens() {
            return tokens;
        }

        public void setTokens(List<Token> tokens) {
            this.tokens = tokens;
        }

    }

    public static class Token {

        private Long id;
        private String text;
        private String lemma;
        private String upos;
        private String xpos;
        private String feats;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getLemma() {
            return lemma;
        }

        public void setLemma(String lemma) {
            this.lemma = lemma;
        }

        public String getUpos() {
            return upos;
        }

        public void setUpos(String upos) {
            this.upos = upos;
        }

        public String getXpos() {
            return xpos;
        }

        public void setXpos(String xpos) {
            this.xpos = xpos;
        }

        public String getFeats() {
            return feats;
        }

        public void setFeats(String feats) {
            this.feats = feats;
        }

    }

}
