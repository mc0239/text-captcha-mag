package com.textcaptcha.taskmanager.controller;


import com.textcaptcha.annotation.Loggable;
import com.textcaptcha.data.model.response.CorefCaptchaTaskResponse;
import com.textcaptcha.data.model.response.NerCaptchaTaskResponse;
import com.textcaptcha.data.model.response.content.CorefCaptchaTaskResponseContent;
import com.textcaptcha.data.model.response.content.NerCaptchaTaskResponseContent;
import com.textcaptcha.data.model.task.CaptchaTask;
import com.textcaptcha.data.model.task.CorefCaptchaTask;
import com.textcaptcha.data.model.task.NerCaptchaTask;
import com.textcaptcha.data.model.task.TaskType;
import com.textcaptcha.data.model.task.content.CorefCaptchaTaskContent;
import com.textcaptcha.data.model.task.content.NerCaptchaTaskContent;
import com.textcaptcha.data.repository.CaptchaTaskResponseRepository;
import com.textcaptcha.taskmanager.dto.*;
import com.textcaptcha.taskmanager.exception.InvalidTaskTypeException;
import com.textcaptcha.taskmanager.exception.TaskSelectionException;
import com.textcaptcha.taskmanager.pojo.IssuedTaskInstance;
import com.textcaptcha.taskmanager.service.TaskInstanceKeeper;
import com.textcaptcha.taskmanager.service.TaskSelectionService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Loggable
    private Logger logger;

    private final TaskInstanceKeeper taskInstanceKeeper;
    private final CaptchaTaskResponseRepository taskResponseRepository;
    private final TaskSelectionService taskSelectionService;

    @Autowired
    public TaskController(
            TaskInstanceKeeper taskInstanceKeeper,
            CaptchaTaskResponseRepository taskResponseRepository,
            TaskSelectionService taskSelectionService
    ) {
        this.taskInstanceKeeper = taskInstanceKeeper;
        this.taskResponseRepository = taskResponseRepository;
        this.taskSelectionService = taskSelectionService;
    }

    @PostMapping("/request")
    public TaskInstanceDto getTask(@RequestBody TaskRequestRequestBody body) {
        logger.debug("Received task request: " + body.toString());

        try {
            TaskType.valueOf(body.getTaskType());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InvalidTaskTypeException(e);
        }

        return getTaskInstance(body);
    }

    @PostMapping("/response")
    public TaskSolutionResponseDto postSolution(@RequestBody TaskSolutionRequestBody body) {
        logger.debug("Received task response: " + body.toString());

        if (body instanceof NerTaskSolutionRequestBody) {
            return postNerSolution((NerTaskSolutionRequestBody) body);
        } else if (body instanceof CorefTaskSolutionRequestBody) {
            return postCorefSolution((CorefTaskSolutionRequestBody) body);
        } else {
            throw new InvalidTaskTypeException();
        }
    }

    private TaskInstanceDto getTaskInstance(TaskRequestRequestBody body) {
        CaptchaTask task;
        try {
            task = taskSelectionService.getTask(TaskType.valueOf(body.getTaskType()), body.getHashes());
        } catch (TaskSelectionException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

        UUID taskInstanceId = taskInstanceKeeper.issue(task);

        logger.debug("Issued task ID " + task.getId() + " with instance ID " + taskInstanceId + ".");
        return TaskInstanceDto.fromIssuedTaskInstance(taskInstanceId, task);
    }

    private TaskSolutionResponseDto postNerSolution(NerTaskSolutionRequestBody body) {
        UUID instanceId = body.getId();

        IssuedTaskInstance taskInstance = taskInstanceKeeper.invalidate(instanceId);

        if (taskInstance == null) {
            logger.trace("Received task response for invalid instance ID: " + instanceId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid task instance ID.");
        }

        NerCaptchaTask task = (NerCaptchaTask) taskInstance.getTask();

        NerCaptchaTaskResponse r = new NerCaptchaTaskResponse();
        r.setCaptchaTask(task);
        r.setContent(new NerCaptchaTaskResponseContent(body.getIndexes()));
        taskResponseRepository.save(r);
        //

        // positive = entity
        // negative = not entity
        // true = selected
        // false = not selected

        int totalPositives = 0;
        int truePositives = 0;
        int trueNegatives = 0;

        String primaryAnnotation = task.getContent().getPrimaryAnnotation().split("-")[1];
        for (int i = 0; i < task.getContent().getTokens().size(); i++) {
            NerCaptchaTaskContent.Token word = task.getContent().getTokens().get(i);
            boolean isNamedEntity = !word.getAnnotation().equals("O");
            boolean hasPrimaryAnnotation = word.getAnnotation().contains(primaryAnnotation);
            boolean isSelected = body.getIndexes().contains(i);

            if (isNamedEntity && hasPrimaryAnnotation) {
                totalPositives++;
            }

            if (isSelected) {
                if (isNamedEntity && hasPrimaryAnnotation) {
                    truePositives++;
                } else {
                    trueNegatives++;
                }
            }
        }

        TaskSolutionResponseDto response = new TaskSolutionResponseDto();
        response.setContent("You selected " + truePositives + "/" + totalPositives + " entities and made " + ((totalPositives - truePositives) + trueNegatives) + " errors.");

        return response;
    }

    private TaskSolutionResponseDto postCorefSolution(CorefTaskSolutionRequestBody body) {
        UUID instanceId = body.getId();

        IssuedTaskInstance taskInstance = taskInstanceKeeper.invalidate(instanceId);

        if (taskInstance == null) {
            logger.trace("Received task response for invalid instance ID: " + instanceId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid task instance ID.");
        }

        CorefCaptchaTask task = (CorefCaptchaTask) taskInstance.getTask();

        CorefCaptchaTaskResponse r = new CorefCaptchaTaskResponse();
        r.setCaptchaTask(task);
        r.setContent(new CorefCaptchaTaskResponseContent(body.getIndexes()));
        taskResponseRepository.save(r);
        //

        List<CorefCaptchaTaskContent.Token> mentionOfInterest = task.getContent().getMentionOfInterest();
        Integer correctClusterId = clusterIdFromMention(mentionOfInterest);

        if (correctClusterId == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // positive = entity
        // negative = not entity
        // true = selected
        // false = not selected

        int totalPositives = 0;
        int truePositives = 0;
        int trueNegatives = 0;
        for (int i = 0; i < task.getContent().getSuggestedMentions().size(); i++) {
            List<CorefCaptchaTaskContent.Token> suggestedMention = task.getContent().getSuggestedMentions().get(i);
            Integer currentClusterId = clusterIdFromMention(suggestedMention);
            boolean currentClusterIsCorrect = correctClusterId.equals(currentClusterId);
            boolean isSelected = body.getIndexes().contains(i);

            if (currentClusterIsCorrect) {
                totalPositives++;
            }

            if (isSelected) {
                if (currentClusterIsCorrect) {
                    truePositives++;
                } else {
                    trueNegatives++;
                }
            }
        }

        TaskSolutionResponseDto response = new TaskSolutionResponseDto();
        response.setContent("You selected " + truePositives + "/" + totalPositives + " mentions and made " + ((totalPositives - truePositives) + trueNegatives) + " errors.");

        return response;
    }

    private Integer clusterIdFromMention(List<CorefCaptchaTaskContent.Token> mention) {
        Optional<CorefCaptchaTaskContent.Token> t = mention
                .stream()
                .filter(m -> m.getClusterId() != null)
                .findFirst();

        if (t.isPresent()) {
            return t.get().getClusterId();
        } else {
            return null;
        }
    }

}
