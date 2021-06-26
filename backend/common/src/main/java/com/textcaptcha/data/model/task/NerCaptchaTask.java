package com.textcaptcha.data.model.task;

import com.textcaptcha.converter.AnnotatedTokenListConverter;
import com.textcaptcha.data.pojo.AnnotatedToken;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ner_captcha_task")
public class NerCaptchaTask extends CaptchaTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "ner_captcha_task_seqgen")
    @SequenceGenerator(name = "ner_captcha_task_seqgen", sequenceName = "ner_captcha_task_seq", allocationSize = 1)
    private Long id;

    @Column
    @Convert(converter = AnnotatedTokenListConverter.class)
    private List<AnnotatedToken> tokens = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<AnnotatedToken> getTokens() {
        return tokens;
    }

    public void setTokens(List<AnnotatedToken> tokens) {
        this.tokens = tokens;
    }

}
