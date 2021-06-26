package com.textcaptcha.data.model.task;

import com.textcaptcha.data.IdentifiableEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class CaptchaTask implements IdentifiableEntity<Long> {

    @Column
    private String articleUrlHash;

    @Column
    private String articleTextHash;

    @CreatedDate
    private Date createdAt;

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

    public Date getCreatedAt() {
        return createdAt;
    }

}
