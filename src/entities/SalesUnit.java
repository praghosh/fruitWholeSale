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



@Entity
@Table(name="SALES_UNIT", uniqueConstraints={@UniqueConstraint (columnNames ="UNIT_NAME")})

public class SalesUnit {


	@Id
	@GeneratedValue 
	@Column(name="UNIT_ID")
	private Integer id;

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

	@Column(name="UNIT_NAME")
	private String name;
	
	public String toString() {
		return name;
	}

	public static List <SalesUnit> getSalesUnitNames() {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			//	session.beginTransaction();
			return session.createCriteria(SalesUnit.class).list();
		}

		finally {
			if(session!=null&&session.isOpen()){
				session.close();
			}
		}
	}
	
}
