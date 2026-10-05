package com.legacy.integration.customer.client;

import com.legacy.integration.customer.soap.generated.CustomerSoapResponse;
import com.legacy.integration.customer.soap.generated.CustomerSoapService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerSoapClientTest {

    private static final String CUSTOMER_ID = "8f6a10d4-b574-4162-916e-fc3349bb528a";

    @Test
    void forwardsCustomerIdAndReturnsSoapResponse() {
        CustomerSoapResponse expectedResponse = new CustomerSoapResponse();
        String[] receivedCustomerId = new String[1];
        CustomerSoapService soapService = customerId -> {
            receivedCustomerId[0] = customerId;
            return expectedResponse;
        };
        CustomerSoapClient client = new CustomerSoapClient(soapService);

        CustomerSoapResponse actualResponse = client.getCustomerById(CUSTOMER_ID);

        assertEquals(CUSTOMER_ID, receivedCustomerId[0]);
        assertSame(expectedResponse, actualResponse);
    }

    @Test
    void shouldCaptureRealSoapResponse() {

        CustomerSoapClient client =
                new CustomerSoapClient();

        CustomerSoapResponse response =
                client.getCustomerById(CUSTOMER_ID);

        assertNotNull(response);

        String xml =
                client.getLastResponseXml();

        assertNotNull(xml);
        assertFalse(xml.isBlank());

        System.out.println(xml);
    }
}
