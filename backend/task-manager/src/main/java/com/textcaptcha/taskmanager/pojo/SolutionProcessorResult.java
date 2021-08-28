package com.textcaptcha.taskmanager.pojo;

import com.textcaptcha.data.model.response.CaptchaTaskResponse;

public class SolutionProcessorResult {

    private final CaptchaTaskResponse taskResponse;
    private final SolutionCheckerResult checkResult;

    public SolutionProcessorResult(CaptchaTaskResponse taskResponse, SolutionCheckerResult checkResult) {
        this.taskResponse = taskResponse;
        this.checkResult = checkResult;
    }

    public CaptchaTaskResponse getTaskResponse() {
        return taskResponse;
    }

    public SolutionCheckerResult getCheckResult() {
        return checkResult;
    }

}
