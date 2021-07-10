package com.textcaptcha.data.repository;

import com.textcaptcha.data.model.response.CaptchaTaskResponse;
import com.textcaptcha.data.model.response.content.CaptchaTaskResponseContent;
import com.textcaptcha.data.model.task.CaptchaTask;
import com.textcaptcha.data.model.task.content.CaptchaTaskContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CaptchaTaskResponseRepository<X extends CaptchaTaskResponse<? extends CaptchaTask<? extends CaptchaTaskContent>, ? extends CaptchaTaskResponseContent>> extends JpaRepository<X, Long> {

}
