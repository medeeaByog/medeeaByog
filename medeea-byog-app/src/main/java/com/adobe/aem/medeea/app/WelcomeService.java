package com.adobe.aem.medeea.app;

import com.adobe.aem.medeea.config.AppConfig;
import com.adobe.aem.medeea.core.GreetingService;

/**
 * Combines the core and ui-config modules.
 */
public class WelcomeService {

    private final GreetingService greetingService = new GreetingService();
    private final AppConfig config = new AppConfig();

    public String welcome(String name) {
        String target = (name == null || name.isEmpty()) ? config.getDefaultGreeting() : name;
        return greetingService.greet(target);
    }
}
