package com.textcaptcha.exception;

public class TextCaptchaException extends Exception {

    public TextCaptchaException(String message) {
        super(message);
    }

    public TextCaptchaException(String message, Throwable cause) {
        super(message, cause);
    }

    public TextCaptchaException(Throwable cause) {
        super(cause);
    }

}
