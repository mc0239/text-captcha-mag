package com.textcaptcha.data.model.response;

import com.textcaptcha.data.model.response.content.CorefCaptchaTaskResponseContent;
import com.textcaptcha.data.model.task.CorefCaptchaTask;
import com.textcaptcha.data.model.task.TaskType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(TaskType.Name.COREF)
public class CorefCaptchaTaskResponse extends CaptchaTaskResponse<CorefCaptchaTask, CorefCaptchaTaskResponseContent> {

    @Override
    public CorefCaptchaTask getCaptchaTask() {
        return (CorefCaptchaTask) this.captchaTask;
    }

    @Override
    public CorefCaptchaTaskResponseContent getContent() {
        return (CorefCaptchaTaskResponseContent) this.content;
    }

}
