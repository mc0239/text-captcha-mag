package com.textcaptcha.taskmanager.service;

import com.textcaptcha.taskmanager.model.AnnotatedToken;
import com.textcaptcha.taskmanager.model.CaptchaTask;
import com.textcaptcha.taskmanager.repository.CaptchaTaskRepository;
import com.textcaptcha.taskmanager.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    public int generateTasks(String articleUrl, List<AnnotatedToken> tokens) {
        List<CaptchaTask> generatedTasks = new ArrayList<>();

        for (int i = 0; i < tokens.size(); i++) {
            AnnotatedToken token = tokens.get(i);

            if (!token.getAnnotation().startsWith("B")) {
                continue;
            }

            // We have an entity - create a task for it.
            CaptchaTask task = new CaptchaTask();
            task.setArticleUid(Utils.articleUrlToUid(articleUrl));

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



}
