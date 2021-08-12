package com.textcaptcha.taskmanager.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.textcaptcha.data.model.task.CorefCaptchaTask;
import com.textcaptcha.data.model.task.TaskType;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CorefTaskInstanceDto extends TaskInstanceDto<CorefCaptchaTask, CorefTaskInstanceDto.Content> {

    private final List<Word> mentionOfInterest;
    private final List<List<Word>> suggestedMentions;

    public CorefTaskInstanceDto(UUID taskInstanceId, CorefCaptchaTask captchaTask) {
        super(TaskType.COREF, taskInstanceId);

        this.mentionOfInterest = captchaTask
                .getContent()
                .getMentionOfInterest()
                .stream()
                .map(t -> new Word(t.getWord(), t.getMentionId() != null))
                .collect(Collectors.toList());

        this.suggestedMentions = captchaTask
                .getContent()
                .getSuggestedMentions()
                .stream()
                .map(mention -> mention
                        .stream()
                        .map(t -> new Word(t.getWord(), t.getMentionId() != null))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    @JsonIgnore
    public List<Word> getMentionOfInterest() {
        return mentionOfInterest;
    }

    @JsonIgnore
    public List<List<Word>> getSuggestedMentions() {
        return suggestedMentions;
    }

    @Override
    public Content getContent() {
        return new Content(this.mentionOfInterest, this.suggestedMentions);
    }

    public static class Word {
        private final String word;
        private final boolean isMention;

        Word(String word, boolean isMention) {
            this.word = word;
            this.isMention = isMention;
        }

        public String getWord() {
            return word;
        }

        public boolean isMention() {
            return isMention;
        }
    }

    public static class Content {
        private final List<Word> primaryWordsList;
        private final List<List<Word>> suggestedWordsLists;

        Content(List<Word> primaryWordsList, List<List<Word>> suggestedWordsLists) {
            this.primaryWordsList = primaryWordsList;
            this.suggestedWordsLists = suggestedWordsLists;
        }

        public List<Word> getPrimaryWordsList() {
            return primaryWordsList;
        }

        public List<List<Word>> getSuggestedWordsLists() {
            return suggestedWordsLists;
        }
    }

}
