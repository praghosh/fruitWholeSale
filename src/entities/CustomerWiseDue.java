package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

public class CustomerWiseDue {
	

@Entity
@Table(name="CUSTOMER_WISE_DUE")

public class Customer {
	
	@Id
	@GeneratedValue
	@Column(name="CUST_DUE_ID")
	private Long id;
	
    @OneToOne
    @JoinColumn(name="CUST_ID")
    private Customer customer;   

	@Column(name="AMOUNT_DUE")
	private Float amountDue;
	

	@Column(name="BIG_CRATE_DUE")
	private Long bigCrateDue;
	

	@Column(name="BUSKET_DUE")
	private Long busketDue;
	

	@Column(name="SMALL_CRATE_DUE")
	private Long smallCrate;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Customer getCustomer() {
		return customer;
	}


	public void setCustomer(Customer customer) {
		this.customer = customer;
	}


	public Float getAmountDue() {
		return amountDue;
	}


	public void setAmountDue(Float amountDue) {
		this.amountDue = amountDue;
	}


	public Long getBigCrateDue() {
		return bigCrateDue;
	}


	public void setBigCrateDue(Long bigCrateDue) {
		this.bigCrateDue = bigCrateDue;
	}


	public Long getBusketDue() {
		return busketDue;
	}


	public void setBusketDue(Long busketDue) {
		this.busketDue = busketDue;
	}


	public Long getSmallCrate() {
		return smallCrate;
	}


	public void setSmallCrate(Long smallCrate) {
		this.smallCrate = smallCrate;
	}
	
	
}
	

}
