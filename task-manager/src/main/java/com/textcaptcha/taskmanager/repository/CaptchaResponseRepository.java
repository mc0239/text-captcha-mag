package com.textcaptcha.taskmanager.repository;

import com.textcaptcha.taskmanager.model.CaptchaResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaptchaResponseRepository extends JpaRepository<CaptchaResponse, Long> {

}
