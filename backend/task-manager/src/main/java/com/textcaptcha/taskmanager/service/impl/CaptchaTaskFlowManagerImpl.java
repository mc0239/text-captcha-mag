package com.textcaptcha.taskmanager.service.impl;

import com.textcaptcha.data.model.CaptchaLink;
import com.textcaptcha.data.model.response.CaptchaTaskResponse;
import com.textcaptcha.data.model.task.CaptchaTask;
import com.textcaptcha.data.model.task.TaskType;
import com.textcaptcha.data.repository.CaptchaLinkRepository;
import com.textcaptcha.data.repository.CaptchaTaskResponseRepository;
import com.textcaptcha.dto.ArticleHashPairDto;
import com.textcaptcha.taskmanager.exception.NoTasksAvailableException;
import com.textcaptcha.taskmanager.exception.TaskSelectionException;
import com.textcaptcha.taskmanager.pojo.CaptchaTaskFlow;
import com.textcaptcha.taskmanager.pojo.IssuedTaskInstance;
import com.textcaptcha.taskmanager.pojo.SolutionCheckerResult;
import com.textcaptcha.taskmanager.pojo.SolutionProcessorResult;
import com.textcaptcha.taskmanager.pojo.selection.ConfidenceSelectionOptions;
import com.textcaptcha.taskmanager.service.CaptchaTaskFlowManager;
import com.textcaptcha.taskmanager.service.TaskInstanceKeeper;
import com.textcaptcha.taskmanager.service.TaskSolutionProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CaptchaTaskFlowManagerImpl extends BaseCaptchaLinkTaskManager implements CaptchaTaskFlowManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CaptchaTaskResponseRepository taskResponseRepository;
    private final TaskInstanceKeeper taskInstanceKeeper;

    private final ConfidenceTaskSelectionService taskSelectionService;
    private final TaskSolutionProcessor taskSolutionProcessor;

    private final Map<UUID, UUID> taskFlowMapping;

    public CaptchaTaskFlowManagerImpl(
            CaptchaLinkRepository captchaFlowRepository,
            CaptchaTaskResponseRepository taskResponseRepository,
            TaskInstanceKeeper taskInstanceKeeper,
            ConfidenceTaskSelectionService taskSelectionService,
            TaskSolutionProcessor taskSolutionProcessor
    ) {
        super(captchaFlowRepository);
        this.taskResponseRepository = taskResponseRepository;
        this.taskInstanceKeeper = taskInstanceKeeper;
        this.taskSelectionService = taskSelectionService;
        this.taskSolutionProcessor = taskSolutionProcessor;
        taskFlowMapping = new HashMap<>();
    }

    @Override
    public CaptchaTaskFlow beginFlow(TaskType taskType, ArticleHashPairDto articleHashes) {
        UUID linkId = UUID.randomUUID();
        CaptchaLink link = new CaptchaLink();
        link.setUuid(linkId);

        IssuedTaskInstance issuedTaskInstance = getTaskInstance(taskType, articleHashes, null);
        if (issuedTaskInstance == null) {
            logger.error("Empty CAPTCHA flow cannot be considered successful.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        taskFlowMapping.put(issuedTaskInstance.getId(), linkId);

        return new CaptchaTaskFlow(link, issuedTaskInstance);
    }

    @Override
    public CaptchaTaskFlow continueFlow(UUID taskInstanceId, List<Integer> taskSolution) {
        UUID linkInstanceId = taskFlowMapping.get(taskInstanceId);
        if (linkInstanceId == null) {
            logger.trace("Received task response for invalid flow ID: " + linkInstanceId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid task instance ID.");
        }

        CaptchaLink link = captchaLinkRepository.getByUuid(linkInstanceId);
        if (link == null) {
            link = new CaptchaLink();
            link.setUuid(linkInstanceId);
            link = captchaLinkRepository.save(link);
        }

        SolutionProcessorResult solutionProcessorResult = taskSolutionProcessor.processSolution(taskInstanceId, taskSolution);
        SolutionCheckerResult solutionCheckerResult = solutionProcessorResult.getCheckResult();

        CaptchaTaskResponse r = solutionProcessorResult.getTaskResponse();
        r.setCaptchaLink(link);

        double verifySensitivityThreshold = 0.75;
        double verifySpecificityThreshold = 0.75;

        double trustedSensitivityThreshold = 0.25;
        double trustedSpecificityThreshold = 0.25;

        boolean shouldGiveNextTask = true;
        if (!link.isCompleteVerify()) {
            r.setVerify(true);
            if (solutionCheckerResult.isSuccessful(verifySensitivityThreshold, verifySpecificityThreshold)) {
                link.setCompleteVerify(true);
            }

        } else {
            if (solutionCheckerResult.isSuccessful(trustedSensitivityThreshold, trustedSpecificityThreshold)) {
                link.setCompleteTrusted(true);
                shouldGiveNextTask = false;
            }

        }

        r = taskResponseRepository.save(r);
        link = captchaLinkRepository.save(link);

        if (!shouldGiveNextTask) {
            return new CaptchaTaskFlow(link, null);
        } else {
            CaptchaTask t = solutionProcessorResult.getTaskResponse().getCaptchaTask();
            IssuedTaskInstance issuedTaskInstance = getTaskInstance(t.getTaskType(), t.getArticleHashes(), link);
            if (issuedTaskInstance == null) {
                link.setCompleteTrusted(true);
                link = captchaLinkRepository.save(link);
                return new CaptchaTaskFlow(link, null);
            }
            taskFlowMapping.put(issuedTaskInstance.getId(), link.getUuid());
            return new CaptchaTaskFlow(link, issuedTaskInstance);
        }
    }

    private IssuedTaskInstance getTaskInstance(TaskType taskType, ArticleHashPairDto articleHashes, CaptchaLink link) {
        CaptchaTask task;
        try {
            if (link != null) {
                try {
                    task = taskSelectionService.getTaskForArticle(taskType, articleHashes, new ConfidenceSelectionOptions(
                            switch (taskType) {
                                case COREF -> 0.7f;
                                case NER -> 0.9f;
                            },
                            !link.isCompleteVerify(),
                            taskResponseRepository.getCaptchaTaskResponseByCaptchaLinkUuid(link.getUuid()).stream().map(r -> r.getCaptchaTask().getId()).distinct().collect(Collectors.toList())
                    ));
                } catch (NoTasksAvailableException e) {
                    if (link.isCompleteVerify()) {
                        // if verification is complete and there are no more tasks available, we consider link
                        // successful.
                        return null;
                    } else {
                        throw e;
                    }
                }

            } else {
                task = taskSelectionService.getTaskForArticle(taskType, articleHashes, new ConfidenceSelectionOptions(
                        switch (taskType) {
                            case COREF -> 0.7f;
                            case NER -> 0.9f;
                        },
                        true,
                        Collections.emptyList()
                ));
            }
        } catch (TaskSelectionException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        return taskInstanceKeeper.issue(task);
    }

}
