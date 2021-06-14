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
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/task")
public class TaskController {

    private static final long INSTANCE_EXPIRATION = 1000 * 60 * 2;

    private final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final CaptchaTaskRepository taskRepository;
    private final CaptchaResponseRepository responseRepository;

    // instanceId --> (taskId, createdAt)
    private final Map<UUID, Pair<Long, Date>> issuedTasks;

    @Autowired
    public TaskController(
            CaptchaTaskRepository taskRepository,
            CaptchaResponseRepository responseRepository
    ) {
        this.taskRepository = taskRepository;
        this.responseRepository = responseRepository;

        issuedTasks = new ConcurrentHashMap<>();
    }

    @PostMapping("/request")
    public TaskInstanceDto getTask(@RequestBody TaskRequestRequestBody body) {
        logger.debug("Received task request: " + body.toString());

        if (body.getArticleUrlHash() == null || body.getArticleTextHash() == null) {
            // TODO what if it's just ingest still in progress? There's a better way to handle this.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request is missing articleUrlHash and/or articleTextHash parameter(s).");
        }

        List<CaptchaTask> tasks = taskRepository.getByArticleUrlHashAndArticleTextHash(body.getArticleUrlHash(), body.getArticleTextHash());

        if (tasks.isEmpty()) {
            // TODO what to even do here? Is it possible to have a processed article with NO tasks?
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No tasks available.");
        }

        Random r = new Random();

        CaptchaTask selectedTask = tasks.get(r.nextInt(tasks.size()));
        UUID taskInstanceId = UUID.randomUUID();

        issuedTasks.put(taskInstanceId, Pair.of(selectedTask.getId(), new Date()));

        logger.debug("Issued task ID " + selectedTask.getId() + " with instance ID " + taskInstanceId + ".");
        return new TaskInstanceDto(taskInstanceId, selectedTask);
    }

    @PostMapping("/response")
    public TaskSolutionResponseDto checkTaskSolution(@RequestBody TaskSolutionRequestBody body) {
        logger.debug("Received task response: " + body.toString());

        UUID instanceId = body.getId();
        if (!issuedTasks.containsKey(instanceId)) {
            logger.trace("Received task response for invalid instance ID: " + instanceId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid task instance ID.");
        }

        Pair<Long, Date> taskInstance = issuedTasks.get(instanceId);
        issuedTasks.remove(instanceId);

        if (isExpiredInstance(taskInstance)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid task instance ID.");
        }

        CaptchaTask task = taskRepository.getById(taskInstance.getFirst());

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

    @Scheduled(fixedDelay = INSTANCE_EXPIRATION * 4)
    private void cleanupExpiredInstances() {
        int countRemoved = 0;
        for (Iterator<Map.Entry<UUID, Pair<Long, Date>>> i = issuedTasks.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry<UUID, Pair<Long, Date>> entry = i.next();
            if (isExpiredInstance(entry.getValue())) {
                i.remove();
                countRemoved++;
            }
        }

        if (countRemoved > 0) {
            logger.trace("Cleaned up " + countRemoved + " expired task instances.");
        }
    }

    private static boolean isExpiredInstance(Pair<Long, Date> instance) {
        return instance.getSecond().toInstant().plus(INSTANCE_EXPIRATION, ChronoUnit.MILLIS).isBefore(Instant.now());
    }

}
