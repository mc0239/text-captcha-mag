package com.textcaptcha.taskmanager.repository;

import com.textcaptcha.taskmanager.model.Article;
import com.textcaptcha.taskmanager.model.CaptchaTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    boolean existsByArticleUrlHashAndArticleTextHash(String articleUrlHash, String articleTextHash);

}
