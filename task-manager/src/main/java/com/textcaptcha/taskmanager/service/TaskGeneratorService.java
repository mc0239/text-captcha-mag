package com.textcaptcha.taskmanager.service;

import com.textcaptcha.taskmanager.model.AnnotatedToken;

import java.util.List;

public interface TaskGeneratorService {
    int generateTasks(String articleUrl, String articleUid, List<AnnotatedToken> tokens);
    boolean areTasksGenerated(String articleUrl, String articleUid);
}
