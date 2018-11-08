package persistence;

 

import java.net.URL;

import org.hibernate.*;
import org.hibernate.cfg.*;

/**
 * Startup Hibernate and provide access to the singleton SessionFactory
 */
public class HibernateUtil {

  private static SessionFactory sessionFactory;

  static {
    try {
    //	URL input = HibernateUtil.class.getResource("/resources/hibernate.cfg.xml");  
   //    sessionFactory = new Configuration().configure(input).buildSessionFactory();
    	sessionFactory = new Configuration().configure().buildSessionFactory();
    } catch (Throwable ex) {
       throw new ExceptionInInitializerError(ex);
    }
  }
  
  public  static void  buildSessionFactory (String myUrl, String  myUser,  String myPass) {
	  System.out.println ();

      // Create the SessionFactory from hibernate.cfg.xml
	  if ( getSessionFactory() !=null  ) {
		  try {
			  getSessionFactory().close(); 
		  }
		  catch (Exception e) {
			  // As any exception comes during closing previuos
			  // session factory should not affect creation of new 
			  // session factory in any way.
			  e.printStackTrace();
		  }
		  
	  }
      Configuration configuration = new Configuration();
      configuration.configure();

      configuration.setProperty("hibernate.connection.url", myUrl);
      configuration.setProperty("hibernate.connection.username", myUser);
      configuration.setProperty("hibernate.connection.password", myPass);
      sessionFactory =  configuration.buildSessionFactory();
}

  public static SessionFactory getSessionFactory() {
      // Alternatively, we could look up in JNDI here
      return sessionFactory;
  }

  public static void shutdown() {
      // Close caches and connection pools
      getSessionFactory().close();
  }
}

