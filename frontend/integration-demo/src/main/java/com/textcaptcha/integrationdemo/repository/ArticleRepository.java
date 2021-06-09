package com.textcaptcha.integrationdemo.repository;

import com.textcaptcha.integrationdemo.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID> {

    @Query("FROM Article a WHERE a.isShowcase = true")
    List<Article> findAllShowcased();

}
