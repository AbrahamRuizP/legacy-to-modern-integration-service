package com.legacy.integration.xml;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.util.Iterator;

public class CustomerXPathExtractor {

    private static final String NAMESPACE =
            "http://legacy.integration/customer";

    private final XPath xpath;

    public CustomerXPathExtractor() {
        this.xpath = XPathFactory
                .newInstance()
                .newXPath();

        xpath.setNamespaceContext(new NamespaceContext() {

            @Override
            public String getNamespaceURI(String prefix) {
                if ("customer".equals(prefix)) {
                    return NAMESPACE;
                }

                if (XMLConstants.XML_NS_PREFIX.equals(prefix)) {
                    return XMLConstants.XML_NS_URI;
                }

                if (XMLConstants.XMLNS_ATTRIBUTE.equals(prefix)) {
                    return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
                }

                return XMLConstants.NULL_NS_URI;
            }

            @Override
            public String getPrefix(String namespaceURI) {
                if (NAMESPACE.equals(namespaceURI)) {
                    return "customer";
                }

                return null;
            }

            @Override
            public Iterator<String> getPrefixes(String namespaceURI) {
                return null;
            }
        });
    }

    public String extractFirstName(String xml) {
        return evaluateString(xml, "/customer:customer/customer:firstName");
    }

    public String extractLastName(String xml) {
        return evaluateString(xml, "/customer:customer/customer:lastName");
    }

    public String extractStatus(String xml) {
        return evaluateString(xml, "/customer:customer/customer:status");
    }

    public String extractCreatedAt(String xml) {
        return evaluateString(xml, "/customer:customer/customer:createdAt");
    }

    public String extractId(String xml) {
        return evaluateString(xml, "/customer:customer/customer:id");
    }

    private String evaluateString(String xml, String expression) {

        try {
            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();

            factory.setNamespaceAware(true);

            Document document = factory
                    .newDocumentBuilder()
                    .parse(new InputSource(new StringReader(xml)));

            return xpath.evaluate(
                    expression,
                    document
            );

        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Failed to evaluate XPath expression: " + expression,
                    e
            );
        }
    }
}
