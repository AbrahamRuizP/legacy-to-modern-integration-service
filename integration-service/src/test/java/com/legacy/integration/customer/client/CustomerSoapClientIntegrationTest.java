package com.legacy.integration.customer.client;

import com.legacy.integration.customer.soap.generated.CustomerSoapResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerSoapClientIntegrationTest {

    private static final String CUSTOMER_ID =
            "8f6a10d4-b574-4162-916e-fc3349bb528a";

    @Test
    void shouldRetrieveCustomerFromLegacySystem() {

        CustomerSoapClient client =
                new CustomerSoapClient();

        CustomerSoapResponse response =
                client.getCustomerById(CUSTOMER_ID);

        assertNotNull(response);

        assertEquals(CUSTOMER_ID, response.getId());
        assertEquals("John", response.getFirstName());
        assertEquals("Smith", response.getLastName());
        assertEquals("ACTIVE", response.getStatus());
    }

    @Test
    void shouldCaptureExtractAndValidateRealSoapPayload() {

        CustomerSoapClient client =
                new CustomerSoapClient();

        client.getCustomerById(CUSTOMER_ID);

        String customerXml =
                client.getValidatedCustomerXml();

        assertNotNull(customerXml);

        assertTrue(
                customerXml.contains(
                        "http://legacy.integration/customer"
                )
        );

        assertTrue(
                customerXml.contains(
                        "<firstName>John</firstName>"
                )
        );

        assertTrue(
                customerXml.contains(
                        "<status>ACTIVE</status>"
                )
        );
    }
}
