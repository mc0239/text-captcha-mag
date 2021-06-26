package com.textcaptcha.data.repository;

import com.textcaptcha.data.model.task.CaptchaTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.List;

@NoRepositoryBean
public interface CaptchaTaskRepository<X extends CaptchaTask> extends JpaRepository<X, Long> {

    List<X> getByArticleUrlHashAndArticleTextHash(String articleUrlHash, String articleTextHash);

    long countByArticleUrlHashAndArticleTextHash(String articleUrlHash, String articleTextHash);

}
