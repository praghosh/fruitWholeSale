package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.Type;

import persistence.HibernateUtil;

/**
 * 
 * @author Prabir Ghosh
 * 
 * Fruit can be sold or bought in unit like Kilograms, Big Crate, Small Crate,
 * Basket etc
 *
 */



@Entity
@Table(name="HALKHATA", uniqueConstraints={@UniqueConstraint (columnNames ="HALKHATA_NAME")})

public class HalKhata {


	@Id
	@GeneratedValue 
	@Column(name="HALKHATA_ID")
	private Integer id;

	public Integer getId() {
		return id;
	}


	@Column(name="HALKHATA_NAME")
	private String name;

	@Column(name="START_DATE")
	@Type(type="date")
	private Date startDate;

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Date getStartDate() {
		return startDate;
	}


	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	public Date getEndDate() {
		return endDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	@Column(name="END_DATE")
	@Type(type="date")
	private Date endDate;


	public static List <HalKhata> getHalkhataItems() {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			String hql = "from HalKhata";
			Query query = session.createQuery(hql) ;
			query.setCacheable(false); 
			List  <HalKhata> listProducts = query.list();
			return listProducts;			 
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return null;
	 	}

		finally {
			if(session!=null&&session.isOpen()){
				session.flush();
				session.clear();
				session.close();
			}
		}
	}
	
	public String toString() {
		return name;
	}

	// Checks if a HalkhataItems Name already exists.	

	public static boolean halkhataItemsExistWithSameName(String HalkhataName  ) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			String hql = "from HalKhata where  name =" + "'" + HalkhataName + "'";
			Query query = session.createQuery(hql);
			List<HalKhata> listProducts = query.list();
			if ((listProducts == null) || listProducts.size() == 0) {
				System.out.println("HalKhata does not Exists");
				return false;
			} else {
				return true;
			} 
		}
		catch (Exception ex)
		{
			session.getTransaction().rollback();
			ex.printStackTrace();
			return false;
		}

		finally {
			if(session!=null && session.isOpen()){
				session.flush();
				session.clear();
				session.close();
			}
		}

	}
	
	 //  Deletes Selected Container Items
		//   Return true if success
		public static boolean deleteSelectedHalkhataItems (Serializable id )
		
		{   Session session = HibernateUtil.getSessionFactory().openSession(); 
			try {
				
				session.beginTransaction();			 
				Object persistentInstance = session.load(HalKhata.class, id);
				if (persistentInstance != null)  {
					session.delete(persistentInstance);
					session.getTransaction().commit();
					return true;
				}
				else {
					System.out.println ("Did not find the Halkhata Object in persistance");
					return false;
				}
				
			} 
			catch (HibernateException e) {
				session.getTransaction().rollback();
				e.printStackTrace();
				return false;
			}
			
			finally {
				if(session!=null && session.isOpen()){
					session.flush();
					session.clear();
					session.close();
				}
			}
		}

}