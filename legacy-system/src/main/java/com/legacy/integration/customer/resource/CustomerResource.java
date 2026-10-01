package com.legacy.integration.customer.resource;

import com.legacy.integration.customer.entity.Customer;
import com.legacy.integration.customer.service.CustomerService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import java.util.List;

@Path("/customers")
public class CustomerResource {

    @Inject
    private CustomerService customerService;

    @GET
    public List<Customer> getCustomers() {
        return customerService.findAll();
    }

}
