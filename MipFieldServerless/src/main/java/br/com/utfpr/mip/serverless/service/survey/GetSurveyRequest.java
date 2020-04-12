package br.com.utfpr.mip.serverless.service.survey;

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
import br.com.utfpr.mip.serverless.entites.survey.Survey;

public class GetSurveyRequest implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		var sessionFactory = HibernateUtil.getSessionFactory();
		var responseEvent = new APIGatewayProxyResponseEvent();

		List<Survey> resultList = null;
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();

			resultList = session.createQuery("from Survey", Survey.class).getResultList();
			responseEvent.setHeaders(Collections.singletonMap("timeStamp", String.valueOf(System.currentTimeMillis())));
			responseEvent.setStatusCode(200);
			
			responseEvent.setBody(new ObjectMapper().writeValueAsString(resultList));

			session.getTransaction().commit();

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			responseEvent.setStatusCode(500);
			responseEvent.setBody(e.getMessage());

		}
		return responseEvent;
	}


}
