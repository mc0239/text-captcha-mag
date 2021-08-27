package com.textcaptcha.taskmanager.pojo;

import com.textcaptcha.data.model.CaptchaFlow;

public class CaptchaTaskFlow {

    private final CaptchaFlow captchaFlow;
    private final IssuedTaskInstance taskInstance;

    public CaptchaTaskFlow(CaptchaFlow captchaFlow, IssuedTaskInstance taskInstance) {
        this.captchaFlow = captchaFlow;
        this.taskInstance = taskInstance;
    }

    public CaptchaFlow getCaptchaFlow() {
        return captchaFlow;
    }

    public IssuedTaskInstance getTaskInstance() {
        return taskInstance;
    }

}
