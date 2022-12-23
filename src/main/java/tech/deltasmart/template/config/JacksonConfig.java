package com.bookretail.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.geo.Point;
import com.bookretail.util.mapper.PointMixin;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper jacksonObjectMapper() {
        return new ObjectMapper()
                .setPropertyNamingStrategy(propertyNamingStrategy())
                .addMixIn(Point.class, PointMixin.class);
    }

    @Bean
    public PropertyNamingStrategy propertyNamingStrategy() {
        return new PropertyNamingStrategies.SnakeCaseStrategy();
    }
}
