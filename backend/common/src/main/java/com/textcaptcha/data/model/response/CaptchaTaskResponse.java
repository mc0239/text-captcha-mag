package com.textcaptcha.data.model.response;

import com.textcaptcha.data.IdentifiableEntity;
import com.textcaptcha.data.model.task.CaptchaTask;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class CaptchaTaskResponse<T extends CaptchaTask> implements IdentifiableEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "captcha_task_id", foreignKey = @ForeignKey(name = "fk_captcha_task_id"))
    private T captchaTask;

    @CreatedDate
    private Date createdAt;

    public T getCaptchaTask() {
        return captchaTask;
    }

    public void setCaptchaTask(T captchaTask) {
        this.captchaTask = captchaTask;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

}
