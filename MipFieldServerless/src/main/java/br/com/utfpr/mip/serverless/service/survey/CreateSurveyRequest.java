package br.com.utfpr.mip.serverless.service.survey;

import java.util.Collections;
import java.util.HashMap;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.utfpr.mip.serverless.core.Constants;
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

			responseEvent.setBody(new ObjectMapper().writeValueAsString(survey));

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
			responseEvent.setBody(e.toString());

		}
		return responseEvent;
	}

	private void saveOrUpdateField(APIGatewayProxyResponseEvent responseEvent, Session session, Survey survey) {
		session.saveOrUpdate(survey);
		responseEvent.setStatusCode(201);

	}

	private static boolean checkEntityExists(Session session, Survey survey) {
		Query query = session.createNativeQuery("select 1 from survey as s where s.field_id = :field and s.harvest_id = :harvest");
		query.setParameter("field", survey.getField().getId());
		query.setParameter("harvest", survey.getHarvest().getId());

		return (query.uniqueResult() != null);
	}
	
	public static void main(String[] args) throws Exception {
		String test = "{\r\n" + 
				"	\"id\": null,\r\n" + 
				"	\"fieldId\": 18,\r\n" + 
				"	\"harverstId\": 1,\r\n" + 
				"	\"cultivarData\": {\r\n" + 
				"		\"cultivarName\": \"Cultivar 1\",\r\n" + 
				"		\"rustResistant\": false,\r\n" + 
				"		\"bt\": false\r\n" + 
				"	},\r\n" + 
				"	\"cropData\": {\r\n" + 
				"		\"sowedDate\": 1587178800000,\r\n" + 
				"		\"emergenceDate\": 1587178800000,\r\n" + 
				"		\"harvestDate\": null\r\n" + 
				"	},\r\n" + 
				"	\"sizeData\": {\r\n" + 
				"		\"totalArea\": 1.0,\r\n" + 
				"		\"totalPlantedArea\": 1.0,\r\n" + 
				"		\"plantPerMeter\": 1.0\r\n" + 
				"	},\r\n" + 
				"	\"locationData\": {\r\n" + 
				"		\"longitude\": \"-21.5070334899\",\r\n" + 
				"		\"latitude\": \"-21.5070334899\"\r\n" + 
				"	},\r\n" + 
				"	\"productivityData\": {\r\n" + 
				"		\"productivityField\": 0.0,\r\n" + 
				"		\"productivityFarmer\": 0.0,\r\n" + 
				"		\"separatedWeight\": false\r\n" + 
				"	},\r\n" + 
				"	\"midData\": {\r\n" + 
				"		\"sporeCollectorPresent\": false,\r\n" + 
				"		\"collectorInstallationDate\": null\r\n" + 
				"	},\r\n" + 
				"	\"pulverisationData\": {\r\n" + 
				"		\"soyaPrice\": 0.0,\r\n" + 
				"		\"applicationCostCurrency\": 0.0\r\n" + 
				"	},\r\n" + 
				"	\"createdBy\": 1,\r\n" + 
				"	\"modifiedBy\": 1\r\n" + 
				"}";
		var properties = new HashMap<String, String>();
		properties.put(Constants.RDS_USERNAME, "admin");
		properties.put(Constants.RDS_DB_NAME, "mip");
		properties.put(Constants.RDS_HOSTNAME, "mip.cnwecoykhrj7.us-east-1.rds.amazonaws.com");
		properties.put(Constants.RDS_PASSWORD, "joaogoulart666");
		HibernateUtil.setEnv(properties);
		var sessionFactory = HibernateUtil.getSessionFactory();
		
		SurveyDTO readValue = new ObjectMapper().readValue(test, SurveyDTO.class);
		
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			Survey survey = SurveyDTO.generateEntityFromDTO(readValue);
			
			if (!checkEntityExists(session, survey)) {
				session.saveOrUpdate(survey);
			}  
			

			session.getTransaction().commit();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			System.out.println("Finalizado");
			System.exit(1);
		}
	}

}
