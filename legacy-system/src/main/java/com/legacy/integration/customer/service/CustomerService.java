package com.legacy.integration.customer.service;

import com.legacy.integration.customer.entity.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    Customer findById(UUID id);

    List<Customer> findAll();
}
