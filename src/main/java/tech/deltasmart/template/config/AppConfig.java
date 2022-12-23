package com.bookretail.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AppConfig {
    @Value("${application.url}")
    private String url;

    @Value("${application.tmp-folder}")
    private String temporaryFolder;
}
