package com.textcaptcha.taskmanager.service;

import com.textcaptcha.data.model.task.TaskType;
import com.textcaptcha.dto.ArticleHashPairDto;
import com.textcaptcha.taskmanager.pojo.CaptchaTaskFlow;

import java.util.List;
import java.util.UUID;

public interface TaskFlowManager {

    CaptchaTaskFlow beginFlow(TaskType taskType, ArticleHashPairDto articleHashes);
    CaptchaTaskFlow continueFlow(UUID taskInstanceId, List<Integer> taskSolution);

}
