package com.textcaptcha.taskmanager.service.impl;

import com.textcaptcha.data.model.CaptchaFlow;
import com.textcaptcha.data.model.task.CaptchaTask;
import com.textcaptcha.data.model.task.TaskType;
import com.textcaptcha.data.repository.CaptchaTaskRepository;
import com.textcaptcha.dto.ArticleHashPairDto;
import com.textcaptcha.taskmanager.exception.TaskSelectionException;
import org.springframework.stereotype.Service;

@Service
public class ConfidenceTaskSelectionService extends BaseTaskSelectionService {

    protected ConfidenceTaskSelectionService(CaptchaTaskRepository captchaTaskRepository) {
        super(captchaTaskRepository);
    }

    @Override
    public CaptchaTask getTaskForArticle(TaskType taskType, ArticleHashPairDto articleHashes) throws TaskSelectionException {
        return null;
    }

    @Override
    public CaptchaTask getTaskForArticleAndFlow(TaskType taskType, ArticleHashPairDto articleHashes, CaptchaFlow flow) throws TaskSelectionException {
        return null;
    }
}
