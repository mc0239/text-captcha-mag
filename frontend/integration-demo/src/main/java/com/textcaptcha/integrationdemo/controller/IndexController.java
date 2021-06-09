package com.textcaptcha.integrationdemo.controller;

import com.textcaptcha.integrationdemo.model.Article;
import com.textcaptcha.integrationdemo.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
public class IndexController {

    private final ArticleRepository articleRepository;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Autowired
    public IndexController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/")
    public ModelAndView index(Map<String, Object> model) {
        model.put("base_path", contextPath);
        model.put("showcases", articleRepository.findAllShowcased());
        return new ModelAndView("index", model);
    }

    @PostMapping("/submit")
    public RedirectView submit(@RequestParam("content") String content) {
        if (content == null || content.isBlank()) {
            return new RedirectView(contextPath + "/");
        }

        Article article = new Article();
        article.setContent(content);

        article = articleRepository.saveAndFlush(article);

        return new RedirectView(contextPath + "/a/" + article.getId());
    }

    @GetMapping("/a/{uuid}")
    public ModelAndView article(@PathVariable("uuid") UUID uuid, Map<String, Object> model) {
        if (uuid == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Optional<Article> article = articleRepository.findById(uuid);

        if (article.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Article does not exist.");
        }

        model.put("base_path", contextPath);
        model.put("article", article.get());

        return new ModelAndView("article", model);
    }

}
