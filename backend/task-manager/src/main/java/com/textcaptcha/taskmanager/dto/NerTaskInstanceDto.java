package com.textcaptcha.taskmanager.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.textcaptcha.data.model.task.NerCaptchaTask;
import com.textcaptcha.data.model.task.TaskType;
import com.textcaptcha.data.model.task.content.NerCaptchaTaskContent;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class NerTaskInstanceDto extends TaskInstanceDto<NerCaptchaTask, List<String>> {

    private List<String> words;

    public NerTaskInstanceDto(UUID taskInstanceId, NerCaptchaTask captchaTask) {
        super(TaskType.NER, taskInstanceId);

        this.words = captchaTask.getContent().getTokens()
                .stream()
                .map(NerCaptchaTaskContent.Token::getWord)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getContent() {
        return words;
    }

    @JsonIgnore
    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }

}
