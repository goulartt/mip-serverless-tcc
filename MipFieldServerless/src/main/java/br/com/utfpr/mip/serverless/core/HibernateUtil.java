package br.com.utfpr.mip.serverless.core;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static SessionFactory sessionFactory;

	public static SessionFactory getSessionFactory() {
		System.out.println("getSessionFactory() is called");
		if (null != sessionFactory) {
			System.out.println("SessionFactory is already created");
			return sessionFactory;

		}

		Configuration configuration = new Configuration();

		String jdbcUrl = "jdbc:mysql://" + System.getenv(Constants.RDS_HOSTNAME) + "/"
				+ System.getenv(Constants.RDS_DB_NAME)+"?useSSL=false";

		configuration.setProperty("hibernate.connection.url", jdbcUrl);
		configuration.setProperty("hibernate.connection.username", System.getenv(Constants.RDS_USERNAME));
		configuration.setProperty("hibernate.connection.password", System.getenv(Constants.RDS_PASSWORD));

		configuration.configure("hibernate.cfg.xml");
		try {
			sessionFactory = configuration.buildSessionFactory();
		} catch (HibernateException e) {
			System.err.println("Initial SessionFactory creation failed." + e);
			throw new ExceptionInInitializerError(e);
		}
		return sessionFactory;
	}
}
