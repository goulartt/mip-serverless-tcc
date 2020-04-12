package br.com.utfpr.mip.serverless.service.survey;

import java.util.Collections;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.type.LongType;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import br.com.utfpr.mip.serverless.core.HibernateUtil;
import br.com.utfpr.mip.serverless.entites.survey.Survey;

public class DeleteSurveyRequest implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		var sessionFactory = HibernateUtil.getSessionFactory();
		var responseEvent = new APIGatewayProxyResponseEvent();

		var queryStringParameters = input.getQueryStringParameters();
	

		try (Session session = sessionFactory.openSession()) {
			String surveyId 	= queryStringParameters.get("surveyId");
			String userId = queryStringParameters.get("userId");

			if (surveyId == null || userId == null) {
				responseEvent.setStatusCode(404);
				return responseEvent;
			}
			
			session.beginTransaction();


			if (checkSameUser(session, surveyId, userId)) {
				var myObject = (Survey)session.load(Survey.class,Long.valueOf(surveyId));
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

	private boolean checkSameUser(Session session, String surveyId, String userId) {
		List<Long> createdBy = session.createNativeQuery("select created_by_id from survey where id = :id")
				.setParameter("id", Long.valueOf(surveyId))
				.addScalar("created_by_id", LongType.INSTANCE)
				.list();

		return (Long.valueOf(userId).equals(createdBy.get(0)));
	}


}
