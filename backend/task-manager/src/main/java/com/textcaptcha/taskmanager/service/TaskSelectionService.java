package com.textcaptcha.taskmanager.service;

import com.textcaptcha.data.model.CaptchaFlow;
import com.textcaptcha.data.model.task.CaptchaTask;
import com.textcaptcha.data.model.task.TaskType;
import com.textcaptcha.dto.ArticleHashPairDto;
import com.textcaptcha.taskmanager.exception.TaskSelectionException;

public interface TaskSelectionService {

    CaptchaTask getTaskForArticle(TaskType taskType, ArticleHashPairDto articleHashes) throws TaskSelectionException;
    CaptchaTask getTaskForArticleAndFlow(TaskType taskType, ArticleHashPairDto articleHashes, CaptchaFlow flow) throws TaskSelectionException;

}
