package com.textcaptcha.data.repository;

import com.textcaptcha.data.model.CaptchaFlow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CaptchaFlowRepository extends JpaRepository<CaptchaFlow, Long> {

    CaptchaFlow getByUuid(UUID uuid);

}
