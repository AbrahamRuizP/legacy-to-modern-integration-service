package com.legacy.integration.xml;

import com.legacy.integration.customer.soap.dto.CustomerSoapResponse;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import java.io.StringWriter;

public class CustomerXmlMapper {

    private final JAXBContext context;

    public CustomerXmlMapper() {
        try {
            this.context = JAXBContext.newInstance(CustomerSoapResponse.class);
        } catch (JAXBException e) {
            throw new IllegalStateException("Failed to initialize JAXB context", e);
        }
    }

    public String toXml(CustomerSoapResponse customer) {

        try {
            Marshaller marshaller = context.createMarshaller();

            marshaller.setProperty(
                    Marshaller.JAXB_FORMATTED_OUTPUT,
                    Boolean.TRUE
            );

            StringWriter writer = new StringWriter();

            marshaller.marshal(customer, writer);

            return writer.toString();

        } catch (JAXBException e) {
            throw new IllegalArgumentException("Failed to serialize customer to XML", e);
        }
    }

}
