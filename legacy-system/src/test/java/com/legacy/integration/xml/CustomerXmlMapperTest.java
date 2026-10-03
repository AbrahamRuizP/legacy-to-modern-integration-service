package com.legacy.integration.xml;

import com.legacy.integration.customer.soap.dto.CustomerSoapResponse;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomerXmlMapperTest {

    @Test
    void serializesCustomerFieldsAsXml() {
        CustomerSoapResponse customer = new CustomerSoapResponse();
        customer.setId(UUID.fromString("8f6a10d4-b574-4162-916e-fc3349bb528a"));
        customer.setFirstName("John");
        customer.setLastName("Smith");
        customer.setCreatedAt("2026-09-01T10:30:00Z");
        customer.setStatus("ACTIVE");

        String xml = new CustomerXmlMapper().toXml(customer);

        assertTrue(xml.contains("<customerSoapResponse"));
        assertTrue(xml.contains("<id>8f6a10d4-b574-4162-916e-fc3349bb528a</id>"));
        assertTrue(xml.contains("<firstName>John</firstName>"));
        assertTrue(xml.contains("<lastName>Smith</lastName>"));
        assertTrue(xml.contains("<createdAt>2026-09-01T10:30:00Z</createdAt>"));
        assertTrue(xml.contains("<status>ACTIVE</status>"));
    }

    @Test
    void shouldGenerateXmlThatConformsToCustomerSchema() {

        CustomerSoapResponse customer = new CustomerSoapResponse();

        customer.setId(UUID.fromString("8f6a10d4-b574-4162-916e-fc3349bb528a"));
        customer.setFirstName("John");
        customer.setLastName("Smith");
        customer.setCreatedAt("2026-09-01T10:30:00Z");
        customer.setStatus("ACTIVE");

        CustomerXmlMapper customerXmlMapper = new CustomerXmlMapper();
        String xml = customerXmlMapper.toXml(customer);

        XmlSchemaValidator validator = new XmlSchemaValidator();

        assertDoesNotThrow(
                () -> validator.validate(
                        new ByteArrayInputStream(
                                xml.getBytes(StandardCharsets.UTF_8)
                        )
                )
        );
    }
}
