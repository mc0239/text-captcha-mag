package com.textcaptcha.integrationdemo;

import com.textcaptcha.integrationdemo.model.Article;
import com.textcaptcha.integrationdemo.repository.ArticleRepository;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ShowcaseArticleGenerator {

    private final ArticleRepository repository;
    private final ResourceLoader resourceLoader;

    public ShowcaseArticleGenerator(ArticleRepository repository, ResourceLoader resourceLoader) {
        this.repository = repository;
        this.resourceLoader = resourceLoader;
    }

    public String getResourceTitle(Resource resource) {
        try {
            Optional<String> titleLine = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))
                    .lines().filter(line -> line.startsWith("# Naslov:")).findFirst();
            if (titleLine.isPresent()) {
                return titleLine.get().split("# Naslov:")[1].trim();
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getResourceContent(Resource resource) {
        try {
            return new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))
                    .lines().filter(line -> !line.startsWith("# ")).collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public Resource[] getSamples() {
        try {
            return ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources("classpath*:samples/*.txt");
        } catch (IOException e) {
            e.printStackTrace();
            return new Resource[0];
        }
    }

    @EventListener
    public void onApplicationStartup(ContextRefreshedEvent event) {
        List<Article> articles = new ArrayList<>();

        for (Resource sample : getSamples()) {
            Article a = new Article();
            a.setTitle(getResourceTitle(sample));
            a.setContent(getResourceContent(sample));
            a.setShowcase(true);
            articles.add(a);
        }

        repository.saveAllAndFlush(articles);
    }

}
