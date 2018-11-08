package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Index;

import nmfc.helper.ToastMessage;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.Type;

import persistence.HibernateUtil;

@Entity
@Table(name="TRUCK", uniqueConstraints={@UniqueConstraint (columnNames ={"TRUCK_NUMBER", "TRUCK_RECEIVE_DATE"})}
, indexes = {@Index(name = "truck_index_receivedate",  columnList="TRUCK_RECEIVE_DATE"),
		@Index(name = "truc_number_index", columnList="TRUCK_NUMBER"),
		@Index(name = "truck_index_entrydate", columnList="TRUCK_ENTRY_DATE")}
		)
// This constraints is different from 
// @Table(name = "TRUCK",  uniqueConstraints = {
// @UniqueConstraint(columnNames = "TRUCK_NUMBER"),
// @UniqueConstraint(columnNames = "TRUCK_RECEIVE_DATE") })
// This constraints means column TRUCK_NUMBER and TRUCK_RECEIVE_DATE 
// combination are unique
public class Truck {


	@Id
	@GeneratedValue 
	@Column(name="TRUCK_ID")
	private Integer id;


	@Column(name = "TRUCK_NUMBER")
	private String name;


	@Column(name="TRUCK_ENTRY_DATE")
	@Type(type="date")
	private Date entryDate;

	@Column(name="TRUCK_RECEIVE_DATE")
	@Type(type="date")
	private Date receiveDate;


	// If there is no franchise
	@Column(name="TRUCKADDRESS")
	private String truckAddress;



	@Column(name = "IS_SOLD_OUT")
	private boolean soldOut = false;


	public boolean isSoldOut() {
		return soldOut;
	}

	public void setSoldOut(boolean soldOut) {
		this.soldOut = soldOut;
	}

	public String getTruckAddress() {
		return truckAddress;
	}

	public void setTruckAddress(String truckAddress) {
		this.truckAddress = truckAddress;
	}

	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	@ManyToOne
	@JoinColumn(name="FRANCHISE_ID")
	private Franchise franchise;


	@Embedded
	@AttributeOverrides({@AttributeOverride(name="addressLine1", column=@Column(name="USER_ADDRESS_LINE_1")),
		@AttributeOverride(name="addressLine2", column=@Column(name="USER_ADDRESS_LINE_2"))})
	private Address address;

	public void setAddress(Address address) {
		this.address = address;
	}

	public Address getAddress() {
		return address;
	}

	public Franchise getFranchise() {
		return franchise;
	}

	public void setFranchise(Franchise franchise) {
		this.franchise = franchise;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getEntryDate() {
		return entryDate;
	}


	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}


	public void setId(Integer id) {
		this.id = id;
	}

	public String toString() {
		return name;
	}



	public Integer getId() {
		return id;
	}

	/*
	 * Return truck list based on recieve date of truck 
	 */

	public static List <Truck> getTruckNames(Date date) {

		if (date==null){
			return new ArrayList () ;
		}

		System.out.println ("getTruckNames " + date); 
		Session session = HibernateUtil.getSessionFactory().openSession();

		try {
			String hql = "from Truck where receiveDate=:e";
			Query query = session.createQuery(hql).setParameter("e", date);
			List<Truck> listProducts = query.list();
			return listProducts;
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ArrayList <Truck>();

		}
		finally {
			if(session!=null&&session.isOpen()){
				session.close();
			}
		}
	} 

	/*
	 * Return truck list based on from-recieve date 
	 * and t0-receive-date of truck 
	 */

	public static List getTruckNames(Date fromDate, Date toDate) {

		if ((toDate==null) || (fromDate==null)){
			return new ArrayList () ;
		}

		Session session = HibernateUtil.getSessionFactory().openSession();

		try {
			String hql = "from Truck where  receiveDate BETWEEN :fDate and :tDate order by receiveDate ";
			Query query = session.createQuery(hql);
			query.setDate("fDate",fromDate);
			query.setDate("tDate",toDate);
			List<Truck> listProducts = query.list();
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
	 * @param fromDate
	 * @param toDate
	 * @return
	 */

	public static List <Truck> getSoldOutTruckNames(Date fromDate, Date toDate) {

		if ((toDate==null) || (fromDate==null)){
			return new ArrayList <Truck> () ;
		}

		Session session = HibernateUtil.getSessionFactory().openSession();

		try {
			String hql = "from Truck tr1 where  receiveDate BETWEEN :fDate  and :tDate " +
					 
					" AND  tr1.id NOT IN " +
				     " (Select distinct tn.truck.id  from TruckEntry tn where tn.remainingQuantity != 0 " +
							"and tn.truck.soldOut !=:sl )" + 
							"order by receiveDate " ;
			Query query = session.createQuery(hql);
			query.setDate("fDate",fromDate);
			query.setDate("tDate",toDate);
			query.setParameter ("sl", true);
			List<Truck> listProducts = query.list();
			return listProducts;
		} 
		catch (HibernateException e) {
			e.printStackTrace();
			return new ArrayList () ;
		}
		finally {
			if(session!=null&&session.isOpen()){
				session.close();
			}
		}
	} 


	/**
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param fromReceiveDate
	 * @param toReceiveDate
	 * @return
	 */

	public static List <Truck> getPageWiseTruckList (int pageNumber,  int pageSize, 
			Date fromReceiveDate, Date toReceiveDate) 
	{
		List<Truck> trucks =null;
		Session session = HibernateUtil.getSessionFactory().openSession(); 
		try {
			session.beginTransaction();
			Query query = session.createQuery("from Truck");
			// Page 1 will start from 0. Next page will start from 0 + pagesize
			query.setFirstResult((pageNumber - 1) * pageSize); 
			query.setMaxResults(pageSize);	             
			trucks = (List<Truck>) query.list();

			if (trucks!=null) {
				System.out.println("Total Results:" + trucks.size());
				for (Truck truck : trucks) {
					System.out.println(truck.getId() + " - " + truck.getName());

				}

			}

			session.getTransaction().commit();

		}
		catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();

		}
		return trucks;
	}
	/**
	 * 
	 * @param truckid - Truck id stored in database.  Truck willbe searched 
	 * by this id from persistence and will be deleted if found.
	 * @return
	 */
	public static boolean deleteSelectedTruck(Serializable truckid) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		org.hibernate.Transaction tx  =null;
		try {			
			Object persistentInstance = session.load(Truck.class, truckid);
			if (persistentInstance != null)  {
				tx = session.beginTransaction();
				session.delete(persistentInstance);
				tx.commit();
				return true;
			}
			else {
				System.out.println ("Did not find the Truck Object in persistance");
				return false;
			}

		} 
		catch (Exception e) {
			e.printStackTrace();
			if (tx!=null) tx.rollback();
			return false;
		}

		finally {

			if ( session!=null  && session.isOpen() ) {

				session.isDirty();
				session.clear();
				session.flush();
				session.close();
			}
		}
	}



	public static void main(String[] args) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Date newDate;
		try {

			session.beginTransaction();
			Franchise f = new Franchise();
			f.setCode("TEST0x2 21");
			f.setAdharNo("AD0x2");
			f.setName("TEST FR 4  x 2");
			session.save(f);

			Address addr = new Address ();
			addr.setAddressLine1("Bagnan Station Road");
			addr.setAddressLine2("Bagnan Station Road North Howrah");
			addr.setCity("Bagnan");

			Truck truck = new Truck();
			truck.setName("UP  09 4000");
			truck.setEntryDate(new Date());
			//	truck.setAddress(addr);		
			Date date = new Date();
			truck.setReceiveDate(date );


			newDate = new Date( date.getTime() - 24*5 * 60*60* 1000L);

			Truck truck2 = new Truck();
			truck2.setName("WB  09 4300");
			truck2.setEntryDate(new Date());
			Date date2 = new Date();	
			truck.setEntryDate(new Date());
			truck2.setReceiveDate(date2 );

			Address addr2 = new Address ();
			addr2.setAddressLine1("Bagnan Station Road");
			addr2.setAddressLine2("Bagnan Station Road North");
			addr2.setCity("Bagnan");
			//  truck2.setAddress(addr2);

			//truck.setFranchise(f);
			int truckId = (Integer) session.save(truck);
			session.save(truck2);
			session.getTransaction().commit();
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		finally {
			if(session!=null&&session.isOpen()){
				session.close();
			}
		}
		System.out.println("Listing... ");
		List names = getTruckNames(new Date());
		for (Iterator iterator = names.iterator(); iterator.hasNext();) {
			System.out.println("Listing ");
			Truck name = (Truck) iterator.next();
			System.out.println(name.getName());

		} 
		getPageWiseTruckList (1, 5, null, null);		
		HibernateUtil.getSessionFactory().close();
	}

	public static boolean truckExistisWithSameName(String selectedTruckName,
			Date receiveDate) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String hql = "from Truck where  name =:t and receiveDate=:d";
			Query query = session.createQuery(hql);
			query.setParameter("t", selectedTruckName);
			query.setParameter("d", receiveDate);

			List<Truck> listProducts = query.list();
			if ((listProducts == null) || listProducts.size() == 0) {
				System.out.println("Truck does not Exists");
				return false;
			} 
			else {
				ToastMessage toastMessage = new ToastMessage("Truck Already Exists with same name", 3000);
				toastMessage.setVisible(true);
				return true;
			} 
		}
		catch (HibernateException e) {
			e.printStackTrace();
			return false;
		}


	}

	public static List <Truck> getUnsoldTruckNames() {


		System.out.println ("getUnsoldTruckNames " ); 
		Session session = HibernateUtil.getSessionFactory().openSession();

		try {
			String hql = "Select distinct tn.truck  from TruckEntry tn where tn.remainingQuantity != 0 " +
					"and tn.truck.soldOut !=:sl";

			//	String hql ="select distinct  truck  from  TruckEntry tn  join " +
			//	"Truck tr on tr.id= tn.truck.id  where tn.remainingQuantity !=0" ;



			Query query = session.createQuery(hql);
			query.setParameter("sl", true);
			List<Truck> listProducts = query.list();
			return listProducts;
		} 
		catch (HibernateException e) {
			e.printStackTrace();
			return new ArrayList <Truck> ();
		}
		finally {
			if(session!=null && session.isOpen()){
				session.close();
			}
		}

	}  


}
