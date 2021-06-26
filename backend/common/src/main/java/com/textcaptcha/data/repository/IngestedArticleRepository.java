package com.textcaptcha.data.repository;

import com.textcaptcha.data.model.IngestedArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngestedArticleRepository extends JpaRepository<IngestedArticle, Long> {

    boolean existsByArticleUrlHashAndArticleTextHash(String articleUrlHash, String articleTextHash);

}
