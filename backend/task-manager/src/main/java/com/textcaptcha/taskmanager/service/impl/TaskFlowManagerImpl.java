package com.textcaptcha.taskmanager.service.impl;

import com.textcaptcha.annotation.Loggable;
import com.textcaptcha.data.model.CaptchaFlow;
import com.textcaptcha.data.model.response.CaptchaTaskResponse;
import com.textcaptcha.data.model.task.CaptchaTask;
import com.textcaptcha.data.model.task.TaskType;
import com.textcaptcha.data.repository.CaptchaFlowRepository;
import com.textcaptcha.data.repository.CaptchaTaskResponseRepository;
import com.textcaptcha.dto.ArticleHashPairDto;
import com.textcaptcha.taskmanager.exception.TaskSelectionException;
import com.textcaptcha.taskmanager.pojo.CaptchaTaskFlow;
import com.textcaptcha.taskmanager.pojo.IssuedTaskInstance;
import com.textcaptcha.taskmanager.pojo.SolutionProcessorResult;
import com.textcaptcha.taskmanager.service.TaskFlowManager;
import com.textcaptcha.taskmanager.service.TaskInstanceKeeper;
import com.textcaptcha.taskmanager.service.TaskSelectionService;
import com.textcaptcha.taskmanager.service.TaskSolutionProcessor;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TaskFlowManagerImpl implements TaskFlowManager {

    @Loggable
    Logger logger;

    private final CaptchaFlowRepository captchaFlowRepository;
    private final CaptchaTaskResponseRepository taskResponseRepository;
    private final TaskInstanceKeeper taskInstanceKeeper;

    private final TaskSelectionService taskSelectionService;
    private final TaskSolutionProcessor taskSolutionProcessor;

    private final Map<UUID, UUID> taskFlowMapping;

    public TaskFlowManagerImpl(
            CaptchaFlowRepository captchaFlowRepository,
            CaptchaTaskResponseRepository taskResponseRepository,
            TaskInstanceKeeper taskInstanceKeeper,
            TaskSelectionService taskSelectionService,
            TaskSolutionProcessor taskSolutionProcessor
    ) {
        this.captchaFlowRepository = captchaFlowRepository;
        this.taskResponseRepository = taskResponseRepository;
        this.taskInstanceKeeper = taskInstanceKeeper;
        this.taskSelectionService = taskSelectionService;
        this.taskSolutionProcessor = taskSolutionProcessor;
        taskFlowMapping = new HashMap<>();
    }

    @Override
    public CaptchaTaskFlow beginFlow(ArticleHashPairDto articleHashes) {
        UUID flowId = UUID.randomUUID();
        CaptchaFlow flow = new CaptchaFlow();
        flow.setUuid(flowId);

        // TODO flow always starts with NER type task.
        IssuedTaskInstance issuedTaskInstance = getTaskInstance(TaskType.NER, articleHashes);
        taskFlowMapping.put(issuedTaskInstance.getId(), flowId);

        return new CaptchaTaskFlow(flow, issuedTaskInstance);
    }

    @Override
    public CaptchaTaskFlow continueFlow(UUID taskInstanceId, List<Integer> taskSolution) {
        UUID flowInstanceId = taskFlowMapping.get(taskInstanceId);
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

        SolutionProcessorResult solutionProcessorResult = taskSolutionProcessor.processSolution(taskInstanceId, taskSolution);

        CaptchaTaskResponse r = solutionProcessorResult.getTaskResponse();
        r.setCaptchaFlow(f);
        r = taskResponseRepository.save(r);

        boolean isOk = true; // TODO
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
                f = captchaFlowRepository.save(f);
                shouldGiveNextTask = false;
            } else {
                // do nothing
            }
        }

        if (!shouldGiveNextTask) {
            return new CaptchaTaskFlow(f, null);
        } else {
            CaptchaTask t = solutionProcessorResult.getTaskResponse().getCaptchaTask();
            IssuedTaskInstance issuedTaskInstance = getTaskInstance(t.getTaskType(), t.getArticleHashes());
            taskFlowMapping.put(issuedTaskInstance.getId(), f.getUuid());
            return new CaptchaTaskFlow(f, issuedTaskInstance);
        }
    }

    private IssuedTaskInstance getTaskInstance(TaskType taskType, ArticleHashPairDto articleHashes) {
        CaptchaTask task;
        try {
            task = taskSelectionService.getTask(taskType, articleHashes);
        } catch (TaskSelectionException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        return taskInstanceKeeper.issue(task);
    }

}
