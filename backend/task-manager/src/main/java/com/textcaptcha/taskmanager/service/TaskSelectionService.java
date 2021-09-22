package com.textcaptcha.taskmanager.service;

import com.textcaptcha.data.model.task.CaptchaTask;
import com.textcaptcha.data.model.task.TaskType;
import com.textcaptcha.dto.ArticleHashPairDto;
import com.textcaptcha.taskmanager.exception.TaskSelectionException;
import com.textcaptcha.taskmanager.pojo.selection.SelectionOptions;

public interface TaskSelectionService<OPT extends SelectionOptions> {

    CaptchaTask getTaskForArticle(TaskType taskType, ArticleHashPairDto articleHashes, OPT selectionOptions) throws TaskSelectionException;

}
