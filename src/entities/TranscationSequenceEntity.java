package entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import persistence.HibernateUtil;

@Entity
@Table(name="TRANSCATION_SEQUENCE")
public class TranscationSequenceEntity {
	
	
	@Id
	@GeneratedValue 
	@Column(name="SEQUENCE_ID")
	private Long sequenceNumber;
	
	public Long getSequenceNumber() {
		return sequenceNumber;
	}


	public Long getId() {
		return sequenceNumber;
	}
	
	
	public  static void  main (String [] x) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction(); 
			TranscationSequenceEntity tn = new TranscationSequenceEntity ();
			session.save(tn);
			System.out.println( "ID = " + tn.getId());		 
		}
		finally {
			if(session!=null && session.isOpen()){
				session.close();
			}
		}
	}

}
