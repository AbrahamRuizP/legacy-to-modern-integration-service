package com.legacy.integration.customer.soap;

import com.legacy.integration.customer.entity.Customer;
import com.legacy.integration.customer.service.CustomerService;
import com.legacy.integration.customer.soap.dto.CustomerSoapResponse;
import jakarta.inject.Inject;
import jakarta.jws.WebService;

import java.util.UUID;

@WebService(
        serviceName = "CustomerService",
        portName = "CustomerServicePort",
        endpointInterface = "com.legacy.integration.customer.soap.CustomerSoapService"
)
public class CustomerSoapServiceImpl implements CustomerSoapService {

    @Inject
    private CustomerService customerService;

    @Override
    public CustomerSoapResponse getCustomerById(String id) {
        UUID uuid = UUID.fromString(id);

        Customer customer = customerService.findById(uuid);
        return toResponse(customer);
    }

    private static CustomerSoapResponse toResponse(Customer c) {
        if (c != null) {
            CustomerSoapResponse response = new CustomerSoapResponse();
            response.setId(c.getId());
            response.setFirstName(c.getFirstName());
            response.setLastName(c.getLastName());
            response.setStatus(c.getStatus().name());
            response.setCreatedAt(c.getCreatedAt());
            return response;
        }
        return null;
    }
}
