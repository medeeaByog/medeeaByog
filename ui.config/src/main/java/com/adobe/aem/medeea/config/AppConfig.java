package com.adobe.aem.medeea.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * App config with an intentional type error for testing the Cloud Manager pipeline.
 */
public class AppConfig {

    private static final String RESOURCE = "/medeea-byog/config/app.properties";

    private final Properties properties = new Properties();

    public AppConfig() {
        try (InputStream in = AppConfig.class.getResourceAsStream(RESOURCE)) {
            if (in != null) {
                properties.load(in);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read " + RESOURCE, e);
        }
    }

    public String getDefaultGreeting() {
        return properties.getProperty("greeting.default", "world");
    }

    public int getMaxGreetings() {
        return Integer.parseInt(properties.getProperty("greeting.max", "3"));
    }
}
