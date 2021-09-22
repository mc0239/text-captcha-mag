package com.textcaptcha.data.repository;

import com.textcaptcha.data.model.CaptchaLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CaptchaLinkRepository extends JpaRepository<CaptchaLink, Long> {

    CaptchaLink getByUuid(UUID uuid);

}
