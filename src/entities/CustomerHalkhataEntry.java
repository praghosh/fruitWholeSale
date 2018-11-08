package entities;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.swing.JFormattedTextField;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.tool.schema.internal.HibernateSchemaManagementTool;

import nmfc.helper.Utility;
import persistence.HibernateUtil;

/**
 * 
 * @author Prabir Ghosh
 * This entity class keeps records of each halkhata of a customer
 * Unique constraints will ensure each customer will have single
 * entry for each halkhata. 
 *
 */
@Entity
@Table(name="CUSTOMER_HALKHATA_ENTRY", uniqueConstraints={@UniqueConstraint (columnNames ={"CUST_ID",  
"HALKHATA_ID" })})
public class CustomerHalkhataEntry {




	@Id
	@GeneratedValue 
	@Column(name="HALKHATA_ENTRY_ID")
	private Long id;


	@ManyToOne
	@JoinColumn (name="HALKHATA_ID")
	private HalKhata halkhata;

	@ManyToOne
	@JoinColumn (name="CUST_ID")
	private Customer customer;

	@Column(name="DISCOUNTED_SELL")
	private Long discountedSell;

	@Column(name="NON_DISCOUNTED_SELL")
	private Long nonDiscountedSell;
 
	@Column(name="SPECIAL_DISCOUNT")
	private Long specialDiscount;
	
	
	@Column(name="Total_DISCOUNT")
	private Long totalDiscount;
	
	public Long getActualTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(Long totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	@Column(name="CASH_DUE")
	private Long cashDue;
	
 	
	@Column(name="SMALL_CRATE_DUE")
	private Long smallCrateDue;
	
	@Column(name="BIG_CRATE_DUE")
	private Long bigCrateDue;
	
	
	// Cash deposited during Halkhata
	@Column(name="CASH_DEPOSIT")
	private Long cashDepositInHalkhata;
 
	   
	@Column(name="TOTAL_DEPOSIT_MADE")
	private long totalDepositMade;

	@Column(name="DISCOUNT_PERCENT")
	private float discountPercentGiven;

	@Column(name="ELLIGIBLE_DISCOUNT")
	private long eligibleDiscount;
 
	@Column(name="SMALL_CRATE_DEPOSIT")
	private long smallCrateDepositInHalkhata;
	
	@Column(name="BIG_CRATE_DEPOSIT")
	private long bigCrateDepositInHalkhata;
	
	@Column(name="HALKHATA_CLOSED")
	private Boolean isHalkhataCosed;
	
	
	@Column(name="BILL_NUMBER")
	private Long billNumber;
	
	 

	public long getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(long billNumber) {
		this.billNumber = billNumber;
	}

	public Boolean isHalkhataCosed() {
		return isHalkhataCosed;
	}

	public void setHalkhataCosed(boolean isHalkhataCosed) {
		this.isHalkhataCosed = isHalkhataCosed;
	}

	public Long getCashDepositInHalkhata() {
		return cashDepositInHalkhata;
	}

	public void setCashDepositInHalkhata(Long cashDepositInHalkhata) {
		this.cashDepositInHalkhata = cashDepositInHalkhata;
	}

	public long getTotalDepositMade() {
		return totalDepositMade;
	}

	public void setTotalDepositMade(long totalDepositMade) {
		this.totalDepositMade = totalDepositMade;
	}

	public float getDiscountPercentGiven() {
		return discountPercentGiven;
	}

	public void setDiscountPercentGiven(float discountPercentGiven) {
		this.discountPercentGiven = discountPercentGiven;
	}

	public long getEligibleDiscount() {
		return eligibleDiscount;
	}

	public void setEligibleDiscount(long eligibleDiscount) {
		this.eligibleDiscount = eligibleDiscount;
	}
 
	public long getSmallCrateDepositInHalkhata() {
		return smallCrateDepositInHalkhata;
	}

	public void setSmallCrateDepositInHalkhata(long smallCrateDepositInHalkhata) {
		this.smallCrateDepositInHalkhata = smallCrateDepositInHalkhata;
	}

	public long getBigCrateDepositInHalkhata() {
		return bigCrateDepositInHalkhata;
	}

	public void setBigCrateDepositInHalkhata(long bigCrateDepositInHalkhata) {
		this.bigCrateDepositInHalkhata = bigCrateDepositInHalkhata;
	}

	 
	public Long getSpecialDiscount() {
		return specialDiscount;
	}

	public void setSpecialDiscount(Long specialDiscount) {
		this.specialDiscount = specialDiscount;
	}

	public HalKhata getHalkhata() {
		return halkhata;
	}

	public void setHalkhata(HalKhata halkhata) {
		this.halkhata = halkhata;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Long getDiscountedSell() {
		return discountedSell;
	}

	public void setDiscountedSell(Long discountedSell) {
		this.discountedSell = discountedSell;
	}

	public Long getNonDiscountedSell() {
		return nonDiscountedSell;
	}

	public void setNonDiscountedSell(Long nonDiscountedSell) {
		this.nonDiscountedSell = nonDiscountedSell;
	}
 
	public Long getCashDue() {
		return cashDue;
	}

	public void setCashDue(Long cashDue) {
		this.cashDue = cashDue;
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

	/**
	 * 
	 * @param cust
	 * @param halkhata
	 * @return a Customer-Halkhata entry for a particular customer of 
	 * a particular Halkhata.
	 */

	public static  CustomerHalkhataEntry  getCustomerHalkhataEntry (Customer cust,
			HalKhata halkhata) {

		if ((halkhata==null) ||(cust==null)) {
			return null ;
		}

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		try {
			String hql = "from CustomerHalkhataEntry where customer=:c and " +
					" halkhata=:h  ";
			Query query = session.createQuery(hql).setParameter("c", cust);
			query = query.setParameter("h", halkhata);
			List  <CustomerHalkhataEntry> listProducts = query.list();

			if (listProducts!=null && listProducts.size() !=0) {
				return listProducts.get(0);
			}
			else {
				return null;		
			}

		}
		catch (Exception e) {
			e.printStackTrace();			 
			return  null ;

		}
		finally {
			if(session!=null&&session.isOpen()){
				session.flush();
				session.close();
			}
		}
	}


	/**
	 * save a CustomerHalkhataEntry entry in the persitance
	 * @param custHalKhata
	 * @return
	 */
	public static boolean saveCustomerHalkhataEntry (CustomerHalkhataEntry custHalKhata) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();	
			session.save(custHalKhata);
			session.getTransaction().commit();	
			return true;

		}
		catch (Exception ex)
		{   			 
			if (session.getTransaction()!=null) session.getTransaction().rollback();
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			return false;
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
	 * @param credit -- ID of Credit  entry object to be committed
	 * 
	 */
	public static  boolean commitHalkhataEntry (CustomerHalkhataEntry custHalKhata) {
		  
		Session session = HibernateUtil.getSessionFactory().openSession();;
		try {
			session.beginTransaction();
			   
				Customer customer = custHalKhata.getCustomer();

				// As commiting customer crdit entry will update customer's balance
				// it should be called serially for a specific customer so that 
				// no two threads can simultaneously balance customer data

				synchronized (customer) {

					// Accoriding to New Madina a last entry only with the final due will
					// be added at the last row to show the final balance.
					CustomerTransaction finalBalanceEntry =null;
					// Get the last transcation made in customer table to find
					// the final balance
					Criteria crt = session.createCriteria(CustomerTransaction.class);
					crt.addOrder(Order.desc("entryDate")); // can be removed
					crt.add(Restrictions.eq("customer", customer));
					crt.add(Restrictions.eq("isFinalBalance", true));
					List <CustomerTransaction> customerTransactionList = crt.list();
					// First try to find customer last transcation marked as
					// final.
					if (customerTransactionList != null  && (customerTransactionList.size() >0)) {
						finalBalanceEntry = customerTransactionList.get(0);			 			
					}
					// If no data found then
					// create a new record 
					// 
					else {

						finalBalanceEntry = new  CustomerTransaction();
					}

					CustomerTransaction	lastTransaction = new  CustomerTransaction();					

					pouplateHalkhataEntryNewMadina (custHalKhata, finalBalanceEntry, lastTransaction, session );
					session.save(lastTransaction);
					session.save(finalBalanceEntry);
					session.save (custHalKhata);
					session.getTransaction().commit();
					System.out.println ("Committed Sucessfully");	
					return true;
 
				}
	 
		}
		catch (HibernateException e) {
			session.getTransaction().rollback();
			e.printStackTrace();
			return false;
		}

		finally {
			if(session!=null &&session.isOpen()){
				session.close();
			}
		}
	}
	
	private static void pouplateHalkhataEntryNewMadina(CustomerHalkhataEntry custHalKhata,
			CustomerTransaction finalBalance, CustomerTransaction lastTransaction,
			Session session) {


		long bigCrateFinalDue=0;
		long smallCrateFinalDue=0;
		float cashFinalDue=0;
		lastTransaction.setEntryDate(new Date());
		// Final  balance once created row will not be changed. Only the 
		// values will be updated
		TranscationSequenceEntity tn;
		bigCrateFinalDue = Utility.getValue(finalBalance.getBigCrateDue());
		smallCrateFinalDue =Utility.getValue(finalBalance.getSmallCrateDue());
		cashFinalDue = Utility.getValue(finalBalance.getCashDue());
		finalBalance.setFinalBalance(true); 

		// Currently cash issue is not supported in Credit Entry
		// So get the cash deposit only
		float changeInCashBlance = Utility.getValue (custHalKhata.getCashDepositInHalkhata());
		changeInCashBlance += Utility.getValue (custHalKhata.getEligibleDiscount());
		changeInCashBlance += Utility.getValue (custHalKhata.getSpecialDiscount());

		long changeInSmallCrateBalance = 
				Utility.getValue (custHalKhata.getSmallCrateDepositInHalkhata()) ;

		// Currently small crare issue is not supported in Credit Entry
		//	-	Utility.getValue (creditEntry.getCrateIssue());

		long changeInBigCrateBalance = 
				Utility.getValue (custHalKhata.getBigCrateDepositInHalkhata()) ;

		// Add a new Transaction entry
		tn = new TranscationSequenceEntity();
		session.save(tn); // This will create an increasing sequence
		// transaction will be ordered through this sequence
		lastTransaction.setSequenceOfTransaction(tn); 
		lastTransaction.setBigCrateBalance(bigCrateFinalDue);
		lastTransaction.setSmallCrateBalance( smallCrateFinalDue);
		lastTransaction.setCashBalance(cashFinalDue);
		lastTransaction.setCrateDeposit( changeInSmallCrateBalance);
		lastTransaction.setBigCrateDeposit(changeInBigCrateBalance);
		lastTransaction.setCashDeposit(changeInCashBlance);	
		lastTransaction.setFinalBalance(false);
		lastTransaction.setCreditOrSalesDate (custHalKhata.getHalkhata().getEndDate());			
		lastTransaction.setCustomer(custHalKhata.getCustomer());
		lastTransaction.setIsLastTransaction(true);
		lastTransaction.setParticular("HalKhata");

		finalBalance.setSmallCrateDue(smallCrateFinalDue - changeInSmallCrateBalance);
		finalBalance.setBigCrateDue(bigCrateFinalDue - changeInBigCrateBalance);
		finalBalance.setCashDue(cashFinalDue - changeInCashBlance);
		finalBalance.setEntryDate(new Date());
		finalBalance.setCreditOrSalesDate (new Date());
		tn = new TranscationSequenceEntity(); // This creates a new bigger sequence.
		session.save(tn);
		finalBalance.setSequenceOfTransaction(tn);
		finalBalance.setCustomer(custHalKhata.getCustomer());
		custHalKhata.setHalkhataCosed (true);	        
	}

}
