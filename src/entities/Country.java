package entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import  persistence.HibernateUtil;


import org.hibernate.Session;
import org.hibernate.Transaction;




@Entity
@Table(name="COUNTRY")
public class Country {

	@Id
	@GeneratedValue
	@Column(name="CTRY_ID")
	private Integer id;

	@Column(name="CTRY_NAME")
	private String name;

	private int area;

	@Transient
	private int rank;

	public Integer getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setArea(int area) {
		this.area = area;
	}

	public int getArea() {
		return area;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getRank() {
		return rank;
	}

	public static void main(String[] args) {

		// Save a persistent entity
		// Create a new country called 'Croatia'
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		Country croatia = new Country();
		croatia.setName("Croatia");
		croatia.setArea(21831);
		int croatiaId = (Integer) session.save(croatia);

		session.getTransaction().commit();


		// Get a persistent entity using an identifier
		// Read Croatia in a new unit of work
		 session =
					HibernateUtil.getSessionFactory().openSession();
		//session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		croatia = (Country) session.load(Country.class, croatiaId);
		System.out.println(croatia.getName() + " has been created with an id of " + croatiaId);

		session.getTransaction().commit();
		
		session.beginTransaction();
		Country spain = new Country();
		spain.setName("Spain");
		spain.setArea(234567);
		int spainId = (Integer) session.save(spain);


		Country yugoslavia = new Country();
		yugoslavia.setName("Yugoslavia");
		yugoslavia.setArea(134567);
		int yugoId = (Integer) session.save(yugoslavia);
		
		
		session.getTransaction().commit();

		// Update a persistent entity
		// Modify Spain to correct the name and area
		  session =
				HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		
		//session = HibernateUtil.getSessionFactory().getCurrentSession();
		//session.beginTransaction();

		spain = (Country) session.load(Country.class, spainId);
		System.out.print("Country " + spain.getName() +
				", area " + spain.getArea());

		spain.setName("Espana");
		spain.setArea(195365);
		System.out.println(" has been updated to " + spain.getName() +
				", area " + spain.getArea());

		session.getTransaction().commit();


		// Delete a persistent entity
		// Remove Yugoslavia
		
	 	session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		yugoslavia = (Country) session.load(Country.class, yugoId);
		System.out.println(yugoslavia.getName() + " is about to be deleted");
		session.delete(yugoslavia);

		session.getTransaction().commit();


		HibernateUtil.shutdown();

	}
}