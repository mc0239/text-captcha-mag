package com.textcaptcha.taskmanager.service;

import com.textcaptcha.data.model.task.CaptchaTask;
import com.textcaptcha.taskmanager.pojo.IssuedTaskInstance;

import java.util.UUID;

public interface TaskInstanceKeeper<CT extends CaptchaTask> {
    UUID issue(CT task);
    IssuedTaskInstance<CT> invalidate(UUID instanceId);
}
