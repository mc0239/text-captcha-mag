package com.textcaptcha.taskmanager.service.impl;

import com.textcaptcha.data.model.CaptchaFlow;
import com.textcaptcha.data.model.task.CaptchaTask;
import com.textcaptcha.data.model.task.TaskType;
import com.textcaptcha.data.repository.CaptchaTaskRepository;
import com.textcaptcha.dto.ArticleHashPairDto;
import com.textcaptcha.taskmanager.exception.NoTasksAvailableException;
import com.textcaptcha.taskmanager.exception.TaskSelectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class RandomTaskSelectionService extends BaseTaskSelectionService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected RandomTaskSelectionService(CaptchaTaskRepository captchaTaskRepository) {
        super(captchaTaskRepository);
    }

    @Override
    public CaptchaTask getTaskForArticle(TaskType taskType, ArticleHashPairDto articleHashes) throws TaskSelectionException {
        validateCommonParameters(taskType, articleHashes);

        List<CaptchaTask> tasks = getCaptchaTaskRepository().getTasksByNumberOfResponsesAsc(taskType, articleHashes.getUrlHash(), articleHashes.getTextHash());

        if (tasks.isEmpty()) {
            throw new NoTasksAvailableException();
        }

        return pickTaskFromList(tasks);
    }

    @Override
    public CaptchaTask getTaskForArticleAndFlow(TaskType taskType, ArticleHashPairDto articleHashes, CaptchaFlow flow) throws TaskSelectionException {
        validateCommonParameters(taskType, articleHashes);

        if (flow == null) {
            logger.warn("Calling getRandomTaskForArticleNotYetInFlow with flow == null. Delegating to getRandomTaskForArticle.");
            return getTaskForArticle(taskType, articleHashes);
        }

        List<CaptchaTask> tasks = getCaptchaTaskRepository().getTasksNotInFlow(taskType, articleHashes.getUrlHash(), articleHashes.getTextHash(), flow.getId());

        if (tasks.isEmpty()) {
            logger.info("No (more) tasks available for given flow.");
            throw new NoTasksAvailableException();
        }

        return pickTaskFromList(tasks);
    }

    private CaptchaTask pickTaskFromList(List<CaptchaTask> tasks) {
        // Selects a task from the top 10% of the tasks with least responses received.
        Random r = new Random();
        return tasks.get(r.nextInt(tasks.size() / 10));
    }
}
