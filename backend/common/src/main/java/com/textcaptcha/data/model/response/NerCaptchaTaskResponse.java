package com.textcaptcha.data.model.response;

import com.textcaptcha.converter.MarkedTokenIndexListConverter;
import com.textcaptcha.data.model.task.NerCaptchaTask;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ner_captcha_task_response")
public class NerCaptchaTaskResponse extends CaptchaTaskResponse<NerCaptchaTask> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "ner_captcha_task_response_seqgen")
    @SequenceGenerator(name = "ner_captcha_task_response_seqgen", sequenceName = "ner_captcha_task_response_seq", allocationSize = 1)
    private Long id;

    @Column(name = "marked_tokens")
    @Convert(converter = MarkedTokenIndexListConverter.class)
    private List<Integer> markedTokenIndexList = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Integer> getMarkedTokenIndexList() {
        return markedTokenIndexList;
    }

    public void setMarkedTokenIndexList(List<Integer> markedTokenIndexList) {
        this.markedTokenIndexList = markedTokenIndexList;
    }

}
