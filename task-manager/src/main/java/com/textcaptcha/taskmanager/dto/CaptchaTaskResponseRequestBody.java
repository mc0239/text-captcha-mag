package com.textcaptcha.taskmanager.dto;

import java.util.ArrayList;
import java.util.List;

public class CaptchaTaskResponseRequestBody {

    private String id;
    private List<Integer> indexes = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Integer> getIndexes() {
        return indexes;
    }

    public void setIndexes(List<Integer> indexes) {
        this.indexes = indexes;
    }

}
