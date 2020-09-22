package root.demo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import root.POJOs.Student;

public class UpdateStudent
{
	
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
			long stID = 3;
			// Begin Transaction from Session
			
			sess.beginTransaction();
			
			// Do action as per Transaction - Get by ID Here
			
			Student myStudent = sess.get(Student.class, stID);
			if (myStudent != null)
			{
				myStudent.setFirstName("Eena");
				myStudent.setLastName("Salhotra");
				myStudent.setEmail("eena.salhotra@gmail.com");
				
			}
			
			// Commit the Txn
			sess.getTransaction().commit();
			
		} finally // Close the Session Factory
		{
			sFac.close();
		}
		
	}
	
}
