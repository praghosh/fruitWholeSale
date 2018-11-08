package test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import entities.Customer;
import entities.Route;

import persistence.HibernateUtil;
 
public class TestCustomer {
 
    @SuppressWarnings("unchecked")
    
    public static void main(String[] args) {
 
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
 
        Route route = new Route();
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
    }
}