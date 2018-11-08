package entities;


import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import persistence.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;




@Entity
@Table(name="FRUIT", uniqueConstraints={@UniqueConstraint (columnNames ="FRUIT_NAME")})

public class Fruit {
	
	@Id
	@GeneratedValue 
	@Column(name="FRUIT_ID")
	private Integer id;

	@Column(name="FRUIT_NAME")
	private String name;
	
	@Column(name="SHORT_NAME")
	private String shortName="N/A"; 
	
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@OneToMany(cascade=CascadeType.ALL, mappedBy="fruit")
    private Set <FruitQuality> fruitQuality = new HashSet <FruitQuality> () ;
 
	public Set<FruitQuality> getFruitQuality() {
		return fruitQuality;
	}

	public void setFruitQuality(Set<FruitQuality> fruitQuality) {
		this.fruitQuality = fruitQuality;
	}

	public Integer getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	public String toString() {
		return name;
	}
	
	public static List getFruitNames() {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			//	session.beginTransaction();
			return session.createCriteria(Fruit.class).list();
		}
		finally {
			if(session!=null&&session.isOpen()){
				session.close();
			}
		}
	}
	
public static void main(String[] args) {
	
 	Session session = HibernateUtil.getSessionFactory().openSession();
	session.beginTransaction();
	/*	Fruit fruit = new Fruit();
	fruit.setName("Mangosteen");			 
	
	
	
	
	FruitQuality fqr = new  FruitQuality();
	fqr.setName("HIMSAGAR");
    fruit.getFruitQuality().add(fqr);
    fqr.setFruit(fruit);
    session.save(fruit);
	session.getTransaction().commit(); */
	
	List names = getFruitNames();
	System.out.println("Printing Fruit Name");
	for (Iterator iterator = names.iterator(); iterator.hasNext();) {
		Fruit name = (Fruit) iterator.next();
		System.out.println("Fruit = " + name.getName());
		//System.out.println("Fruit Quality = " + name.getFruitQuality());
		
	}
	
}

}