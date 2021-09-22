package com.textcaptcha.data.repository;

import com.textcaptcha.data.model.response.CaptchaTaskResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CaptchaTaskResponseRepository extends JpaRepository<CaptchaTaskResponse, Long> {

    List<CaptchaTaskResponse> getCaptchaTaskResponseByCaptchaLinkUuid(UUID captchaLinkUuid);

}
