package com.textcaptcha.taskmanager.pojo;

public class SolutionCheckerResult {

    private final boolean successful;
    private final String message;

    public SolutionCheckerResult(boolean successful, String message) {
        this.successful = successful;
        this.message = message;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getMessage() {
        return message;
    }

}
