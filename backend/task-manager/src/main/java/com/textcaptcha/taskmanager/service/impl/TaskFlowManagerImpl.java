package com.textcaptcha.taskmanager.service.impl;

import com.textcaptcha.data.model.CaptchaFlow;
import com.textcaptcha.data.model.response.CaptchaTaskResponse;
import com.textcaptcha.data.model.task.CaptchaTask;
import com.textcaptcha.data.model.task.TaskType;
import com.textcaptcha.data.repository.CaptchaFlowRepository;
import com.textcaptcha.data.repository.CaptchaTaskResponseRepository;
import com.textcaptcha.dto.ArticleHashPairDto;
import com.textcaptcha.taskmanager.exception.NoTasksAvailableException;
import com.textcaptcha.taskmanager.exception.TaskSelectionException;
import com.textcaptcha.taskmanager.pojo.CaptchaTaskFlow;
import com.textcaptcha.taskmanager.pojo.IssuedTaskInstance;
import com.textcaptcha.taskmanager.pojo.SolutionProcessorResult;
import com.textcaptcha.taskmanager.service.TaskFlowManager;
import com.textcaptcha.taskmanager.service.TaskInstanceKeeper;
import com.textcaptcha.taskmanager.service.TaskSelectionService;
import com.textcaptcha.taskmanager.service.TaskSolutionProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TaskFlowManagerImpl implements TaskFlowManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
            ConfidenceTaskSelectionService taskSelectionService,
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
    public CaptchaTaskFlow beginFlow(TaskType taskType, ArticleHashPairDto articleHashes) {
        UUID flowId = UUID.randomUUID();
        CaptchaFlow flow = new CaptchaFlow();
        flow.setUuid(flowId);

        IssuedTaskInstance issuedTaskInstance = getTaskInstance(taskType, articleHashes, null);
        if (issuedTaskInstance == null) {
            logger.error("Empty CAPTCHA flow cannot be considered successful.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

        boolean isOk = solutionProcessorResult.getCheckResult().isSuccessful();
        boolean shouldGiveNextTask = true;

        if (!f.isCompleteVerify()) {
            r.setVerify(true);
            if (isOk) {
                f.setCompleteVerify(true);
            }
        } else /*if (!f.isCompleteTrusted())*/ {
            f.setCompleteTrusted(true);
            shouldGiveNextTask = false;
        }

        r = taskResponseRepository.save(r);
        f = captchaFlowRepository.save(f);

        if (!shouldGiveNextTask) {
            return new CaptchaTaskFlow(f, null);
        } else {
            CaptchaTask t = solutionProcessorResult.getTaskResponse().getCaptchaTask();
            IssuedTaskInstance issuedTaskInstance = getTaskInstance(t.getTaskType(), t.getArticleHashes(), f);
            if (issuedTaskInstance == null) {
                f.setCompleteTrusted(true);
                f = captchaFlowRepository.save(f);
                return new CaptchaTaskFlow(f, null);
            }
            taskFlowMapping.put(issuedTaskInstance.getId(), f.getUuid());
            return new CaptchaTaskFlow(f, issuedTaskInstance);
        }
    }

    private IssuedTaskInstance getTaskInstance(TaskType taskType, ArticleHashPairDto articleHashes, CaptchaFlow flow) {
        CaptchaTask task;
        try {
            if (flow != null) {
                try {
                    task = taskSelectionService.getTaskForArticleAndFlow(taskType, articleHashes, flow);
                } catch (NoTasksAvailableException e) {
                    if (flow.isCompleteVerify()) {
                        // if verification is complete and there are no more tasks available, we consider flow
                        // successful.
                        return null;
                    } else {
                        throw e;
                    }
                }

            } else {
                task = taskSelectionService.getTaskForArticle(taskType, articleHashes);
            }
        } catch (TaskSelectionException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        return taskInstanceKeeper.issue(task);
    }

}
