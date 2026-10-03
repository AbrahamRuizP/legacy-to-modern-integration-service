package com.legacy.integration.customer.soap.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@XmlRootElement(
        name = "customer",
        namespace = "http://legacy.integration/customer"
)
@XmlType(
        propOrder = {
                "id",
                "firstName",
                "lastName",
                "createdAt",
                "status"
        }
)
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerSoapResponse {

    private UUID id;
    private String firstName;
    private String lastName;
    private String createdAt;
    private String status;

}