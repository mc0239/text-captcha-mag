package com.textcaptcha.textingest.pojo.annotator;

import com.textcaptcha.data.model.task.content.CorefCaptchaTaskContent;

public class CorefAnnotatedToken extends AnnotatedToken {

    private Integer mentionId;
    private Integer clusterId;

    public Integer getClusterId() {
        return clusterId;
    }

    public void setClusterId(Integer clusterId) {
        this.clusterId = clusterId;
    }

    public Integer getMentionId() {
        return mentionId;
    }

    public void setMentionId(Integer mentionId) {
        this.mentionId = mentionId;
    }

    public static CorefCaptchaTaskContent.Token toContentToken(CorefAnnotatedToken in) {
        CorefCaptchaTaskContent.Token out = new CorefCaptchaTaskContent.Token();
        out.setWord(in.word);
        out.setMentionId(in.getMentionId());
        out.setClusterId(in.getClusterId());
        return out;
    }

    @Override
    public String toString() {
        return word + (mentionId != null ? "[M=" + mentionId + ",C=" + clusterId + "]" : "");
    }
}
