package com.textcaptcha.taskmanager.pojo;

import com.textcaptcha.data.model.CaptchaLink;

public class CaptchaTaskFlow {

    private final CaptchaLink captchaLink;
    private final IssuedTaskInstance taskInstance;

    public CaptchaTaskFlow(CaptchaLink captchaLink, IssuedTaskInstance taskInstance) {
        this.captchaLink = captchaLink;
        this.taskInstance = taskInstance;
    }

    public CaptchaLink getCaptchaLink() {
        return captchaLink;
    }

    public IssuedTaskInstance getTaskInstance() {
        return taskInstance;
    }

}
