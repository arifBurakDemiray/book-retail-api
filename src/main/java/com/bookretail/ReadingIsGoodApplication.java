package com.bookretail;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;


@EnableScheduling
@SpringBootApplication
public class ReadingIsGoodApplication {

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(ReadingIsGoodApplication.class)
                .bannerMode(Banner.Mode.CONSOLE)
                .run(args);
    }
}
