package com.utfpr.serverless;

import java.util.Collections;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utfpr.serverless.repository.FieldRepository;


@Component
public class HelloHandler implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	@Autowired
	private FieldRepository repository;

	@Override
	public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent input) {
		var field = repository.findAll();
		var responseEvent = new APIGatewayProxyResponseEvent();
		try {
			responseEvent.setHeaders(Collections.singletonMap("timeStamp", String.valueOf(System.currentTimeMillis())));
			responseEvent.setStatusCode(200);
			responseEvent.setBody(new ObjectMapper().writeValueAsString(field));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return responseEvent;
	}

	
}
