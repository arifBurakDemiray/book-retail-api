package com.bookretail.config;

import net.kaczmarzyk.spring.data.jpa.web.SpecificationArgumentResolver;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import com.bookretail.util.ReadingIsGoodLocaleResolver;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;

@Configuration
@EnableAsync
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(@NotNull ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/public/index.html");
    }

    @Override
    public void addResourceHandlers(@NotNull ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/public/**")
                .addResourceLocations("classpath:/public/");
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

    @Bean
    public MessageSource messageSource() throws IOException {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(
                getClass().getClassLoader()
        );
        Resource[] resources = resolver.getResources("classpath:messages/**/*.properties");

        Path classpath = Path.of(StringUtils.substringBefore(System.getProperty("java.class.path"), ":"));

        var basenames = new HashSet<String>();

        for (var resource : resources) {
            String basename;
            if (resource instanceof FileSystemResource) {
                basename = classpath.relativize(resource.getFile().toPath()).toString();
            } else if (resource instanceof ClassPathResource) {
                basename = ((ClassPathResource) resource).getPath();
            } else {
                throw new IllegalStateException("Resource must be either FileSystemResource or ClassPathResource.");
            }

            String resourceName = basename.replaceAll("(|_[a-z]{2}_[A-Z]{2}).properties$", "");

            basenames.add("classpath:" + resourceName);
        }

        messageSource.setBasenames(basenames.toArray(String[]::new));
        messageSource.setDefaultEncoding("UTF-8");

        return messageSource;
    }

    @Bean
    public MessageSourceAccessor accessor(MessageSource source) {
        return new MessageSourceAccessor(source);
    }


    @Bean
    public LocalValidatorFactoryBean validator(MessageSource messageSource) {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();

        bean.setValidationMessageSource(messageSource);

        return bean;
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new ReadingIsGoodLocaleResolver();
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();

        localeChangeInterceptor.setParamName("lang");

        return localeChangeInterceptor;
    }

    @Override
    public void addInterceptors(@NotNull InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new SpecificationArgumentResolver());
    }
}
