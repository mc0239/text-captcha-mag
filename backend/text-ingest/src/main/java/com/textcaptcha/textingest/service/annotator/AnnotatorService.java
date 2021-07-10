package com.textcaptcha.textingest.service.annotator;

import com.textcaptcha.textingest.exception.AnnotatorException;

public interface AnnotatorService<I, O> {
    O annotate(I input) throws AnnotatorException;
}
