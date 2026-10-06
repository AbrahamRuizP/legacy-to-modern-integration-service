package com.legacy.integration.xml;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

public class XmlSchemaValidator {

    private final Schema schema;

    public XmlSchemaValidator() {

        try {
            SchemaFactory schemaFactory =
                    SchemaFactory.newInstance(
                            XMLConstants.W3C_XML_SCHEMA_NS_URI
                    );

            schemaFactory.setProperty(
                    XMLConstants.ACCESS_EXTERNAL_DTD,
                    ""
            );

            try (InputStream xsdStream =
                    getClass().getResourceAsStream(
                            "/xsd/customer.xsd"
                    )) {

                if (xsdStream == null) {
                    throw new IllegalStateException(
                            "XSD resource not found: /xsd/customer.xsd"
                    );
                }

                this.schema =
                        schemaFactory.newSchema(
                                new StreamSource(xsdStream)
                        );
            }

        } catch (SAXException | IOException e) {
            throw new IllegalStateException(
                    "Failed to load customer XSD schema",
                    e
            );
        }
    }

    public void validate(String xml) {

        try {
            Validator validator =
                    schema.newValidator();

            validator.validate(
                    new StreamSource(
                            new StringReader(xml)
                    )
            );

        } catch (SAXException | IOException e) {
            throw new IllegalStateException(
                    "XML validation failed",
                    e
            );
        }

    }

}
