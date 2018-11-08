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

import nmfc.helper.ToastMessage;
import nmfc.helper.Utility;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.Type;
import org.hibernate.criterion.Restrictions;

import javax.persistence.Index;
import persistence.HibernateUtil;

/**
 * 
 * @author Prabir Ghosh
 * This entity class creates line items for each truck. 
 *
 */
@Entity
@Table(name="TRUCK_SALES" 
, indexes ={@Index (name="TRUCK_SALE_DATE_INDEX", columnList="SELLING_DATE")})
public class TruckSales {

	@Id
	@GeneratedValue 
	@Column(name="TRUCK_SALES_ID")
	private Long id;

	@Column(name="SELLING_DATE")
	@Type(type="date")
	private Date sellingDate;


	@ManyToOne
	@JoinColumn (name="CUSTOMER_ID", nullable=false)
	private Customer customer;


	@ManyToOne
	@JoinColumn (name="TRUCK_ENTRY_ID", nullable=false)
	private TruckEntry truckEntry;


	// Tthis atttributte can be deletetd as 
	// truckEntry conttains TtruckID
	@ManyToOne
	@JoinColumn (name="TRUCK_ID")
	private Truck itemsFromTruck;

	public TruckEntry getTruckEntry() {
		return truckEntry;
	}

	public void setTruckEntry(TruckEntry truckEntry) {
		this.truckEntry = truckEntry;
	}

	@ManyToOne
	@JoinColumn (name="FRUIT_ID")
	private Fruit fruit;

	@ManyToOne
	@JoinColumn (name="FRUIT_QUALITY_ID")
	private FruitQuality fruitQuality;

	@Column(name="LOT_NUMBER")
	private String lotNumber;	

	@Column(name="PURCHASE_UNIT")
	private String purchaseUnit;


	@Column(name="SALES_UNIT")
	private String salesUnit;

	@Column(name = "DATA_COMMITTED")
	private Boolean dataCommited;

	// To indicate cash (debit) or credit  item
	@Column(name = "IS_DEBIT")
	private Boolean isDebitPurchase;

	// To indicate discouned or  non-discounted item
	@Column(name = "IS_DISCOUNTED")
	private Boolean isDiscounted;

	public String getSalesUnit() {
		return salesUnit;
	}

	public Boolean getDataCommited() {
		return dataCommited;
	}

	public Boolean getIsDebitPurchase() {
		return isDebitPurchase;
	}

	public void setIsDebitPurchase(Boolean isDebitPurchase) {
		this.isDebitPurchase = isDebitPurchase;
	}

	public void setDataCommited(Boolean dataCommited) {
		this.dataCommited = dataCommited;
	}

	public void setSalesUnit(String salesUnit) {
		this.salesUnit = salesUnit;
	}

	public Boolean getIsDiscounted() {
		return isDiscounted;
	}

	public void setIsDiscounted(Boolean isDiscounted) {
		this.isDiscounted = isDiscounted;
	}

	public Long getQuantityInPurchaseUnit() {
		return quantityInPurchaseUnit;
	}

	public void setQuantityInPurchaseUnit(Long quantityInPurchaseUnit) {
		this.quantityInPurchaseUnit = quantityInPurchaseUnit;
	}

	@Column(name="QUANTITY")
	private Float quantity;


	@Column(name="QUANTITY_IN_PURCHASE_UNIT")
	private Long quantityInPurchaseUnit;

	@Column(name="AMOUNT")
	private Float amount;

	@Column(name="RATE")
	private Float rate;

	@Column(name="DISCOUNTED_RATE")
	private Float discountedRate;

	public Float getDiscountedRate() {
		return discountedRate;
	}

	public void setDiscountedRate(Float discountedRate) {
		this.discountedRate = discountedRate;
	}

	@Column(name="PAY_METHOD")
	private String payMethod;


	public String getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	/**
	 * 
	 * @param truck
	 * @return list of trucks Sales (line items) to customer for a particular truck
	 */
	public static List <TruckSales>  getTruckSalesList(Truck truck) {

		System.out.println ("getTruckEntryList " + truck);
		if (truck==null) {
			return new ArrayList <TruckSales> ();

		}
		Session session = HibernateUtil.getSessionFactory().openSession();

		try {
			String hql = "from TruckSales where itemsFromTruck=:e";
			Query query = session.createQuery(hql).setParameter("e", truck);
			List<TruckSales> listProducts = query.list();
			return listProducts;
		} 
		finally {
			if(session!=null&&session.isOpen()){
				session.close();
			}
		}
	} 

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getSellingDate() {
		return sellingDate;
	}

	public void setSellingDate(Date sellingDate) {
		this.sellingDate = sellingDate;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Truck getItemsFromTruck() {
		return itemsFromTruck;
	}

	public void setItemsFromTruck(Truck itemsFromTruck) {
		this.itemsFromTruck = itemsFromTruck;
	}

	public Fruit getFruit() {
		return fruit;
	}

	public void setFruit(Fruit fruit) {
		this.fruit = fruit;
	}

	public FruitQuality getFruitQuality() {
		return fruitQuality;
	}

	public void setFruitQuality(FruitQuality fruitQuality) {
		this.fruitQuality = fruitQuality;
	}

	public String getPurchaseUnit() {
		return purchaseUnit;
	}

	public void setPurchaseUnit(String purchaseUnit) {
		this.purchaseUnit = purchaseUnit;
	}

	public Float getQuantity() {
		return quantity;
	}

	public void setQuantity(Float quantity) {
		this.quantity = quantity;
	}


	public  Float  getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public Float getRate() {
		return rate;
	}

	public void setRate(Float rate) {
		this.rate = rate;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public static boolean deleteTruckSaleItem(Long itemTrucKSaleId) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		session.beginTransaction();
		Serializable id =  itemTrucKSaleId;
		Object persistentInstance = session.load(TruckSales.class, id);
		TruckSales trSales = (TruckSales) persistentInstance;
		synchronized (trSales) {
			try {
				if (persistentInstance != null)  {
					TruckEntry trEntry=trSales.getTruckEntry();
					Serializable trId = trEntry.getId();
					TruckEntry persitantTrEntry = (TruckEntry) session.load(TruckEntry.class, trId);
					long quantityInPurchaseUnit = Utility.getValue(  trSales.getQuantityInPurchaseUnit());
					long previousBalance = trEntry.getRemainingQuantity();
					// While deleting a truck Sale balance of item will also increase.
					// This is opposite to item sales.
					long finalBalance = previousBalance + quantityInPurchaseUnit;
					persitantTrEntry.setRemainingQuantity(finalBalance);
					session.save(persitantTrEntry);
					session.delete(persistentInstance);
					session.getTransaction().commit();
					System.out.println ("Deleted Sucessfully");	
					return true;
				}
				else {
					System.out.println ("Did not find the Truck Object in persistance");
					return false;
				}

			} 
			catch (HibernateException e) {
				e.printStackTrace();
				session.getTransaction().rollback();
				return false;
			}

			finally {
				if(session!=null &&session.isOpen()){
					session.close();
				}
			}
		}
	}
	
	/**
	 * 
	 * @param truck
	 * @return  Discounted sales for a customer in date range
	 */
	public static double  getTotalDiscountedSales(Customer cust, Date startDate, Date endDate ) {
 
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		try {
			String hql = "SELECT  sum(amount) FROM TruckSales trcksls " + 
					"where trcksls.customer=:c  and trcksls.sellingDate >=:start and " +
	 				"trcksls.sellingDate <=:end and isDiscounted=true and "  +
					" isDebitPurchase=false";		    
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
	 * @param truck
	 * @return  Non Discounted sales for a customer in date range
	 */
	public static double  getTotalNonDiscountedSales(Customer cust, Date startDate, Date endDate ) {
 
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		try {
			String hql = "SELECT  sum(amount) FROM TruckSales trcksls " + 
					"where trcksls.customer=:c  and trcksls.sellingDate >=:start and " +
	 				"trcksls.sellingDate <=:end and isDiscounted=false and "  +
					" isDebitPurchase=false";		    
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
	 * @param truck
	 * @return list of trucks entry (line items) for a particular truck
	 */
	public static List  getDisctinctFruitAndQuality(Truck truck) {

		if (truck==null){
			return new ArrayList <TruckEntry> () ;
		}
		System.out.println ("getDisctinctFruitAndQuality " + truck); 
		Session session = HibernateUtil.getSessionFactory().openSession();

		try {
			String hql = "Select distinct t.fruit, t.fruitQuality from TruckSales t where t.itemsFromTruck=:trck";
			Query query = session.createQuery(hql).setParameter("trck", truck);
			query.setCacheable(false);
			List    listProducts = query.list();			
			return listProducts;
		} 
		finally {
			if(session!=null&&session.isOpen()){
				session.close();
			}
		}
	}
	
	/**
	 * 
	 * @param date
	 * @return list of  distinct customers on a selected date
	 */
	public static List <Customer>  getDisctinctCustomers(Date date) {

	 
		System.out.println ("getDisctinctCustomer " + date); 
		Session session = HibernateUtil.getSessionFactory().openSession();

		try {
			String hql = "Select distinct t.customer  from TruckSales t where t.sellingDate =:date";
			Query query = session.createQuery(hql).setParameter("date", date);
			query.setCacheable(false);
			List    listProducts = query.list();			
			return listProducts;
		}
		
		catch (HibernateException e) {
			e.printStackTrace();
			return new ArrayList <Customer> () ; 
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
	 * @param truck
	 * @return list of Truck Sale Detail for a particular fruit and Quality of 
	 * a specific truck
	 */
	public static List <TruckSales> getTruckSaleList(Truck truck, Fruit frt, FruitQuality fQlty) {

		if (truck==null){
			return new ArrayList <TruckSales> () ;
		}
		System.out.println ("getTruckEntryList " + truck); 
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		try {
			String hql = "from TruckSales where fruit=:f and fruitQuality=:q and itemsFromTruck=:trck";
			Query query = session.createQuery(hql).setParameter("f", frt);
			query.setParameter("q", fQlty);
			query.setParameter("trck", truck);
			query.setCacheable(false);
			List  <TruckSales> listProducts = query.list();
			return listProducts;
		} 
		finally {
			if(session!=null&&session.isOpen()){
				session.flush();
				session.close();
			}
		}
	}
	/**
	 * Add a new truck sale entry to persistence
	 * @param truckSale
	 */
	public static boolean addNewEntry(TruckSales truckSale) {
		synchronized (truckSale) {
			Session session = HibernateUtil.getSessionFactory().openSession();
			TruckEntry tr = truckSale.getTruckEntry();	
			try {
				Serializable truckEntryId = tr.getId();
				TruckEntry persitnatTruckEntry = session.load (TruckEntry.class, truckEntryId);
				synchronized (persitnatTruckEntry) {
					session.beginTransaction();	
					long quantityInPurchaseUnit = Utility.getValue( truckSale.getQuantityInPurchaseUnit());
					long previousBalance = tr.getRemainingQuantity();
					long finalBalance = previousBalance -quantityInPurchaseUnit;
					if (finalBalance<0) {
						throw new Exception ("item balance can not be negative");
					}
					persitnatTruckEntry.setRemainingQuantity(finalBalance);
					session.save (persitnatTruckEntry);
					long truckSaleId = (Long) session.save(truckSale);
					commitTruckSaleItem (truckSale, session) ;
					
					session.getTransaction().commit();
				}
				return true;
			}
			catch (Exception ex)
			{   		
				System.out.println(ex.getMessage());
				ex.printStackTrace();
				if (session.getTransaction()!=null) session.getTransaction().rollback();
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

	/**
	 * 
	 * @param truckSaleId
	 * @return
	 */
	public static  boolean  commitTruckSaleItem(TruckSales truckSaleEntry, Session session) {

		System.out.println ("commitTruckSaleItem " + truckSaleEntry); 
	 	try {
		 	if  (Utility.getValue (truckSaleEntry.getDataCommited())) {
				System.out.println ("Can not commit committed data again");
				return false;				
			}
			//  debit data is not commited
			if  (Utility.getValue (truckSaleEntry.getIsDebitPurchase())) {
				System.out.println ("Can not commit Debit Purchase");
				return false;				
			}
			 	
			Serializable id = truckSaleEntry.getCustomer().getId();
			Customer customer = (Customer) session.load(Customer.class, id);


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
				System.out.println ("Upadted Ledger  Sucessfully");	
				return true;

			}


		} 
		catch (HibernateException e) {
			e.printStackTrace();			 
			throw new HibernateException (" Upadte Ledger Failed");			
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

		float currenPurchase = truckSaleEntry.getAmount();

		// Probably This is not required, as debit entry will not
		// upadate customerledger
		boolean isDebit = truckSaleEntry.getIsDebitPurchase();
		boolean  isDiscounted = Utility.getValue (truckSaleEntry.getIsDiscounted());
		String currentParticular = truckSaleEntry.getFruit().getShortName() +
				": "  + truckSaleEntry.getQuantityInPurchaseUnit() + " "+ truckSaleEntry.getPurchaseUnit() ;


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
		finalBalance.setCashDue(cashFinalDue +  currenPurchase);
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
	 * Modify rate, quantity and amount of an existing truck sale entry
	 * @param truckSaleId
	 * @param rate
	 * @param quantity
	 * @param normalAmount
	 */

	public static boolean saveModifiedQuantityRateAmount(long truckSaleId,
			float quantity, float rate,  float normalAmount) {

		Session session = HibernateUtil.getSessionFactory().openSession();;
		try {
			session.beginTransaction();
			Serializable id =  truckSaleId;
			Object persistentInstance = session.load(TruckSales.class, id);
			if (persistentInstance != null)  {
				TruckSales tr = (TruckSales) persistentInstance;
				tr.setAmount(normalAmount);
				tr.setQuantity(quantity);
				tr.setRate(rate);
				session.save(tr);
				session.getTransaction().commit();
				System.out.println ("Modified Sucessfully");	
				return true;
			}
			else {
				System.out.println ("Did not find the Truck Sale Object to modify rate, qty, amt");
				return false;
			}

		} 
		catch (HibernateException e) {
			if (session.getTransaction()!=null) session.getTransaction().rollback();
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
	 * @param truck
	 * @return list of Truck Sale Detail for a particular customer
	 * that will be required for bill print
	 * a specific truck
	 */
	public static List <TruckSales> getCustomerWiseSaleList(Customer customer, Date onDate) {

		if (customer==null || onDate ==null){
			return new ArrayList <TruckSales> () ;
		}
		System.out.println ("getCustomerWiseSaleList " + customer); 
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		try {
			String hql = "from TruckSales t where t.customer=:cust and t.sellingDate=:dt";
			Query query = session.createQuery(hql).setParameter("cust", customer);
			query.setParameter("dt", onDate);	
			query.setCacheable(false);
			List  <TruckSales> listProducts = query.list();
			return listProducts;
		} 
		catch (Exception ex)
		{   		
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			return new ArrayList <TruckSales> () ;
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
	 * @param truck
	 * @return list of Truck Sale Detail for a particular customer
	 * that will be required for bill print
	 * a specific truck
	 */
	public static List <TruckSales> getCashPurchaseReport( Date onDate) {

		if ( onDate ==null){
			return new ArrayList <TruckSales> () ;
		}
		System.out.println ("getCashPurchaseReport " + onDate); 
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		try {
			String hql = "from TruckSales t where   t.sellingDate=:dt and isDebitPurchase =true";
			Query query = session.createQuery(hql).setParameter("dt", onDate);	
			query.setCacheable(false);
			List  <TruckSales> listProducts = query.list();
			return listProducts;
		} 
		catch (Exception ex)
		{   		
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			return new ArrayList <TruckSales> () ;
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
	 * @param truck
	 * @return status
	 * that will be required for bill print
	 * a specific truck
	 */
	public static Double recalculateTruckSales( Truck truck) {

		 
		System.out.println ("recalculateTruckSales " + truck); 
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		try {
			String hql = "select sum(amount) from TruckSales trs where   trs.itemsFromTruck=:t";
			Query query = session.createQuery(hql).setParameter("t", truck );			 		
			List  <Double> listProducts = query.list();
			return listProducts.get(0);
		} 
		catch (Exception ex)
		{   		
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			return 0d;
		}
		
		finally {
			if(session!=null&&session.isOpen()){
				session.flush ();
				session.close();
			}
		}
	}
	
	/**
	 * 
	 * @param itemTrucKSaleId
	 * @return TruckSale from persistence
	 */
	
	public static TruckSales getTrckSaleEntries (Long itemTrucKSaleId) {
		System.out.println ("Get persitent TruckSales " + itemTrucKSaleId); 
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		try {
			String hql = "from TruckSales where id=:ID";
			Query query = session.createQuery(hql).setParameter("ID", itemTrucKSaleId);			 
			query.setCacheable(false);
			List  <TruckSales> listProducts = query.list();
			return listProducts.get(0);
		} 
		finally {
			if(session!=null&&session.isOpen()){
				session.flush();
				session.close();
			}
		}

	 
	}

}



