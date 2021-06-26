package com.textcaptcha.textingest.service;

import com.textcaptcha.data.pojo.AnnotatedToken;
import com.textcaptcha.textingest.pojo.ReceivedArticle;

import java.util.List;

public interface TaskGeneratorService {
    int generateTasks(ReceivedArticle article, List<AnnotatedToken> tokens);
    boolean areTasksGenerated(ReceivedArticle article);
}
