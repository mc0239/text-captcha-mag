package com.textcaptcha.taskmanager.service.impl;

import com.textcaptcha.annotation.Loggable;
import com.textcaptcha.data.model.CaptchaFlow;
import com.textcaptcha.data.model.task.CaptchaTask;
import com.textcaptcha.data.model.task.TaskType;
import com.textcaptcha.data.repository.CaptchaTaskRepository;
import com.textcaptcha.dto.ArticleHashPairDto;
import com.textcaptcha.taskmanager.exception.NoTasksAvailableException;
import com.textcaptcha.taskmanager.exception.TaskSelectionException;
import com.textcaptcha.taskmanager.service.TaskSelectionService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class TaskSelectionServiceImpl implements TaskSelectionService {

    @Loggable
    Logger logger;

    private final CaptchaTaskRepository captchaTaskRepository;

    public TaskSelectionServiceImpl(CaptchaTaskRepository captchaTaskRepository) {
        this.captchaTaskRepository = captchaTaskRepository;
    }

    @Override
    public CaptchaTask getRandomTaskForArticle(TaskType taskType, ArticleHashPairDto articleHashes) throws TaskSelectionException {
        validateCommonParameters(taskType, articleHashes);

        List<CaptchaTask> tasks = captchaTaskRepository.getTasks(taskType, articleHashes.getUrlHash(), articleHashes.getTextHash());

        if (tasks.isEmpty()) {
            throw new NoTasksAvailableException();
        }

        // TODO a smarter-than-random task selection.
        Random r = new Random();
        CaptchaTask selectedTask = tasks.get(r.nextInt(tasks.size()));
        return selectedTask;
    }

    @Override
    public CaptchaTask getRandomTaskForArticleNotYetInFlow(TaskType taskType, ArticleHashPairDto articleHashes, CaptchaFlow flow) throws TaskSelectionException {
        validateCommonParameters(taskType, articleHashes);

        if (flow == null) {
            logger.warn("Calling getRandomTaskForArticleNotYetInFlow with flow == null. Delegating to getRandomTaskForArticle.");
            return getRandomTaskForArticle(taskType, articleHashes);
        }

        List<CaptchaTask> tasks = captchaTaskRepository.getTasksNotInFlow(taskType, articleHashes.getUrlHash(), articleHashes.getTextHash(), flow.getId());

        if (tasks.isEmpty()) {
            logger.info("No (more) tasks available for given flow.");
            throw new NoTasksAvailableException();
        }

        // TODO a smarter-than-random task selection.
        Random r = new Random();
        CaptchaTask selectedTask = tasks.get(r.nextInt(tasks.size()));
        return selectedTask;
    }

    private void validateCommonParameters(TaskType taskType, ArticleHashPairDto articleHashes) throws TaskSelectionException {
        if (taskType == null) {
            throw new TaskSelectionException("Missing taskType parameter.");
        }

        if (!articleHashes.hasHashes()) {
            throw new TaskSelectionException("Missing articleUrlHash and/or articleTextHash parameter(s).");
        }
    }
}
