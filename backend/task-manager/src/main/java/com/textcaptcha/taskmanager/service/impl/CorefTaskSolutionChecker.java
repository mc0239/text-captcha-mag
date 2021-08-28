package com.textcaptcha.taskmanager.service.impl;

import com.textcaptcha.data.model.task.CorefCaptchaTask;
import com.textcaptcha.data.model.task.content.CorefCaptchaTaskContent;
import com.textcaptcha.taskmanager.pojo.SolutionCheckerResult;
import com.textcaptcha.taskmanager.service.TaskSolutionChecker;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CorefTaskSolutionChecker implements TaskSolutionChecker<CorefCaptchaTask> {

    @Override
    public SolutionCheckerResult checkSolution(CorefCaptchaTask task, List<Integer> solution) {
        List<CorefCaptchaTaskContent.Token> mentionOfInterest = task.getContent().getMentionOfInterest();
        Integer correctClusterId = clusterIdFromMention(mentionOfInterest);

        if (correctClusterId == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // positive = entity
        // negative = not entity
        // true = selected
        // false = not selected

        int totalPositives = 0;
        int truePositives = 0;
        int trueNegatives = 0;
        for (int i = 0; i < task.getContent().getSuggestedMentions().size(); i++) {
            List<CorefCaptchaTaskContent.Token> suggestedMention = task.getContent().getSuggestedMentions().get(i);
            Integer currentClusterId = clusterIdFromMention(suggestedMention);
            boolean currentClusterIsCorrect = correctClusterId.equals(currentClusterId);
            boolean isSelected = solution.contains(i);

            if (currentClusterIsCorrect) {
                totalPositives++;
            }

            if (isSelected) {
                if (currentClusterIsCorrect) {
                    truePositives++;
                } else {
                    trueNegatives++;
                }
            }
        }

        // TODO
        String message = "You selected " + truePositives + "/" + totalPositives + " mentions and made " + ((totalPositives - truePositives) + trueNegatives) + " errors.";
        return new SolutionCheckerResult(message);
    }

    private Integer clusterIdFromMention(List<CorefCaptchaTaskContent.Token> mention) {
        Optional<CorefCaptchaTaskContent.Token> t = mention
                .stream()
                .filter(m -> m.getClusterId() != null)
                .findFirst();

        if (t.isPresent()) {
            return t.get().getClusterId();
        } else {
            return null;
        }
    }

}
