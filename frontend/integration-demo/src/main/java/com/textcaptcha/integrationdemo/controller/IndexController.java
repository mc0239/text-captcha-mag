package com.textcaptcha.integrationdemo.controller;

import com.textcaptcha.integrationdemo.dto.CaptchaCheckDto;
import com.textcaptcha.integrationdemo.model.Article;
import com.textcaptcha.integrationdemo.model.Reaction;
import com.textcaptcha.integrationdemo.repository.ArticleRepository;
import com.textcaptcha.integrationdemo.repository.ReactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
public class IndexController {

    private final ArticleRepository articleRepository;
    private final ReactionRepository reactionRepository;

    private final RestTemplate rest;

    @Value("${captcha.check-url}")
    private String captchaCheckUrl;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Autowired
    public IndexController(ArticleRepository articleRepository, ReactionRepository reactionRepository) {
        this.articleRepository = articleRepository;
        this.reactionRepository = reactionRepository;

        rest = new RestTemplate();
        rest.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
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
    public ModelAndView article(@PathVariable("uuid") UUID uuid, HttpServletRequest servletRequest, Map<String, Object> model) {
        if (uuid == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Optional<Article> article = articleRepository.findById(uuid);

        if (article.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Article does not exist.");
        }

        model.put("base_path", contextPath);
        model.put("article", article.get());
        model.put("message", servletRequest.getSession().getAttribute("message"));

        Optional<Reaction> reaction = reactionRepository.findByArticleAndCreatedBy(article.get(), servletRequest.getSession().getId());
        model.put("reaction", reaction.isPresent());

        return new ModelAndView("article", model);
    }

    @PostMapping("/r/{uuid}")
    public RedirectView react(@PathVariable("uuid") UUID articleUuid, @RequestParam("captcha") String captchaId, HttpServletRequest servletRequest) {
        boolean captchaOk = captchaCheck(captchaId);

        if (!captchaOk) {
            servletRequest.getSession().setAttribute("message", "CAPTCHA not solved. Please solve CAPTCHA first.");
            return new RedirectView("/a/" + articleUuid);
        }

        Article a = articleRepository.getById(articleUuid);

        Optional<Reaction> existing = reactionRepository.findByArticleAndCreatedBy(a, servletRequest.getSession().getId());

        if (existing.isEmpty()) {
            Reaction r = new Reaction();
            r.setArticle(a);
            r.setCreatedBy(servletRequest.getSession().getId());
            r = reactionRepository.save(r);
            servletRequest.getSession().setAttribute("message", "You liked this.");
        } else {
            reactionRepository.delete(existing.get());
            servletRequest.getSession().setAttribute("message", "You unliked this.");
        }

        return new RedirectView("/a/" + articleUuid);
    }

    private boolean captchaCheck(String captchaId) {
        String url = captchaCheckUrl + "/" + captchaId;

        try {
            ResponseEntity<CaptchaCheckDto> restResponse = rest.getForEntity(url, CaptchaCheckDto.class);
            CaptchaCheckDto response = restResponse.getBody();
            if (response != null) {
                return response.getFlowComplete();
            }
        } catch (Exception ignored) { }

        return false;
    }

}
