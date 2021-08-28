package com.textcaptcha.taskmanager.service.impl;

import com.textcaptcha.data.model.task.CaptchaTask;
import com.textcaptcha.data.model.task.TaskType;
import com.textcaptcha.data.repository.CaptchaTaskRepository;
import com.textcaptcha.dto.ArticleHashPairDto;
import com.textcaptcha.taskmanager.exception.TaskSelectionException;
import com.textcaptcha.taskmanager.service.TaskSelectionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class TaskSelectionServiceImpl implements TaskSelectionService {

    private final CaptchaTaskRepository captchaTaskRepository;

    public TaskSelectionServiceImpl(CaptchaTaskRepository captchaTaskRepository) {
        this.captchaTaskRepository = captchaTaskRepository;
    }

    @Override
    public CaptchaTask getTask(TaskType taskType, ArticleHashPairDto articleHashes) throws TaskSelectionException {
        if (taskType == null) {
            throw new TaskSelectionException("Missing taskType parameter.");
        }

        if (!articleHashes.hasHashes()) {
            throw new TaskSelectionException("Missing articleUrlHash and/or articleTextHash parameter(s).");
        }

        List<CaptchaTask> tasks = captchaTaskRepository.getTasks(taskType, articleHashes.getUrlHash(), articleHashes.getTextHash());

        if (tasks.isEmpty()) {
            // TODO what if it's just ingest still in progress? There's a better way to handle this.
            // TODO what to even do here? Is it possible to have a processed article with NO tasks?
            throw new TaskSelectionException("No tasks available");
        }

        // TODO a smarter-than-random task selection.
        Random r = new Random();
        CaptchaTask selectedTask = tasks.get(r.nextInt(tasks.size()));
        return selectedTask;
    }

}
