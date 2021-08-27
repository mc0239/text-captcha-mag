package com.textcaptcha.data.model.response;

import com.textcaptcha.converter.CaptchaTaskResponseContentConverter;
import com.textcaptcha.data.IdentifiableEntity;
import com.textcaptcha.data.model.response.content.CaptchaTaskResponseContent;
import com.textcaptcha.data.model.task.CaptchaTask;
import com.textcaptcha.data.model.task.TaskType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "captcha_task_response")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "task_type")
@EntityListeners(AuditingEntityListener.class)
public abstract class CaptchaTaskResponse implements IdentifiableEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "captcha_task_response_seqgen")
    @SequenceGenerator(name = "captcha_task_response_seqgen", sequenceName = "captcha_task_response_seq", allocationSize = 1)
    private Long id;

    @Column(name = "task_type", insertable = false, updatable = false, nullable = false)
    @Enumerated(value = EnumType.STRING)
    protected TaskType taskType;

    @ManyToOne
    @JoinColumn(name = "captcha_task_id", foreignKey = @ForeignKey(name = "fk_captcha_task_id"))
    protected CaptchaTask captchaTask;

    @Column(name = "response_content", columnDefinition = "text")
    @Convert(converter = CaptchaTaskResponseContentConverter.class)
    protected CaptchaTaskResponseContent content;

    @CreatedDate
    private Date createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public abstract CaptchaTask getCaptchaTask();

    public void setCaptchaTask(CaptchaTask captchaTask) {
        this.captchaTask = captchaTask;
    }

    public abstract CaptchaTaskResponseContent getContent();

    public void setContent(CaptchaTaskResponseContent content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

}
