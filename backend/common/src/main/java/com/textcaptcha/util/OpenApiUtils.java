package com.textcaptcha.util;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

public class OpenApiUtils {

    public static String openApiDocUrl(ConfigurableApplicationContext appContext) {
        Environment env = appContext.getEnvironment();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");

        return "http://localhost:" + port + ("/".equals(path) ? "" : path) + "/swagger-ui.html";
    }

}
