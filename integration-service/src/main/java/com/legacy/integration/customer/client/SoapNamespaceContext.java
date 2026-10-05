package com.legacy.integration.customer.client;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import java.util.Iterator;

public class SoapNamespaceContext implements NamespaceContext {

    private static final String SOAP_NAMESPACE =
            "http://schemas.xmlsoap.org/soap/envelope/";

    private static final String CUSTOMER_SERVICE_NAMESPACE =
            "http://soap.customer.integration.legacy.com/";

    @Override
    public String getNamespaceURI(String prefix) {
        return switch (prefix) {

            case "soap" -> SOAP_NAMESPACE;

            case "ns" -> CUSTOMER_SERVICE_NAMESPACE;

            case XMLConstants.XML_NS_PREFIX ->
                XMLConstants.XML_NS_URI;

            case XMLConstants.XMLNS_ATTRIBUTE ->
                XMLConstants.XMLNS_ATTRIBUTE_NS_URI;

            default -> XMLConstants.NULL_NS_URI;
        };
    }

    @Override
    public String getPrefix(String namespaceURI) {
        if (SOAP_NAMESPACE.equals(namespaceURI)) {
            return "soap";
        }

        if (CUSTOMER_SERVICE_NAMESPACE.equals(namespaceURI)) {
            return "ns";
        }

        return null;
    }

    @Override
    public Iterator<String> getPrefixes(String namespaceURI) {
        return null;
    }
}
