package com.textcaptcha.taskmanager.model;

import com.textcaptcha.taskmanager.util.AnnotatedTokenListConverter;
import com.textcaptcha.taskmanager.util.MarkedTokenIndexListConverter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "captcha_response")
public class CaptchaResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "captcha_response_seqgen")
    @SequenceGenerator(name = "captcha_response_seqgen", sequenceName = "captcha_response_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "captcha_task_id", foreignKey = @ForeignKey(name = "fk_captcha_task_id"))
    private CaptchaTask captchaTask;

    @Column(columnDefinition = "TEXT")
    @Convert(converter = MarkedTokenIndexListConverter.class)
    private List<Integer> markedTokenIndexList = new ArrayList<>();

    @CreationTimestamp
    private Date createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CaptchaTask getCaptchaTask() {
        return captchaTask;
    }

    public void setCaptchaTask(CaptchaTask captchaTask) {
        this.captchaTask = captchaTask;
    }

    public List<Integer> getMarkedTokenIndexList() {
        return markedTokenIndexList;
    }

    public void setMarkedTokenIndexList(List<Integer> markedTokenIndexList) {
        this.markedTokenIndexList = markedTokenIndexList;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

}
