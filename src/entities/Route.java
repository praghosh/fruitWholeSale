package entities;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import persistence.HibernateUtil;
import javax.persistence.OneToMany;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;




@Entity
@Table(name="ROUTE", uniqueConstraints={@UniqueConstraint (columnNames ="ROUTE_NAME")})

public class Route {
	
	@Id
	@GeneratedValue 
	@Column(name="ROUTE_ID")
	private Integer id;

	@Column(name="ROUTE_NAME")
	private String name;
	
	@OneToMany(mappedBy="route")
    private Set<Customer> customer;

 
    
	public Set<Customer> getCustomer() {
		return customer;
	}

	public void setCustomer(Set<Customer> customer) {
		this.customer = customer;
	}

	public Integer getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	public String toString() {
		return name;
	}
	
	public static List getRouteNames() {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			//	session.beginTransaction();
			return session.createCriteria(Route.class).list();
		}

		finally {
			if(session!=null&&session.isOpen()){
				session.close();
			}
		}
	}
	
	/**
	 * 
	 * @param customer
	 * @return list of Credit entry (line items) for a particular truck
	 * between date range
	 */
	public static List <Customer> getAllCustomerInRoute(Route route) {

		System.out.println ("getAllCustomerInRoute " + route); 
		Session session = HibernateUtil.getSessionFactory().openSession();

		try {
			Criteria crt = session.createCriteria(Customer.class);
			crt.addOrder(Order.asc("name"));
			crt.add(Restrictions.eq("route", route));			
			List <Customer> customerList = crt.list();			 
			return customerList;
		} 
		finally {
			if(session!=null&&session.isOpen()){
				session.close();
			}
		}
	}
	
	
public static void main(String[] args) {
	
	Session session = HibernateUtil.getSessionFactory().openSession();
	session.beginTransaction();
	Route route = new Route();
	route.setName("Bagnan ND Block");			 
	int routeId = (Integer) session.save(route);
	
	Customer cust = new  Customer("Anil", "ANo1");
	cust.setRoute(route); 
	session.save(cust);
	session.getTransaction().commit();
	
	List names = getRouteNames();
	for (Iterator iterator = names.iterator(); iterator.hasNext();) {
		Route name = (Route) iterator.next();
		System.out.println(name.getName());
		
	}
	
}

}
