package com.textcaptcha.taskmanager.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.textcaptcha.data.model.task.NerCaptchaTask;
import com.textcaptcha.data.model.task.TaskType;
import com.textcaptcha.data.model.task.content.NerCaptchaTaskContent;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class NerTaskInstanceDto extends TaskInstanceDto<NerTaskInstanceDto.Content> {

    private final String primaryAnnotation;
    private final List<String> words;

    public NerTaskInstanceDto(UUID taskInstanceId, NerCaptchaTask captchaTask) {
        super(TaskType.NER, taskInstanceId);

        this.primaryAnnotation = captchaTask.getContent().getPrimaryAnnotation();
        this.words = captchaTask.getContent().getTokens()
                .stream()
                .map(NerCaptchaTaskContent.Token::getWord)
                .collect(Collectors.toList());
    }

    @Override
    public Content getContent() {
        return new Content(this.primaryAnnotation, this.words);
    }

    @JsonIgnore
    public String getPrimaryAnnotation() {
        return primaryAnnotation;
    }

    @JsonIgnore
    public List<String> getWords() {
        return words;
    }

    public static class Content {
        private final String primaryAnnotation;
        private final List<String> words;

        public Content(String primaryAnnotation, List<String> words) {
            this.primaryAnnotation = primaryAnnotation;
            this.words = words;
        }

        public String getPrimaryAnnotation() {
            return primaryAnnotation;
        }

        public List<String> getWords() {
            return words;
        }
    }

}
