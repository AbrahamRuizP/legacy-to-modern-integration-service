package com.legacy.integration.xml;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;

public class XmlSchemaValidator {

    private final Schema schema;

    public XmlSchemaValidator() {
        try {
            SchemaFactory schemaFactory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            try (InputStream xsdStream =
                         getClass().getResourceAsStream("/xsd/customer.xsd")) {

                if (xsdStream == null) {
                    throw new IllegalStateException("XSD resource not found: /xsd/customer.xsd");
                }

                this.schema = schemaFactory.newSchema(new StreamSource(xsdStream));

            } catch (SAXException | IOException e) {
                throw new IllegalStateException(
                        "Failed to load customer XSD schema",
                        e
                );
            }
        } catch (IllegalStateException e) {
            throw new RuntimeException(e);
        }

    }

    public void validate(InputStream xmlStream) {

        try {
            Validator validator = schema.newValidator();

            validator.validate(new StreamSource(xmlStream));

        } catch (SAXException | IOException e) {
            throw new IllegalArgumentException("XML validation failed", e);
        }
    }
}
