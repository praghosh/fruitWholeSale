package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import nmfc.helper.Result;
import nmfc.helper.ToastMessage;
import persistence.HibernateUtil;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;



@Entity
@Table(name="CUSTOMER")

public class Customer {
	
	@Id
	@GeneratedValue
	@Column(name="CUST_ID")
	private Integer id;

	@Column(name="CUST_NAME")
	private String name;
	
	@Column(name="CUST_CODE")
	private String code;
	
	
	public Customer( ) {
		super();     		 
	}
	public String toString() {
		return name;
	}
	public Customer(String name, String code) {
		super();
		this.name = name;
		this.code = code;
		
	}

	@Embedded
	@AttributeOverrides({@AttributeOverride(name="addressLine1", column=@Column(name="USER_ADDRESS_LINE_1")),
		@AttributeOverride(name="addressLine2", column=@Column(name="USER_ADDRESS_LINE_2"))})
	private Address address;
	
	public void setAddress(Address address) {
		this.address = address;
	}

	@Column(name="PARENT_NAME")
	private String parentName;
	
	@Column(name="MOBILE_NUMB")
	private String mobile;
	

	@Column(name="CREDIT_LIMIT")
	private Float  creditLImit;
	
	@Column(name="DISCOUNT_LIMIT")
	private Float  discountEligible=1f;// percentage of discount
	
	@Column(name="IS_DEFAULTER")  // Customer who do not pay the due in time
	private Boolean isDefaulter=false;
		
	
	public Boolean getIsDefaulter() {
		return isDefaulter;
	}
	public void setIsDefaulter(Boolean isDefaulter) {
		this.isDefaulter = isDefaulter;
	}
	public Float getDiscountEligible() {
		return discountEligible;
	}
	public void setDiscountEligible(Float discountElligible) {
		this.discountEligible = discountElligible;
	}
	public Float getCreditLImit() {
		return creditLImit;
	}
	public void setCreditLImit(Float creditLImit) {
		this.creditLImit = creditLImit;
	}

	@Column(name="ADHAR")
	private String  adharNo;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Address getAddress() {
		return address;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAdharNo() {
		return adharNo;
	}

	public void setAdharNo(String adharNo) {
		this.adharNo = adharNo;
	}

	public String getVoterIDNumber() {
		return voterIDNumber;
	}

	public void setVoterIDNumber(String voterIDNumber) {
		this.voterIDNumber = voterIDNumber;
	}

	public Route getRoute() {
		return route;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="VOTERID")
	private String  voterIDNumber;
	
    @ManyToOne
    @JoinColumn(name="ROUTE_ID", nullable=false)
    private Route route;

	public void setRoute(Route route) {
		this.route = route;
	}

	public Integer getId() {
		return id;
	}
	

	public static boolean customerExistsWithSameName (String customerName) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			
			session.beginTransaction();
			String hql = "from Customer where  name =:cust";
			Query query = session.createQuery(hql);
			query.setParameter("cust", customerName);
			List<Customer> listProducts = query.list();
			if ((listProducts == null) || listProducts.size() == 0) {
				System.out.println("Customer does not Exists");
				return false;
			} 
			else {		 
				System.out.println("Customer Exists With Same Name");
				return true;
			} 
		}
		catch (HibernateException e) {
			e.printStackTrace();
			return  false;
		}

		finally {
			if(session!=null && session.isOpen()){
				session.close();
			}
		}
 		
	}
 
	public static boolean addCustomer (Customer customer) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();				 
			session.save(customer);
			session.getTransaction().commit();			 
			return true;
		}
		catch (Exception ex)
		{   
 			System.out.println(ex.getMessage());
			ex.printStackTrace();
			session.getTransaction().rollback();		 
			return false;
		}
		finally {
			if(session!=null && session.isOpen()){
				session.close();
			}
		}
	}
	
	public static Result deleteSelectedRCustomer(Serializable id ) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();		
			Object persistentInstance = session.load(Customer.class, id);
			if (persistentInstance != null)  {
				session.delete(persistentInstance);
				session.getTransaction().commit();
				return new Result (true, "Customer Deleted Successfully");
				 
			}
			else {
				System.out.println ("Did not find the Customer Object in database");
				return new Result (false, "Did not find the Customer Object in database");
			}

		} 
		catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			return new Result (false, "Database Exception While Deleting Customer");
		}

		finally {
			if(session!=null && session.isOpen()){
				session.close();
			}
		}
	}
	
	public  static List <Customer> getCustomerLists() {
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
	/**
	 * To verify if already a customer exists with same name
	 * 
	 * @param customerName
	 * @return
	 */
	public static Customer  getExistingCustomerWithName (String customerName) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			
			session.beginTransaction();
			String hql = "from Customer where  name =:cust";
			Query query = session.createQuery(hql);
			query.setParameter("cust", customerName);
			List<Customer> listProducts = query.list();
			if ((listProducts == null) || listProducts.size() == 0) {
				System.out.println("Customer does not Exists");
				return null;
			} 
			else {		 
				System.out.println("Customer Exists With Same Name");
				return listProducts.get(0);
			} 
		}
		catch (HibernateException e) {
			e.printStackTrace();
			return  null;
		}

		finally {
			if(session!=null && session.isOpen()){
				session.close();
			}
		}
 		
	}
	
	/**
	 * To fetch a customer   with  given ID
	 * 
	 * @param customerID
	 * @return
	 */
	public static Customer  getExistingCustomerWithID (Integer customerID)
			{
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			
			session.beginTransaction();
			String hql = "from Customer where  id =:custID";
			Query query = session.createQuery(hql);
			query.setParameter("custID", customerID);
			List<Customer> listProducts = query.list();
			if ((listProducts == null) || listProducts.size() == 0) {
				System.out.println("Customer does not Exists");
				return null;
			} 
			else {		 				 
				return listProducts.get(0);
			} 
		}
		catch (HibernateException e) {
			e.printStackTrace();
			return  null;
		}

		finally {
			if(session!=null && session.isOpen()){
				session.flush();
				session.close();
			}
		}
 		
	}
	/**
	 * 
	 * @return list of defaulter who did not pay in time
	 */
	public static List <Customer> getDefaulterCustomerList  ( ) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			
			session.beginTransaction();
			String hql = "from Customer cr where  isDefaulter=true order by cr.name";
			Query query = session.createQuery(hql);			 
			List<Customer> listProducts = query.list();
			return  listProducts;
		}
		catch (HibernateException e) {
			e.printStackTrace();
			return  null;
		}

		finally {
			if(session!=null && session.isOpen()){
				session.close();
			}
		}
 		
	}
 
}