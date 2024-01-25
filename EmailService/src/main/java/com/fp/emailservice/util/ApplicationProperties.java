package com.fp.emailservice.util;

import com.google.common.base.Objects;

import java.io.IOException;
import java.util.Properties;

/**
 * Reads build properties that can be injected into other beans
 */
public final class ApplicationProperties {

    Properties applicationProperties;

    public  ApplicationProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(this.getClass().getResourceAsStream("/build.properties"));
        this.applicationProperties = properties;
    }

    public Properties getApplicationProperties() {
        return applicationProperties;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("applicationProperties", applicationProperties)
                .toString();
    }
}
