package com.textcaptcha.taskmanager.repository;

import com.textcaptcha.taskmanager.model.CaptchaTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaptchaTaskRepository extends JpaRepository<CaptchaTask, Long> {

    List<CaptchaTask> getByArticleUrlHashAndArticleTextHash(String articleUrlHash, String articleTextHash);

    long countByArticleUrlHashAndArticleTextHash(String articleUrlHash, String articleTextHash);

}
