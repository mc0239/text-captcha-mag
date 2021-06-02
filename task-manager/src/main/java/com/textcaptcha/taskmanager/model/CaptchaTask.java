package com.textcaptcha.taskmanager.model;

import com.textcaptcha.taskmanager.util.AnnotatedTokenListConverter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "captcha_task")
public class CaptchaTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "captcha_task_seqgen")
    @SequenceGenerator(name = "captcha_task_seqgen", sequenceName = "captcha_task_seq", allocationSize = 1)
    private Long id;

    @Column
    private String articleUid;

    @Column(columnDefinition = "TEXT")
    @Convert(converter = AnnotatedTokenListConverter.class)
    private List<AnnotatedToken> tokens = new ArrayList<>();

    @CreationTimestamp
    private Date createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArticleUid() {
        return articleUid;
    }

    public void setArticleUid(String articleUid) {
        this.articleUid = articleUid;
    }

    public List<AnnotatedToken> getTokens() {
        return tokens;
    }

    public void setTokens(List<AnnotatedToken> tokens) {
        this.tokens = tokens;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

}
