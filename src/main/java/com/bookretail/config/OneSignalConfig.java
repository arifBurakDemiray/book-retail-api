package com.bookretail.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class OneSignalConfig {
    @Value("${onesignal.api-key}")
    private String apiKey;

    @Value("${onesignal.app-id}")
    private String appId;
}
