package com.textcaptcha.data.model.response;

import com.textcaptcha.data.model.response.content.NerCaptchaTaskResponseContent;
import com.textcaptcha.data.model.task.NerCaptchaTask;
import com.textcaptcha.data.model.task.TaskType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(TaskType.Name.NER)
public class NerCaptchaTaskResponse extends CaptchaTaskResponse {

    @Override
    public NerCaptchaTask getCaptchaTask() {
        return (NerCaptchaTask) this.captchaTask;
    }

    @Override
    public NerCaptchaTaskResponseContent getContent() {
        return (NerCaptchaTaskResponseContent) this.content;
    }

}
