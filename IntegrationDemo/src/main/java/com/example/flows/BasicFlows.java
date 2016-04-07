package com.example.flows;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.Adapters;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.stream.CharacterStreamReadingMessageSource;
import org.springframework.integration.stream.CharacterStreamWritingMessageHandler;
import org.springframework.integration.ws.SimpleWebServiceOutboundGateway;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;

/**
 * Created by zulk on 07.04.16.
 */
@Configuration
public class BasicFlows {

    @Bean
    public MessageChannel input() {
        return MessageChannels.direct().get();
    }

    @Bean
    public IntegrationFlow flow1() {
        return IntegrationFlows.from(input())
                .handle(gateway())
                .<String,String>transform(a -> {
            System.out.println(a);
            return a;
        }).handle(CharacterStreamWritingMessageHandler.stdout()).get();
    }


    private SimpleWebServiceOutboundGateway gateway() {
        SimpleWebServiceOutboundGateway simpleWebServiceOutboundGateway = new SimpleWebServiceOutboundGateway("http://www.webservicex.com/globalweather.asmx");
        SaajSoapMessageFactory          saajSoapMessageFactory          = new SaajSoapMessageFactory();
        try {
            saajSoapMessageFactory.setMessageFactory(MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL));
        } catch (SOAPException e) {
            throw new RuntimeException(e);
        }
        simpleWebServiceOutboundGateway.setMessageFactory(saajSoapMessageFactory);
        return  simpleWebServiceOutboundGateway;
    }

}
