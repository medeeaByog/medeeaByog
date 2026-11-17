package com.adobe.aem.medeea;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * JUnit 5 tests for SampleService.
 */
class SampleServiceTest {

    private SampleService service;

    @BeforeEach
    void setUp() {
        service = new SampleService();
    }

    @Test
    void defaultServiceName_is_MedeaBYOGService() {
        assertEquals("Medeea BYOG Service", service.getServiceName());
    }

    @Test
    void setServiceName_updates_value() {
        service.setServiceName("Custom Service");
        assertEquals("Custom Service", service.getServiceName());
    }
}
