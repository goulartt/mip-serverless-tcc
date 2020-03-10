package br.com.utfpr.mip.serverless.service;

import java.util.Collections;
import java.util.List;

import org.hibernate.Session;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.utfpr.mip.serverless.core.HibernateUtil;
import br.com.utfpr.mip.serverless.entites.Field;
import br.com.utfpr.mip.serverless.entites.MacroRegion;

public class GetFieldByIdRequest implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		var sessionFactory = HibernateUtil.getSessionFactory();
		var responseEvent = new APIGatewayProxyResponseEvent();

		Field resultList = null;
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();

			resultList = session.createQuery("from Field f where f.id = :id", Field.class)
					.setParameter("id", input.getQueryStringParameters().get("id"))
					.getSingleResult();
			responseEvent.setHeaders(Collections.singletonMap("timeStamp", String.valueOf(System.currentTimeMillis())));
			responseEvent.setStatusCode(200);
			
			responseEvent.setBody(new ObjectMapper().writeValueAsString(resultList));

			session.getTransaction().commit();

		} catch (JsonProcessingException e) {
			e.printStackTrace();
			responseEvent.setStatusCode(500);

		} catch (NullPointerException e) {
			e.printStackTrace();
			responseEvent.setStatusCode(204);
		}
		
		return responseEvent;
	}


}
