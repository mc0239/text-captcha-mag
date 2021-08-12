package com.textcaptcha.textingest.pojo.annotator;

public abstract class AnnotatedToken {

    protected Integer index;
    protected String word;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

}
