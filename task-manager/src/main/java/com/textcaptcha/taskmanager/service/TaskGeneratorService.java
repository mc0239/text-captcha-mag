package com.textcaptcha.taskmanager.service;

import com.textcaptcha.taskmanager.model.AnnotatedToken;

import java.util.List;

public interface TaskGeneratorService {
    int generateTasks(String articleUrl, List<AnnotatedToken> tokens);
}
