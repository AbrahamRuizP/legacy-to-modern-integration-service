package com.legacy.integration.customer.client;

import com.legacy.integration.customer.soap.generated.CustomerService;
import com.legacy.integration.customer.soap.generated.CustomerSoapResponse;
import com.legacy.integration.customer.soap.generated.CustomerSoapService;
import org.springframework.stereotype.Component;

@Component
public class CustomerSoapClient {

    private final CustomerSoapService soapService;

    public CustomerSoapClient() {

        CustomerService service =
                new CustomerService();

        this.soapService =
                service.getCustomerServicePort();
    }

    public CustomerSoapResponse getCustomerById(String customerId) {
        return soapService.getCustomerById(customerId);
    }
}
