package com.bookretail.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.nio.charset.StandardCharsets;

@Configuration
public class ThymeleafConfig {
    
    @Bean
    public SpringTemplateEngine springTemplateEngine(ApplicationContext applicationContext) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();

        templateEngine.addTemplateResolver(defaultTemplateResolver(applicationContext));
        templateEngine.setEnableSpringELCompiler(true);

        return templateEngine;
    }

    @Bean
    public SpringResourceTemplateResolver defaultTemplateResolver(ApplicationContext applicationContext) {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();

        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("classpath:/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        templateResolver.setCacheable(true);
        templateResolver.setOrder(0);

        return templateResolver;
    }
}