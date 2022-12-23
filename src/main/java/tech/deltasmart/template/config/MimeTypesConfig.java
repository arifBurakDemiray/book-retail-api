package com.bookretail.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MimeTypesConfig {
    @Getter
    private final List<String> validImageMimeTypes = List.of("image/jpeg", "image/png");
}
