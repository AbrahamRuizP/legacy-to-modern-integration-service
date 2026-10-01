package com.legacy.integration.customer.service;

import com.legacy.integration.customer.entity.Customer;

import java.util.UUID;

public interface CustomerService {

    Customer findById(UUID id);

}
