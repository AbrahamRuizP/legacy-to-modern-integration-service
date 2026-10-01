package com.legacy.integration.customer.soap;

import com.legacy.integration.customer.soap.dto.CustomerSoapResponse;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

import java.util.UUID;

@WebService
public interface CustomerSoapService {

    @WebMethod
    CustomerSoapResponse getCustomerById(
        @WebParam(name = "customerId") UUID customerId
    );

}
