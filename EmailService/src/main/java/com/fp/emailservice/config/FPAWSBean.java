package com.fp.emailservice.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Utility config class that wraps AWS Connectivity info
 */
@Configuration
public class FPAWSBean implements AWSCredentials {
    @Value("${aws_access_key_id}")
    private String accessKey;

    @Value(("${aws_secret_access_key}"))
    private String secretKey;

    @Override
    public String getAWSAccessKeyId() {
        return accessKey;
    }

    @Override
    public String getAWSSecretKey() {
        return secretKey;
    }

}
