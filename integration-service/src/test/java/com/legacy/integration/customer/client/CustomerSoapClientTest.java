package com.legacy.integration.customer.client;

import com.legacy.integration.customer.soap.generated.CustomerService;
import com.legacy.integration.customer.soap.generated.CustomerSoapResponse;
import com.legacy.integration.customer.soap.generated.CustomerSoapService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CustomerSoapClientTest {

    private static final String CUSTOMER_ID = "8f6a10d4-b574-4162-916e-fc3349bb528a";

    private CustomerSoapService soapService;
    private CustomerSoapClient client;
    private MockedConstruction<CustomerService> serviceConstruction;

    @BeforeEach
    void setUp() {
        soapService = mock(CustomerSoapService.class);
        serviceConstruction = mockConstruction(
                CustomerService.class,
                (service, context) -> when(service.getCustomerServicePort())
                        .thenReturn(soapService)
        );

        client = new CustomerSoapClient();
    }

    @AfterEach
    void tearDown() {
        serviceConstruction.close();
    }

    @Test
    void forwardsCustomerIdToSoapService() {
        CustomerSoapResponse expectedResponse = new CustomerSoapResponse();
        when(soapService.getCustomerById(CUSTOMER_ID)).thenReturn(expectedResponse);

        client.getCustomerById(CUSTOMER_ID);

        verify(soapService).getCustomerById(CUSTOMER_ID);
    }

    @Test
    void returnsResponseFromSoapService() {
        CustomerSoapResponse expectedResponse = new CustomerSoapResponse();
        when(soapService.getCustomerById(CUSTOMER_ID)).thenReturn(expectedResponse);

        CustomerSoapResponse actualResponse = client.getCustomerById(CUSTOMER_ID);

        assertSame(expectedResponse, actualResponse);
    }
}
