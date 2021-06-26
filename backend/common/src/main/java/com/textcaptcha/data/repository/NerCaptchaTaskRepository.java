package com.textcaptcha.data.repository;

import com.textcaptcha.data.model.task.NerCaptchaTask;
import org.springframework.stereotype.Repository;

@Repository
public interface NerCaptchaTaskRepository extends CaptchaTaskRepository<NerCaptchaTask> {
}
