package entities;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.Query;
import org.hibernate.Session;

import persistence.HibernateUtil;



@Entity
@Table(name="FRANCHISE")
public class Franchise {

	@Id
	@GeneratedValue
	@Column(name="FRANCHISE_ID")
	private Integer id;

	@Column(name="FRANCHISE_NAME")
	private String name;

	@Column(name="FRANCHISE_CODE")
	private String code;

	@Embedded
	@AttributeOverrides({@AttributeOverride(name="addressLine1", column=@Column(name="USER_ADDRESS_LINE_1")),
		@AttributeOverride(name="addressLine2", column=@Column(name="USER_ADDRESS_LINE_2"))})
	private Address address;

	/*	@OneToMany(mappedBy="franchise")
	private Set<Truck> truck;  */


	@Column(name="MOBILE_NUMB")
	private String mobile;

	@Column(name="PHONE_NUMB")
	private String phone;

	@Column(name="ADHAR")
	private String  adharNo;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAdharNo() {
		return adharNo;
	}

	public void setAdharNo(String adharNo) {
		this.adharNo = adharNo;
	}
	public String toString() {
		return name;
	}

	public static void main(String[] args) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		Franchise franchise = new Franchise();
		franchise.setName("Mineral");
		franchise.setCode("ML");
		Address addr = new Address ();
		addr.setAddressLine1("Bagnan Station Road");
		addr.setAddressLine2("Bagnan Station Road North");
		addr.setCity("Bagnan");
		int franchiseId = (Integer) session.save(franchise);		
		session.getTransaction().commit();

		/* 	List names = getFranchiseNames();
		for (Iterator iterator = names.iterator(); iterator.hasNext();) {
			Franchise name = (Franchise) iterator.next();
			System.out.println(name.getName());

		}
		 */
	}

	// Checks if a Franchise Name already exists.	
	// It is called within a hibernate transaction
	public static boolean franchiseExistWithSameName(String franchiseName  ) {
 
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			String hql = "from Franchise where  name =" + "'" + franchiseName + "'";
			Query query = session.createQuery(hql);
			List<Franchise> listProducts = query.list();
			if ((listProducts == null) || listProducts.size() == 0) {
				System.out.println("Franchise does not Exists");
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

	public  static List getFranchiseNames() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			//	session.beginTransaction();
			List franchiseList =  session.createCriteria(Franchise.class).list();
			System.out.println( "Franchise size = " + franchiseList.size());
			return franchiseList;
		}
		finally {
			if(session!=null&&session.isOpen()){
				session.close();
			}
		}

	}



}
