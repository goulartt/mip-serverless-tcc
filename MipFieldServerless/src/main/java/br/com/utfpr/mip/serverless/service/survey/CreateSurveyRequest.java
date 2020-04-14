package br.com.utfpr.mip.serverless.service.survey;

import java.util.Collections;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.utfpr.mip.serverless.core.HibernateUtil;
import br.com.utfpr.mip.serverless.dto.SurveyDTO;
import br.com.utfpr.mip.serverless.entites.survey.Survey;

public class CreateSurveyRequest implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		var sessionFactory = HibernateUtil.getSessionFactory();
		var responseEvent = new APIGatewayProxyResponseEvent();

		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			SurveyDTO readValue = new ObjectMapper().readValue(input.getBody(), SurveyDTO.class);
			Survey survey = SurveyDTO.generateEntityFromDTO(readValue);

			responseEvent.setBody(survey.toString());

			if (!checkEntityExists(session, survey)) {
				saveOrUpdateField(responseEvent, session, survey);
			} else {
				responseEvent.setStatusCode(409);
			}

			responseEvent.setHeaders(Collections.singletonMap("timeStamp", String.valueOf(System.currentTimeMillis())));

			session.getTransaction().commit();

		} catch (Exception e) {
			e.printStackTrace();
			responseEvent.setStatusCode(500);

		}
		return responseEvent;
	}

	private void saveOrUpdateField(APIGatewayProxyResponseEvent responseEvent, Session session, Survey survey) {
		session.saveOrUpdate(survey);
		responseEvent.setStatusCode(201);

	}

	private boolean checkEntityExists(Session session, Survey survey) {
		Query query = session.createQuery("select 1 from Survey s where s.field = :field and s.harvest = :harvest");
		query.setParameter("field", survey.getField().getId());
		query.setParameter("harvest", survey.getHarvest().getId());

		return (query.uniqueResult() != null);
	}

}
