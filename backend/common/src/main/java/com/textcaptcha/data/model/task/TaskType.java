package com.textcaptcha.data.model.task;

public enum TaskType {
    NER,
    COREF;

    public static class Name {
        public static final String NER = "NER";
        public static final String COREF = "COREF";
    }

    public static class ShortName {
        public static final int length = 3;

        public static final String NER = "NER";
        public static final String COREF = "CRF";
    }
}
