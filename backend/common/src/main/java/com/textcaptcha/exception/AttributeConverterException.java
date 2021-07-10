package com.textcaptcha.exception;

public class AttributeConverterException extends RuntimeException {

    public AttributeConverterException(String message) {
        super(message);
    }

    public AttributeConverterException(String message, Throwable cause) {
        super(message, cause);
    }

    public AttributeConverterException(Throwable cause) {
        super(cause);
    }

}
