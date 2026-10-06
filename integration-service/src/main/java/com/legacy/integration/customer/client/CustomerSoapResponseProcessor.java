package com.legacy.integration.customer.client;

import com.legacy.integration.xml.XmlSchemaValidator;

public class CustomerSoapResponseProcessor {

    private final CustomerSoapPayloadExtractor payloadExtractor;
    private final XmlSchemaValidator schemaValidator;

    public CustomerSoapResponseProcessor() {
        this.payloadExtractor = new CustomerSoapPayloadExtractor();
        this.schemaValidator = new XmlSchemaValidator();
    }

    public String extractAndValidate(String soapXml) {
        String customerXml =
                payloadExtractor.extractCustomerXml(
                        soapXml
                );

        schemaValidator.validate(customerXml);

        return customerXml;
    }

}
