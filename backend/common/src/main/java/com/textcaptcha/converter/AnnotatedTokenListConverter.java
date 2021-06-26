package com.textcaptcha.converter;

import com.textcaptcha.data.pojo.AnnotatedToken;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class AnnotatedTokenListConverter implements AttributeConverter<List<AnnotatedToken>, String> {

    @Override
    public String convertToDatabaseColumn(List<AnnotatedToken> annotatedTokens) {
        return annotatedTokens.stream()
                .map(token -> token.getWord() + "\t" + token.getAnnotation() + "\t" + token.getScore())
                .collect(Collectors.joining("\n"));
    }

    @Override
    public List<AnnotatedToken> convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) {
            return Collections.emptyList();
        }

        return Arrays.stream(s.split("\n"))
                .filter(t -> t.matches(".+\\t.*\\t[0-9.,]+"))
                .map(t -> {
                    String[] tp = t.split("\t");
                    AnnotatedToken at = new AnnotatedToken();
                    at.setWord(tp[0]);
                    at.setAnnotation(tp[1]);
                    at.setScore(Double.valueOf(tp[2]));
                    return at;
                }).collect(Collectors.toList());
    }

}
