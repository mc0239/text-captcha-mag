package com.textcaptcha.taskmanager.service;

import com.textcaptcha.data.model.task.TaskType;
import com.textcaptcha.dto.ArticleHashPairDto;
import com.textcaptcha.taskmanager.dto.CompoundTaskSolutionRequestBody;
import com.textcaptcha.taskmanager.pojo.CaptchaTaskCompound;
import com.textcaptcha.taskmanager.pojo.CaptchaTaskFlow;

import java.util.List;
import java.util.UUID;

public interface CaptchaTaskCompoundManager extends CaptchaLinkTaskManager {

    CaptchaTaskCompound startCompound(TaskType taskType, ArticleHashPairDto articleHashes);
    CaptchaTaskCompound solveCompound(CompoundTaskSolutionRequestBody compoundTaskSolutionRequestBody);

}
