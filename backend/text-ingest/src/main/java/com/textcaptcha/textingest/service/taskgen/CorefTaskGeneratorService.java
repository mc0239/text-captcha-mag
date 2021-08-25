package com.textcaptcha.textingest.service.taskgen;

import com.textcaptcha.data.model.task.CorefCaptchaTask;
import com.textcaptcha.data.model.task.content.CorefCaptchaTaskContent;
import com.textcaptcha.data.repository.CorefCaptchaTaskRepository;
import com.textcaptcha.textingest.pojo.CorefTokenGroup;
import com.textcaptcha.textingest.pojo.ReceivedArticle;
import com.textcaptcha.textingest.pojo.annotator.CorefAnnotatedToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CorefTaskGeneratorService implements TaskGeneratorService<List<CorefAnnotatedToken>> {

    private final CorefCaptchaTaskRepository taskRepository;

    @Autowired
    public CorefTaskGeneratorService(CorefCaptchaTaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    private static Map<Integer, CorefTokenGroup> tokensByMention(List<CorefAnnotatedToken> tokens) {
        // mentionId -> tokens[]
        HashMap<Integer, CorefTokenGroup> mentions = new HashMap<>();
        for (CorefAnnotatedToken token : tokens) {
            if (token.getMentionId() != null) {
                if (!mentions.containsKey(token.getMentionId())) {
                    mentions.put(token.getMentionId(), new CorefTokenGroup());
                }
                mentions.get(token.getMentionId()).add(token);
            }
        }
        return mentions;
    }

    private static Map<Integer, List<CorefTokenGroup>> mentionsByCluster(Map<Integer, CorefTokenGroup> mentions) {
        // clusterId -> mentions[]
        HashMap<Integer, List<CorefTokenGroup>> clusters = new HashMap<>();
        for (CorefTokenGroup mention : mentions.values()) {
            if (mention.getClusterId() != null) {
                if (!clusters.containsKey(mention.getClusterId())) {
                    clusters.put(mention.getClusterId(), new ArrayList<>());
                }
                clusters.get(mention.getClusterId()).add(mention);
            }
        }
        return clusters;
    }

    @Override
    public int generateTasks(ReceivedArticle article, List<CorefAnnotatedToken> tokens) {
        Map<Integer, CorefTokenGroup> mentions = tokensByMention(tokens);
        Map<Integer, List<CorefTokenGroup>> clusters = mentionsByCluster(mentions);

        List<CorefCaptchaTask> generatedTasks = new ArrayList<>();

        Integer previousMentionId = null;
        for (int tokenIndex = 0; tokenIndex < tokens.size(); tokenIndex++) {
            CorefAnnotatedToken token = tokens.get(tokenIndex);

            Integer currentMentionId = token.getMentionId();
            Integer currentClusterId = token.getClusterId();

            if (currentMentionId == null || currentMentionId.equals(previousMentionId)) {
                previousMentionId = null;
                continue;
            }

            // We have an entity - create a task for it.
            CorefCaptchaTask task = new CorefCaptchaTask();
            task.setArticleUrlHash(article.getUrlHash());
            task.setArticleTextHash(article.getTextHash());
            CorefCaptchaTaskContent taskContent = new CorefCaptchaTaskContent();

            List<CorefCaptchaTaskContent.Token> m =
                    tokenWithContext(tokens, tokenIndex, 5, getMentionSize(tokens, tokenIndex) + 5)
                            .stream()
                            .map(CorefAnnotatedToken::toContentToken)
                            .peek(t -> {
                                if (!currentMentionId.equals(t.getMentionId())) {
                                    t.setMentionId(null);
                                    t.setClusterId(null);
                                }
                            })
                            .collect(Collectors.toList());

            taskContent.setMentionOfInterest(m);

            //

            List<List<CorefCaptchaTaskContent.Token>> suggestedMentions = new ArrayList<>();
            Random r = new Random();

            int correctMentionsCount = r.nextInt(4) + 1;
            int incorrectMentionsCount = 5 - correctMentionsCount;

            // (1) add from 1 to 4 correct answers (mentions from same cluster)
            for (int i = 0; i < correctMentionsCount; i++) {
                // randomly picked mention from same cluster must not be current mention of interest or already picked
                // mention.
                List<CorefTokenGroup> currentCluster = clusters.get(currentClusterId);
                // TODO possible duplicates
                suggestedMentions.add(randomMentionFromClusterWithContext(tokens, currentCluster));
            }

            // (2) add mentions from other clusters (randomly selected) to have 5 suggested mentions total.
            for (int i = 0; i < incorrectMentionsCount; i++) {
                List<CorefTokenGroup> randomCluster = null;
                while (randomCluster == null || randomCluster.get(0).getClusterId().equals(currentClusterId)) {
                    // TODO possible duplicates
                    randomCluster = clusters.get(r.nextInt(clusters.size()));
                }
                // TODO possible duplicates
                suggestedMentions.add(randomMentionFromClusterWithContext(tokens, randomCluster));
            }

            taskContent.setSuggestedMentions(suggestedMentions);

            //

            task.setContent(taskContent);
            generatedTasks.add(task);

            //

            previousMentionId = currentMentionId;
        }

        taskRepository.saveAll(generatedTasks);
        return generatedTasks.size();
    }

    @Override
    public boolean areTasksGenerated(ReceivedArticle article) {
        long existing = taskRepository.countByArticleUrlHashAndArticleTextHash(article.getUrlHash(), article.getTextHash());
        return existing > 0;
    }

    private static int getMentionSize(List<CorefAnnotatedToken> tokens, int tokenIndex) {
        if (tokenIndex >= tokens.size()) {
            return 0;
        }

        CorefAnnotatedToken t = tokens.get(tokenIndex);
        Integer mentionId = t.getMentionId();

        if (mentionId == null) {
            return 0;
        }

        int count = 0;
        int currIndex = tokenIndex;
        while (currIndex < tokens.size() && mentionId.equals(tokens.get(currIndex).getMentionId())) {
            count++;
            currIndex++;
        }

        return count;
    }

    private static List<CorefCaptchaTaskContent.Token> randomMentionFromClusterWithContext(List<CorefAnnotatedToken> tokens, List<CorefTokenGroup> cluster) {
        Random r = new Random();
        CorefTokenGroup randomMention = cluster.get(r.nextInt(cluster.size()));
        CorefAnnotatedToken mentionToken1 = randomMention.getTokens().get(0);

        return tokenWithContext(tokens, mentionToken1.getIndex(), 5, getMentionSize(tokens, mentionToken1.getIndex()) + 5)
                        .stream()
                        .map(CorefAnnotatedToken::toContentToken)
                        .peek(t -> {
                            if (!Objects.equals(t.getMentionId(), mentionToken1.getMentionId())) {
                                t.setMentionId(null);
                                t.setClusterId(null);
                            }
                        })
                        .collect(Collectors.toList());
    }

    private static List<CorefAnnotatedToken> tokenWithContext(List<CorefAnnotatedToken> tokens, int tokenIndex, int countBefore, int countAfter) {
        int startIndex = tokenIndex - countBefore;
        int endIndex = tokenIndex + countAfter;

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

        return tokens.subList(startIndex, endIndex);
    }

}
