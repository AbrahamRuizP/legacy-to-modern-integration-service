package com.legacy.integration.customer.client;

import jakarta.xml.soap.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.io.StringWriter;

public class CustomerSoapPayloadExtractor {

    private static final String CUSTOMER_NAMESPACE =
            "http://legacy.integration/customer";

    private static final String SOAP_NAMESPACE =
            "http//schemas.xmlsoap.org/soap/envelope";

    public String extractCustomerXml(String soapXml) {

        try {
            Document soapDocument =
                    parseXml(soapXml);

            XPath xPath = XPathFactory
                    .newInstance()
                    .newXPath();

            String returnPath =
                    "/soap:Envelope/soap:Body/" +
                            "ns:getCustomerByIdResponse/return";

            xPath.setNamespaceContext(
                    new SoapNamespaceContext()
            );

            Node returnNode =
                    (Node) xPath.evaluate(
                            returnPath,
                            soapDocument,
                            XPathConstants.NODE
                    );

            if (returnNode == null) {
                throw new IllegalArgumentException(
                        "SOAP response does not contain customer payload"
                );
            }

            return buildCustomerXml(returnNode);

        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Failed to extract customer payload from SOAP response",
                    e
            );
        }
    }

    private Document parseXml(String soapXml) throws Exception {

        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();

        factory.setNamespaceAware(true);

        factory.setFeature(
                "http://xml.org/sax/features/external-general-entities",
                false
        );

        factory.setFeature(
                "http://xml.org/sax/features/external-parameter-entities",
                false
        );

        factory.setAttribute(
                XMLConstants.ACCESS_EXTERNAL_DTD,
                ""
        );

        factory.setAttribute(
                XMLConstants.ACCESS_EXTERNAL_SCHEMA,
                ""
        );

        return factory
                .newDocumentBuilder()
                .parse(
                        new InputSource(
                                new StringReader(soapXml)
                        )
                );
    }

    private String buildCustomerXml(Node returnNode) throws Exception {

        Document customerDoc =
                returnNode
                        .getOwnerDocument()
                        .getImplementation()
                        .createDocument(
                                CUSTOMER_NAMESPACE,
                                "customer",
                                null
                        );

        Element customer =
                customerDoc.getDocumentElement();

        String[] fields = {
            "id",
                "firstName",
                "lastName",
                "createdAt",
                "status"
        };

        for (String field : fields) {
            NodeList nodes =
                    ((Element) returnNode)
                            .getElementsByTagName(field);

            if (nodes.getLength() == 0) {
                throw new IllegalArgumentException(
                        "Customer payload is missing field: " + field
                );
            }

            Element sourceElement =
                    (Element) nodes.item(0);

            Element targetElement =
                    customerDoc.createElementNS(
                            CUSTOMER_NAMESPACE,
                            field
                    );

            targetElement.setTextContent(
                    sourceElement.getTextContent()
            );

            customer.appendChild(targetElement);
        }

        TransformerFactory transformerFactory =
                TransformerFactory.newInstance();

        transformerFactory.setAttribute(
                XMLConstants.ACCESS_EXTERNAL_DTD,
                ""
        );

        transformerFactory.setAttribute(
                XMLConstants.ACCESS_EXTERNAL_STYLESHEET,
                ""
        );

        Transformer transformer =
                transformerFactory.newTransformer();

        transformer.setOutputProperty(
                OutputKeys.INDENT,
                "yes"
        );

        transformer.setOutputProperty(
                OutputKeys.OMIT_XML_DECLARATION,
                "no"
        );

        StringWriter writer =
                new StringWriter();

        transformer.transform(
                new DOMSource(customerDoc),
                new StreamResult(writer)
        );

        return writer.toString();
    }


}

