package com.textcaptcha.taskmanager.service.impl;

import com.textcaptcha.data.model.CaptchaLink;
import com.textcaptcha.data.repository.CaptchaLinkRepository;
import com.textcaptcha.taskmanager.service.CaptchaLinkTaskManager;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public abstract class BaseCaptchaLinkTaskManager implements CaptchaLinkTaskManager {

    protected final CaptchaLinkRepository captchaLinkRepository;

    public BaseCaptchaLinkTaskManager(CaptchaLinkRepository repository) {
        this.captchaLinkRepository = repository;
    }

    @Override
    public CaptchaLink getLink(UUID linkId) {
        return captchaLinkRepository.getByUuid(linkId);
    }

}
