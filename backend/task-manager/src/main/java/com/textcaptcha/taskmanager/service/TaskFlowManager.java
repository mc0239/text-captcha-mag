package com.textcaptcha.taskmanager.service;

import com.textcaptcha.taskmanager.dto.TaskRequestRequestBody;
import com.textcaptcha.taskmanager.dto.TaskSolutionRequestBody;
import com.textcaptcha.taskmanager.pojo.CaptchaTaskFlow;

public interface TaskFlowManager {

    CaptchaTaskFlow beginFlow(TaskRequestRequestBody body);
    CaptchaTaskFlow continueFlow(TaskSolutionRequestBody body);

}
