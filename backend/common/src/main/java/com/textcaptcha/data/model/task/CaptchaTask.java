package com.textcaptcha.data.model.task;

import com.textcaptcha.converter.CaptchaTaskContentConverter;
import com.textcaptcha.data.IdentifiableEntity;
import com.textcaptcha.data.model.task.content.CaptchaTaskContent;
import com.textcaptcha.dto.ArticleHashPairDto;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "captcha_task")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "task_type")
@EntityListeners(AuditingEntityListener.class)
public abstract class CaptchaTask implements IdentifiableEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "captcha_task_seqgen")
    @SequenceGenerator(name = "captcha_task_seqgen", sequenceName = "captcha_task_seq", allocationSize = 1)
    private Long id;

    @Column(name = "task_type", insertable = false, updatable = false, nullable = false)
    @Enumerated(value = EnumType.STRING)
    protected TaskType taskType;

    @Column(length = 512)
    private String articleUrlHash;

    @Column(length = 512)
    private String articleTextHash;

    @Column(name = "task_content", columnDefinition = "text")
    @Convert(converter = CaptchaTaskContentConverter.class)
    protected CaptchaTaskContent content;

    @Column
    private Float confidence;

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

    public ArticleHashPairDto getArticleHashes() {
        return new ArticleHashPairDto(articleUrlHash, articleTextHash);
    }

    public void setArticleHashes(String articleUrlHash, String articleTextHash) {
        this.setArticleUrlHash(articleUrlHash);
        this.setArticleTextHash(articleTextHash);
    }

    public abstract CaptchaTaskContent getContent();

    public void setContent(CaptchaTaskContent content) {
        this.content = content;
    }

    public Float getConfidence() {
        return confidence;
    }

    public void setConfidence(Float confidence) {
        this.confidence = confidence;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
