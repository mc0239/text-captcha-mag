package com.textcaptcha.textingest.exception;

import com.textcaptcha.exception.TextCaptchaException;

public class AnnotatorException extends TextCaptchaException {

    public AnnotatorException(String message) {
        super(message);
    }

    public AnnotatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public AnnotatorException(Throwable cause) {
        super(cause);
    }

}
