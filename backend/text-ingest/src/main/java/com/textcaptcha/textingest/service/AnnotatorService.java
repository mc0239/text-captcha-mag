package com.textcaptcha.textingest.service;

import com.textcaptcha.data.pojo.AnnotatedToken;
import com.textcaptcha.textingest.exception.AnnotatorException;

import java.util.List;

public interface AnnotatorService {
    List<AnnotatedToken> annotate(String text) throws AnnotatorException;
}
