package com.textcaptcha.util;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.util.Arrays;

public class OpenApiUtils {

    public static void printApiDocUrl(ConfigurableApplicationContext context) {
        Environment env = context.getEnvironment();

        if (!Arrays.asList(env.getActiveProfiles()).contains("dev")) {
            // Only print address on dev environment.
            return;
        }

        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");

        String address = "http://localhost:" + port + ("/".equals(path) ? "" : path) + "/swagger-ui.html";

        System.out.println("OpenAPI/Swagger is available at: " + address);
    }

}
