package entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.annotations.Type;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import persistence.HibernateUtil;

@Entity
@Table (name="FINAL_DUE")
public class FinalDueOfCustomer {
	
	@Id
	@GeneratedValue 
	@Column(name="FINAL_DUE_ID")
	private Long id;

	@Column(name="LAST_ENTRY_DATE")
	@Type(type="date")
	private Date entryDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Long getSmallCrateDue() {
		return smallCrateDue;
	}

	public void setSmallCrateDue(Long smallCrateDue) {
		this.smallCrateDue = smallCrateDue;
	}

	public Float getCashDue() {
		return cashDue;
	}

	public void setCashDue(Float cashDue) {
		this.cashDue = cashDue;
	}

	public Long getBigCrateDue() {
		return bigCrateDue;
	}

	public void setBigCrateDue(Long bigCrateDue) {
		this.bigCrateDue = bigCrateDue;
	}

	@OneToOne
	@JoinColumn (name="CUSTOMER_ID")
	private Customer customer;

	@Column(name = "SMALL_CRATE_DUE")
	private Long smallCrateDue;

	@Column(name = "CASH_DUE")
	private Float cashDue;
  	
	@Column(name = "BIG_CRATE_DUE")
	private Long bigCrateDue;
	
	
	/**
	 * 
	 * @param customer
	 * @return list of Credit entry (line items) for a particular truck
	 * between date range
	 */
	public static List <FinalDueOfCustomer> getFinalDueList( Route route) {

		System.out.println ("getCreditEntryList " + route); 
		
		
		Session session = HibernateUtil.getSessionFactory().openSession();

		try {
			Criteria crt = session.createCriteria(FinalDueOfCustomer.class);

			crt.add(Restrictions.eq("customer.route",  route));
			crt.addOrder(Order.asc("customer.name"));	 		 		
			List <FinalDueOfCustomer> finalDueList = crt.list();			 
			return finalDueList;
		} 
		finally {
			if(session!=null&&session.isOpen()){
				session.close();
			}
		}
	}
	

}
