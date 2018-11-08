package entities;

import java.io.Serializable;
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

import persistence.HibernateUtil;

@Entity
@Table(name="CONTAINER_ITEMS", uniqueConstraints={@UniqueConstraint (columnNames ="CONTAINER_NAME")})

public class ContainerItems {


	@Id
	@GeneratedValue 
	@Column(name="CONTAINER_ID")
	private Integer id;

	@Column(name="CONTAINER_NAME", nullable=false)
	private String name;
	
	@Column(name="IS_RETURNABLE")
	private boolean returnable;

	public String toString() {
		return name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static List getContainerItemsNames() {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			return session.createCriteria(ContainerItems.class).list();
		}

		finally {
			if(session!=null&&session.isOpen()){
				session.close();
			}
		}
	}
	
/*	
	public String []  getContainerItemsStrings() {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			List <ContainerItems> itemList= session.createCriteria(ContainerItems.class).list();
			if (itemList!=null) {
				
			}
		}

		finally {
			if(session!=null&&session.isOpen()){
				session.close();
			}
		}
	}
	*/
	
	 //  Deletes Selected Container Items
	//   Return true if success
	public static boolean deleteSelectedContainerItems (Serializable id )
	
	{   Session session = HibernateUtil.getSessionFactory().openSession(); 
		try {
			
			session.beginTransaction();			 
			Object persistentInstance = session.load(ContainerItems.class, id);
			if (persistentInstance != null)  {
				session.delete(persistentInstance);
				session.getTransaction().commit();
				return true;
			}
			else {
				System.out.println ("Did not find the ContainerItems Object in persistance");
				return false;
			}
			
		} 
		catch (HibernateException e) {
			e.printStackTrace();
			return false;
		}
		
		finally {
			if(session!=null && session.isOpen()){
				session.clear();
				session.close();
			}
		}
	}

	public boolean isReturnable() {
		return returnable;
	}

	public void setReturnable(boolean returnable) {
		this.returnable = returnable;
	}

	
	   // Checks if a ContainerItems Name already exists.	
		// It is called within a hibernate transaction
		public static boolean containerItemsExistWithSameName(String containerItemNames  ) {
	 
			Session session = HibernateUtil.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				String hql = "from ContainerItems where  name =" + "'" + containerItemNames + "'";
				Query query = session.createQuery(hql);
				List<Franchise> listProducts = query.list();
				if ((listProducts == null) || listProducts.size() == 0) {
					System.out.println("ContainerItems does not Exists");
					return false;
				} else {
					return true;
				} 
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				return false;
			}

			finally {
				if(session!=null && session.isOpen()){
					session.close();
				}
			}

		}
}

