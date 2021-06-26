package com.textcaptcha.data;

public interface IdentifiableEntity<T> {
    T getId();
    void setId(T id);
}
