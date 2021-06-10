package com.textcaptcha.integrationdemo;

import com.textcaptcha.integrationdemo.model.Article;
import com.textcaptcha.integrationdemo.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class ShowcaseArticleGenerator {

    public final ArticleRepository repository;

    @Value("classpath:samples/article1.txt")
    private Resource articleResource1;

    @Value("classpath:samples/article2.txt")
    private Resource articleResource2;

    @Value("classpath:samples/article3.txt")
    private Resource articleResource3;

    public ShowcaseArticleGenerator(ArticleRepository repository) {
        this.repository = repository;
    }

    public String getResourceContent(Resource resource) {
        try {
            return new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @EventListener
    public void onApplicationStartup(ContextRefreshedEvent event) {
        Article article1 = new Article();
        article1.setContent(getResourceContent(articleResource1));
        article1.setShowcase(true);

        Article article2 = new Article();
        article2.setContent(getResourceContent(articleResource2));
        article2.setShowcase(true);

        Article article3 = new Article();
        article3.setContent(getResourceContent(articleResource3));
        article3.setShowcase(true);

        repository.saveAllAndFlush(Arrays.asList(article1, article2, article3));
    }

}
