package com.textcaptcha.textingest.service.taskgen;

import com.textcaptcha.textingest.pojo.ReceivedArticle;

public interface TaskGeneratorService<I> {
    int generateTasks(ReceivedArticle article, I input);
    boolean areTasksGenerated(ReceivedArticle article);
}
