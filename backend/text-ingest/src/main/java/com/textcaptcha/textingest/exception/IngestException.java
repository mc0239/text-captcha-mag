package com.textcaptcha.textingest.exception;

import com.textcaptcha.exception.TextCaptchaException;

public class IngestException extends TextCaptchaException {

    public IngestException(String message) {
        super(message);
    }

    public IngestException(String message, Throwable cause) {
        super(message, cause);
    }

    public IngestException(Throwable cause) {
        super(cause);
    }

}
