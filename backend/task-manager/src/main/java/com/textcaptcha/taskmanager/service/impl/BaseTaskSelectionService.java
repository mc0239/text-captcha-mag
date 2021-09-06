package com.textcaptcha.taskmanager.service.impl;

import com.textcaptcha.data.model.task.TaskType;
import com.textcaptcha.data.repository.CaptchaTaskRepository;
import com.textcaptcha.dto.ArticleHashPairDto;
import com.textcaptcha.taskmanager.exception.TaskSelectionException;
import com.textcaptcha.taskmanager.service.TaskSelectionService;

public abstract class BaseTaskSelectionService implements TaskSelectionService {

    private final CaptchaTaskRepository captchaTaskRepository;

    protected BaseTaskSelectionService(CaptchaTaskRepository captchaTaskRepository) {
        this.captchaTaskRepository = captchaTaskRepository;
    }

    public CaptchaTaskRepository getCaptchaTaskRepository() {
        return captchaTaskRepository;
    }

    protected void validateCommonParameters(TaskType taskType, ArticleHashPairDto articleHashes) throws TaskSelectionException {
        if (taskType == null) {
            throw new TaskSelectionException("Missing taskType parameter.");
        }

        if (!articleHashes.hasHashes()) {
            throw new TaskSelectionException("Missing articleUrlHash and/or articleTextHash parameter(s).");
        }
    }

}
