package com.textcaptcha.integrationdemo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class ErrorControllerImpl implements ErrorController {

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request, Map<String, Object> model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        model.put("base_path", contextPath);
        model.put("code", status.toString());
        model.put("message", message.toString());

        return new ModelAndView("error", model);
    }

}
