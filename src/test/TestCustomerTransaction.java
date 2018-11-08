package test;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import entities.Customer;
import entities.CustomerTransaction;
import entities.Route;
import persistence.HibernateUtil;

public class TestCustomerTransaction {

	
	
	@SuppressWarnings("unchecked")
    public static void main(String[] args) {
		System.out.println( "start");
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        Customer selectedCustomer =  Customer.getExistingCustomerWithName ("test1");
        Date startDate =  new Date ();
    	Date endDate =   new Date ();
    	List  customerTransactionList = CustomerTransaction.getTransactionListByEntryDate(selectedCustomer,startDate , endDate);	
        System.out.println(customerTransactionList.size());
 
    /*    Route route = new Route();
        route.setName("Bainan");
        session.save(route);
         
        Customer emp1 = new Customer("Nina", "Mayers");
        Customer emp2 = new Customer("Tony", "Almeida");
 
        emp1.setRoute(route);
        emp2.setRoute(route);
         
        session.save(emp1);
        session.save(emp2);
 
        session.getTransaction().commit();
        session.close();
        */
    }
	
	
}
