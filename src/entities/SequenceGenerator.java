package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.Session;

import persistence.HibernateUtil;

@Entity
@Table(name="SEQUENCE_GENERATOR")
public class SequenceGenerator {
 		
		@Id
		@GeneratedValue
		@Column(name="ID")
		private Integer id;

		public  static void  main (String [] x) {
			Session session = HibernateUtil.getSessionFactory().openSession();
			try {
				session.beginTransaction(); 
				SequenceGenerator tn = new SequenceGenerator ();
				session.save(tn);
				System.out.println( "ID = " + tn.getId());	
				session.getTransaction().commit();
			}
			finally {
				if(session!=null && session.isOpen()){
					session.close();
				}
			}
		}

		public Integer getId() {
			return id;
		}
}
