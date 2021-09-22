package com.textcaptcha.taskmanager.controller;

import com.textcaptcha.data.model.CaptchaLink;
import com.textcaptcha.taskmanager.dto.link.CaptchaLinkDto;
import com.textcaptcha.taskmanager.service.impl.CaptchaLinkTaskManagerImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/link")
public class LinkController {

    private final CaptchaLinkTaskManagerImpl manager;

    public LinkController(CaptchaLinkTaskManagerImpl manager) {
        this.manager = manager;
    }

    @GetMapping("/check/{uuid}")
    public CaptchaLinkDto checkTaskFlow(@PathVariable("uuid") UUID linkUuid) {
        CaptchaLink link = manager.getLink(linkUuid);
        if (link == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid link ID.");
        }
        return new CaptchaLinkDto(link.getUuid(), link.isComplete());
    }

}
