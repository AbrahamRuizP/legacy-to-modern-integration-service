package com.legacy.integration.customer.client;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CustomerSoapPayloadExtractorTest {

    private static final String CUSTOMER_NAMESPACE =
            "http://legacy.integration/customer";

    private static final String SOAP_RESPONSE = """
            <?xml version="1.0" encoding="UTF-8"?>
            <soap:Envelope
                    xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                    xmlns:service="http://soap.customer.integration.legacy.com/">
                <soap:Body>
                    <service:getCustomerByIdResponse>
                        <return>
                            <id>8f6a10d4-b574-4162-916e-fc3349bb528a</id>
                            <firstName>John</firstName>
                            <lastName>Smith</lastName>
                            <createdAt>2026-09-01T10:30:00Z</createdAt>
                            <status>ACTIVE</status>
                        </return>
                    </service:getCustomerByIdResponse>
                </soap:Body>
            </soap:Envelope>
            """;

    private final CustomerSoapPayloadExtractor extractor =
            new CustomerSoapPayloadExtractor();

    @Test
    void extractsCustomerPayloadIntoCustomerXml() throws Exception {
        String customerXml = extractor.extractCustomerXml(SOAP_RESPONSE);
        Document document = parseXml(customerXml);

        assertEquals("customer", document.getDocumentElement().getLocalName());
        assertEquals(CUSTOMER_NAMESPACE, document.getDocumentElement().getNamespaceURI());
        assertEquals("8f6a10d4-b574-4162-916e-fc3349bb528a",
                document.getElementsByTagNameNS(CUSTOMER_NAMESPACE, "id").item(0).getTextContent());
        assertEquals("John",
                document.getElementsByTagNameNS(CUSTOMER_NAMESPACE, "firstName").item(0).getTextContent());
        assertEquals("Smith",
                document.getElementsByTagNameNS(CUSTOMER_NAMESPACE, "lastName").item(0).getTextContent());
        assertEquals("2026-09-01T10:30:00Z",
                document.getElementsByTagNameNS(CUSTOMER_NAMESPACE, "createdAt").item(0).getTextContent());
        assertEquals("ACTIVE",
                document.getElementsByTagNameNS(CUSTOMER_NAMESPACE, "status").item(0).getTextContent());
    }

    @Test
    void throwsWhenSoapResponseHasNoCustomerPayload() {
        String soapXml = """
                <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                               xmlns:service="http://soap.customer.integration.legacy.com/">
                    <soap:Body>
                        <service:getCustomerByIdResponse/>
                    </soap:Body>
                </soap:Envelope>
                """;

        assertThrows(IllegalArgumentException.class,
                () -> extractor.extractCustomerXml(soapXml));
    }

    @Test
    void throwsWhenCustomerPayloadIsMissingARequiredField() {
        String soapXml = """
                <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                               xmlns:service="http://soap.customer.integration.legacy.com/">
                    <soap:Body>
                        <service:getCustomerByIdResponse>
                            <return>
                                <id>8f6a10d4-b574-4162-916e-fc3349bb528a</id>
                                <firstName>John</firstName>
                                <lastName>Smith</lastName>
                                <createdAt>2026-09-01T10:30:00Z</createdAt>
                            </return>
                        </service:getCustomerByIdResponse>
                    </soap:Body>
                </soap:Envelope>
                """;

        assertThrows(IllegalArgumentException.class,
                () -> extractor.extractCustomerXml(soapXml));
    }

    @Test
    void throwsWhenSoapXmlIsMalformed() {
        assertThrows(IllegalArgumentException.class,
                () -> extractor.extractCustomerXml("<soap:Envelope>"));
    }

    private static Document parseXml(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        return factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
    }
}
