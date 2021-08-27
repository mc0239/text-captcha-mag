package com.textcaptcha.taskmanager.service;

import com.textcaptcha.data.model.task.CaptchaTask;
import com.textcaptcha.taskmanager.pojo.IssuedTaskInstance;

import java.util.UUID;

public interface TaskInstanceKeeper {
    UUID issue(CaptchaTask task);
    IssuedTaskInstance invalidate(UUID instanceId);
}
