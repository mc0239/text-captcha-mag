package com.textcaptcha.converter;

import com.textcaptcha.data.model.response.content.CaptchaTaskResponseContent;
import com.textcaptcha.data.model.response.content.NerCaptchaTaskResponseContent;
import com.textcaptcha.exception.AttributeConverterException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class CaptchaTaskResponseContentConverter implements AttributeConverter<CaptchaTaskResponseContent, String> {

    @Override
    public String convertToDatabaseColumn(CaptchaTaskResponseContent responseContent) {
        if (responseContent instanceof NerCaptchaTaskResponseContent) {
            NerCaptchaTaskResponseContent r = (NerCaptchaTaskResponseContent) responseContent;

            return "NER\n" + r.getTokenIndexes().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining("\n"));

        } else {
            throw new AttributeConverterException("Cannot convert type " + responseContent.getClass() + " to a string.");
        }
    }

    @Override
    public CaptchaTaskResponseContent convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }

        String[] lines = s.split("\n");
        // first line is content type

        if (lines[0].startsWith("NER")) {

            List<Integer> tokenIndexes = Arrays.stream(s.split("\n"))
                    .skip(1)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            return new NerCaptchaTaskResponseContent(tokenIndexes);

        } else {
            throw new AttributeConverterException("Cannot convert string [" + s + "] to any valid type.");
        }
    }

}
