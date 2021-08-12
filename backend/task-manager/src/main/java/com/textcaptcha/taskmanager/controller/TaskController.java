package com.textcaptcha.taskmanager.controller;


import com.textcaptcha.annotation.Loggable;
import com.textcaptcha.data.model.response.CorefCaptchaTaskResponse;
import com.textcaptcha.data.model.response.NerCaptchaTaskResponse;
import com.textcaptcha.data.model.response.content.CorefCaptchaTaskResponseContent;
import com.textcaptcha.data.model.response.content.NerCaptchaTaskResponseContent;
import com.textcaptcha.data.model.task.CorefCaptchaTask;
import com.textcaptcha.data.model.task.NerCaptchaTask;
import com.textcaptcha.data.model.task.TaskType;
import com.textcaptcha.data.model.task.content.CorefCaptchaTaskContent;
import com.textcaptcha.data.repository.CorefCaptchaTaskRepository;
import com.textcaptcha.data.repository.CorefCaptchaTaskResponseRepository;
import com.textcaptcha.data.repository.NerCaptchaTaskRepository;
import com.textcaptcha.data.repository.NerCaptchaTaskResponseRepository;
import com.textcaptcha.taskmanager.dto.*;
import com.textcaptcha.taskmanager.exception.InvalidTaskTypeException;
import com.textcaptcha.taskmanager.pojo.IssuedTaskInstance;
import com.textcaptcha.taskmanager.service.impl.CorefTaskInstanceKeeper;
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
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Loggable
    private Logger logger;

    private final NerTaskInstanceKeeper nerTaskInstanceKeeper;
    private final CorefTaskInstanceKeeper corefTaskInstanceKeeper;

    private final NerCaptchaTaskRepository nerTaskRepository;
    private final CorefCaptchaTaskRepository corefTaskRepository;

    private final NerCaptchaTaskResponseRepository nerResponseRepository;
    private final CorefCaptchaTaskResponseRepository corefResponseRepository;


    @Autowired
    public TaskController(
            NerTaskInstanceKeeper nerTaskInstanceKeeper,
            CorefTaskInstanceKeeper corefTaskInstanceKeeper,
            NerCaptchaTaskRepository nerTaskRepository,
            CorefCaptchaTaskRepository corefTaskRepository,
            NerCaptchaTaskResponseRepository nerResponseRepository,
            CorefCaptchaTaskResponseRepository corefResponseRepository
    ) {
        this.nerTaskInstanceKeeper = nerTaskInstanceKeeper;
        this.corefTaskInstanceKeeper = corefTaskInstanceKeeper;
        this.nerTaskRepository = nerTaskRepository;
        this.corefTaskRepository = corefTaskRepository;

        this.nerResponseRepository = nerResponseRepository;
        this.corefResponseRepository = corefResponseRepository;
    }

    @PostMapping("/request")
    public TaskInstanceDto getTask(@RequestBody TaskRequestRequestBody body) {
        logger.debug("Received task request: " + body.toString());

        TaskType taskType = null;
        try {
            taskType = TaskType.valueOf(body.getTaskType());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InvalidTaskTypeException(e);
        }

        switch (taskType) {
            case NER:
                return getNerTask(body);
            case COREF:
                return getCorefTask(body);
            default:
                // TaskType is null?
                throw new InvalidTaskTypeException(new RuntimeException("TaskType is " + taskType));
        }
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

    private TaskInstanceDto getNerTask(TaskRequestRequestBody body) {
        if (body.getArticleUrlHash() == null || body.getArticleTextHash() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request is missing articleUrlHash and/or articleTextHash parameter(s).");
        }

        List<NerCaptchaTask> tasks = nerTaskRepository.getByArticleUrlHashAndArticleTextHash(body.getArticleUrlHash(), body.getArticleTextHash());

        if (tasks.isEmpty()) {
            // TODO what if it's just ingest still in progress? There's a better way to handle this.
            // TODO what to even do here? Is it possible to have a processed article with NO tasks?
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No tasks available.");
        }

        Random r = new Random();
        NerCaptchaTask selectedTask = tasks.get(r.nextInt(tasks.size()));
        UUID taskInstanceId = nerTaskInstanceKeeper.issue(selectedTask);

        logger.debug("Issued task ID " + selectedTask.getId() + " with instance ID " + taskInstanceId + ".");
        return new NerTaskInstanceDto(taskInstanceId, selectedTask);
    }

    private TaskInstanceDto getCorefTask(TaskRequestRequestBody body) {
        if (body.getArticleUrlHash() == null || body.getArticleTextHash() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request is missing articleUrlHash and/or articleTextHash parameter(s).");
        }

        List<CorefCaptchaTask> tasks = corefTaskRepository.getByArticleUrlHashAndArticleTextHash(body.getArticleUrlHash(), body.getArticleTextHash());

        if (tasks.isEmpty()) {
            // TODO what if it's just ingest still in progress? There's a better way to handle this.
            // TODO what to even do here? Is it possible to have a processed article with NO tasks?
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No tasks available.");
        }

        Random r = new Random();
        CorefCaptchaTask selectedTask = tasks.get(r.nextInt(tasks.size()));
        UUID taskInstanceId = corefTaskInstanceKeeper.issue(selectedTask);

        logger.debug("Issued task ID " + selectedTask.getId() + " with instance ID " + taskInstanceId + ".");
        return new CorefTaskInstanceDto(taskInstanceId, selectedTask);
    }

    private TaskSolutionResponseDto postNerSolution(NerTaskSolutionRequestBody body) {
        UUID instanceId = body.getId();

        IssuedTaskInstance<NerCaptchaTask> taskInstance = nerTaskInstanceKeeper.invalidate(instanceId);

        if (taskInstance == null) {
            logger.trace("Received task response for invalid instance ID: " + instanceId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid task instance ID.");
        }

        NerCaptchaTask task = (NerCaptchaTask) taskInstance.getTask();

        NerCaptchaTaskResponse r = new NerCaptchaTaskResponse();
        r.setCaptchaTask(task);
        r.setContent(new NerCaptchaTaskResponseContent(body.getIndexes()));
        nerResponseRepository.save(r);
        //

        // positive = entity
        // negative = not entity
        // true = selected
        // false = not selected

        int totalPositives = 0;
        int truePositives = 0;
        int trueNegatives = 0;
        for (int i = 0; i < task.getContent().getTokens().size(); i++) {
            boolean isNamedEntity = !task.getContent().getTokens().get(i).getAnnotation().equals("O");
            boolean isSelected = body.getIndexes().contains(i);

            if (isNamedEntity) {
                totalPositives++;
            }

            if (isSelected) {
                if (isNamedEntity) {
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

        IssuedTaskInstance<CorefCaptchaTask> taskInstance = corefTaskInstanceKeeper.invalidate(instanceId);

        if (taskInstance == null) {
            logger.trace("Received task response for invalid instance ID: " + instanceId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid task instance ID.");
        }

        CorefCaptchaTask task = (CorefCaptchaTask) taskInstance.getTask();

        CorefCaptchaTaskResponse r = new CorefCaptchaTaskResponse();
        r.setCaptchaTask(task);
        r.setContent(new CorefCaptchaTaskResponseContent(body.getIndexes()));
        corefResponseRepository.save(r);
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
                if(currentClusterIsCorrect) {
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
