package com.textcaptcha.data.model.task;

import com.textcaptcha.data.model.task.content.NerCaptchaTaskContent;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(TaskType.Name.NER)
public class NerCaptchaTask extends CaptchaTask {

    @Override
    public NerCaptchaTaskContent getContent() {
        return (NerCaptchaTaskContent) this.content;
    }

}
