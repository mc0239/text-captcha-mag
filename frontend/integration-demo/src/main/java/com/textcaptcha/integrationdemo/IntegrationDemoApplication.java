package com.textcaptcha.integrationdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Arrays;

@SpringBootApplication
@EnableJpaAuditing
public class IntegrationDemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(IntegrationDemoApplication.class, args);

        printLocalAddress(context);
    }

    private static void printLocalAddress(ConfigurableApplicationContext context) {
        Environment env = context.getEnvironment();

        if (!Arrays.asList(env.getActiveProfiles()).contains("dev")) {
            // Only print address on dev environment.
            return;
        }

        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");

        if (path == null || "/".equals(path)) {
            path = "";
        }

        String address = "http://localhost:" + port + path;

        System.out.println("Application started on address: " + address);
    }

}