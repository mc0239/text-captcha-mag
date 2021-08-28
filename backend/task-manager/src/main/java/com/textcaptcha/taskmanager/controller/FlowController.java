package com.textcaptcha.taskmanager.controller;

import com.textcaptcha.annotation.Loggable;
import com.textcaptcha.taskmanager.dto.CaptchaFlowDto;
import com.textcaptcha.taskmanager.dto.TaskInstanceDto;
import com.textcaptcha.taskmanager.dto.TaskRequestRequestBody;
import com.textcaptcha.taskmanager.dto.TaskSolutionRequestBody;
import com.textcaptcha.taskmanager.pojo.CaptchaTaskFlow;
import com.textcaptcha.taskmanager.service.TaskFlowManager;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flow")
public class FlowController {

    @Loggable
    Logger logger;

    private final TaskFlowManager taskFlowManager;

    public FlowController(
            TaskFlowManager taskFlowManager
    ) {
        this.taskFlowManager = taskFlowManager;
    }

    @PostMapping("/begin")
    public CaptchaFlowDto beginTaskFlow(@RequestBody TaskRequestRequestBody body) {
        // begins a new captcha flow. server keeps a flowInstanceId and related task instanceIds.
        // returns a task instance and its id.

        CaptchaTaskFlow flow = taskFlowManager.beginFlow(body.getHashes());

        return new CaptchaFlowDto(TaskInstanceDto.fromIssuedTaskInstance(flow.getTaskInstance().getId(), flow.getTaskInstance().getTask()));
    }

    @PostMapping("/continue")
    public CaptchaFlowDto continueTaskFlow(@RequestBody TaskSolutionRequestBody body) {
        // continues a started captcha flow. server validates given task solution and either proceeds to next task or
        // considers task flow successful

        CaptchaTaskFlow flow = taskFlowManager.continueFlow(body.getId(), body.getContent());

        if (flow.getCaptchaFlow().isCompleteTrusted()) {
            return new CaptchaFlowDto(true);
        } else {
            return new CaptchaFlowDto(flow.getTaskInstance().toTaskInstanceDto());
        }
    }
}
