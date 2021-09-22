package com.textcaptcha.taskmanager.service.impl;

import com.textcaptcha.data.model.CaptchaLink;
import com.textcaptcha.data.model.response.CaptchaTaskResponse;
import com.textcaptcha.data.model.task.CaptchaTask;
import com.textcaptcha.data.model.task.TaskType;
import com.textcaptcha.data.repository.CaptchaLinkRepository;
import com.textcaptcha.data.repository.CaptchaTaskResponseRepository;
import com.textcaptcha.dto.ArticleHashPairDto;
import com.textcaptcha.taskmanager.dto.CompoundTaskSolutionRequestBody;
import com.textcaptcha.taskmanager.dto.TaskSolutionRequestBody;
import com.textcaptcha.taskmanager.exception.TaskSelectionException;
import com.textcaptcha.taskmanager.pojo.CaptchaTaskCompound;
import com.textcaptcha.taskmanager.pojo.IssuedTaskInstance;
import com.textcaptcha.taskmanager.pojo.SolutionCheckerResult;
import com.textcaptcha.taskmanager.pojo.SolutionProcessorResult;
import com.textcaptcha.taskmanager.pojo.selection.ConfidenceSelectionOptions;
import com.textcaptcha.taskmanager.service.CaptchaTaskCompoundManager;
import com.textcaptcha.taskmanager.service.TaskInstanceKeeper;
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
import java.util.stream.Collectors;

@Service
public class CaptchaTaskCompoundManagerImpl extends BaseCaptchaLinkTaskManager implements CaptchaTaskCompoundManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CaptchaTaskResponseRepository taskResponseRepository;
    private final TaskInstanceKeeper taskInstanceKeeper;

    private final ConfidenceTaskSelectionService taskSelectionService;
    private final TaskSolutionProcessor taskSolutionProcessor;

    private final Map<UUID, UUID> taskLinkMapping;

    public CaptchaTaskCompoundManagerImpl(
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
        taskLinkMapping = new HashMap<>();
    }

    @Override
    public CaptchaTaskCompound startCompound(TaskType taskType, ArticleHashPairDto articleHashes) {
        return startCompound(taskType, articleHashes, null);
    }

    public CaptchaTaskCompound startCompound(TaskType taskType, ArticleHashPairDto articleHashes, CaptchaLink link) {
        UUID linkId;
        if (link == null) {
            linkId = UUID.randomUUID();
            link = new CaptchaLink();
            link.setUuid(linkId);
        } else {
            linkId = link.getUuid();
        }

        CaptchaTask validationTask;
        CaptchaTask additionalTask;
        try {
            validationTask = taskSelectionService.getTaskForArticle(taskType, articleHashes, new ConfidenceSelectionOptions(
                    switch (taskType) {
                        case COREF -> 0.7f;
                        case NER -> 0.9f;
                    },
                    true,
                    getTaskIdsForCaptchaLink(linkId)
            ));

            if (validationTask == null) {
                logger.error("Incomplete CAPTCHA link cannot be considered successful.");
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            additionalTask = taskSelectionService.getTaskForArticle(taskType, articleHashes, new ConfidenceSelectionOptions(
                    switch (taskType) {
                        case COREF -> 0.7f;
                        case NER -> 0.9f;
                    },
                    false,
                    getTaskIdsForCaptchaLink(linkId, validationTask.getId())
            ));

            if (additionalTask == null) {
                logger.error("Incomplete CAPTCHA link cannot be considered successful.");
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (TaskSelectionException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

        IssuedTaskInstance issuedValidationTask = taskInstanceKeeper.issue(validationTask);
        taskLinkMapping.put(issuedValidationTask.getId(), linkId);

        return new CaptchaTaskCompound(link, issuedValidationTask, taskInstanceKeeper.issue(additionalTask));
    }

    @Override
    public CaptchaTaskCompound solveCompound(CompoundTaskSolutionRequestBody response) {
        UUID linkId = null;
        UUID verificationTaskSolutionInstanceId = null;
        for (TaskSolutionRequestBody solution : response.getTaskSolutions()) {
            if (taskLinkMapping.containsKey(solution.getId())) {
                linkId = taskLinkMapping.get(solution.getId());
                verificationTaskSolutionInstanceId = solution.getId();
                break;
            }
        }

        if (linkId == null) {
            logger.trace("Received task response for invalid link ID: " + linkId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid task instance ID.");
        }

        CaptchaLink link = captchaLinkRepository.getByUuid(linkId);
        if (link == null) {
            link = new CaptchaLink();
            link.setUuid(linkId);
            link = captchaLinkRepository.save(link);
        }

        TaskType taskType = null;
        ArticleHashPairDto articleHashes = null;
        for (TaskSolutionRequestBody solution : response.getTaskSolutions()) {
            boolean isVerificationTask = solution.getId().equals(verificationTaskSolutionInstanceId);

            SolutionProcessorResult solutionProcessorResult = taskSolutionProcessor.processSolution(solution.getId(), solution.getContent());
            SolutionCheckerResult solutionCheckerResult = solutionProcessorResult.getCheckResult();

            CaptchaTaskResponse r = solutionProcessorResult.getTaskResponse();
            r.setCaptchaLink(link);

            double verifySensitivityThreshold = 0.75;
            double verifySpecificityThreshold = 0.75;

            double trustedSensitivityThreshold = 0.25;
            double trustedSpecificityThreshold = 0.25;

            if (isVerificationTask) {
                taskType = solutionProcessorResult.getTaskResponse().getCaptchaTask().getTaskType();
                articleHashes = solutionProcessorResult.getTaskResponse().getCaptchaTask().getArticleHashes();

                r.setVerify(true);
                if (solutionCheckerResult.isSuccessful(verifySensitivityThreshold, verifySpecificityThreshold)) {
                    link.setCompleteVerify(true);
                }
            } else {
                if (solutionCheckerResult.isSuccessful(trustedSensitivityThreshold, trustedSpecificityThreshold)) {
                    link.setCompleteTrusted(true);
                }
            }

            r = taskResponseRepository.save(r);
        }

        link = captchaLinkRepository.save(link);

        if (link.isComplete()) {
            return new CaptchaTaskCompound(link);
        } else {
            return startCompound(taskType, articleHashes, link);
        }
    }

    private List<Long> getTaskIdsForCaptchaLink(UUID linkUuid, Long... additionalIds) {
        List<Long> existing = taskResponseRepository
                .getCaptchaTaskResponseByCaptchaLinkUuid(linkUuid)
                .stream()
                .map(r -> r.getCaptchaTask().getId())
                .collect(Collectors.toList());

        existing.addAll(List.of(additionalIds));

        return existing.stream().distinct().collect(Collectors.toList());
    }

}
