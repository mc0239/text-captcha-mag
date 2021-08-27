package com.textcaptcha.taskmanager.pojo;

import com.textcaptcha.data.model.task.CaptchaTask;
import com.textcaptcha.data.model.task.CorefCaptchaTask;
import com.textcaptcha.data.model.task.NerCaptchaTask;
import com.textcaptcha.taskmanager.dto.CorefTaskInstanceDto;
import com.textcaptcha.taskmanager.dto.NerTaskInstanceDto;
import com.textcaptcha.taskmanager.dto.TaskInstanceDto;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

public class IssuedTaskInstance {

    private final UUID id;
    private final CaptchaTask task;
    private final Date createdAt;

    public IssuedTaskInstance(UUID id, CaptchaTask task) {
        this.id = id;
        this.task = task;
        this.createdAt = new Date();
    }

    public UUID getId() {
        return id;
    }

    public CaptchaTask getTask() {
        return task;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public boolean isExpired(long maxInstanceAge) {
        return this.getCreatedAt().toInstant().plus(maxInstanceAge, ChronoUnit.MILLIS).isBefore(Instant.now());
    }

    public TaskInstanceDto<?> toTaskInstanceDto() {
        CaptchaTask t = this.getTask();

        if (t instanceof NerCaptchaTask) {
            return new NerTaskInstanceDto(id, (NerCaptchaTask) task);
        } else if (t instanceof CorefCaptchaTask) {
            return new CorefTaskInstanceDto(id, (CorefCaptchaTask) task);
        }
        
        return null;
    }

}
