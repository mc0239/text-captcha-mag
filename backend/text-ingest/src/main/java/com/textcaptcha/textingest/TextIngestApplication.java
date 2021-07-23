package com.textcaptcha.textingest;

import com.textcaptcha.util.OpenApiUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.textcaptcha"})
@ComponentScan(basePackages = {"com.textcaptcha"})
@EnableJpaRepositories(basePackages = {"com.textcaptcha"})
@EnableJpaAuditing
public class TextIngestApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TextIngestApplication.class, args);

        System.out.println("OpenAPI: " + OpenApiUtils.openApiDocUrl(context));
    }

}
