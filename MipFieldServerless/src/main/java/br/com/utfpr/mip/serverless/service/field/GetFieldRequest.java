package br.com.utfpr.mip.serverless.service.field;

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
import br.com.utfpr.mip.serverless.entites.base.Field;
import br.com.utfpr.mip.serverless.entites.base.MacroRegion;

public class GetFieldRequest implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		var sessionFactory = HibernateUtil.getSessionFactory();
		var responseEvent = new APIGatewayProxyResponseEvent();

		List<Field> resultList = null;
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();

			resultList = session.createQuery("from Field", Field.class).getResultList();
			responseEvent.setHeaders(Collections.singletonMap("timeStamp", String.valueOf(System.currentTimeMillis())));
			responseEvent.setStatusCode(200);
			
			responseEvent.setBody(new ObjectMapper().writeValueAsString(resultList));

			session.getTransaction().commit();

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return responseEvent;
	}


}
