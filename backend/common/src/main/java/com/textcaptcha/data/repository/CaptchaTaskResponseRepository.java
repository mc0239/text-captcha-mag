package com.textcaptcha.data.repository;

import com.textcaptcha.data.model.response.CaptchaTaskResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaptchaTaskResponseRepository extends JpaRepository<CaptchaTaskResponse, Long> {

}
