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
import java.util.stream.Collectors;

@Service
public class ConfidenceTaskSelectionService extends BaseTaskSelectionService {

    protected ConfidenceTaskSelectionService(CaptchaTaskRepository captchaTaskRepository) {
        super(LoggerFactory.getLogger(ConfidenceTaskSelectionService.class), captchaTaskRepository);
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

        float confidenceThreshold = switch (taskType) {
            case COREF -> 0.7f;
            case NER -> 0.9f;
        };

        List<CaptchaTask> tasks = captchaTaskRepository.getTasksNotInFlowByNumberOfResponsesAsc(taskType, articleHashes.getUrlHash(), articleHashes.getTextHash(), flow.getId());

        if (!flow.isCompleteVerify()) {
            // If verification is not complete, we give a task with high confidence.
            List<CaptchaTask> possibleTasks = tasks.stream()
                    .filter(t -> t.getConfidence() == null || t.getConfidence() >= confidenceThreshold)
                    .collect(Collectors.toList());
            return pickTaskFromList(possibleTasks);
        } else {
            // Verification complete, we can give a task with low confidence.
            List<CaptchaTask> possibleTasks = tasks.stream()
                    .filter(t -> t.getConfidence() == null || t.getConfidence() < confidenceThreshold)
                    .collect(Collectors.toList());
            return pickTaskFromList(possibleTasks);
        }
    }
}
