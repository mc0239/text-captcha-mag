package com.textcaptcha.integrationdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class IntegrationDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntegrationDemoApplication.class, args);
    }

}