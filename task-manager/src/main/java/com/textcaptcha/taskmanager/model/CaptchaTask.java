package com.textcaptcha.taskmanager.model;

import com.textcaptcha.taskmanager.util.AnnotatedTokenListConverter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "captcha_task")
@EntityListeners(AuditingEntityListener.class)
public class CaptchaTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "captcha_task_seqgen")
    @SequenceGenerator(name = "captcha_task_seqgen", sequenceName = "captcha_task_seq", allocationSize = 1)
    private Long id;

    @Column
    private String articleUrl;

    @Column
    private String articleUrlHash;

    @Column
    private String articleTextHash;

    @Column(columnDefinition = "TEXT")
    @Convert(converter = AnnotatedTokenListConverter.class)
    private List<AnnotatedToken> tokens = new ArrayList<>();

    @CreatedDate
    private Date createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public String getArticleUrlHash() {
        return articleUrlHash;
    }

    public void setArticleUrlHash(String articleUrlHash) {
        this.articleUrlHash = articleUrlHash;
    }

    public String getArticleTextHash() {
        return articleTextHash;
    }

    public void setArticleTextHash(String articleTextHash) {
        this.articleTextHash = articleTextHash;
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
