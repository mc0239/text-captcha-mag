package com.textcaptcha.taskmanager.service.impl;

import com.textcaptcha.data.model.CaptchaFlow;
import com.textcaptcha.data.model.task.CaptchaTask;
import com.textcaptcha.data.model.task.TaskType;
import com.textcaptcha.data.repository.CaptchaTaskRepository;
import com.textcaptcha.dto.ArticleHashPairDto;
import com.textcaptcha.taskmanager.exception.TaskSelectionException;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RandomTaskSelectionService extends BaseTaskSelectionService {

    protected RandomTaskSelectionService(CaptchaTaskRepository captchaTaskRepository) {
        super(LoggerFactory.getLogger(RandomTaskSelectionService.class), captchaTaskRepository);
    }

    @Override
    public CaptchaTask getTaskForArticle(TaskType taskType, ArticleHashPairDto articleHashes) throws TaskSelectionException {
        validateCommonParameters(taskType, articleHashes);

        List<CaptchaTask> tasks = captchaTaskRepository.getTasksByNumberOfResponsesAsc(taskType, articleHashes.getUrlHash(), articleHashes.getTextHash());
        return pickTaskFromList(tasks);
    }

    @Override
    public CaptchaTask getTaskForArticleAndFlow(TaskType taskType, ArticleHashPairDto articleHashes, CaptchaFlow flow) throws TaskSelectionException {
        validateCommonParameters(taskType, articleHashes);

        if (flow == null) {
            logger.warn("Calling getRandomTaskForArticleNotYetInFlow with flow == null. Delegating to getTaskForArticle.");
            return getTaskForArticle(taskType, articleHashes);
        }

        List<CaptchaTask> tasks = captchaTaskRepository.getTasksNotInFlowByNumberOfResponsesAsc(taskType, articleHashes.getUrlHash(), articleHashes.getTextHash(), flow.getId());
        return pickTaskFromList(tasks);
    }

}
