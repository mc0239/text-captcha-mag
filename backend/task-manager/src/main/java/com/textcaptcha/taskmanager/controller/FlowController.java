package com.textcaptcha.taskmanager.controller;

import com.textcaptcha.data.model.task.TaskType;
import com.textcaptcha.taskmanager.dto.link.CaptchaFlowDto;
import com.textcaptcha.taskmanager.dto.TaskInstanceDto;
import com.textcaptcha.taskmanager.dto.TaskRequestRequestBody;
import com.textcaptcha.taskmanager.dto.TaskSolutionRequestBody;
import com.textcaptcha.taskmanager.exception.InvalidTaskTypeException;
import com.textcaptcha.taskmanager.pojo.CaptchaTaskFlow;
import com.textcaptcha.taskmanager.service.CaptchaTaskFlowManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flow")
public class FlowController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CaptchaTaskFlowManager taskFlowManager;

    public FlowController(CaptchaTaskFlowManager taskFlowManager) {
        this.taskFlowManager = taskFlowManager;
    }

    @PostMapping("/begin")
    public CaptchaFlowDto beginTaskFlow(@RequestBody TaskRequestRequestBody body) {
        logger.debug("Received flow begin request: " + body.toString());

        try {
            TaskType.valueOf(body.getTaskType());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InvalidTaskTypeException(e);
        }

        CaptchaTaskFlow flow = taskFlowManager.beginFlow(TaskType.valueOf(body.getTaskType()), body.getHashes());

        return new CaptchaFlowDto(TaskInstanceDto.fromIssuedTaskInstance(flow.getTaskInstance()));
    }

    @PostMapping("/continue")
    public CaptchaFlowDto continueTaskFlow(@RequestBody TaskSolutionRequestBody body) {
        logger.debug("Received flow continue request: " + body.toString());

        CaptchaTaskFlow flow = taskFlowManager.continueFlow(body.getId(), body.getContent());

        if (flow.getCaptchaLink().isCompleteTrusted()) {
            return new CaptchaFlowDto(flow.getCaptchaLink().getUuid(), true);
        } else {
            return new CaptchaFlowDto(flow.getTaskInstance().toTaskInstanceDto());
        }
    }

}
