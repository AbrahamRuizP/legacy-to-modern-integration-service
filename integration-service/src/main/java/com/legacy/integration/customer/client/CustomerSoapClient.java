package com.legacy.integration.customer.client;

import com.legacy.integration.customer.soap.generated.CustomerService;
import com.legacy.integration.customer.soap.generated.CustomerSoapResponse;
import com.legacy.integration.customer.soap.generated.CustomerSoapService;
import jakarta.xml.ws.Binding;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.handler.Handler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerSoapClient {

    private final CustomerSoapResponseProcessor responseProcessor;
    private final CustomerSoapService soapService;
    private final CustomerSoapHandler soapHandler;

    public CustomerSoapClient() {

        CustomerService service =
                new CustomerService();

        this.responseProcessor
                = new CustomerSoapResponseProcessor();

        this.soapService =
                service.getCustomerServicePort();

        this.soapHandler = new CustomerSoapHandler();

        configureHandler();
    }

    CustomerSoapClient(CustomerSoapService soapService) {
        this.soapService = soapService;
        this.responseProcessor = null;
        this.soapHandler = null;
    }

    public CustomerSoapResponse getCustomerById(String customerId) {
        return soapService.getCustomerById(customerId);
    }

    public String getValidateCustomerXml() {

        if (soapHandler == null ||
        responseProcessor == null) {
            throw new IllegalStateException(
                    "SOAP response processing is not configured"
            );
        }

        String soapXml =
                soapHandler.getLastResponseXml();

        if (soapXml == null) {
            throw new IllegalStateException(
                    "No SOAP response has been captured"
            );
        }

        return responseProcessor.extractAndValidate(soapXml);
    }

    public String getLastResponseXml() {
        if (soapHandler == null) {
            throw new IllegalStateException(
                    "SOAP handler is not configured"
            );
        }

        return soapHandler.getLastResponseXml();
    }

    private void configureHandler() {

        BindingProvider bindingProvider =
                (BindingProvider) soapService;

        Binding binding =
                bindingProvider.getBinding();

        List<Handler> handlers =
                new ArrayList<>(
                        binding.getHandlerChain()
                );

        handlers.add(soapHandler);

        binding.setHandlerChain(handlers);
    }
}
