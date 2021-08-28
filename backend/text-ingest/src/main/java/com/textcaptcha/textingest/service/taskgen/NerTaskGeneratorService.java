package com.textcaptcha.textingest.service.taskgen;

import com.textcaptcha.data.model.task.NerCaptchaTask;
import com.textcaptcha.data.model.task.TaskType;
import com.textcaptcha.data.model.task.content.NerCaptchaTaskContent;
import com.textcaptcha.data.repository.CaptchaTaskRepository;
import com.textcaptcha.textingest.pojo.ReceivedArticle;
import com.textcaptcha.textingest.pojo.annotator.NerAnnotatedToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NerTaskGeneratorService implements TaskGeneratorService<List<NerAnnotatedToken>> {

    private final CaptchaTaskRepository taskRepository;

    @Autowired
    public NerTaskGeneratorService(CaptchaTaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public int generateTasks(ReceivedArticle article, List<NerAnnotatedToken> tokens) {
        List<NerCaptchaTask> generatedTasks = new ArrayList<>();

        for (int i = 0; i < tokens.size(); i++) {
            NerAnnotatedToken token = tokens.get(i);

            if (!token.getAnnotation().startsWith("B")) {
                continue;
            }

            // We have an entity - create a task for it.
            NerCaptchaTask task = new NerCaptchaTask();
            task.setArticleHashes(article.getUrlHash(), article.getTextHash());

            int selectedIndex = i;
            int startIndex = selectedIndex - 15;
            int endIndex = selectedIndex + 15;

            if (startIndex < 0) {
                endIndex += Math.abs(startIndex);
                startIndex = 0;
            }
            if (endIndex > tokens.size()) {
                int diff = endIndex - tokens.size();
                endIndex = tokens.size();
                startIndex -= diff;
                if (startIndex < 0) {
                    startIndex = 0;
                }
            }

            List<NerCaptchaTaskContent.Token> taskTokens = tokens.subList(startIndex, endIndex)
                    .stream()
                    .map(NerAnnotatedToken::toContentToken)
                    .collect(Collectors.toList());

            task.setContent(new NerCaptchaTaskContent(token.getAnnotation(), taskTokens));
            generatedTasks.add(task);
        }

        taskRepository.saveAll(generatedTasks);
        return generatedTasks.size();
    }

    @Override
    public boolean areTasksGenerated(ReceivedArticle article) {
        long existing = taskRepository.countTasks(TaskType.NER, article.getUrlHash(), article.getTextHash());
        return existing > 0;
    }

}
