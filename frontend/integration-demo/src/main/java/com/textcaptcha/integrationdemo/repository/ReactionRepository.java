package com.textcaptcha.integrationdemo.repository;

import com.textcaptcha.integrationdemo.model.Article;
import com.textcaptcha.integrationdemo.model.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, UUID> {

    Optional<Reaction> findByArticleAndCreatedBy(Article article, String createdBy);

}
