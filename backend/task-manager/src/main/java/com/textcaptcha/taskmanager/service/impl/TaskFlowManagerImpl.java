package com.textcaptcha.taskmanager.service.impl;

import com.textcaptcha.annotation.Loggable;
import com.textcaptcha.data.model.CaptchaFlow;
import com.textcaptcha.data.model.response.NerCaptchaTaskResponse;
import com.textcaptcha.data.model.response.content.NerCaptchaTaskResponseContent;
import com.textcaptcha.data.model.task.CaptchaTask;
import com.textcaptcha.data.model.task.TaskType;
import com.textcaptcha.data.repository.CaptchaFlowRepository;
import com.textcaptcha.data.repository.CaptchaTaskRepository;
import com.textcaptcha.data.repository.CaptchaTaskResponseRepository;
import com.textcaptcha.taskmanager.dto.TaskRequestRequestBody;
import com.textcaptcha.taskmanager.dto.TaskSolutionRequestBody;
import com.textcaptcha.taskmanager.pojo.CaptchaTaskFlow;
import com.textcaptcha.taskmanager.pojo.IssuedTaskInstance;
import com.textcaptcha.taskmanager.service.TaskFlowManager;
import com.textcaptcha.taskmanager.service.TaskInstanceKeeper;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class TaskFlowManagerImpl implements TaskFlowManager {

    @Loggable
    Logger logger;

    private final CaptchaTaskRepository captchaTaskRepository;
    private final CaptchaFlowRepository captchaFlowRepository;
    private final CaptchaTaskResponseRepository taskResponseRepository;
    private final TaskInstanceKeeper taskInstanceKeeper;

    private final Map<UUID, UUID> taskFlowMapping;

    public TaskFlowManagerImpl(
            CaptchaTaskRepository captchaTaskRepository,
            CaptchaFlowRepository captchaFlowRepository,
            CaptchaTaskResponseRepository taskResponseRepository,
            TaskInstanceKeeper taskInstanceKeeper
    ) {
        this.captchaTaskRepository = captchaTaskRepository;
        this.captchaFlowRepository = captchaFlowRepository;
        this.taskResponseRepository = taskResponseRepository;
        this.taskInstanceKeeper = taskInstanceKeeper;
        taskFlowMapping = new HashMap<>();
    }

    @Override
    public CaptchaTaskFlow beginFlow(TaskRequestRequestBody body) {
        UUID flowId = UUID.randomUUID();
        CaptchaFlow flow = new CaptchaFlow();
        flow.setUuid(flowId);

        IssuedTaskInstance issuedTaskInstance = getTaskInstance(body);
        taskFlowMapping.put(issuedTaskInstance.getId(), flowId);

        return new CaptchaTaskFlow(flow, issuedTaskInstance);
    }

    @Override
    public CaptchaTaskFlow continueFlow(TaskSolutionRequestBody body) {
        UUID taskInstanceId = body.getId();
        IssuedTaskInstance taskInstance = taskInstanceKeeper.invalidate(taskInstanceId);

        UUID flowInstanceId = taskFlowMapping.get(taskInstanceId);

        if (taskInstance == null) {
            logger.trace("Received task response for invalid instance ID: " + taskInstanceId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid task instance ID.");
        }

        if (flowInstanceId == null) {
            logger.trace("Received task response for invalid flow ID: " + flowInstanceId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid task instance ID.");
        }

        CaptchaFlow f = captchaFlowRepository.getByUuid(flowInstanceId);
        if (f == null) {
            f = new CaptchaFlow();
            f.setUuid(flowInstanceId);
            f = captchaFlowRepository.save(f);
        }

        boolean isOk = verifyTaskInstance(taskInstance, f, body);
        boolean shouldGiveNextTask = true;

        if (!f.isCompleteSanity()) {
            if (isOk) {
                f.setCompleteSanity(true);
                f = captchaFlowRepository.save(f);
            } else {
                // do nothing
            }
        } else if (!f.isCompleteTrusted()) {
            if (isOk) {
                f.setCompleteTrusted(true);
                shouldGiveNextTask = false;
            } else {
                // do nothing
            }
        }

        if (!shouldGiveNextTask) {
            return new CaptchaTaskFlow(f, null);
        } else {
            TaskRequestRequestBody b = new TaskRequestRequestBody();
            b.setTaskType(taskInstance.getTask().getTaskType().name());
            b.setArticleUrlHash(taskInstance.getTask().getArticleUrlHash());
            b.setArticleTextHash(taskInstance.getTask().getArticleTextHash());
            IssuedTaskInstance issuedTaskInstance = getTaskInstance(b);
            taskFlowMapping.put(issuedTaskInstance.getId(), f.getUuid());
            return new CaptchaTaskFlow(f, issuedTaskInstance);
        }
    }

    private IssuedTaskInstance getTaskInstance(TaskRequestRequestBody body) {
        if (body.getArticleUrlHash() == null || body.getArticleTextHash() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request is missing articleUrlHash and/or articleTextHash parameter(s).");
        }

        List<CaptchaTask> tasks = captchaTaskRepository.getTasks(TaskType.valueOf(body.getTaskType()), body.getArticleUrlHash(), body.getArticleTextHash());

        if (tasks.isEmpty()) {
            // TODO what if it's just ingest still in progress? There's a better way to handle this.
            // TODO what to even do here? Is it possible to have a processed article with NO tasks?
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No tasks available.");
        }

        Random r = new Random();
        CaptchaTask selectedTask = tasks.get(r.nextInt(tasks.size()));
        UUID taskInstanceId = taskInstanceKeeper.issue(selectedTask);

        logger.debug("Issued task ID " + selectedTask.getId() + " with instance ID " + taskInstanceId + ".");
        return new IssuedTaskInstance(taskInstanceId, selectedTask);
    }

    private boolean verifyTaskInstance(IssuedTaskInstance taskInstance, CaptchaFlow flow, /*Ner*/TaskSolutionRequestBody body) {
        UUID instanceId = taskInstance.getId();
        CaptchaTask task = taskInstance.getTask();

        NerCaptchaTaskResponse r = new NerCaptchaTaskResponse();
        r.setCaptchaTask(task);
        r.setContent(new NerCaptchaTaskResponseContent());
        r.setCaptchaFlow(flow);
        r = taskResponseRepository.save(r);

        return true;
    }
}
