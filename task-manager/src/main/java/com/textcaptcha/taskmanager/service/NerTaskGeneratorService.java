package com.textcaptcha.taskmanager.service;

import com.textcaptcha.taskmanager.model.AnnotatedToken;
import com.textcaptcha.taskmanager.model.CaptchaTask;
import com.textcaptcha.taskmanager.repository.CaptchaTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NerTaskGeneratorService implements TaskGeneratorService {

    private final CaptchaTaskRepository captchaTaskRepository;

    @Autowired
    public NerTaskGeneratorService(CaptchaTaskRepository captchaTaskRepository) {
        this.captchaTaskRepository = captchaTaskRepository;
    }

    @Override
    public int generateTasks(String articleUrl, String articleUid, List<AnnotatedToken> tokens) {
        List<CaptchaTask> generatedTasks = new ArrayList<>();

        for (int i = 0; i < tokens.size(); i++) {
            AnnotatedToken token = tokens.get(i);

            if (!token.getAnnotation().startsWith("B")) {
                continue;
            }

            // We have an entity - create a task for it.
            CaptchaTask task = new CaptchaTask();
            task.setArticleUrl(articleUrl);
            task.setArticleUid(articleUid);

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
            }

            task.setTokens(tokens.subList(startIndex, endIndex));
            generatedTasks.add(task);
        }

        captchaTaskRepository.saveAll(generatedTasks);
        return generatedTasks.size();
    }

    @Override
    public boolean areTasksGenerated(String articleUrl, String articleUid) {
        int existing = captchaTaskRepository.countByArticleUrlAndArticleUid(articleUrl, articleUid);
        return existing > 0;
    }

}
