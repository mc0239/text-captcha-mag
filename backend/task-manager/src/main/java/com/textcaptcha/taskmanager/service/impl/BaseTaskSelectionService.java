package com.textcaptcha.taskmanager.service.impl;

import com.textcaptcha.data.model.task.CaptchaTask;
import com.textcaptcha.data.model.task.TaskType;
import com.textcaptcha.data.repository.CaptchaTaskRepository;
import com.textcaptcha.dto.ArticleHashPairDto;
import com.textcaptcha.taskmanager.exception.NoTasksAvailableException;
import com.textcaptcha.taskmanager.exception.TaskSelectionException;
import com.textcaptcha.taskmanager.service.TaskSelectionService;
import org.slf4j.Logger;

import java.util.List;
import java.util.Random;

public abstract class BaseTaskSelectionService implements TaskSelectionService {

    protected final Logger logger;
    protected final CaptchaTaskRepository captchaTaskRepository;

    protected BaseTaskSelectionService(Logger logger, CaptchaTaskRepository captchaTaskRepository) {
        this.logger = logger;
        this.captchaTaskRepository = captchaTaskRepository;
    }

    protected CaptchaTask pickTaskFromList(List<CaptchaTask> tasks) throws NoTasksAvailableException {
        if (tasks.isEmpty()) {
            throw new NoTasksAvailableException();
        }

        // Selects a task from the top 10% of the tasks.
        Random r = new Random();
        return tasks.get(r.nextInt((int) Math.ceil(tasks.size() / 10f)));
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
