package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import nmfc.helper.Utility;
import persistence.HibernateUtil;

/**
 * 
 * 
 *  This class commits all the data in a synchronized way and
 *  upadate customer transaction and final balance or due of
 *  Customer.
 *  
 *  @see CreditEntry CustomerTrnsaction and FinalDueOfCustomer
 * 
 * 
 * 
 */

public class CommitData {

	/**
	 * New Madina Company maintains customer ledger intheir own fashion.
	 * Last balance is entered in a new blank row.
	 * 
	 * @param truckSaleId
	 * @return
	 */

	public static  boolean  commitTruckSaleItemNewMadina (Long truckSaleId) {

		return false;

	}

	/**
	 * New Madina Company maintains customer ledger intheir own fashion.
	 * Last balance is entered in a new blank row.
	 * 
	 * @param truckSaleId
	 * @return
	 */

	public static  boolean  commitOpeningBalanceNewMadina (Customer customer) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Serializable id =  customer.getId() ;
		Object persistentInstance = session.load(Customer.class, id);
		Customer persistentCustomer = (Customer) persistentInstance;

		synchronized (persistentCustomer) {
			try  {
				session.beginTransaction();
				Criteria crt = session.createCriteria(CustomerTransaction.class);
				crt.add(Restrictions.eq("customer", persistentCustomer));
				List <CustomerTransaction> customerTransactionList = crt.list();
				// First try to find customer last transcation marked as
				// final.
				if (customerTransactionList != null  && (customerTransactionList.size() >0)) {
					System.out.println ("Customer Ledger is not empty, opening balance can not be entered");
					return false;
				}
				else {
					CustomerTransaction newTranscation = new  CustomerTransaction();
					crt = session.createCriteria(CustomerOpeningBalance.class);
					crt.add(Restrictions.eq("customer", persistentCustomer));
					List <CustomerOpeningBalance> openingBalanceList = crt.list();
					// Retrieve customer opening balance for a particular
					// customer and then get the balance. Then upadate 
					// the customer transaction table to update ledger
					CustomerOpeningBalance openingBalance = (CustomerOpeningBalance) 
							openingBalanceList.get(0); // There will be only one entry
					newTranscation.setSmallCrateBalance ( Utility.getValue(openingBalance.getSmallCrateDue()));
					newTranscation.setBigCrateBalance ( Utility.getValue(openingBalance.getBigCrateDue()));
					newTranscation.setCashBalance (Utility.getValue(openingBalance.getCashDue()));
					newTranscation.setEntryDate(new Date());
					TranscationSequenceEntity tn = new TranscationSequenceEntity();
					session.save(tn);
					newTranscation.setSequenceOfTransaction(tn);
					newTranscation.setCreditOrSalesDate(openingBalance.getEntryDate() );
					newTranscation.setFinalBalance(true);
					newTranscation.setCustomer(persistentCustomer);
					openingBalance.setDataCommited(true);
					session.save(openingBalance);
					session.save(newTranscation);
					session.getTransaction().commit();
					return true;
				}
			}
			catch (HibernateException e) {
				session.getTransaction().rollback();
				e.printStackTrace();
				return false;
			}

			finally {
				if(session!=null){
					session.close();
				}
			}
		}


	}

	/**
	 * New Madina Company maintains customer ledger intheir own fashion.
	 * Last balance is entered in a new blank row.
	 * 
	 * @param truckSaleId
	 * @return
	 */

	public static  boolean  commitDepoistNewMadina (Customer customer) {


		return false;

	}

	/**
	 * 
	 * @param truckSaleId
	 * @return
	 */
	public static  boolean  commitTruckSaleItem(Long truckSaleId) {

		System.out.println ("commitTruckSaleItem " + truckSaleId); 
		TruckSales truckSaleEntry =null;
		Session session = HibernateUtil.getSessionFactory().openSession(); 
		try {
			session.beginTransaction();
			Serializable id =  truckSaleId;
			Object persistentInstance = session.load(TruckSales.class, id);			
			if (persistentInstance == null)  {		 
				System.out.println ("Could not commit trucksale data as entry not found");
				return false;
			}

			truckSaleEntry =  (TruckSales) persistentInstance;
			if  (Utility.getValue (truckSaleEntry.getDataCommited())) {
				System.out.println ("Can not commit committed data again");
				return false;				
			}
			//  debit data is not commited
			if  (Utility.getValue (truckSaleEntry.getIsDebitPurchase())) {
				System.out.println ("Can not commit Debit Purchase");
				return false;				
			}
			id = truckSaleEntry.getCustomer().getId();
			persistentInstance = session.load(Customer.class, id);	
			Customer customer = (Customer) persistentInstance;


			// As committing customer credit entry will update customer's balance
			// it should be called serially for a specific customer so that 
			// no two threads can simultaneously balance customer data.

			synchronized (customer) {

				CustomerTransaction finalBalanceTransaction =null;
				// Get  the final balance
				Criteria crt = session.createCriteria(CustomerTransaction.class);
				crt.add(Restrictions.eq("customer", customer));
				crt.add(Restrictions.eq("isFinalBalance", true));
				List <CustomerTransaction> customerTransactionList = crt.list();
				// First try to find customer last transcation marked as
				// final.
				if (customerTransactionList != null  && (customerTransactionList.size() >0)) {
					finalBalanceTransaction = customerTransactionList.get(0);			 			
				}
				// If no data found then create a new transaction
				//  
				else {

					finalBalanceTransaction = new CustomerTransaction();

				}
				 
				CustomerTransaction lastTransaction = new  CustomerTransaction();
				pouplateTruckSaleDataNewMadinaSingleEntry (truckSaleEntry, finalBalanceTransaction, lastTransaction, session );
				session.save(lastTransaction);
				session.save(finalBalanceTransaction);
				session.save (truckSaleEntry);
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
			if(session!=null){
				session.flush();
				session.close();
			}
		}


	}

	/**
	 * 
	 * New Madina adds a blank row at the end to display the total
	 * @param truckSaleEntry
	 * @param finalBalance
	 * @param lastTransaction
	 */

	private static void pouplateTruckSaleDataNewMadinaSingleEntry(TruckSales truckSaleEntry, CustomerTransaction finalBalance,
			CustomerTransaction lastTransaction, Session session) {

		lastTransaction.setEntryDate(new Date());
		float cashFinalDue = Utility.getValue( (finalBalance.getCashDue()));
		long bigCrateFinalDue = Utility.getValue( finalBalance.getBigCrateDue());
		long smallCrateFinalDue = Utility.getValue( finalBalance.getSmallCrateDue());

		long smallCrateIssued =0;
		long bigCrateIssued =0;
		String container = truckSaleEntry.getPurchaseUnit();
		if ("Small Crate".equals (container)) {
			smallCrateIssued = truckSaleEntry.getQuantityInPurchaseUnit();
		}
		else if ("Big Crate".equals (container)) {
			bigCrateIssued = truckSaleEntry.getQuantityInPurchaseUnit();
		}
		float rate =truckSaleEntry.getRate();

		float currenPurchase = truckSaleEntry.getAmount();

		// Probably This is not required, as debit entry will not
		// upadate customerledger
		boolean isDebit = truckSaleEntry.getIsDebitPurchase();
		boolean  isDiscounted = Utility.getValue (truckSaleEntry.getIsDiscounted());
		String currentParticular = truckSaleEntry.getFruit().getShortName() +
				": "  + truckSaleEntry.getQuantityInPurchaseUnit() + " "+ truckSaleEntry.getPurchaseUnit() 
				+ " X " + truckSaleEntry.getQuantity();
				  


		// Add a new Transaction entry if  last transaction is of
		// other date
		TranscationSequenceEntity tn = new TranscationSequenceEntity();
		session.save(tn); // This will create an increasing sequence
		// transaction will be ordered through this sequence
		lastTransaction.setSequenceOfTransaction(tn); 
		lastTransaction.setBigCrateBalance(bigCrateFinalDue);
		lastTransaction.setSmallCrateBalance( smallCrateFinalDue);
		lastTransaction.setCashBalance(cashFinalDue);
		lastTransaction.setItemRate(rate);
		lastTransaction.setCrateIssue( smallCrateIssued);
		lastTransaction.setBigCrateIssue(bigCrateIssued);
		lastTransaction.setFinalBalance(false);
		lastTransaction.setParticular(currentParticular);
		if (!isDebit && isDiscounted) {

			lastTransaction.setDiscountedPurchase(currenPurchase);
		}
		else {
			lastTransaction.setCashPurchase(currenPurchase);
		}
		lastTransaction.setCustomer(truckSaleEntry.getCustomer());
		lastTransaction.setIsLastTransaction(true);	


		lastTransaction.setCreditOrSalesDate (truckSaleEntry.getSellingDate());	
		finalBalance.setSmallCrateDue(smallCrateFinalDue +  smallCrateIssued);
		finalBalance.setBigCrateDue(bigCrateFinalDue + bigCrateIssued);
		finalBalance.setCashDue((long) cashFinalDue +  currenPurchase);
		finalBalance.setCustomer(truckSaleEntry.getCustomer());
		finalBalance.setEntryDate(new Date());
		finalBalance.setCreditOrSalesDate (new Date());
	    tn = new TranscationSequenceEntity();
		session.save(tn); // This will create an increasing sequence and 
		// will ensure that final balance is the last entry
		finalBalance.setSequenceOfTransaction(tn); 
		finalBalance.setFinalBalance(true); 
		truckSaleEntry.setDataCommited(true);

	}



	/**
	 * 
	 * New Madina adds a blank row at the end to display the total
	 * @param truckSaleEntry
	 * @param finalBalance
	 * @param lastTransaction
	 */
	/*
	private static void pouplateTruckSaleDataNewMadina(TruckSales truckSaleEntry, CustomerTransaction finalBalance,
			CustomerTransaction lastTransaction, Session session) {

		lastTransaction.setEntryDate(new Date());
		float cashFinalDue = Utility.getValue( (finalBalance.getCashDue()));
		long bigCrateFinalDue = Utility.getValue( finalBalance.getBigCrateDue());
		long smallCrateFinalDue = Utility.getValue( finalBalance.getSmallCrateDue());

		long smallCrateIssued =0;
		long bigCrateIssued =0;
		String container = truckSaleEntry.getPurchaseUnit();
		if ("Small Crate".equals (container)) {
			smallCrateIssued = truckSaleEntry.getQuantityInPurchaseUnit();
		}
		else if ("Big Crate".equals (container)) {
			bigCrateIssued = truckSaleEntry.getQuantityInPurchaseUnit();
		}

		float currenPurchase = truckSaleEntry.getAmount();

		// Probably This is not required, as debit entry will not
		// upadate customerledger
		boolean isDebit = truckSaleEntry.getIsDebitPurchase();
		boolean  isDiscounted = Utility.getValue (truckSaleEntry.getIsDiscounted());
		String currentParticular = truckSaleEntry.getFruit().getShortName() +
				": "  + truckSaleEntry.getQuantityInPurchaseUnit() + truckSaleEntry.getPurchaseUnit() + " ";

		// If there exists previous transaction on same day then
		// update the same.
		if ( Utility.getValue(lastTransaction.getIsLastTransaction())) {

			long previuosSmallCrateIssued=Utility.getValue( (lastTransaction.getCrateIssue()));
			long previuosBigCrateIssued =Utility.getValue( lastTransaction.getBigCrateIssue());
			float previuosCashPurchase =Utility.getValue( lastTransaction.getCashPurchase());
			float previuosDiscountedPurchase =Utility.getValue( lastTransaction.getDiscountedPurchase());

			String previousParticular = lastTransaction.getParticular();

			if ( (previousParticular==null) || previousParticular.trim().equals("") ) {
				lastTransaction.setParticular(currentParticular);

			}
			else {
				lastTransaction.setParticular(previousParticular + ", " + currentParticular);
			}
			if (!isDebit && isDiscounted) {

				lastTransaction.setDiscountedPurchase(previuosDiscountedPurchase + currenPurchase);
			}
			else {
				lastTransaction.setCashPurchase(previuosCashPurchase + currenPurchase);
			}

			lastTransaction.setBigCrateIssue(previuosBigCrateIssued + bigCrateIssued );
			lastTransaction.setCrateIssue(previuosSmallCrateIssued + smallCrateIssued );

		}
		else {
			// Add a new Transaction entry if  last transaction is of
			// other date
			TranscationSequenceEntity tn = new TranscationSequenceEntity();
			session.save(tn); // This will create an increasing sequence
			// transaction will be ordered through this sequence
			lastTransaction.setSequenceOfTransaction(tn); 
			lastTransaction.setBigCrateBalance(bigCrateFinalDue);
			lastTransaction.setSmallCrateBalance( smallCrateFinalDue);
			lastTransaction.setCashBalance(cashFinalDue);
			lastTransaction.setCrateIssue( smallCrateIssued);
			lastTransaction.setBigCrateIssue(bigCrateIssued);
			lastTransaction.setParticular(currentParticular);
			if (!isDebit && isDiscounted) {

				lastTransaction.setDiscountedPurchase(currenPurchase);
			}
			else {
				lastTransaction.setCashPurchase(currenPurchase);
			}
			lastTransaction.setCustomer(truckSaleEntry.getCustomer());
			lastTransaction.setIsLastTransaction(true);	

		}
		lastTransaction.setCreditOrSalesDate (truckSaleEntry.getSellingDate());	
		finalBalance.setSmallCrateDue(smallCrateFinalDue +  smallCrateIssued);
		finalBalance.setBigCrateDue(bigCrateFinalDue + bigCrateIssued);
		finalBalance.setCashDue(cashFinalDue +  currenPurchase);
		finalBalance.setCustomer(truckSaleEntry.getCustomer());
		finalBalance.setEntryDate(new Date());
		finalBalance.setCreditOrSalesDate (new Date());
		TranscationSequenceEntity tn = new TranscationSequenceEntity();
		session.save(tn); // This will create an increasing sequence and 
		// will ensure that final balance is the last entry
		finalBalance.setSequenceOfTransaction(tn); 
		finalBalance.setFinalBalance(true); 
		truckSaleEntry.setDataCommited(true);

	}
	 */

	/**
	 * 
	 * @param credit -- ID of Credit  entry object to be committed
	 * 
	 */
	public static  boolean commitCreditEntrySingleEntry (long  creditEntryId) {

		System.out.println ("commitCreditEntry " + creditEntryId); 
		Session session = HibernateUtil.getSessionFactory().openSession();;
		try {
			session.beginTransaction();
			Serializable id =  creditEntryId;
			Object persistentInstance = session.load(CreditEntry.class, id);			
			if (persistentInstance != null)  {

				CreditEntry creditEntry =  (CreditEntry) persistentInstance;
				if  (creditEntry.getDataCommited()) {
					System.out.println ("Can not commit committed data again");
					return false;				
				}
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
					session.getTransaction().commit();
					System.out.println ("Committed Sucessfully");	
					return true;


				}
			}
			else {
				System.out.println ("Did not find the Credit Entry in persistance");
				return false;
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


	/**
	 * 
	 * @param credit -- ID of Credit  entry object to be committed
	 * 
	 */
	public static  boolean commitCreditEntry (long  creditEntryId) {

		System.out.println ("commitCreditEntry " + creditEntryId); 
		Session session = HibernateUtil.getSessionFactory().openSession();;
		try {
			session.beginTransaction();
			Serializable id =  creditEntryId;
			Object persistentInstance = session.load(CreditEntry.class, id);			
			if (persistentInstance != null)  {

				CreditEntry creditEntry =  (CreditEntry) persistentInstance;
				if  (creditEntry.getDataCommited()) {
					System.out.println ("Can not commit committed data again");
					return false;				
				}
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
					// If no data found then try to get latest record
					// otherwise create a new record 
					// 
					else {

						finalBalanceEntry = new  CustomerTransaction();
					}

					CustomerTransaction lastTransaction;				
					crt = session.createCriteria(CustomerTransaction.class);
					crt.add(Restrictions.eq("customer", customer));
					crt.add(Restrictions.eq("isLastTransaction", true));
					customerTransactionList = crt.list();
					if (customerTransactionList != null  && (customerTransactionList.size() >0)) {
						// If last transaction is of the same day then club the
						// new truck sale entry in the same transaction. Generally three
						// four sold fruit items are entered in the same transaction.
						lastTransaction = customerTransactionList.get(0);


						if (!lastTransaction.getCreditOrSalesDate().equals
								(creditEntry.getEntryDate())) {
							// In case the deposits were on different day earlier or
							// later then carry over the entry to another row.
							// And unmark the last transaction.
							lastTransaction.setIsLastTransaction(false);
							session.save(lastTransaction);
							// This new transaction will be used for 
							// new entry.
							lastTransaction = new  CustomerTransaction();
						}

					}
					else {
						lastTransaction = new  CustomerTransaction();
					}

					pouplateCustomerCreditNewMadinaSingleEntry (creditEntry, finalBalanceEntry, lastTransaction, session );
					session.save(lastTransaction);
					session.save(finalBalanceEntry);
					session.save (creditEntry);
					session.getTransaction().commit();
					System.out.println ("Committed Sucessfully");	
					return true;

				}
			}
			else {
				System.out.println ("Did not find the Credit Entry in persistance");
				return false;
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

	/**
	 * As New Madina company maintans their balance shit in different way
	 * a separate method is created for that purpose. 
	 * A new row will added at the end to reflect the final due
	 * @param creditEntry
	 * @param finalBalance
	 * @param lastTransaction
	 */
	/*
	private static void pouplateCustomerCreditNewMadina(CreditEntry creditEntry,
			CustomerTransaction finalBalance, CustomerTransaction lastTransaction,
			Session session) {


		long bigCrateFinalDue=0;
		long smallCrateFinalDue=0;
		float cashFinalDue=0;
		lastTransaction.setEntryDate(new Date());
		// Final  balance once created row will not be changed. Only the 
		// values will be updated

		bigCrateFinalDue = Utility.getValue(finalBalance.getBigCrateDue());
		smallCrateFinalDue =Utility.getValue(finalBalance.getSmallCrateDue());
		cashFinalDue = Utility.getValue(finalBalance.getCashDue());
		finalBalance.setFinalBalance(true); 

		// Currently cash issue is not supported in Credit Entry
		float changeInCashBlance = Utility.getValue (creditEntry.getCashDeposit());

		long changeInSmallCrateBalance = 
				Utility.getValue (creditEntry.getCrateDeposit()) ;

		// Currently small crare issue is not supported in Credit Entry
		//	-	Utility.getValue (creditEntry.getCrateIssue());

		long changeInBigCrateBalance = 
				Utility.getValue (creditEntry.getBigCrateDeposit()) ;

		long bigCrateLastDeposit =Utility.getValue(lastTransaction.getBigCrateDeposit());
		long smallCrateLastDeposit = Utility.getValue(lastTransaction.getCrateDeposit());
		float cashLastDeposit = Utility.getValue(lastTransaction.getCashDeposit());

		// If last transaction is of the same date then club the data
		if (Utility.getValue (lastTransaction.getIsLastTransaction())) {

			lastTransaction.setCrateDeposit( smallCrateLastDeposit +  changeInSmallCrateBalance);
			lastTransaction.setBigCrateDeposit(bigCrateLastDeposit + changeInBigCrateBalance);
			lastTransaction.setCashDeposit(cashLastDeposit + changeInCashBlance);
			lastTransaction.setCreditOrSalesDate (creditEntry.getEntryDate());

		}
		else {
			// Add a new Transaction entry
			TranscationSequenceEntity tn = new TranscationSequenceEntity();
			session.save(tn); // This will create an increasing sequence
			// transaction will be ordered through this sequence
			lastTransaction.setSequenceOfTransaction(tn); 
			lastTransaction.setBigCrateBalance(bigCrateFinalDue);
			lastTransaction.setSmallCrateBalance( smallCrateFinalDue);
			lastTransaction.setCashBalance(cashFinalDue);
			lastTransaction.setCrateDeposit( changeInSmallCrateBalance);
			lastTransaction.setBigCrateDeposit(changeInBigCrateBalance);
			lastTransaction.setCashDeposit(changeInCashBlance);		
			lastTransaction.setCreditOrSalesDate (creditEntry.getEntryDate());			
			lastTransaction.setCustomer(creditEntry.getCustomer());
			lastTransaction.setIsLastTransaction(true);	
		}


		finalBalance.setSmallCrateDue(smallCrateFinalDue - changeInSmallCrateBalance);
		finalBalance.setBigCrateDue(bigCrateFinalDue - changeInBigCrateBalance);
		finalBalance.setCashDue(cashFinalDue - changeInCashBlance);
		finalBalance.setEntryDate(new Date());
		finalBalance.setCreditOrSalesDate (new Date());
		TranscationSequenceEntity tn = new TranscationSequenceEntity();
		session.save(tn);
		finalBalance.setSequenceOfTransaction(tn);
		finalBalance.setCustomer(creditEntry.getCustomer());
		creditEntry.setDataCommited(true);	        
	}


	 */
	/**
	 * As New Madina company maintans their balance shit in different way
	 * a separate method is created for that purpose. 
	 * A new row will added at the end to reflect the final due
	 * @param creditEntry
	 * @param finalBalance
	 * @param lastTransaction
	 */

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
	 
		float changeInCashBlance = Utility.getValue (creditEntry.getCashDeposit());

		long changeInSmallCrateBalance = 
				Utility.getValue (creditEntry.getCrateDeposit()) ;
 
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
		lastTransaction.setCashDeposit(changeInCashBlance) ;	
		lastTransaction.setFinalBalance(false);
		lastTransaction.setCreditOrSalesDate (creditEntry.getEntryDate());			
		lastTransaction.setCustomer(creditEntry.getCustomer());
		lastTransaction.setIsLastTransaction(true);
		lastTransaction.setParticular("Deposit");

		finalBalance.setSmallCrateDue(smallCrateFinalDue - changeInSmallCrateBalance);
		finalBalance.setBigCrateDue(bigCrateFinalDue - changeInBigCrateBalance);
		finalBalance.setCashDue (cashFinalDue - changeInCashBlance);
		finalBalance.setEntryDate(new Date());
		finalBalance.setCreditOrSalesDate (new Date());
		tn = new TranscationSequenceEntity();
		session.save(tn);
		finalBalance.setSequenceOfTransaction(tn);
		finalBalance.setCustomer( creditEntry.getCustomer());
		creditEntry.setDataCommited(true);	        
	}

	public static boolean unCommitCreditEntrySingleEntry(Long creditEntryId) {
		System.out.println ("UncommitCreditEntry " + creditEntryId); 
		Session session = HibernateUtil.getSessionFactory().openSession();;
		try {
			session.beginTransaction();
			Serializable id =  creditEntryId;
			Object persistentInstance = session.load(CreditEntry.class, id);			
			if (persistentInstance != null)  {

				CreditEntry creditEntry =  (CreditEntry) persistentInstance;
				if  (!creditEntry.getDataCommited()) {
					System.out.println ("Can not un commit uncommitted data again");
					return false;				
				}
				creditEntry.setDataCommited(false); 
				session.save (creditEntry);
				session.getTransaction().commit();
				System.out.println ("Un Committed Sucessfully");	
				return true;

				 
			}
			else {
				System.out.println ("Did not find the Credit Entry in persistance");
				return false;
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
/*
 * 
 */
	
	public static boolean unCommitTruckSale(Long truckSaleID) {
		System.out.println ("Uncommit Truck Sales " + truckSaleID); 
		Session session = HibernateUtil.getSessionFactory().openSession();;
		try {
			session.beginTransaction();
			Serializable id =  truckSaleID;
			Object persistentInstance = session.load(TruckSales.class, id);			
			if (persistentInstance != null)  {

				TruckSales truckSalesEntry =  (TruckSales) persistentInstance;
				if  (!truckSalesEntry.getDataCommited()) {
					System.out.println ("Can not un commit uncommitted data again");
					return false;				
				}
				truckSalesEntry.setDataCommited(false); 
				session.save (truckSalesEntry);
				session.getTransaction().commit();
				System.out.println ("Un Committed Sucessfully");	
				return true;

				 
			}
			else {
				System.out.println ("Did not find the Truck Sales Entry in persistance");
				return false;
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

	/**
	 * Currently not used as New Madina follows a different method
	 * @param creditEntry
	 * @param latestTranscation
	 * @param newTranscation
	 */
	/*
	private static void pouplateCustomerCredit(CreditEntry creditEntry,
			CustomerTransaction latestTranscation, CustomerTransaction newTranscation) {

		long bigCrateDue=0;
		long smallCrateDue=0;
		float cashDue=0;

		if (latestTranscation != null) {
			bigCrateDue = Utility.getValue(latestTranscation.getBigCrateDue());
			smallCrateDue =Utility.getValue(latestTranscation.getSmallCrateDue());
			cashDue = Utility.getValue(latestTranscation.getCashDue());
			latestTranscation.setFinalBalance(false);
		}


		// Currently cash issue is not supported in Credit Entry
		float changeInCashBlance = Utility.getValue (creditEntry.getCashDeposit());

		float changeInSmallCrateBalance = 
				Utility.getValue (creditEntry.getCrateDeposit()) -
				Utility.getValue (creditEntry.getCrateIssue());

		float changeInBigCrateBalance = 
				Utility.getValue (creditEntry.getBigCrateDeposit()) -
				Utility.getValue (creditEntry.getBigCrateIssue());
		// Update the balance
		//		newTranscation.setBigCrateBalance(bigCrateDue + changeInBigCrateBalance );
		//	newTranscation.setSmallCrateBalance( smallCrateDue + changeInSmallCrateBalance);
		newTranscation.setCashBalance(cashDue  + changeInCashBlance);

		// Also enter the actual transcation detail.
		newTranscation.setBigCrateDeposit(creditEntry.getBigCrateDeposit());
		newTranscation.setBigCrateIssue(creditEntry.getBigCrateIssue());

		newTranscation.setCrateIssue(creditEntry.getCrateIssue());
		//	newTranscation.setCrateDeposit(creditEntry.getCrateDeposit());
		newTranscation.setCashDeposit(creditEntry.getCashDeposit());
		newTranscation.setCustomer(creditEntry.getCustomer());
		newTranscation.setEntryDate(new Date());
		newTranscation.setCreditOrSalesDate (creditEntry.getEntryDate());
		newTranscation.setCreditOrSalesEntryId(creditEntry.getId());
		newTranscation.setParticular("Credit Entry"); 
		newTranscation.setFinalBalance(true);
		creditEntry.setDataCommited(true);	        
	}
	 */
}
