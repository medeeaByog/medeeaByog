package com.adobe.aem.medeea;

/**
 * Sample service class for testing the Cloud Manager pipeline.
 */
public class SampleService {

    private String serviceName;

    public SampleService() {
        this.serviceName = "Medeea BYOG Service";
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
