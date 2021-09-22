package com.textcaptcha.taskmanager.service.impl;

import com.textcaptcha.data.model.task.CaptchaTask;
import com.textcaptcha.data.model.task.TaskType;
import com.textcaptcha.data.repository.CaptchaTaskRepository;
import com.textcaptcha.dto.ArticleHashPairDto;
import com.textcaptcha.taskmanager.exception.TaskSelectionException;
import com.textcaptcha.taskmanager.pojo.selection.ConfidenceSelectionOptions;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConfidenceTaskSelectionService extends BaseTaskSelectionService<ConfidenceSelectionOptions> {

    protected ConfidenceTaskSelectionService(CaptchaTaskRepository captchaTaskRepository) {
        super(LoggerFactory.getLogger(ConfidenceTaskSelectionService.class), captchaTaskRepository);
    }

    @Override
    public CaptchaTask getTaskForArticle(TaskType taskType, ArticleHashPairDto articleHashes, ConfidenceSelectionOptions options) throws TaskSelectionException {
        validateCommonParameters(taskType, articleHashes);

        List<CaptchaTask> tasks = captchaTaskRepository.getTasksByNumberOfResponsesAsc(taskType, articleHashes.getUrlHash(), articleHashes.getTextHash());

        if (options.shouldBeAboveThreshold()) {
            List<CaptchaTask> possibleTasks = tasks.stream()
                    .filter(t -> t.getConfidence() == null || t.getConfidence() >= options.getConfidenceThreshold())
                    .filter(t -> t.getId() != null && !options.getIgnoredTaskIds().contains(t.getId()))
                    .collect(Collectors.toList());
            return pickTaskFromList(possibleTasks);
        } else {
            List<CaptchaTask> possibleTasks = tasks.stream()
                    .filter(t -> t.getConfidence() == null || t.getConfidence() < options.getConfidenceThreshold())
                    .filter(t -> t.getId() != null && !options.getIgnoredTaskIds().contains(t.getId()))
                    .collect(Collectors.toList());
            return pickTaskFromList(possibleTasks);
        }
    }
}
