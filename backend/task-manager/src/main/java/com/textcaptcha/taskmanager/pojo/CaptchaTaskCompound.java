package com.textcaptcha.taskmanager.pojo;

import com.textcaptcha.data.model.CaptchaLink;

import java.util.Arrays;
import java.util.List;

public class CaptchaTaskCompound {

    private final CaptchaLink captchaLink;
    private final List<IssuedTaskInstance> taskInstances;

    public CaptchaTaskCompound(CaptchaLink captchaLink, IssuedTaskInstance ...taskInstances) {
        this.captchaLink = captchaLink;
        if (taskInstances.length != 0) {
            this.taskInstances = Arrays.asList(taskInstances);
        } else {
            this.taskInstances = null;
        }
    }

    public CaptchaLink getCaptchaLink() {
        return captchaLink;
    }

    public List<IssuedTaskInstance> getTaskInstances() {
        return taskInstances;
    }

}
