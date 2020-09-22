package root.demo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import root.POJOs.Course;
import root.POJOs.Instructor;

public class GetInstructorCourses_EagerLazyDemo
{
	
	public static void main(
	        String[] args
	)
	{
		// Create Session Factory
		SessionFactory sFac = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Instructor.class)
		        .addAnnotatedClass(Course.class).buildSessionFactory();
		
		int InstId = 1;
		// Get Session Instance from Factory
		
		Session sess = sFac.getCurrentSession();
		
		try
		{
			
			// Begin Transaction from Session
			
			sess.beginTransaction();
			
			Instructor inst = sess.get(Instructor.class, InstId);
			if (inst != null)
			{
				System.out.println(inst);
				// Now get the Courses on Demand by specifying Fetch type as Lazy
				
				System.out.println(inst.getCourses());
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
