package com.textcaptcha.taskmanager.dto;

import java.util.UUID;

public abstract class TaskSolutionRequestBody {

    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}
