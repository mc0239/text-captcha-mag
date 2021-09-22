package com.textcaptcha.taskmanager.service.impl;

import com.textcaptcha.data.model.response.CaptchaTaskResponse;
import com.textcaptcha.data.model.response.CorefCaptchaTaskResponse;
import com.textcaptcha.data.model.response.NerCaptchaTaskResponse;
import com.textcaptcha.data.model.response.content.CorefCaptchaTaskResponseContent;
import com.textcaptcha.data.model.response.content.NerCaptchaTaskResponseContent;
import com.textcaptcha.data.model.task.CaptchaTask;
import com.textcaptcha.data.model.task.CorefCaptchaTask;
import com.textcaptcha.data.model.task.NerCaptchaTask;
import com.textcaptcha.data.repository.CaptchaTaskResponseRepository;
import com.textcaptcha.taskmanager.exception.InvalidTaskTypeException;
import com.textcaptcha.taskmanager.pojo.IssuedTaskInstance;
import com.textcaptcha.taskmanager.pojo.SolutionCheckerResult;
import com.textcaptcha.taskmanager.pojo.SolutionProcessorResult;
import com.textcaptcha.taskmanager.service.TaskInstanceKeeper;
import com.textcaptcha.taskmanager.service.TaskSolutionProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class TaskSolutionProcessorImpl implements TaskSolutionProcessor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CaptchaTaskResponseRepository taskResponseRepository;

    private final TaskInstanceKeeper taskInstanceKeeper;

    private final NerTaskSolutionChecker nerTaskSolutionChecker;
    private final CorefTaskSolutionChecker corefTaskSolutionChecker;

    protected TaskSolutionProcessorImpl(
            CaptchaTaskResponseRepository taskResponseRepository,
            TaskInstanceKeeper taskInstanceKeeper,
            NerTaskSolutionChecker nerTaskSolutionChecker,
            CorefTaskSolutionChecker corefTaskSolutionChecker
    ) {
        this.taskResponseRepository = taskResponseRepository;
        this.taskInstanceKeeper = taskInstanceKeeper;
        this.nerTaskSolutionChecker = nerTaskSolutionChecker;
        this.corefTaskSolutionChecker = corefTaskSolutionChecker;
    }

    @Override
    public SolutionProcessorResult processSolution(UUID taskInstanceId, List<Integer> taskSolution) {
        IssuedTaskInstance taskInstance = taskInstanceKeeper.invalidate(taskInstanceId);

        if (taskInstance == null) {
            logger.trace("Received task response for invalid instance ID: " + taskInstanceId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid task instance ID.");
        }

        CaptchaTask task = taskInstance.getTask();
        CaptchaTaskResponse response;
        SolutionCheckerResult result;

        switch (task.getTaskType()) {
            case NER:
                response = new NerCaptchaTaskResponse();
                response.setCaptchaTask(task);
                response.setContent(new NerCaptchaTaskResponseContent(taskSolution));
                response = taskResponseRepository.save(response);

                result = nerTaskSolutionChecker.checkSolution((NerCaptchaTask) task, taskSolution);
                return new SolutionProcessorResult(response, result);

            case COREF:
                response = new CorefCaptchaTaskResponse();
                response.setCaptchaTask(task);
                response.setContent(new CorefCaptchaTaskResponseContent(taskSolution));
                response = taskResponseRepository.save(response);

                result = corefTaskSolutionChecker.checkSolution((CorefCaptchaTask) task, taskSolution);
                return new SolutionProcessorResult(response, result);

            default:
                throw new InvalidTaskTypeException();
        }
    }

}
