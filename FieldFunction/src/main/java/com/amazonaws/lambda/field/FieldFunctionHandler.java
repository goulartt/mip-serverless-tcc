package com.amazonaws.lambda.field;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.amazonaws.lambda.field.model.Field;
import com.amazonaws.lambda.field.model.MacroRegion;
import com.amazonaws.lambda.field.repository.HibernateUtil;
import com.amazonaws.lambda.field.service.FieldService;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class FieldFunctionHandler implements RequestHandler<String, String> {

	private final FieldService service = new FieldService();

	@Override
	public String handleRequest(String input, Context context) {
		context.getLogger().log("Input: " + input);

		/*
		 * Field newField =
		 * Field.builder().name(input.getName()).location(input.getLocation())
		 * .city(service.readCityById(input.getCity().getId()))
		 * .farmer(service.readFarmerById(input.getFarmer().getId()))
		 * .supervisors(service.readSupervisorsByIds(input.getSupervisors())).build();
		 * 
		 * service.create(newField);
		 * 
		 * return newField;
		 */
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			MacroRegion macro = new MacroRegion();
			macro.setId(10L);
			macro.setName("Teste");
			session.save(macro);
			session.getTransaction().commit();
		}

		return String.format("Added %s %s.", 10, "teste");
	}

}
