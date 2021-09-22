package com.textcaptcha.taskmanager.service.impl;

import com.textcaptcha.data.model.task.CaptchaTask;
import com.textcaptcha.data.model.task.TaskType;
import com.textcaptcha.data.repository.CaptchaTaskRepository;
import com.textcaptcha.dto.ArticleHashPairDto;
import com.textcaptcha.taskmanager.exception.TaskSelectionException;
import com.textcaptcha.taskmanager.pojo.selection.SelectionOptions;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RandomTaskSelectionService extends BaseTaskSelectionService<SelectionOptions> {

    protected RandomTaskSelectionService(CaptchaTaskRepository captchaTaskRepository) {
        super(LoggerFactory.getLogger(RandomTaskSelectionService.class), captchaTaskRepository);
    }

    @Override
    public CaptchaTask getTaskForArticle(TaskType taskType, ArticleHashPairDto articleHashes, SelectionOptions options) throws TaskSelectionException {
        validateCommonParameters(taskType, articleHashes);

        List<CaptchaTask> tasks = captchaTaskRepository.getTasksByNumberOfResponsesAsc(taskType, articleHashes.getUrlHash(), articleHashes.getTextHash());
        return pickTaskFromList(tasks);
    }

}
