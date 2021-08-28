package com.textcaptcha.taskmanager.service;

import com.textcaptcha.dto.ArticleHashPairDto;
import com.textcaptcha.taskmanager.pojo.CaptchaTaskFlow;

import java.util.UUID;

public interface TaskFlowManager {

    CaptchaTaskFlow beginFlow(ArticleHashPairDto articleHashes);
    CaptchaTaskFlow continueFlow(UUID taskInstanceId, Object taskSolution);

}
