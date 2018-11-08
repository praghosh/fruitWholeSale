package entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.Session;

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
@Table(name="ITEMS_UNITS", uniqueConstraints={@UniqueConstraint (columnNames ="ITEM_UNIT_NAME")})

public class ItemUnit {


	@Id
	@GeneratedValue 
	@Column(name="ITEMS_UNITS_ID")
	private Integer id;
	
	@Column(name="ITEM_UNIT_NAME")
	private String name;
	
	public static List <ItemUnit> getContainerItemsNames() {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			return session.createCriteria(ItemUnit.class).list();
		}

		finally {
			if(session!=null&&session.isOpen()){
				session.close();
			}
		}
	}
	
}