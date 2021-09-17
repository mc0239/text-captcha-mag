package com.textcaptcha.integrationdemo.repository;

import com.textcaptcha.integrationdemo.model.Article;
import com.textcaptcha.integrationdemo.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {

    List<Comment> findByArticleOrderByCreatedAtDesc(Article article);

}
