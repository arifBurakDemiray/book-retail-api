package com.bookretail.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.util.AnnotatedTypeScanner;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


@Configuration
public class SwaggerConfig {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${application.url}")
    private String url;


    @Bean
    public GroupedOpenApi publicApi() {

        return GroupedOpenApi.builder()
                .group("BOOKRETAIL")
                .pathsToExclude("/actuator/**")
                .packagesToScan("com.bookretail.controller")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI api() {
        OAuthFlow oAuthFlow = new OAuthFlow();
        oAuthFlow.scopes(new Scopes().addString("Default", "Default Scope."));
        oAuthFlow.tokenUrl("/auth/login");


        var openApi = new OpenAPI()
                .addServersItem(getServer())
                .info(new Info()
                        .title("ReadingIsGood API")
                        .version("1.0.0")
                        .description("ReadingIsGood Documentation"))
                .components(new Components()
                        .addSecuritySchemes("OAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .scheme("bearer")
                                .bearerFormat("jwt")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                                .flows(new OAuthFlows()
                                        .password(oAuthFlow))))
                .addSecurityItem(new SecurityRequirement().addList("OAuth"));

        openApi.setTags(getTags());

        return openApi;
    }

    public Server getServer() {
        var server = new Server();

        server.setUrl(url);

        return server;
    }

    public List<Tag> getTags() {
        var tags = new LinkedList<Tag>();

        var scanner = new AnnotatedTypeScanner(RestController.class);

        for (var controllerClass : scanner.findTypes("com.bookretail.controller")) {
            try {
                var name = (String) controllerClass.getDeclaredField("tag").get(null);
                var description = (String) controllerClass.getDeclaredField("description").get(null);

                tags.add(new Tag().name(name).description(description));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                logger.warn("The controller class {} does not have static string tag and description fields. " +
                        "All rest controllers must have these.", controllerClass.getSimpleName());
            }
        }

        tags.sort(Comparator.comparing(Tag::getName));

        return tags;
    }
}