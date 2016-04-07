package com.example.flows;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.Adapters;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.stream.CharacterStreamReadingMessageSource;
import org.springframework.integration.stream.CharacterStreamWritingMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;

/**
 * Created by zulk on 07.04.16.
 */
@Configuration
public class BasicFlows {

    @Bean
    public MessageChannel input() {
        return MessageChannels.direct("ala").get();
    }

    @Bean
    public IntegrationFlow flow1() {
        return IntegrationFlows.from(input()).transform(a -> {
            System.out.println(a);
            return a;
        }).handle(CharacterStreamWritingMessageHandler.stdout()).get();
    }

}
