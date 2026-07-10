package com.adobe.aem.medeea;

/**
 * Sample service class with intentional syntax error for testing Cloud Manager pipeline
 */
public class SampleService {

    private String serviceName;

    public SampleService() {
        this.serviceName = "Medeea BYOG Service"
        // Missing semicolon above will cause compilation to fail
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
