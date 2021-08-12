package com.textcaptcha.textingest.pojo;

import com.textcaptcha.data.model.task.content.CorefCaptchaTaskContent;
import com.textcaptcha.textingest.pojo.annotator.CorefAnnotatedToken;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CorefTokenGroup {

    private final List<CorefAnnotatedToken> tokens;

    public CorefTokenGroup() {
        tokens = new ArrayList<>();
    }

    public List<CorefAnnotatedToken> getTokens() {
        return tokens;
    }

    public Integer getMentionId() {
        if (isEmpty()) {
            return null;
        }
        return getTokens().get(0).getMentionId();
    }

    public Integer getClusterId() {
        if (isEmpty()) {
            return null;
        }
        return getTokens().get(0).getClusterId();
    }

    public void add(CorefAnnotatedToken token) {
        this.tokens.add(token);
    }

    public boolean isEmpty() {
        return this.tokens.isEmpty();
    }

    public static List<CorefCaptchaTaskContent.Token> toContentTokens(CorefTokenGroup in) {
        return in.getTokens()
                .stream()
                .map(CorefAnnotatedToken::toContentToken)
                .collect(Collectors.toList());
    }

}
