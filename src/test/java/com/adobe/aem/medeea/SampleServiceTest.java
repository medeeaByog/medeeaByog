package com.adobe.aem.medeea;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SampleServiceTest {

    @Test
    public void testDefaultConstructorSetsServiceName() {
        SampleService service = new SampleService();
        assertNotNull(service.getServiceName());
        assertEquals("Medeea BYOG Service", service.getServiceName());
    }

    @Test
    public void testSetServiceName() {
        SampleService service = new SampleService();
        service.setServiceName("Custom Service");
        assertEquals("Custom Service", service.getServiceName());
    }

    @Test
    public void testSetServiceNameNull() {
        SampleService service = new SampleService();
        service.setServiceName(null);
        assertEquals(null, service.getServiceName());
    }

    @Test
    public void testSetServiceNameEmpty() {
        SampleService service = new SampleService();
        service.setServiceName("");
        assertEquals("", service.getServiceName());
    }
}
