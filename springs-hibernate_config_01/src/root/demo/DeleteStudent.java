package root.demo;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import root.POJOs.Student;

public class DeleteStudent
{
	
	@SuppressWarnings(
	    "unchecked"
	)
	public static void main(
	        String[] args
	)
	{
		// Create Session Factory
		SessionFactory sFac = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Student.class)
		        .buildSessionFactory();
		
		// Get Session Instance from Factory
		
		Session sess = sFac.getCurrentSession();
		
		try
		{
			
			// Begin Transaction from Session
			
			sess.beginTransaction();
			
			String lv_email_domain = "%@yahoo.com";
			
			// Instantiate Query From Session
			Query stbyLname_Q = sess.createQuery(
			        "delete Student  where email  LIKE :lv_email_domain");
			if (stbyLname_Q != null)
			{
				stbyLname_Q.setParameter("lv_email_domain", lv_email_domain);
				stbyLname_Q.executeUpdate();
			}
			
			// Commit the Txn
			sess.getTransaction().commit();
			
		} finally // Close the Session Factory
		{
			sFac.close();
		}
		
	}
	
}
