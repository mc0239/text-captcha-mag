package com.textcaptcha.taskmanager.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "article_seqgen")
    @SequenceGenerator(name = "article_seqgen", sequenceName = "article_seq", allocationSize = 1)
    private Long id;

    @Column
    private String articleUrl;

    @Column(columnDefinition = "text")
    private String articleText;

    @Column
    private String articleUrlHash;

    @Column
    private String articleTextHash;

    @CreationTimestamp
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

    public String getArticleText() {
        return articleText;
    }

    public void setArticleText(String articleText) {
        this.articleText = articleText;
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

    public Date getCreatedAt() {
        return createdAt;
    }

}
