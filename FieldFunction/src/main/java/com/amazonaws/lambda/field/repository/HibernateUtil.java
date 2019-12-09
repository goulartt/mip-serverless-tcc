package com.amazonaws.lambda.field.repository;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {

	private static SessionFactory sessionFactory;

	public static SessionFactory getSessionFactory() {
		if (null != sessionFactory)
			return sessionFactory;

		Configuration configuration = new Configuration();

		String jdbcUrl = System.getenv("RDS_URL");

		configuration.setProperty("hibernate.connection.url", jdbcUrl);
		configuration.setProperty("hibernate.connection.username", System.getenv("RDS_USERNAME"));
		configuration.setProperty("hibernate.connection.password", System.getenv("RDS_PASSWORD"));

		configuration.configure("/resources/hibernate.cfg.xml");
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties()).build();
		try {
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		} catch (HibernateException e) {
			System.err.println("Initial SessionFactory creation failed." + e);
			throw new ExceptionInInitializerError(e);
		}
		return sessionFactory;
	}
}