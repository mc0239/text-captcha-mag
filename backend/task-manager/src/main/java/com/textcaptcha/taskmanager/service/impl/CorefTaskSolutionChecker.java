package com.textcaptcha.taskmanager.service.impl;

import com.textcaptcha.data.model.task.CorefCaptchaTask;
import com.textcaptcha.data.model.task.content.CorefCaptchaTaskContent;
import com.textcaptcha.taskmanager.pojo.SolutionCheckerResult;
import com.textcaptcha.taskmanager.service.TaskSolutionChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CorefTaskSolutionChecker implements TaskSolutionChecker<CorefCaptchaTask> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public SolutionCheckerResult checkSolution(CorefCaptchaTask task, List<Integer> solution) {
        List<CorefCaptchaTaskContent.Token> mentionOfInterest = task.getContent().getMentionOfInterest();
        Integer correctClusterId = clusterIdFromMention(mentionOfInterest);

        if (correctClusterId == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // should be picked, but were not
        int falseNegatives = 0;
        // should be and were picked
        int truePositives = 0;
        // should not be picked and weren't
        int trueNegatives = 0;
        // should not be picked, but were
        int falsePositives = 0;

        for (int i = 0; i < task.getContent().getSuggestedMentions().size(); i++) {
            List<CorefCaptchaTaskContent.Token> suggestedMention = task.getContent().getSuggestedMentions().get(i);
            Integer currentClusterId = clusterIdFromMention(suggestedMention);

            boolean isRelevant = correctClusterId.equals(currentClusterId);
            boolean isSelected = solution.contains(i);

            if (isRelevant) {
                if (isSelected) {
                    truePositives++;
                } else {
                    falseNegatives++;
                }
            } else {
                if (isSelected) {
                    falsePositives++;
                } else {
                    trueNegatives++;
                }
            }
        }

//        int totalPositives = truePositives + falsePositives;
//        int totalFalse= falseNegatives + falsePositives;
//        String message = "You selected " + truePositives + "/" + totalPositives + " entities and made " + totalFalse + " errors.";

        double sensitivity = (double) truePositives / (falseNegatives + truePositives);
        double specificity = (double) trueNegatives / (trueNegatives + falsePositives);

        logger.trace(String.format("TP=%d, FN=%d, FP=%d, TN=%d, sens=%.2f, spec=%.2f", truePositives, falseNegatives, falsePositives, trueNegatives, sensitivity, specificity));

        return new SolutionCheckerResult(sensitivity, specificity);
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
