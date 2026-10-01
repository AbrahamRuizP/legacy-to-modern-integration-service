package com.legacy.integration.customer.service;

import com.legacy.integration.customer.entity.Customer;
import com.legacy.integration.customer.repository.CustomerRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;
import java.util.UUID;

@Stateless
public class CustomerServiceBean implements CustomerService {

    @Inject
    private CustomerRepository customerRepository;

    @Override
    public Customer findById(UUID id) {
        return customerRepository.findById(id);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

}
