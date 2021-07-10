package com.textcaptcha.taskmanager.controller;


import com.textcaptcha.annotation.Loggable;
import com.textcaptcha.data.model.response.NerCaptchaTaskResponse;
import com.textcaptcha.data.model.response.content.NerCaptchaTaskResponseContent;
import com.textcaptcha.data.model.task.NerCaptchaTask;
import com.textcaptcha.data.repository.NerCaptchaTaskRepository;
import com.textcaptcha.data.repository.NerCaptchaTaskResponseRepository;
import com.textcaptcha.taskmanager.dto.*;
import com.textcaptcha.taskmanager.pojo.IssuedTaskInstance;
import com.textcaptcha.taskmanager.service.impl.NerTaskInstanceKeeper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Loggable
    private Logger logger;

    private final NerTaskInstanceKeeper taskInstanceKeeper;

    private final NerCaptchaTaskRepository taskRepository;
    private final NerCaptchaTaskResponseRepository responseRepository;


    @Autowired
    public TaskController(
            NerTaskInstanceKeeper taskInstanceKeeper,
            NerCaptchaTaskRepository taskRepository,
            NerCaptchaTaskResponseRepository responseRepository
    ) {
        this.taskInstanceKeeper = taskInstanceKeeper;
        this.taskRepository = taskRepository;
        this.responseRepository = responseRepository;
    }

    @PostMapping("/request")
    public TaskInstanceDto getTask(@RequestBody TaskRequestRequestBody body) {
        logger.debug("Received task request: " + body.toString());

        if (body.getArticleUrlHash() == null || body.getArticleTextHash() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request is missing articleUrlHash and/or articleTextHash parameter(s).");
        }

        List<NerCaptchaTask> tasks = taskRepository.getByArticleUrlHashAndArticleTextHash(body.getArticleUrlHash(), body.getArticleTextHash());

        if (tasks.isEmpty()) {
            // TODO what if it's just ingest still in progress? There's a better way to handle this.
            // TODO what to even do here? Is it possible to have a processed article with NO tasks?
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No tasks available.");
        }

        Random r = new Random();
        NerCaptchaTask selectedTask = tasks.get(r.nextInt(tasks.size()));
        UUID taskInstanceId = taskInstanceKeeper.issue(selectedTask);

        logger.debug("Issued task ID " + selectedTask.getId() + " with instance ID " + taskInstanceId + ".");
        return new NerTaskInstanceDto(taskInstanceId, selectedTask);
    }

    @PostMapping("/response")
    public TaskSolutionResponseDto checkTaskSolution(@RequestBody NerTaskSolutionRequestBody body) {
        logger.debug("Received task response: " + body.toString());

        UUID instanceId = body.getId();

        // TODO this is NER-specific
        IssuedTaskInstance<NerCaptchaTask> taskInstance = taskInstanceKeeper.invalidate(instanceId);

        if (taskInstance == null) {
            logger.trace("Received task response for invalid instance ID: " + instanceId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid task instance ID.");
        }

        NerCaptchaTask task = (NerCaptchaTask) taskInstance.getTask();

        NerCaptchaTaskResponse r = new NerCaptchaTaskResponse();
        r.setCaptchaTask(task);
        r.setContent(new NerCaptchaTaskResponseContent(body.getIndexes()));
        responseRepository.save(r);
        //

        // positive = entity
        // negative = not entity
        // true = selected
        // false = not selected

        int totalPositives = 0;
        int truePositives = 0;
        int trueNegatives = 0;
        for (int i = 0; i < task.getContent().getTokens().size(); i++) {
            if (!task.getContent().getTokens().get(i).getAnnotation().equals("O")) {
                totalPositives++;
            }

            if (body.getIndexes().contains(i)) {
                if (task.getContent().getTokens().get(i).getAnnotation().equals("O")) {
                    trueNegatives++;
                } else {
                    truePositives++;
                }
            }
        }

        TaskSolutionResponseDto response = new TaskSolutionResponseDto();
        response.setContent("You selected " + truePositives + "/" + totalPositives + " entities and made " + trueNegatives + " errors.");

        return response;
    }

}
