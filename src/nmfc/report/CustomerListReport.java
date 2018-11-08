package nmfc.report;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import entities.Customer;
import persistence.HibernateUtil;

public class CustomerListReport {
	
 
  
	public  static List <Customer> getCustomerNames() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction(); 
			Criteria c = session.createCriteria(Customer.class);
			c.addOrder(Order.asc("name"));
			List <Customer> customerList = c.list();			
			System.out.println( "customer size = " + customerList.size());
			return customerList;
		}
		
		catch (HibernateException e) {
			e.printStackTrace();
			return new ArrayList <Customer> ();
 		}
		finally {
			if(session!=null && session.isOpen()){
				session.close();
			}
		}
	}
	
	

}
