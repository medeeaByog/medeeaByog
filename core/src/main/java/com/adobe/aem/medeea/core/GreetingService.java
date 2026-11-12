package com.adobe.aem.medeea.core;

/**
 * Greeting service with an intentional syntax error for testing the Cloud Manager pipeline.
 */
public class GreetingService {

    public String greet(String name) {
        String target = (name == null || name.isEmpty()) ? "world" : name;
        // Missing semicolon above will cause compilation to fail
        return "Hello, " + target + "!";
    }
}
