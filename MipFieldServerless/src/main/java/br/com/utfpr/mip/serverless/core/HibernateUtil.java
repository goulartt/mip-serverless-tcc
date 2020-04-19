package br.com.utfpr.mip.serverless.core;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.github.fluent.hibernate.cfg.scanner.EntityScanner;
import com.github.fluent.hibernate.cfg.strategy.hibernate5.adapter.ImprovedNamingStrategyHibernate5;

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
		configuration.setImplicitNamingStrategy(ImprovedNamingStrategyHibernate5.INSTANCE);
		configuration.configure("hibernate.cfg.xml");
		
		EntityScanner.scanPackages("br.com.utfpr.mip.serverless.entites.base", "br.com.utfpr.mip.serverless.entites.survey")
	    		.addTo(configuration);

		try {
			sessionFactory = configuration.buildSessionFactory();
		} catch (HibernateException e) {
			System.err.println("Initial SessionFactory creation failed." + e);
			throw new ExceptionInInitializerError(e);
		}
		return sessionFactory;
	}
	
	public static void setEnv(Map<String, String> newenv) throws Exception {
		  try {
		    Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
		    Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
		    theEnvironmentField.setAccessible(true);
		    Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
		    env.putAll(newenv);
		    Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
		    theCaseInsensitiveEnvironmentField.setAccessible(true);
		    Map<String, String> cienv = (Map<String, String>)     theCaseInsensitiveEnvironmentField.get(null);
		    cienv.putAll(newenv);
		  } catch (NoSuchFieldException e) {
		    Class[] classes = Collections.class.getDeclaredClasses();
		    Map<String, String> env = System.getenv();
		    for(Class cl : classes) {
		      if("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
		        Field field = cl.getDeclaredField("m");
		        field.setAccessible(true);
		        Object obj = field.get(env);
		        Map<String, String> map = (Map<String, String>) obj;
		        map.clear();
		        map.putAll(newenv);
		      }
		    }
		  }
		}
}
