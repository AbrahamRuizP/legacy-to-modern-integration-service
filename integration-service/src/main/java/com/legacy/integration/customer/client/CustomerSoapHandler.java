package com.legacy.integration.customer.client;

import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;

import javax.xml.namespace.QName;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.Set;

public class CustomerSoapHandler implements SOAPHandler<SOAPMessageContext> {

    private final ThreadLocal<String> lastResponseXml =
            new ThreadLocal<>();

    @Override
    public boolean handleMessage(SOAPMessageContext context) {

        Boolean outbound =
                (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (Boolean.FALSE.equals(outbound)) {
            captureMessage(context.getMessage());
        }

        return true;
    }

    public String getLastResponseXml() {
        return lastResponseXml.get();
    }

    private void captureMessage(SOAPMessage message) {
        try {
            TransformerFactory transformerFactory =
                    TransformerFactory.newInstance();

            Transformer transformer =
                    transformerFactory.newTransformer();

            transformer.setOutputProperty(
                    OutputKeys.OMIT_XML_DECLARATION, "no"
            );

            StringWriter writer = new StringWriter();

            transformer.transform(
                    new DOMSource(message.getSOAPPart()),
                    new StreamResult(writer)
            );

            lastResponseXml.set(writer.toString());

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to capture SOAP message",
                    e
            );
        }
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public void close(MessageContext context) {
        lastResponseXml.remove();
    }

    @Override
    public Set<QName> getHeaders() {
        return Set.of();
    }
}
