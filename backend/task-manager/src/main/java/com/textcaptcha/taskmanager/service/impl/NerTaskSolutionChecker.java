package com.textcaptcha.taskmanager.service.impl;

import com.textcaptcha.data.model.task.NerCaptchaTask;
import com.textcaptcha.data.model.task.content.NerCaptchaTaskContent;
import com.textcaptcha.taskmanager.pojo.SolutionCheckerResult;
import com.textcaptcha.taskmanager.service.TaskSolutionChecker;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NerTaskSolutionChecker implements TaskSolutionChecker<NerCaptchaTask> {

    @Override
    public SolutionCheckerResult checkSolution(NerCaptchaTask task, List<Integer> solution) {
        // positive = entity
        // negative = not entity
        // true = selected
        // false = not selected
        int totalPositives = 0;
        int truePositives = 0;
        int trueNegatives = 0;

        String primaryAnnotation = task.getContent().getPrimaryAnnotation().split("-")[1];
        for (int i = 0; i < task.getContent().getTokens().size(); i++) {
            NerCaptchaTaskContent.Token word = task.getContent().getTokens().get(i);
            boolean isNamedEntity = !word.getAnnotation().equals("O");
            boolean hasPrimaryAnnotation = word.getAnnotation().contains(primaryAnnotation);
            boolean isSelected = solution.contains(i);

            if (isNamedEntity && hasPrimaryAnnotation) {
                totalPositives++;
            }

            if (isSelected) {
                if (isNamedEntity && hasPrimaryAnnotation) {
                    truePositives++;
                } else {
                    trueNegatives++;
                }
            }
        }

        // TODO
        String message = "You selected " + truePositives + "/" + totalPositives + " entities and made " + ((totalPositives - truePositives) + trueNegatives) + " errors.";
        return new SolutionCheckerResult(message);
    }

}
