package com.textcaptcha.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.textcaptcha.data.model.task.TaskType;
import com.textcaptcha.data.model.task.content.CaptchaTaskContent;
import com.textcaptcha.data.model.task.content.CorefCaptchaTaskContent;
import com.textcaptcha.data.model.task.content.NerCaptchaTaskContent;
import com.textcaptcha.exception.AttributeConverterException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CaptchaTaskContentConverter implements AttributeConverter<CaptchaTaskContent, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(CaptchaTaskContent taskContent) {
        if (taskContent instanceof NerCaptchaTaskContent) {
            NerCaptchaTaskContent c = (NerCaptchaTaskContent) taskContent;

            try {
                return TaskType.ShortName.NER + mapper.writeValueAsString(c);
            } catch (JsonProcessingException e) {
                throw new AttributeConverterException(e);
            }
        } else if (taskContent instanceof CorefCaptchaTaskContent) {
            CorefCaptchaTaskContent c = (CorefCaptchaTaskContent) taskContent;

            try {
                return TaskType.ShortName.COREF + mapper.writeValueAsString(c);
            } catch (JsonProcessingException e) {
                throw new AttributeConverterException(e);
            }
        } else {
            throw new AttributeConverterException("Cannot convert type " + taskContent.getClass() + " to a string.");
        }
    }

    @Override
    public CaptchaTaskContent convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }

        String contentIdentifier = s.substring(0, TaskType.ShortName.length);
        String content = s.substring(TaskType.ShortName.length);
        if (contentIdentifier.equals(TaskType.ShortName.NER)) {
            try {
                return mapper.readValue(content, NerCaptchaTaskContent.class);
            } catch (JsonProcessingException e) {
                throw new AttributeConverterException(e);
            }
        } else if (contentIdentifier.equals(TaskType.ShortName.COREF)) {
            try {
                return mapper.readValue(content, CorefCaptchaTaskContent.class);
            } catch (JsonProcessingException e) {
                throw new AttributeConverterException(e);
            }
        } else {
            throw new AttributeConverterException("Cannot convert string [" + s + "] to any valid type.");
        }
    }

}
