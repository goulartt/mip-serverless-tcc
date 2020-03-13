package br.com.utfpr.mip.serverless.service;

import java.util.Collections;
import java.util.List;
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

public class CreateFieldRequest implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		var sessionFactory = HibernateUtil.getSessionFactory();
		var responseEvent = new APIGatewayProxyResponseEvent();

		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			FieldDTO readValue = new ObjectMapper().readValue(input.getBody(), FieldDTO.class);
			Field field = FieldDTO.generateEntityFromDTO(readValue);

			responseEvent.setBody(field.toString());

			if (field.getId() != null) {
				saveOrUpdateField(responseEvent, session, field);
			} else {
				if (!checkEntityExists(session, field)) {
					saveOrUpdateField(responseEvent, session, field);
				} else {
					responseEvent.setStatusCode(409);
				}
			}

			responseEvent.setHeaders(Collections.singletonMap("timeStamp", String.valueOf(System.currentTimeMillis())));

			session.getTransaction().commit();

		} catch (Exception e) {
			e.printStackTrace();
			responseEvent.setStatusCode(500);

		}
		return responseEvent;
	}

	private void saveOrUpdateField(APIGatewayProxyResponseEvent responseEvent, Session session, Field field) {
		if (supervisorAllowedInCity(session, field)) {
			session.saveOrUpdate(field);
			responseEvent.setStatusCode(201);
		} else {
			responseEvent.setStatusCode(405);
		}
	}

	private boolean supervisorAllowedInCity(Session session, Field field) {

		Set<Long> ids = field.getSupervisors().stream().map(s -> {
			return s.getId();
		}).collect(Collectors.toSet());
		Long citySelected = field.getCityId();

		List<Long> regionsIds = session.createNativeQuery("select region_id from supervisor where id in (:ids)")
				.setParameter("ids", ids).addScalar("region_id", LongType.INSTANCE).getResultList();

		List<Long> cities = session.createNativeQuery("select cities_id from region_cities where region_id in (:ids)")
				.setParameter("ids", regionsIds).addScalar("cities_id", LongType.INSTANCE).list();

		for (Long city : cities) {
			if (city.equals(citySelected))
				return true;
		}

		return false;
	}

	private boolean checkEntityExists(Session session, Field field) {
		Query query = session.createQuery("select 1 from Field f where f.name = :name and f.location = :location");
		query.setParameter("name", field.getName());
		query.setParameter("location", field.getLocation());

		return (query.uniqueResult() != null);
	}

}
