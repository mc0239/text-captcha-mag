package com.textcaptcha.taskmanager.model;

import com.textcaptcha.taskmanager.util.AnnotatedTokenListConverter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class CaptchaTask {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String articleUid;

    @Column(columnDefinition = "TEXT")
    @Convert(converter = AnnotatedTokenListConverter.class)
    private List<AnnotatedToken> tokens = new ArrayList<>();

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

}
