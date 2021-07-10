package com.textcaptcha.taskmanager.pojo;

import com.textcaptcha.data.model.task.CaptchaTask;
import com.textcaptcha.data.model.task.content.CaptchaTaskContent;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

public class IssuedTaskInstance<CT extends CaptchaTask<?>> {

    private final UUID id;
    private final CT task;
    private final Date createdAt;

    public IssuedTaskInstance(UUID id, CT task) {
        this.id = id;
        this.task = task;
        this.createdAt = new Date();
    }

    public UUID getId() {
        return id;
    }

    public CaptchaTask<?> getTask() {
        return task;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public boolean isExpired(long maxInstanceAge) {
        return this.getCreatedAt().toInstant().plus(maxInstanceAge, ChronoUnit.MILLIS).isBefore(Instant.now());
    }

}
