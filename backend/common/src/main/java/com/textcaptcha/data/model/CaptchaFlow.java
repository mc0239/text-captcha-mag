package com.textcaptcha.data.model;

import com.textcaptcha.data.IdentifiableEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "captcha_flow")
@EntityListeners(AuditingEntityListener.class)
public class CaptchaFlow implements IdentifiableEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "captcha_flow_seqgen")
    @SequenceGenerator(name = "captcha_flow_seqgen", sequenceName = "captcha_flow_seq", allocationSize = 1)
    private Long id;

    @Column
    private UUID uuid;

    @Column(name = "complete_sanity")
    private Boolean completeSanity = false;

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

    public boolean isCompleteSanity() {
        return completeSanity != null ? completeSanity : false;
    }

    public Boolean getCompleteSanity() {
        return completeSanity;
    }

    public void setCompleteSanity(Boolean completeSanity) {
        this.completeSanity = completeSanity;
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
