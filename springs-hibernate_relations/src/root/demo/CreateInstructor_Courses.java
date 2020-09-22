package root.demo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import root.POJOs.Course;
import root.POJOs.Instructor;
import root.POJOs.Reviews;

public class CreateInstructor_Courses
{
	
	public static void main(
	        String[] args
	)
	{
		// Create Session Factory
		SessionFactory sFac = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Instructor.class)
		        .addAnnotatedClass(Course.class).addAnnotatedClass(Reviews.class).buildSessionFactory();
		
		// Get Session Instance from Factory
		
		Session sess = sFac.getCurrentSession();
		
		try
		{
			
			// Begin Transaction from Session
			
			sess.beginTransaction();
			
			// Populate POJOS
			Instructor newInstr = new Instructor("Mohit Raini", "mraina.com");
			
			// Do action as per Transaction - Save Here
			sess.save(newInstr);
			
			// Commit the Txn
			sess.getTransaction().commit();
			
			sess.close();
			
			// Begin Transaction from Session
			
			sess = sFac.getCurrentSession();
			
			sess.beginTransaction();
			
			Instructor instDb = sess.get(Instructor.class, 7);
			if (instDb != null)
			{
				
				Course newCourse1 = new Course("Modelling");
				Course newCourse2 = new Course("Acting");
				instDb.addCourse(newCourse1);
				instDb.addCourse(newCourse2);
				
				// Do action as per Transaction - Save Here
				
				sess.save(newCourse1);
				sess.save(newCourse2);
				
			}
			
			// Commit the Txn
			sess.getTransaction().commit();
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally // Close the Session Factory
		{
			sess.close();
			sFac.close();
		}
		
	}
	
}
