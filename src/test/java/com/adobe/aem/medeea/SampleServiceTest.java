package com.adobe.aem.medeea;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit tests for {@link SampleService}.
 */
class SampleServiceTest {

    @Test
    void defaultGetServiceNameReturnsExpectedValue() {
        SampleService service = new SampleService();
        assertEquals("Medeea BYOG Service", service.getServiceName());
    }

    @Test
    void setServiceNameUpdatesReturnedValue() {
        SampleService service = new SampleService();
        service.setServiceName("X");
        assertEquals("X", service.getServiceName());
    }

    @Test
    void setServiceNameNullReturnsNull() {
        SampleService service = new SampleService();
        service.setServiceName(null);
        assertNull(service.getServiceName());
    }
}
