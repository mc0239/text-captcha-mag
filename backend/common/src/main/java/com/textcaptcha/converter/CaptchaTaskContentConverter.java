package com.textcaptcha.converter;

import com.textcaptcha.data.model.task.content.CaptchaTaskContent;
import com.textcaptcha.data.model.task.content.NerCaptchaTaskContent;
import com.textcaptcha.exception.AttributeConverterException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class CaptchaTaskContentConverter implements AttributeConverter<CaptchaTaskContent, String> {

    @Override
    public String convertToDatabaseColumn(CaptchaTaskContent taskContent) {
        if (taskContent instanceof NerCaptchaTaskContent) {
            NerCaptchaTaskContent c = (NerCaptchaTaskContent) taskContent;

            return "NER\n" + c.getTokens().stream()
                    .map(token -> token.getWord() + "\t" + token.getAnnotation() + "\t" + token.getScore())
                    .collect(Collectors.joining("\n"));

        } else {
            throw new AttributeConverterException("Cannot convert type " + taskContent.getClass() + " to a string.");
        }
    }

    @Override
    public CaptchaTaskContent convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }

        String[] lines = s.split("\n");
        // first line is content type

        if (lines[0].startsWith("NER")) {

            List<NerCaptchaTaskContent.Token> tokens = Arrays.stream(s.split("\n"))
                    .skip(1)
                    .filter(t -> t.matches(".+\\t.*\\t[0-9.,]+"))
                    .map(t -> {
                        String[] tp = t.split("\t");
                        NerCaptchaTaskContent.Token at = new NerCaptchaTaskContent.Token();
                        at.setWord(tp[0]);
                        at.setAnnotation(tp[1]);
                        at.setScore(Double.valueOf(tp[2]));
                        return at;
                    }).collect(Collectors.toList());

            return new NerCaptchaTaskContent(tokens);

        } else {
            throw new AttributeConverterException("Cannot convert string [" + s + "] to any valid type.");
        }
    }

}
