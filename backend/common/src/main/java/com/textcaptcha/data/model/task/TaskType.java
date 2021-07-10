package com.textcaptcha.data.model.task;

public enum TaskType {
    NER,
    COREF;

    public static class Name {
        public static final String NER = "NER";
        public static final String COREF = "COREF";
    }
}
