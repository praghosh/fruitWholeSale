package entities;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

 
@Entity
@Table(name="FRUIT_QUALITY", uniqueConstraints={@UniqueConstraint (columnNames ={"FRUIT_QUALITY_NAME", "FRUIT_ID" })})
public class FruitQuality {
	
	@Id
	@GeneratedValue 
	@Column(name="FRUIT_QUALITY_ID")
	private Integer id;

	@Column(name="FRUIT_QUALITY_NAME")
	private String name="N/A";
	
	@ManyToOne
	@JoinColumn (name="FRUIT_ID")
    private Fruit fruit;
 
	public Fruit getFruit() {
		return fruit;
	}

	public void setFruit(Fruit fruit) {
		this.fruit = fruit;
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
	
	public static List getFruitQualityNames() {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			//	session.beginTransaction();
			return session.createCriteria(FruitQuality.class).list();
		}
		finally {
			if(session!=null&&session.isOpen()){
				session.close();
			}
		}
	}
/**
 *   
 *  @param fruit
 *  @return names of Fruit Quality for a particular fruit
 *  for example for a fruit like Mango Fruit Quality can be 
 *  Himsagar, Langra etc
 */
	public static List getFruitQualityNames(Fruit fruit) {
		if (fruit==null){
			return new ArrayList();
		}

		System.out.println ("getFruitNames  of " + fruit); 
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			String hql = "from FruitQuality where fruit=:e";
			Query query = session.createQuery(hql).setParameter("e", fruit);
			List<Customer> listProducts = query.list();
			return listProducts;
		} 
		finally {
			if(session!=null&&session.isOpen()){
				session.close();
			}
		}
	} 
	
public static void main(String[] args) {
	
/*	Session session = HibernateUtil.getSessionFactory().openSession();
	session.beginTransaction();
	FruitQuality quality = new FruitQuality();
	quality.setName("APPLE");			 
	int fruitQualityId = (Integer) session.save(quality);

 
	session.getTransaction().commit();
	*/
	
	List names = Fruit.getFruitNames();
	System.out.println("Printing Fruit Name");
	for (Iterator iterator = names.iterator(); iterator.hasNext();) {
		Fruit name = (Fruit) iterator.next();
		System.out.println("FruitQuality List = " +  getFruitQualityNames(name) );
		
	}
	
}

}