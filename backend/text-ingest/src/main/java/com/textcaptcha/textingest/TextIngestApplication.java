package com.textcaptcha.textingest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan(basePackages = {"com.textcaptcha"})
@ComponentScan(basePackages = {"com.textcaptcha"})
@EnableJpaRepositories(basePackages = {"com.textcaptcha"})
@EnableJpaAuditing
public class TextIngestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TextIngestApplication.class, args);
    }

}
