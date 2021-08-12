package com.textcaptcha.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.textcaptcha.data.model.response.content.CaptchaTaskResponseContent;
import com.textcaptcha.data.model.response.content.CorefCaptchaTaskResponseContent;
import com.textcaptcha.data.model.response.content.NerCaptchaTaskResponseContent;
import com.textcaptcha.data.model.task.TaskType;
import com.textcaptcha.exception.AttributeConverterException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CaptchaTaskResponseContentConverter implements AttributeConverter<CaptchaTaskResponseContent, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(CaptchaTaskResponseContent responseContent) {
        if (responseContent instanceof NerCaptchaTaskResponseContent) {
            NerCaptchaTaskResponseContent c = (NerCaptchaTaskResponseContent) responseContent;

            try {
                return TaskType.ShortName.NER + mapper.writeValueAsString(c);
            } catch (JsonProcessingException e) {
                throw new AttributeConverterException(e);
            }
        } else if (responseContent instanceof CorefCaptchaTaskResponseContent) {
            CorefCaptchaTaskResponseContent c = (CorefCaptchaTaskResponseContent) responseContent;

            try {
                return TaskType.ShortName.COREF + mapper.writeValueAsString(c);
            } catch (JsonProcessingException e) {
                throw new AttributeConverterException(e);
            }
        } else {
            throw new AttributeConverterException("Cannot convert type " + responseContent.getClass() + " to a string.");
        }
    }

    @Override
    public CaptchaTaskResponseContent convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }

        String contentIdentifier = s.substring(0, TaskType.ShortName.length);
        String content = s.substring(TaskType.ShortName.length);
        if (contentIdentifier.equals(TaskType.ShortName.NER)) {
            try {
                return mapper.readValue(content, NerCaptchaTaskResponseContent.class);
            } catch (JsonProcessingException e) {
                throw new AttributeConverterException(e);
            }
        } else if (contentIdentifier.equals(TaskType.ShortName.COREF)) {
            try {
                return mapper.readValue(content, CorefCaptchaTaskResponseContent.class);
            } catch (JsonProcessingException e) {
                throw new AttributeConverterException(e);
            }
        } else {
            throw new AttributeConverterException("Cannot convert string [" + s + "] to any valid type.");
        }
    }

}
