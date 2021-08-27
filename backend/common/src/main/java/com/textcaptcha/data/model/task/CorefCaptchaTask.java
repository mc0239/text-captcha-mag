package com.textcaptcha.data.model.task;

import com.textcaptcha.data.model.task.content.CorefCaptchaTaskContent;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(TaskType.Name.COREF)
public class CorefCaptchaTask extends CaptchaTask {

    @Override
    public CorefCaptchaTaskContent getContent() {
        return (CorefCaptchaTaskContent) this.content;
    }

}
