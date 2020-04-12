package br.com.utfpr.mip.serverless.service.field;

import java.util.Collections;

import org.hibernate.Session;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import br.com.utfpr.mip.serverless.core.HibernateUtil;
import br.com.utfpr.mip.serverless.entites.base.Field;

public class GetFieldByIdRequest implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		var sessionFactory = HibernateUtil.getSessionFactory();
		var responseEvent = new APIGatewayProxyResponseEvent();

		Field resultList = null;
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();

			resultList = (Field) session.load(Field.class, Long.valueOf(input.getQueryStringParameters().get("id")));

			responseEvent.setHeaders(Collections.singletonMap("timeStamp", String.valueOf(System.currentTimeMillis())));
			responseEvent.setStatusCode(200);
			var myObjectMapper = new ObjectMapper();
			myObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

			responseEvent.setBody(myObjectMapper.writeValueAsString(resultList));

			session.getTransaction().commit();
		} catch (NullPointerException e) {
			e.printStackTrace();
			responseEvent.setStatusCode(204);
		}
		catch (Exception e) {
			e.printStackTrace();
			responseEvent.setStatusCode(500);

		}
		return responseEvent;
	}

}
