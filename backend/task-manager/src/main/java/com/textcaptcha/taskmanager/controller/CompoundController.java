package com.textcaptcha.taskmanager.controller;

import com.textcaptcha.data.model.task.TaskType;
import com.textcaptcha.taskmanager.dto.link.CaptchaCompoundDto;
import com.textcaptcha.taskmanager.dto.CompoundTaskSolutionRequestBody;
import com.textcaptcha.taskmanager.dto.TaskRequestRequestBody;
import com.textcaptcha.taskmanager.exception.InvalidTaskTypeException;
import com.textcaptcha.taskmanager.pojo.CaptchaTaskCompound;
import com.textcaptcha.taskmanager.pojo.IssuedTaskInstance;
import com.textcaptcha.taskmanager.service.CaptchaTaskCompoundManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/comp")
public class CompoundController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CaptchaTaskCompoundManager taskCompoundManager;

    public CompoundController(CaptchaTaskCompoundManager taskCompoundManager) {
        this.taskCompoundManager = taskCompoundManager;
    }

    @PostMapping("/start")
    public CaptchaCompoundDto startCaptchaCompound(@RequestBody TaskRequestRequestBody body) {
        logger.debug("Received compound start request: " + body.toString());

        TaskType taskType;
        try {
            taskType = TaskType.valueOf(body.getTaskType());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InvalidTaskTypeException(e);
        }

        CaptchaTaskCompound compound = taskCompoundManager.startCompound(taskType, body.getHashes());
        return new CaptchaCompoundDto(compound.getTaskInstances().stream().map(IssuedTaskInstance::toTaskInstanceDto).collect(Collectors.toList()));
    }

    @PostMapping("/solve")
    public CaptchaCompoundDto solveCaptchaCompound(@RequestBody CompoundTaskSolutionRequestBody body) {
        logger.debug("Received compound solve request: " + body.toString());

        CaptchaTaskCompound comp = taskCompoundManager.solveCompound(body);

        if (comp.getCaptchaLink().isCompleteTrusted()) {
            return new CaptchaCompoundDto(comp.getCaptchaLink().getUuid(), true);
        } else {
            return new CaptchaCompoundDto(comp.getTaskInstances().stream().map(IssuedTaskInstance::toTaskInstanceDto).collect(Collectors.toList()));
        }
    }


}
