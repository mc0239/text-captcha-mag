package com.textcaptcha.taskmanager.service;

import com.textcaptcha.data.model.CaptchaLink;

import java.util.UUID;

public interface CaptchaLinkTaskManager {

    CaptchaLink getLink(UUID linkId);

}
