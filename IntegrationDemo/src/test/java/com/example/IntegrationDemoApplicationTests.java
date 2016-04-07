package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IntegrationDemoApplication.class)
public class IntegrationDemoApplicationTests {


	@Autowired
	MessageChannel input;

	@Autowired
	IntegrationFlow flow1;

	@Test
	public void contextLoads() {
		String payload = "<ns1:GetWeather xmlns:ns1='http://www.webserviceX.NET'><ns1:CityName>?XXX?</ns1:CityName>" +
				"<ns1:CountryName>?XXX?</ns1:CountryName></ns1:GetWeather>";
		input.send(MessageBuilder.withPayload(payload).build());
	}

}
