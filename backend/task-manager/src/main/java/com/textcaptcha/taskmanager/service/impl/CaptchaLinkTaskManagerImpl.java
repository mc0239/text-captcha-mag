package com.textcaptcha.taskmanager.service.impl;

import com.textcaptcha.data.repository.CaptchaLinkRepository;
import com.textcaptcha.taskmanager.service.CaptchaLinkTaskManager;
import org.springframework.stereotype.Service;

@Service
public class CaptchaLinkTaskManagerImpl extends BaseCaptchaLinkTaskManager implements CaptchaLinkTaskManager {

    public CaptchaLinkTaskManagerImpl(CaptchaLinkRepository repository) {
        super(repository);
    }

}
