package com.bookretail.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;

import javax.annotation.PostConstruct;

@Configuration
public class AwsConfig {
    @Value("#{'${spring.profiles.active}'.split('-')[0]}")
    private String profile;

    @Value("${aws.access-key}")
    private String accessKey;

    @Value("${aws.secret-key}")
    private String secretKey;

    @Getter
    @Value("${application.aws.s3.bucket-name}")
    private String bucketName;

    @Getter
    @Value("#{T(software.amazon.awssdk.regions.Region).of('${application.aws.s3.region}')}")
    private Region region;

    @Getter
    private String baseUrl = null;

    @Getter
    private AwsBasicCredentials credentials = null;

    @PostConstruct()
    protected void init() {
        if (!profile.equals("dev") && !profile.equals("prod"))
            throw new IllegalStateException("Profile must be start with 'dev' or 'prod'.");

        baseUrl = profile;
        credentials = AwsBasicCredentials.create(accessKey, secretKey);
    }

}
