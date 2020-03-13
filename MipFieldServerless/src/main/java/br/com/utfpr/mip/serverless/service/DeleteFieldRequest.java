package br.com.utfpr.mip.serverless.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.type.LongType;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.utfpr.mip.serverless.core.HibernateUtil;
import br.com.utfpr.mip.serverless.dto.base.FieldDTO;
import br.com.utfpr.mip.serverless.entites.Field;
import br.com.utfpr.mip.serverless.entites.MacroRegion;
import br.com.utfpr.mip.serverless.entites.Supervisor;
import javassist.bytecode.analysis.Type;

public class DeleteFieldRequest implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		var sessionFactory = HibernateUtil.getSessionFactory();
		var responseEvent = new APIGatewayProxyResponseEvent();

		var queryStringParameters = input.getQueryStringParameters();
	

		try (Session session = sessionFactory.openSession()) {
			String fieldId 	= queryStringParameters.get("fieldId");
			String userId = queryStringParameters.get("userId");

			if (fieldId == null || userId == null) {
				responseEvent.setStatusCode(404);
				return responseEvent;
			}
			
			session.beginTransaction();


			if (checkSameUser(session, fieldId, userId)) {
				var myObject = (Field)session.load(Field.class,Long.valueOf(fieldId));
				session.delete(myObject);
				responseEvent.setStatusCode(204);

			} else {
				responseEvent.setStatusCode(405);
			}

			responseEvent.setHeaders(Collections.singletonMap("timeStamp", String.valueOf(System.currentTimeMillis())));

			session.getTransaction().commit();
		
			
		} catch (NullPointerException e) {
			e.printStackTrace();
			responseEvent.setStatusCode(404);
		} catch (Exception e) {
			e.printStackTrace();
			responseEvent.setStatusCode(500);

		}
		return responseEvent;
	}

	private boolean checkSameUser(Session session, String fieldId, String userId) {
		List<Long> createdBy = session.createNativeQuery("select created_by_id from field where id = :id")
				.setParameter("id", Long.valueOf(fieldId))
				.addScalar("created_by_id", LongType.INSTANCE)
				.list();

		return (Long.valueOf(userId).equals(createdBy.get(0)));
	}


}
