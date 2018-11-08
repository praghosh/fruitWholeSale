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

import nmfc.helper.ToastMessage;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.tool.schema.internal.HibernateSchemaManagementTool;

import persistence.HibernateUtil;

/**
 * 
 * @author Prabir Ghosh
 * This entity class creates line items for each truck. 
 *
 */
@Entity
@Table(name="TRUCK_ENTRY", uniqueConstraints={@UniqueConstraint (columnNames ={"TRUCK_ID", "FRUIT_ID",
		"FRUIT_QUALITY_ID", "LOT_NUMBER", "PURCHASE_UNIT" })})
public class TruckEntry {


	@Id
	@GeneratedValue 
	@Column(name="TRUCK_ENTRY_ID")
	private Long id;


	@ManyToOne
	@JoinColumn (name="TRUCK_ID")
	private Truck truck;

	@ManyToOne
	@JoinColumn (name="FRUIT_ID", nullable=false)
	private Fruit fruit;

	@ManyToOne
	@JoinColumn (name="FRUIT_QUALITY_ID")
	private FruitQuality fruitQuality;

	@Column(name="LOT_NUMBER")
	private String lotNumber;

	public String getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}
	@Column(name="PURCHASE_UNIT")
	private String purchaseUnit;

	@Column(name="SALES_UNIT")
	private String salesUnit;

	@Column(name="QUANTITY", nullable=false)
	private Long quantity;

	@Column(name="REMAINING_QUANTITY")
	private Long remainingQuantity;

	public Long getRemainingQuantity() {
		return remainingQuantity;
	}

	public void setRemainingQuantity(Long remainingQuantity) {
		this.remainingQuantity = remainingQuantity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Truck getTruck() {
		return truck;
	}

	public void setTruck(Truck truck) {
		this.truck = truck;
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

	public String getSalesUnit() {
		return salesUnit;
	}

	public void setSalesUnit(String salesUnit) {
		this.salesUnit = salesUnit;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	// save a truck entry in the persitance
	public static boolean saveTruckEntry (TruckEntry truck) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();	
			session.save(truck);
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
	 * fetch  TruckEntry object from the databse by Id
	 * 
	 * @param trckEntryId
	 */

	public static TruckEntry  getTruckEntry (long trckEntryId) {	

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			TruckEntry tr =  (TruckEntry) session.get(TruckEntry.class, trckEntryId);
			return tr;

		} 
		catch (HibernateException e) {
			e.printStackTrace();
			return null;
		}


		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		finally {
			if(session!=null){
				session.flush();
				session.close();
			}
		}
	}



	/**
	 * This method uses truck id to retrieve truck data instead of whole
	 * truck object
	 * @param truckId
	 * @return list of trucks entry (line items) for a particular truck
	 */
	public static List <TruckEntry> getTruckEntryList(int truckId) {

		System.out.println ("getTruckEntryList " + truckId); 
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		try {
			String hql = "from TruckEntry trentry where trentry.truck.id=:e";
			Query query = session.createQuery(hql).setParameter("e", truckId);
			List  <TruckEntry> listProducts = query.list();
			return listProducts;
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println ("getTruckEntryList falied for " + truckId); 
			return new ArrayList <TruckEntry> () ;

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
	public static List <TruckEntry> getTruckEntryList(Truck truck) {

		if (truck==null){
			return new ArrayList <TruckEntry> () ;
		}
		System.out.println ("getTruckEntryList " + truck); 
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		try {
			String hql = "from TruckEntry where truck=:e";
			Query query = session.createQuery(hql).setParameter("e", truck);
			List  <TruckEntry> listProducts = query.list();
			return listProducts;
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println ("getTruckEntryList falied for " + truck); 
			return new ArrayList <TruckEntry> () ;

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
	 * @param itemEntryId - TruckEntry item containing   items (Mainly fruits) recived
	 * through truck .
	 */

	public static boolean  deleteItemEntryRow(Long itemEntryId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {

			session.beginTransaction();
			Serializable id =  itemEntryId;
			Object persistentInstance = session.load(TruckEntry.class, id);
			if (persistentInstance != null)  {
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
			if (session.getTransaction()!=null) session.getTransaction().rollback();
			e.printStackTrace();
			return false;
		}

		finally {
			if(session!=null && session.isOpen()){
				session.flush();
				session.close();
			}
		}

	}
	
	// save a truck entry in the persitance
		public static boolean modifyTruckEntryItemQuantity (Long truckID, Long modifiedQuantity) {

			Session session = HibernateUtil.getSessionFactory().openSession();

			try {
				session = HibernateUtil.getSessionFactory().openSession();
				session.beginTransaction();	
				Serializable id =  truckID;
				TruckEntry truck = session.load(TruckEntry.class, id);				
				truck.setQuantity(modifiedQuantity);
				// Find sum of all sold quantity from truckSales 
				String hql = "select sum(quantityInPurchaseUnit) from TruckSales trs where trs.truckEntry =:trck";				 
				Query query = session.createQuery(hql);
				query.setParameter("trck", truck);
				List listResult = query.list();
				Number number = (Number) listResult.get(0);
				
				long soldQuantity =0;
				if (number!=null) {
					soldQuantity =number.longValue();
				}
 
				truck.setRemainingQuantity(modifiedQuantity - soldQuantity);				
				session.save(truck);
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

	

}
