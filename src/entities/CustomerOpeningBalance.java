package entities;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.annotations.Type;
import org.hibernate.criterion.Restrictions;

import persistence.HibernateUtil;

@Entity
@Table(name="CUSTOMER_OPENING_BALANCE")

// combination are unique
public class CustomerOpeningBalance   {


	@Id
	@GeneratedValue 
	@Column(name="OPENING_BALANCE_ID")
	private Integer id;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "CUST_ID")
	private Customer customer;

	@Column(name="ENTRY_DATE")
	@Type(type="date")
	private Date entryDate;

	@Column(name="SMALL_CRATE_DUE")
	private Long smallCrateDue;

	@Column(name="BIG_CRATE_DUE")
	private Long bigCrateDue;

	@Column(name="CASH_DUE")
	private Float cashDue;

	@Column(name = "DATA_COMMITTED")
	private Boolean dataCommited;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public Long getSmallCrateDue() {
		return smallCrateDue;
	}

	public void setSmallCrateDue(Long smallCrateDue) {
		this.smallCrateDue = smallCrateDue;
	}

	public Long getBigCrateDue() {
		return bigCrateDue;
	}

	public void setBigCrateDue(Long bigCrateDue) {
		this.bigCrateDue = bigCrateDue;
	}

	public Float getCashDue() {
		return cashDue;
	}

	public void setCashDue(Float cashDue) {
		this.cashDue = cashDue;
	}

	/**
	 * 
	 * 
	 * @param selectedCustomer
	 * @return Opening balance of the customer.
	 */

	public static List getOpeningBalance(Customer customer) {

		System.out.println ("CustomerOpeningBalance for " + customer); 
		Session session = HibernateUtil.getSessionFactory().openSession();		 
		try {
			session.beginTransaction();
			Criteria crt = session.createCriteria(CustomerOpeningBalance.class);
			crt.add(Restrictions.eq("customer", customer));	
			crt.setCacheable(false); 
			List <CustomerOpeningBalance> customerOpeningtBalance = crt.list();	
			session.getTransaction().commit();
			return customerOpeningtBalance;
		} 

		catch (HibernateException e) {
			session.getTransaction().rollback();
			e.printStackTrace();
			return new ArrayList <> ();
		}
		finally {
			if(session!=null&&session.isOpen()){
				session.flush();
				session.close();
			}
		}
	}

	public Boolean istDataCommited() {
		return dataCommited;
	}

	public void setDataCommited(Boolean dataCommited) {
		this.dataCommited = dataCommited;
	}

	// save  Opening balnce entry of the customer in the persitance
	// There will only one entry 
	public static boolean saveCustomerOpeningBalance (CustomerOpeningBalance custOpening) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		try {			        
			session.beginTransaction();	
			// First check if already opening balance entry is avaialable
			// Then update the same.
			// Otherwise create new entry.

			Criteria crt = session.createCriteria(CustomerOpeningBalance.class);
			Customer customer = custOpening.getCustomer(); 
			crt.add(Restrictions.eq("customer", customer));		 
			List <CustomerOpeningBalance> openingtBalanceList = crt.list();		

			if (openingtBalanceList!=null && openingtBalanceList.size()>0) {
				CustomerOpeningBalance existingOpeningBalance = openingtBalanceList.get(0);
				 
				Boolean val = existingOpeningBalance.istDataCommited();
				boolean dataCommitted = (val!=null)? (boolean) val: false;
				if (dataCommitted) {
					System.out.println("Data already commited");
					return false;
				}
				
				existingOpeningBalance.setBigCrateDue(custOpening.getBigCrateDue());
				existingOpeningBalance.setSmallCrateDue(custOpening.getSmallCrateDue());
				existingOpeningBalance.setCashDue(custOpening.getCashDue());
				existingOpeningBalance.setEntryDate(custOpening.getEntryDate());
				session.save(existingOpeningBalance);
				session.getTransaction().commit();
				return true;
			}
			// Creating  fresh new entry
			else { 
				session.save(custOpening);
				session.getTransaction().commit();	
				return true;
			}

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
				session.flush();
				session.close();
			}
		}
	}

}
