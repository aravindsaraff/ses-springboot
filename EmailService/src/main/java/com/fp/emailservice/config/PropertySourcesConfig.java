package com.fp.emailservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Configuration
@SuppressWarnings("UnusedDeclaration")
public class PropertySourcesConfig {
    // TODO need a way to combine multiple locations
    /*private static final Resource[] LOCAL_PROPERTIES = new ClassPathResource[]{
            new ClassPathResource("emailservice.properties"),
    };*/

    private static final Resource[] FILE_SYSTEM_PROPERTIES = new FileSystemResource[]{
            new FileSystemResource("/usr/local/fp/emailservice.properties")
    };

    @SuppressWarnings("UnusedDeclaration")
    public static class Config {
        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
            pspc.setIgnoreResourceNotFound(true);
            pspc.setLocations(FILE_SYSTEM_PROPERTIES);
            return pspc;
        }
    }
}