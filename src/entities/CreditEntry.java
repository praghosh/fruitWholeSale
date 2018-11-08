package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.Type;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import nmfc.helper.ToastMessage;
import nmfc.helper.Utility;
import persistence.HibernateUtil;



@Entity
@Table(name="CREDIT_ENTRY" )
public class CreditEntry {

	@Id
	@GeneratedValue 
	@Column(name="CREDIT_ENTRY_ID")
	private Long id;

	@Column(name="CREDIT_ENTRY_DATE" , nullable=false)
	@Type(type="date")
	private Date entryDate;
	
	@Column(name="CREDIT_ENTRY_TYPE"  )
	private String creditType="Deposit";

	public String getCreditType() {
		return creditType;
	}

	public void setCreditType(String creditType) {
		this.creditType = creditType;
	}

	// This is the date when data is being credited 
	// in the transaction
	@Column(name="CREDIT_OR_DEPOSIT_DATE")
	@Type(type="date")
	private Date creditOrDepositDate;

	public Boolean getDataCommited() {
		return dataCommited;
	}

	public void setDataCommited(Boolean dataCommited) {
		this.dataCommited = dataCommited;
	}

	@ManyToOne
	@JoinColumn (name="CUSTOMER_ID", nullable=false)
	private Customer customer;

	@Column(name = "BILL_NUMBER")
	private Long billNumber;

	public Long getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(Long billNumber) {
		this.billNumber = billNumber;
	}

	@Column(name = "CRATE_DEPOSIT")
	private Long crateDeposit;

	@Column(name = "CASH_DEPOSIT")
	private Float cashDeposit;

	@Column(name = "CRATE_ISSUE")
	private Long crateIssue;

	@Column(name = "CASH_PURCHASE")
	private Float cashPurchaset;


	@Column(name = "BIG_CRATE_DEPOSIT")
	private Long bigCrateDeposit;

	@Column(name = "DATA_COMMITTED")
	private Boolean dataCommited;


	public Long getBigCrateDeposit() {
		return bigCrateDeposit;
	}

	public void setBigCrateDeposit(Long bigCrateDeposit) {
		this.bigCrateDeposit = bigCrateDeposit;
	}

	public Long getBigCrateIssue() {
		return bigCrateIssue;
	}

	public void setBigCrateIssue(Long bigCrateIssue) {
		this.bigCrateIssue = bigCrateIssue;
	}

	@Column(name = "BIG_CRATE_ISSUE")
	private Long bigCrateIssue;



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

	public Long getCrateIssue() {
		return crateIssue;
	}

	public void setCrateIssue(Long crateIssue) {
		this.crateIssue = crateIssue;
	}

	public Float getCashPurchaset() {
		return cashPurchaset;
	}

	public void setCashPurchaset(Float cashPurchaset) {
		this.cashPurchaset = cashPurchaset;
	}


	/**
	 * 
	 * @param customer
	 * @return   deposit made by all customers on a single day
	 */
	public static List <CreditEntry>   getLastDepositDetail(Date date) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		try {
			String hql = "from CreditEntry  where  entryDate =:d "   ;
			Query query = session.createQuery(hql).setParameter("d", date);
			query.setCacheable(false); 
			List  <CreditEntry> listProducts = query.list();
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
	
	/**
	 * 
	 * @param customer
	 * @return list of Credit entry  for a particular customer
	 * on a particular date
	 */
	public static List <CreditEntry> getCreditEntryListOnDate(Customer customer, Date onDate) {

		 
		Session session = HibernateUtil.getSessionFactory().openSession();

		try {
			String hql = "from CreditEntry where customer=:cr and entryDate=:edate";
			Query query = session.createQuery(hql).setParameter("cr", customer);
			query.setParameter("edate", onDate);
			query.setCacheable(false); 
			List  <CreditEntry> listProducts = query.list();
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


	/**
	 * 
	 * @param customer
	 * @return list of Credit entry (line items) for a particular customer
	 */
	public static List <CreditEntry> getCreditEntryList(Customer customer) {

		System.out.println ("getCreditEntryList " + customer); 
		Session session = HibernateUtil.getSessionFactory().openSession();

		try {
			String hql = "from CreditEntry where customer=:e";
			Query query = session.createQuery(hql).setParameter("e", customer);
			query.setCacheable(false); 
			List  <CreditEntry> listProducts = query.list();
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

	/**
	 * 
	 * @param customer
	 * @return list of Credit entry (line items) for a particular truck
	 * between date range
	 */
	public static List <CreditEntry> getCreditEntryList(Customer customer, Date startDate, Date endDate) {

		System.out.println ("getCreditEntryList " + customer); 
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();		// Not really required but It shows stale data without creating transaction ?????
		try {
			Criteria crt = session.createCriteria(CreditEntry.class);
			crt.addOrder(Order.asc("entryDate"));
			crt.add(Restrictions.eq("customer", customer));
			crt.add(Restrictions.ge("entryDate", startDate)); 
			crt.setCacheable(false); 

			// Sometime when  we use date time in hibernate, it does not
			// display the entry of last date we need to add one day extra. 	 	 
			Date maxDate = new Date(endDate.getTime() + TimeUnit.DAYS.toMillis(1));
			crt.add(Restrictions.le("entryDate", maxDate));			
			List <CreditEntry> customerList = crt.list();	
			session.getTransaction().commit(); // Just to stop caching
			return customerList;
		}
		catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback(); // Just to stop caching
			return new ArrayList <CreditEntry> ();
		}
		finally {
			if(session!=null&&session.isOpen()){
				session.flush();
				session.close();
			}
		}
	}

	public static boolean saveCreditEntryItem(CreditEntry creditEntry) {
		Session	session = HibernateUtil.getSessionFactory().openSession();	
		try {
			session.beginTransaction();				 
			session.save(creditEntry);			
			commitCreditEntrySingleEntry (creditEntry, session);
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
			if(session!=null&&session.isOpen()){
				session.flush();
				session.close();
			}
		}
	}	

	public static boolean deleteCreditEntryItem(Long itemCreditId) {
		Session session = HibernateUtil.getSessionFactory().openSession();;
		try {
			session.beginTransaction();
			Serializable id =  itemCreditId;
			Object persistentInstance = session.load(CreditEntry.class, id);
			CreditEntry cntr =  (CreditEntry) persistentInstance;
			boolean isCommited = Utility.getValue(cntr.getDataCommited());
			if  (isCommited) {
				System.out.println ("Can not delete committed data");
				return false;				
			}
			if (persistentInstance != null)  {
				session.delete(persistentInstance);
				session.getTransaction().commit();
				System.out.println ("Deleted Sucessfully");	
				return true;
			}
			else {
				System.out.println ("Did not find the Credit Entry in persistance");
				return false;
			}

		} 
		catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			return false;
		}

		finally {
			if(session!=null &&session.isOpen() ){	
				session.flush();
				session.close();
			}
		}

	}

	/**
	 * 
	 * @param customer
	 * @return  last deposit amount for a particular customer just before a particular date
	 */
	public static float  getLastDepositAmount(Customer customer, Date date) {

		System.out.println ("getCreditEntryList " + customer); 
		Session session = HibernateUtil.getSessionFactory().openSession();

		try {
			String hql = "from CreditEntry cr where customer=:e and entryDate <:d " +
					"and  cr.cashDeposit is not null and cr.cashDeposit!=0" +
					"order by cr.entryDate desc" ;
			Query query = session.createQuery(hql).setParameter("e", customer);
			query.setParameter("d", date);

			List  <CreditEntry> listProducts = query.list();
			if (listProducts.size()!=0)  {
				return listProducts.get(0).getCashDeposit();
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

	/**
	 * 
	 * @param customer
	 * @return  last deposit date for a particular customer just before a particular date
	 */
	public static Date  getLastDepositDate(Customer customer, Date date) {

		System.out.println ("getCreditEntryList " + customer); 
		Session session = HibernateUtil.getSessionFactory().openSession();

		try {
			String hql = "from CreditEntry cr where customer=:e and entryDate <:d " +
					"and  cr.cashDeposit is not null and cr.cashDeposit!=0" +
					"order by cr.entryDate desc" ;
			Query query = session.createQuery(hql).setParameter("e", customer);
			query.setParameter("d", date);
			query.setCacheable(false); 
			List  <CreditEntry> listProducts = query.list();
			if (listProducts.size()!=0)  {
				return listProducts.get(0).getEntryDate();
			}
			else {
				return null;
			}
		} 
		catch (HibernateException e) {
			e.printStackTrace();
			return null;
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
	 * @param credit -- ID of Credit  entry object to be committed
	 * 
	 */
	public static  void commitCreditEntrySingleEntry (CreditEntry creditEntry, Session session ) {

		System.out.println ("commitCreditEntry "); 

		try {		 	 			 
			Customer customer = creditEntry.getCustomer();

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

				pouplateCustomerCreditNewMadinaSingleEntry (creditEntry, finalBalanceEntry, lastTransaction, session );
				session.save(lastTransaction);
				session.save(finalBalanceEntry);
				session.save (creditEntry);
				System.out.println ("Committed Sucessfully");	

			}

		}
		catch (HibernateException e) {
			e.printStackTrace();
			throw new HibernateException ("Ledger not updated");

		}

	}
	private static void pouplateCustomerCreditNewMadinaSingleEntry(CreditEntry creditEntry,
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
		float changeInCashBlance = Utility.getValue (creditEntry.getCashDeposit());

		long changeInSmallCrateBalance = 
				Utility.getValue (creditEntry.getCrateDeposit()) ;

		// Currently small crare issue is not supported in Credit Entry
		//	-	Utility.getValue (creditEntry.getCrateIssue());

		long changeInBigCrateBalance = 
				Utility.getValue (creditEntry.getBigCrateDeposit()) ;

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
		lastTransaction.setCreditOrSalesDate (creditEntry.getEntryDate());			
		lastTransaction.setCustomer(creditEntry.getCustomer());
		lastTransaction.setIsLastTransaction(true);
		lastTransaction.setParticular("Deposit");

		finalBalance.setSmallCrateDue(smallCrateFinalDue - changeInSmallCrateBalance);
		finalBalance.setBigCrateDue(bigCrateFinalDue - changeInBigCrateBalance);
		finalBalance.setCashDue(cashFinalDue - changeInCashBlance);
		finalBalance.setEntryDate(new Date());
		finalBalance.setCreditOrSalesDate (new Date());
		tn = new TranscationSequenceEntity();
		session.save(tn);
		finalBalance.setSequenceOfTransaction(tn);
		finalBalance.setCustomer(creditEntry.getCustomer());
		creditEntry.setDataCommited(true);	        
	}


	/**
	 * 
	 * @param  
	 * @return   Total deposit for a customer in date range 
	 */
	public static double  getTotalDepositForCustomer (Customer cust, Date startDate, 
			Date endDate ) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		try {
			String hql = "SELECT  sum(cashDeposit) FROM CreditEntry crentr " + 
					"where crentr.customer=:c  and crentr.entryDate >=:start and " +
					"crentr.entryDate <=:end   " ; /*
					+
					" creditType >< :hal";	  */
			
			Query query = session.createQuery(hql).setParameter("start", startDate);
			query = query.setParameter("end", endDate);
			query = query.setParameter("c", cust);
			query.setCacheable(false);
			List    listProducts = query.list();			
			if (listProducts!=null && listProducts.size() !=0 && listProducts.get(0) !=null) {
				return  (double) listProducts.get(0);
			}
			else return 0;

		} 
		catch (HibernateException e) {
			e.printStackTrace();
			return 0;
		}
		catch (Exception e) {
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
	
	/**
	 * 
	 * @param  
	 * @return   Total deposit for a customer in date range excluding last date
	 */
	public static double  getTotalDepositForCustomerBeforeDate (Customer cust, Date startDate, 
			Date endDate ) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		try {
			String hql = "SELECT  sum(cashDeposit) FROM CreditEntry crentr " + 
					"where crentr.customer=:c  and crentr.entryDate >=:start and " +
					"crentr.entryDate <:end   " ; /*
					+
					" creditType >< :hal";	  */
			
			Query query = session.createQuery(hql).setParameter("start", startDate);
			query = query.setParameter("end", endDate);
			query = query.setParameter("c", cust);
			query.setCacheable(false);
			List    listProducts = query.list();			
			if (listProducts!=null && listProducts.size() !=0 && listProducts.get(0) !=null) {
				return  (double) listProducts.get(0);
			}
			else return 0;

		} 
		catch (HibernateException e) {
			e.printStackTrace();
			return 0;
		}
		catch (Exception e) {
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
}

