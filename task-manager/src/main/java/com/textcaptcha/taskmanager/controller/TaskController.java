package com.textcaptcha.taskmanager.controller;

import com.textcaptcha.taskmanager.dto.TaskInstanceDto;
import com.textcaptcha.taskmanager.dto.TaskRequestRequestBody;
import com.textcaptcha.taskmanager.dto.TaskSolutionRequestBody;
import com.textcaptcha.taskmanager.dto.TaskSolutionResponseDto;
import com.textcaptcha.taskmanager.model.CaptchaResponse;
import com.textcaptcha.taskmanager.model.CaptchaTask;
import com.textcaptcha.taskmanager.repository.CaptchaResponseRepository;
import com.textcaptcha.taskmanager.repository.CaptchaTaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final CaptchaTaskRepository taskRepository;
    private final CaptchaResponseRepository responseRepository;

    private final Map<String, Long> issuedTasks;

    @Autowired
    public TaskController(
            CaptchaTaskRepository taskRepository,
            CaptchaResponseRepository responseRepository
    ) {
        this.taskRepository = taskRepository;
        this.responseRepository = responseRepository;

        issuedTasks = new HashMap<>();
    }

    @PostMapping("/request")
    public TaskInstanceDto getTask(@RequestBody TaskRequestRequestBody body) {
        logger.debug("Received task request: " + body.toString());

        if (body.getArticleUrl() == null || body.getArticleUid() == null) {
            // TODO what if it's just ingest still in progress? There's a better way to handle this.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request is missing articleUrl and/or articleUid parameter.");
        }

        List<CaptchaTask> tasks = taskRepository.getByArticleUrlAndArticleUid(body.getArticleUrl(), body.getArticleUid());

        if (tasks.isEmpty()) {
            // TODO what to even do here? Is it possible to have a processed article with NO tasks?
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No tasks available for article.");
        }

        Random r = new Random();

        CaptchaTask selectedTask = tasks.get(r.nextInt(tasks.size()));
        String taskInstanceId = String.valueOf(Math.abs(r.nextLong()));

        issuedTasks.put(taskInstanceId, selectedTask.getId());

        logger.debug("Issued task ID " + selectedTask.getId() + " with instance ID " + taskInstanceId + ".");
        return new TaskInstanceDto(taskInstanceId, selectedTask);
    }

    @PostMapping("/response")
    public TaskSolutionResponseDto checkTaskSolution(@RequestBody TaskSolutionRequestBody body) {
        logger.debug("Received task response: " + body.toString());

        String instanceId = body.getId();
        if (!issuedTasks.containsKey(instanceId)) {
            logger.trace("Received task response for invalid instance ID: " + instanceId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid task instance ID.");
        }

        Long taskId = issuedTasks.get(instanceId);
        issuedTasks.remove(instanceId);

        CaptchaTask task = taskRepository.getById(taskId);

        //
        CaptchaResponse r = new CaptchaResponse();
        r.setCaptchaTask(task);
        r.getMarkedTokenIndexList().addAll(body.getIndexes());
        responseRepository.save(r);
        //

        // positive = entity
        // negative = not entity
        // true = selected
        // false = not selected

        int totalPositives = 0;
        int truePositives = 0;
        int trueNegatives = 0;
        for (int i = 0; i < task.getTokens().size(); i++) {
            if (!task.getTokens().get(i).getAnnotation().equals("O")) {
                totalPositives++;
            }

            if (body.getIndexes().contains(i)) {
                if (task.getTokens().get(i).getAnnotation().equals("O")) {
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
