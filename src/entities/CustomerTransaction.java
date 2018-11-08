package entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.Type;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import nmfc.helper.Utility;
import persistence.HibernateUtil;

@Entity
@Table(name="CUSTOMER_TRANSACTION", 
indexes={@Index (name="Customer_Transaction_Entry_date_index", columnList ="CREDIT_ENTRY_DATE"),
		@Index (name="Customer_Transaction_ACTUAL_CREDIT_date_index", columnList ="ACTUAL_CREDIT_OR_SALES_DATE"),
		@Index (name="Customer_Transaction_CUSTOMER_index", columnList ="CUSTOMER_ID"),
		@Index (name="Customer_Transaction_SEQUENCE_index", columnList ="SEQUENCE_ID"),

})
public class CustomerTransaction {

	@Id
	@GeneratedValue 
	@Column(name="CUSTOMER_TRANSACTION_ID")
	private Long id;

	// This is the date when transaction is entered.
	// Transaction data will be arranged according to the
	// assecnding order of entry date. However, actual
	// sales date or or deposit date can be different
	@Column(name="CREDIT_ENTRY_DATE", nullable=false)
	@Type(type="timestamp")
	private Date entryDate;

	@OneToOne
	@JoinColumn (name="SEQUENCE_ID")
	private TranscationSequenceEntity sequenceOfTransaction;


	public TranscationSequenceEntity getSequenceOfTransaction() {
		return sequenceOfTransaction;
	}

	public void setSequenceOfTransaction(TranscationSequenceEntity sequenceOfTransaction) {
		this.sequenceOfTransaction = sequenceOfTransaction;
	}

	/**
	 *  This is the date of transaction as per ledger.
	 *  It will first check the sales date / credit date of the transaction
	 *  If user forgets to update a previous sale then this will be clubbed 
	 *  with latest ledger date
	 */


	@Column(name="ACTUAL_CREDIT_OR_SALES_DATE", nullable=false)
	@Type(type="date")
	private Date creditOrSalesDate;


	public Date getCreditOrSalesDate() {
		return creditOrSalesDate;
	}

	public void setCreditOrSalesDate(Date creditOrSalesDate) {
		this.creditOrSalesDate = creditOrSalesDate;
	}

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

	public Long getCrateIssue() {
		return crateIssue;
	}

	public void setCrateIssue(Long f) {
		this.crateIssue = f;
	}

	public Float getCashPurchase() {
		return cashPurchase;
	}

	public void setCashPurchase(Float cashPurchase) {
		this.cashPurchase = cashPurchase;
	}

	public Long getCrateDeposit() {
		return crateDeposit;
	}

	public void setCrateDeposit(Long crateDeposit) {
		this.crateDeposit = crateDeposit;
	}

	public Float getCashDeposit() {
		return cashDeposit;
	}

	public void setCashDeposit(Float cashDeposit) {
		this.cashDeposit = cashDeposit;
	}

	public Float getDescription() {
		return description;
	}

	public void setDescription(Float description) {
		this.description = description;
	}

	public Long getBigCrateIssue() {
		return bigCrateIssue;
	}

	public Float getDiscountedPurchase() {
		return discountedPurchase;
	}

	public void setDiscountedPurchase(Float discountedPurchase) {
		this.discountedPurchase = discountedPurchase;
	}

	public void setBigCrateIssue(Long f) {
		this.bigCrateIssue = f;
	}

	public Long getBigCrateDeposit() {
		return bigCrateDeposit;
	}

	public void setBigCrateDeposit(Long bigCrateDeposit) {
		this.bigCrateDeposit = bigCrateDeposit;
	}

	public Long getBigCrateDue() {
		return bigCrateDue;
	}

	public void setBigCrateBalance(Long bigCrateBalance) {
		this.bigCrateDue = bigCrateBalance;
	}

	public Long getSmallCrateDue() {
		return smallCrateDue;
	}

	public void setSmallCrateBalance(Long l) {
		this.smallCrateDue = l;
	}

	public Float getCashDue() {
		return cashDue;
	}

	public void setCashBalance(Float cashBalance) {
		this.cashDue = cashBalance;
	}

	@ManyToOne
	@JoinColumn (name="CUSTOMER_ID", nullable=false)
	private Customer customer;

	// Instead of mapping the data, just
	// ids are stored to reduce overhead.
	// Also this id can be either from truck sales 
	// or from credit entry.	
	//	@OneToOne  // Not required to create map
	//	@JoinColumn (name="CREDIT_ENTRY_ID")

	@Column(name="CREDIT_OR_SALES_ID")
	private Long creditOrSalesEntryId;



	public Long getCreditOrSalesEntryId() {
		return creditOrSalesEntryId;
	}

	public void setCreditOrSalesEntryId(Long creditOrSalesEntryId) {
		this.creditOrSalesEntryId = creditOrSalesEntryId;
	}


	@Column(name = "CRATE_ISSUE")
	private Long crateIssue;

	@Column(name = "CASH_PURCHASE")
	private Float cashPurchase;
	
	@Column(name = "ITEM_RATE")
	private Float itemRate;

	public Float getItemRate() {
		return itemRate;
	}

	public void setItemRate(Float itemRate) {
		this.itemRate = itemRate;
	}

	@Column(name = "DISCOUNTED_PURCHASE")
	private Float discountedPurchase;

	@Column(name = "CRATE_DEPOSIT")
	private Long crateDeposit;

	@Column(name = "CASH_DEPOSIT")
	private Float cashDeposit;

	@Column(name = "DESCRIPTION")
	private Float description;


	@Column(name = "BIG_CRATE_ISSUE")
	private Long bigCrateIssue;


	@Column(name = "BIG_CRATE_DEPOSIT")
	private Long bigCrateDeposit;

	@Column(name = "BIG_CRATE_BALANCE")
	private Long bigCrateDue;

	@Column(name = "SMALL_CRATE_BALANCE")
	private Long smallCrateDue;

	public String getParticular() {
		return particular;
	}

	public void setParticular(String particular) {
		this.particular = particular;
	}

	public Boolean getFinalBalance() {
		return isFinalBalance;
	}

	public void setFinalBalance(Boolean finalBalance) {
		this.isFinalBalance = finalBalance;
	}

	@Column(name = "CASH_BALANCE")
	private float cashDue;

	@Column(name = "PARTICULAR")
	private String particular;


	// To indicate this is the last entry
	@Column(name = "LAST_TRANSACTION")
	private Boolean isLastTransaction;

	public void setBigCrateDue(Long bigCrateDue) {
		this.bigCrateDue = bigCrateDue;
	}

	public void setSmallCrateDue(Long smallCrateDue) {
		this.smallCrateDue = smallCrateDue;
	}

	public void setCashDue(float cashDue) {
		this.cashDue = cashDue;
	}

	public Boolean getIsLastTransaction() {
		return isLastTransaction;
	}

	public void setIsLastTransaction(Boolean lastTransaction) {
		this.isLastTransaction = lastTransaction;
	}

	// To indicate this is the last entry
	@Column(name = "FINAL_BALANCE")
	private Boolean isFinalBalance;


	// To indicate this is the last entry
	@Column(name = "OPENING_BALANCE")
	private Boolean openingBalance;

	public Boolean getOpeningBalance() {
		return openingBalance;
	}

	public void setOpeningBalance(Boolean openingBalance) {
		this.openingBalance = openingBalance;
	}

	// To indicate this is the sales entry
	// not cash or item deposit
	@Column(name = "IS_SALES_ENTRY")
	private Boolean isSalesEntry;


	public Boolean getIsSalesEntry() {
		return isSalesEntry;
	}

	public void setIsSalesEntry(Boolean isSalesEntry) {
		this.isSalesEntry = isSalesEntry;
	}

	// If no boolean argument is passed , this method will return all 
	// transaction data
	public static List getTransactionList(Customer customer, Date startDate, Date endDate) {

		return getTransactionList(  customer,   startDate,   endDate, false);


	}
	/**
	 * 
	 * @param customer
	 * @param startDate
	 * @param endDate
	 * @param salesDataOnly - if it is true then only transaction with sales Data will be returned
	 * @return
	 */
	// If no boolean argument is passed , this method will return all 
	// transaction data
	public static List getTransactionList(Customer customer, Date startDate, Date endDate,  boolean salesDataOnly) {

		System.out.println ("getTransactionList " + customer); 
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		try {
			Criteria crt = session.createCriteria(CustomerTransaction.class);
			crt.add(Restrictions.eq("customer", customer));		
			crt.add(Restrictions.ge("entryDate", startDate)); 
			Date maxDate = new Date(endDate.getTime() + TimeUnit.DAYS.toMillis(1));
			crt.add(Restrictions.lt("entryDate", maxDate));	
			crt.setCacheable(false); 

			//	crt.add(Restrictions.between("entryDate", startDate, maxDate));
			if (salesDataOnly) {
				crt.add(Restrictions.eq("isSalesEntry", true));	
			}
			crt.addOrder(Order.asc("sequenceOfTransaction"));	
			List <CustomerTransaction> customerTranscationList = crt.list();			 
			return customerTranscationList;
		} 

		catch (HibernateException e) {
			e.printStackTrace();
			return null;
		}
		finally {
			if(session!=null&&session.isOpen()){
				session.close();
			}
		}
	}

	/**
	 * This method brings all the customer transaction based on sales/debit/credit date
	 * As actual sales date can be different from exact transaction entry date,
	 * data is shorted according to the order they are entered in the customer ledger.
	 * @param customer
	 * @param startDate
	 * @param endDate
	 * @param  
	 * @return
	 */

	public static List <CustomerTransaction> getTransactionListBySalesDate(Customer customer, Date startDate, Date endDate) {

		System.out.println ("getTransactionListBySalesDate " + customer); 
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		try {
			// First get the first entry of transaction on the start SalesDate
			Criteria crt = session.createCriteria(CustomerTransaction.class);
			crt.add(Restrictions.eq("customer", customer));		
			crt.add(Restrictions.ge ("creditOrSalesDate", startDate));  			 
			crt.addOrder(Order.asc("sequenceOfTransaction"));	
			crt.setCacheable(false); 
			List <CustomerTransaction> customerTranscationList = crt.list();
			long firstTranscationId=0;
			CustomerTransaction firstTranscation=null;
			if (customerTranscationList!=null && customerTranscationList.size() >0) {
				firstTranscation = customerTranscationList.get(0);
				firstTranscationId =firstTranscation.getSequenceOfTransaction().getId();
			}
			else {
				System.out.println("No matching data found");
				return new ArrayList <CustomerTransaction>();
			}

			// Now  get the last entry of transaction on the start SalesDate
			crt = session.createCriteria(CustomerTransaction.class);
			crt.add(Restrictions.eq("customer", customer));		
			crt.add(Restrictions.le ("creditOrSalesDate", endDate));  			 
			crt.addOrder(Order.desc("sequenceOfTransaction"));
			crt.setCacheable(false); 
			customerTranscationList = crt.list();
			CustomerTransaction lastTranscation=null;
			long lastTranscationId=0;
			if (customerTranscationList!=null && customerTranscationList.size() >0) {
				lastTranscation = customerTranscationList.get(0);
				lastTranscationId = lastTranscation.getSequenceOfTransaction().getId();
			}
			else {
				System.out.println("No matching data found");
				return new ArrayList <CustomerTransaction>();
			}


			String hql = "from CustomerTransaction c where c.sequenceOfTransaction.id >=:start and " +
					"c.sequenceOfTransaction.id <=:end order by sequenceOfTransaction"      ;
			Query query = session.createQuery(hql).setParameter("start", firstTranscationId);
			query.setParameter("end", lastTranscationId);
			query.setCacheable(false);
			customerTranscationList = query.list();
			return customerTranscationList;

		} 

		catch (HibernateException e) {
			e.printStackTrace();
			return new ArrayList <CustomerTransaction>();
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ArrayList <CustomerTransaction>();
		}
		finally {
			if(session!=null&&session.isOpen()){
				session.flush();
				session.close();
			}
		}
	}



	public static List <CustomerTransaction> getTransactionListByEntryDate(Customer customer, Date startDate, Date endDate) {

		System.out.println ("getTransactionListBySalesDate " + customer); 
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		try {
			List <CustomerTransaction> customerTranscationList;

			String hql = "from CustomerTransaction c where c.creditOrSalesDate >=:start and " +
					"c.customer=:customer and "  +

					"c.creditOrSalesDate <=:end order by sequenceOfTransaction"      ;
			Query query = session.createQuery(hql).setParameter("start", startDate);
			//		Date maxDate = new Date(endDate.getTime() + TimeUnit.DAYS.toMillis(1));
			query.setParameter("end", endDate);
			query.setParameter("customer", customer);
			query.setCacheable(false);
			customerTranscationList = query.list();
			return customerTranscationList;

		} 

		catch (HibernateException e) {
			e.printStackTrace();
			return new ArrayList <CustomerTransaction>();
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ArrayList <CustomerTransaction>();
		}
		finally {
			if(session!=null&&session.isOpen()){
				session.flush();
				session.close();
			}
		}
	}





	/**
	 * 
	 * 
	 * @param selectedCustomer
	 * @return Opening balance of the customer.
	 */

	public static List getOpeningBalance(Customer customer) {

		System.out.println ("getTransactionList " + customer); 
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		try {
			Criteria crt = session.createCriteria(CustomerTransaction.class);
			crt.add(Restrictions.eq("customer", customer));
			crt.add(Restrictions.eq("isOpeningBalance", true)); 
			List <CustomerTransaction> customerTranscationList = crt.list();			 
			return customerTranscationList;
		} 

		catch (HibernateException e) {
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

	/*
	 *  Return final balances of all customers  
	 *  
	 */

	public static List getFinalBalanceList() {
		System.out.println ("getTransactionList "); 
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		try {
			String hql = "from CustomerTransaction cr where  cr.isFinalBalance=true order by cr.customer.name  ";
			Query query = session.createQuery(hql);
			query.setCacheable(false);
			List<CustomerTransaction> listProducts = query.list();
			return listProducts;
		} 

		catch (HibernateException e) {
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

	/*
	 *  Return final balances of all customer in a route
	 *  
	 */

	public static List getFinalBalanceList(Route selectedRoute) {
		System.out.println ("getTransactionList " + selectedRoute); 
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		try {
			String hql = "from CustomerTransaction cr where  cr.customer.route=:r and cr.isFinalBalance=true";
			Query query = session.createQuery(hql);
			query.setParameter("r",selectedRoute);
			query.setCacheable(false);
			List<CustomerTransaction> listProducts = query.list();
			return listProducts;
		} 

		catch (HibernateException e) {
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

	/*
	 *  Return   final cash balance of a customer  
	 *  
	 */

	public static float  getLastBalance(Customer customer ) {		 
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		try {
			String hql = "from CustomerTransaction tr where  tr.customer=:c" +
					"  and tr.isFinalBalance=true";
			Query query = session.createQuery(hql);
			query.setParameter("c",customer);
			query.setCacheable(false);
			List<CustomerTransaction> listProducts = query.list();
			if ( listProducts!=null && listProducts.size()>0) {
				return Utility.getValue(listProducts.get(0).getCashDue());
			}
			else {
				return 0;
			}
		} 

		catch (HibernateException e) {
			e.printStackTrace();
			return 0; 
		}
		finally {
			if(session!=null&&session.isOpen()){
				session.flush();
				session.close();
			}
		}

	}

	/*
	 *  Return  cash  balance of a customer just before the selected date
	 *  
	 */

	public static float  getLastBalance(Customer customer, Date date) {
		System.out.println ("getBalanceOfCustomer " + customer + " before date " + date); 
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		try {


			String hql = "from CustomerTransaction cr where cr.sequenceOfTransaction = " +
					"( select max(tr.sequenceOfTransaction)" + 
					"from CustomerTransaction tr where " +
					"tr.customer=:c and tr.creditOrSalesDate<:d )" ;
			Query query = session.createQuery(hql);
			query.setParameter("c",customer);
			query.setParameter("d",date);
			query.setCacheable(false);
			List<CustomerTransaction> listProducts = query.list();			
			if ( listProducts!=null && listProducts.size()>0) {				
				CustomerTransaction lastTransaction = listProducts.get(0);
				float cashDue = Utility.getValue(lastTransaction.getCashDue());				
				float lastCashSellAmount = Utility.getValue(lastTransaction.getCashPurchase());
				float lastDiscountSellAmount = Utility.getValue(lastTransaction.getDiscountedPurchase());
				float lastDeposit = Utility.getValue(lastTransaction.getCashDeposit());
				float lastFinalBalance = cashDue + lastCashSellAmount + lastDiscountSellAmount - lastDeposit;				
				return lastFinalBalance;
			} // Otherwise get from Opening Balance
			else {
				return 0;
			}
		} 

		catch (HibernateException e) {
			e.printStackTrace();
			return 0; 
		}
		finally {
			if(session!=null&&session.isOpen()){
				session.flush();
				session.close();
			}
		}

	}
	/*
	 *  Return  cash  balance of a customer on or  just before the selected date
	 *  
	 */
	public static float  getLasCashtBalanceOnDate(Customer customer, Date date) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		try {


			String hql = "from CustomerTransaction cr where cr.sequenceOfTransaction = " +
					"( select max(tr.sequenceOfTransaction)" + 
					"from CustomerTransaction tr where " +
					"tr.customer=:c and tr.creditOrSalesDate<=:d )" ;
			Query query = session.createQuery(hql);
			query.setParameter("c",customer);
			query.setParameter("d",date);
			query.setCacheable(false);
			List<CustomerTransaction> listProducts = query.list();			
			if ( listProducts!=null && listProducts.size()>0) {				
				CustomerTransaction lastTransaction = listProducts.get(0);
				float cashDue = Utility.getValue(lastTransaction.getCashDue());				
				float lastCashSellAmount = Utility.getValue(lastTransaction.getCashPurchase());
				float lastDiscountSellAmount = Utility.getValue(lastTransaction.getDiscountedPurchase());
				float lastDeposit = Utility.getValue(lastTransaction.getCashDeposit());
				float lastFinalBalance = cashDue + lastCashSellAmount + lastDiscountSellAmount - lastDeposit;				
				return lastFinalBalance;
			}  // Otherwise get from opening balance
			else {
				 hql = "from CustomerOpeningBalance crOpen where  crOpen.customer=:c and   " +
						 " crOpen.entryDate <=:d  " ;
				 query = session.createQuery(hql);
				 query.setParameter("c",customer);
				 query.setParameter("d",date);
				 query.setCacheable(false);
				 List<CustomerOpeningBalance> listOpening = query.list();
				 if ( listOpening!=null && listOpening.size()>0) {	
					 return listOpening.get(0).getCashDue();
				 	}
				 else {
					 return 0; 
				 }
			} 
		}

		catch (HibernateException e) {
			e.printStackTrace();
			return 0; 
		}
		finally {
			if(session!=null&&session.isOpen()){
				session.flush();
				session.close();
			}
		}

	}
	
	

	/*
	 *  Return   balance of a customer on or just before the selected date.
	 *  If there is no balance entry on a day then retrieve the balance entry 
	 *  found first in nearest previous date.
	 *  
	 */

	public static List  getBalanceOnOrBeforeDate(Customer customer, Date date) {
		//System.out.println ("getBalanceOfCustomer " + customer + " before date " + date); 
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		List <Number> balanceList = new ArrayList <Number> ();
		try {


			String hql = "from CustomerTransaction cr where cr.sequenceOfTransaction = " +
					"( select max(tr.sequenceOfTransaction)" + 
					"from CustomerTransaction tr where " +
					"tr.customer=:c and tr.creditOrSalesDate<=:d )" ;
			Query query = session.createQuery(hql);
			query.setParameter("c",customer);
			query.setParameter("d",date);
			query.setCacheable(false);
			List<CustomerTransaction> listProducts = query.list();				
			if ( listProducts!=null && listProducts.size()>0) {				
				CustomerTransaction lastTransaction = listProducts.get(0);

				long smallCrateDue = Utility.getValue(lastTransaction.getSmallCrateDue());				
				long smallCrateIssue = Utility.getValue(lastTransaction.getCrateIssue());
				long smallCrateDeposit = Utility.getValue(lastTransaction.getCrateDeposit());
				long lastSmallCrateBalance = smallCrateDue + smallCrateIssue -smallCrateDeposit;	 			
				balanceList.add(lastSmallCrateBalance);
				
				long bigCrateDue = Utility.getValue(lastTransaction.getBigCrateDue());				
				long lastBigCrateIssue = Utility.getValue(lastTransaction.getBigCrateIssue());
				long lastBigCrateDeposit = Utility.getValue(lastTransaction.getBigCrateDeposit());
				long lastBigCrateBalance = bigCrateDue + lastBigCrateIssue - lastBigCrateDeposit;
				balanceList.add(lastBigCrateBalance);

				

				float cashDue = Utility.getValue(lastTransaction.getCashDue());				
				float lastCashSellAmount = Utility.getValue(lastTransaction.getCashPurchase());
				float lastDiscountSellAmount = Utility.getValue(lastTransaction.getDiscountedPurchase());
				float lastDeposit = Utility.getValue(lastTransaction.getCashDeposit());
				float lastFinalBalance = cashDue + lastCashSellAmount + lastDiscountSellAmount - lastDeposit;				
				balanceList.add(lastFinalBalance);

			}
			else {
				 hql = "from CustomerOpeningBalance crOpen where  crOpen.customer=:c and   " +
						 " crOpen.entryDate <=:d  " ;
				 query = session.createQuery(hql);
				 query.setParameter("c",customer);
				 query.setParameter("d",date);
				 query.setCacheable(false);
				 List<CustomerOpeningBalance> listOpening = query.list();
				 if ( listOpening!=null && listOpening.size()>0) {	
					 CustomerOpeningBalance opening = listOpening.get(0);
					  balanceList.add(opening.getSmallCrateDue());
					  balanceList.add(opening.getBigCrateDue());
					  balanceList.add(opening.getCashDue());
				 	}				 
			} 

			return balanceList;


		} 

		catch (HibernateException e) {
			e.printStackTrace();
			return balanceList; 
		}
		finally {
			if(session!=null&&session.isOpen()){
				session.flush();
				session.close();
			}
		}

	}

	
	 
	/**
	 *  Return   balance of a customer just before the selected date.
	 *  If there is no balance entry on a day then retrieve the balance entry 
	 *  found first in nearest previous date. This is different from the 
	 *  method getBalanceOnOrBeforeDate
	 * 
	 * @param customer
	 * @param date
	 * @return
	 */
	public static List  getBalanceBeforeDate(Customer customer, Date date) {
		//System.out.println ("getBalanceOfCustomer " + customer + " before date " + date); 
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		List <Number> balanceList = new ArrayList <Number> ();
		try {


			String hql = "from CustomerTransaction cr where cr.sequenceOfTransaction = " +
					"( select max(tr.sequenceOfTransaction)" + 
					"from CustomerTransaction tr where " +
					"tr.customer=:c and tr.creditOrSalesDate<:d )" ;
			Query query = session.createQuery(hql);
			query.setParameter("c",customer);
			query.setParameter("d",date);
			query.setCacheable(false);
			List<CustomerTransaction> listProducts = query.list();				
			if ( listProducts!=null && listProducts.size()>0) {				
				CustomerTransaction lastTransaction = listProducts.get(0);

				long smallCrateDue = Utility.getValue(lastTransaction.getSmallCrateDue());				
				long smallCrateIssue = Utility.getValue(lastTransaction.getCrateIssue());
				long smallCrateDeposit = Utility.getValue(lastTransaction.getCrateDeposit());
				long lastSmallCrateBalance = smallCrateDue + smallCrateIssue -smallCrateDeposit;	 			
				balanceList.add(lastSmallCrateBalance);
				
				long bigCrateDue = Utility.getValue(lastTransaction.getBigCrateDue());				
				long lastBigCrateIssue = Utility.getValue(lastTransaction.getBigCrateIssue());
				long lastBigCrateDeposit = Utility.getValue(lastTransaction.getBigCrateDeposit());
				long lastBigCrateBalance = bigCrateDue + lastBigCrateIssue - lastBigCrateDeposit;
				balanceList.add(lastBigCrateBalance);
				float cashDue = Utility.getValue(lastTransaction.getCashDue());				
				float lastCashSellAmount = Utility.getValue(lastTransaction.getCashPurchase());
				float lastDiscountSellAmount = Utility.getValue(lastTransaction.getDiscountedPurchase());
				float lastDeposit = Utility.getValue(lastTransaction.getCashDeposit());
				float lastFinalBalance = cashDue + lastCashSellAmount + lastDiscountSellAmount - lastDeposit;				
				balanceList.add(lastFinalBalance);

			}
			else {
				 hql = "from CustomerOpeningBalance crOpen where  crOpen.customer=:c and   " +
						 " crOpen.entryDate <=:d  " ;
				 query = session.createQuery(hql);
				 query.setParameter("c",customer);
				 query.setParameter("d",date);
				 query.setCacheable(false);
				 List<CustomerOpeningBalance> listOpening = query.list();
				 if ( listOpening!=null && listOpening.size()>0) {	
					 CustomerOpeningBalance opening = listOpening.get(0);
					  balanceList.add(opening.getSmallCrateDue());
					  balanceList.add(opening.getBigCrateDue());
					  balanceList.add(opening.getCashDue());
				 	}	
				
			}

			return balanceList;


		} 

		catch (HibernateException e) {
			e.printStackTrace();
			return balanceList; 
		}
		finally {
			if(session!=null&&session.isOpen()){
				session.flush();
				session.close();
			}
		}

	}

	/*
	 *  Return   the last credit/sales update date
	 *  
	 */

	public static Date  getLastUpadateDate() {
		//System.out.println ("getLastUpadateDate"); 
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		Date maxDate = new Date ();

		try {

			Criteria criteria = session
					.createCriteria(CustomerTransaction.class)
					.setProjection(Projections.max("creditOrSalesDate")); 
			maxDate = (Date)criteria.uniqueResult();

		} 

		catch (HibernateException e) {
			e.printStackTrace();
		}
		finally {
			if(session!=null&&session.isOpen()){
				session.flush();
				session.close();
			}
		}

		return maxDate;
	}

	public static boolean  deleteAllTransactionAfter(Customer customer,  Long sequenceOfTransaction) {
		System.out.println ("deleteAllTransactionAfter " + customer + " sequence=" + sequenceOfTransaction  ); 
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		try {
			// First get the current transaction values

			String hql = "from CustomerTransaction tr where tr.sequenceOfTransaction.sequenceNumber =:sq and " +

					"tr.customer=:cr)" ;
			Query query = session.createQuery(hql);
			query.setParameter("cr",customer);
			query.setParameter("sq", sequenceOfTransaction);
			query.setCacheable(false);
			List<CustomerTransaction> listProducts = query.list();	
			// Store the balance value of the current transaction
			if ( listProducts!=null && listProducts.size()>0) {				
				CustomerTransaction lastTransaction = listProducts.get(0);	 				

				long finalCrateDue =  Utility.getValue( lastTransaction.getSmallCrateDue()) +
						Utility.getValue (lastTransaction.getCrateIssue()) - 
						Utility.getValue(lastTransaction.getCrateDeposit());   

				long finalBigCrateDue = Utility.getValue (lastTransaction.getBigCrateDue()) 
						+  Utility.getValue (lastTransaction.getBigCrateIssue()) 
						- Utility.getValue (lastTransaction.getBigCrateDeposit()) ;


				float finalCashDue = Utility.getValue (lastTransaction.getCashDue()) 
						+  Utility.getValue (lastTransaction.getDiscountedPurchase()) 
						+  Utility.getValue (lastTransaction.getCashPurchase()) 
						- Utility.getValue (lastTransaction.getCashDeposit()) ;

				hql = "delete from CustomerTransaction tr where tr.sequenceOfTransaction.sequenceNumber >:sq " +

					" and tr.customer=:cr and tr.isFinalBalance!=true)" ;
				query = session.createQuery(hql);
				query.setParameter("cr",customer);
				query.setParameter("sq",sequenceOfTransaction);
				System.out.println ("Result =" + query.executeUpdate());

				// Now set the value in final transaction
				CustomerTransaction finalBalanceEntry =null;
				// Get the last transcation made in customer table to find
				// the final balance
				Criteria crt = session.createCriteria(CustomerTransaction.class);				 
				crt.add(Restrictions.eq("customer", customer));
				crt.add(Restrictions.eq("isFinalBalance", true));
				List <CustomerTransaction> customerTransactionList = crt.list();

				if (customerTransactionList != null  && (customerTransactionList.size() >0)) {
					finalBalanceEntry = customerTransactionList.get(0);	
					finalBalanceEntry.setBigCrateDue(finalBigCrateDue);
					finalBalanceEntry.setSmallCrateDue(finalCrateDue);
					finalBalanceEntry.setCashDue(finalCashDue);
					TranscationSequenceEntity tn = new TranscationSequenceEntity();
					session.save(tn);
					finalBalanceEntry.setSequenceOfTransaction(tn);
					session.save(finalBalanceEntry);					 
					session.getTransaction().commit();
					System.out.println ("Committed Sucessfully");	
				}
				else {
					throw new HibernateException ("No final balance found");
				}


				return true;
			}
			else {
				return false;
			}
		} 

		catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			return false; 
		}
		finally {
			if(session!=null&&session.isOpen()){
				session.flush();
				session.close();
			}
		}

	} 

}
