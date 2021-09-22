package com.textcaptcha.data.model;

import com.textcaptcha.data.IdentifiableEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "captcha_link")
@EntityListeners(AuditingEntityListener.class)
public class CaptchaLink implements IdentifiableEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "captcha_link_seqgen")
    @SequenceGenerator(name = "captcha_link_seqgen", sequenceName = "captcha_link_seq", allocationSize = 1)
    private Long id;

    @Column
    private UUID uuid;

    @Column(name = "complete_verify")
    private Boolean completeVerify = false;

    @Column(name = "complete_trusted")
    private Boolean completeTrusted = false;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date modifiedAt;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public boolean isCompleteVerify() {
        return completeVerify != null ? completeVerify : false;
    }

    public Boolean getCompleteVerify() {
        return completeVerify;
    }

    public void setCompleteVerify(Boolean completeVerify) {
        this.completeVerify = completeVerify;
    }

    public boolean isCompleteTrusted() {
        return completeTrusted != null ? completeTrusted : false;
    }

    public Boolean getCompleteTrusted() {
        return completeTrusted;
    }

    public void setCompleteTrusted(Boolean completeTrusted) {
        this.completeTrusted = completeTrusted;
    }

    public boolean isComplete() {
        return isCompleteVerify() && isCompleteTrusted();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}
