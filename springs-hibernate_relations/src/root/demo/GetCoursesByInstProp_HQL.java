package root.demo;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import root.POJOs.Course;
import root.POJOs.Instructor;

public class GetCoursesByInstProp_HQL
{
	
	public static void main(
	        String[] args
	)
	{
		// Create Session Factory
		SessionFactory sFac = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Instructor.class)
		        .addAnnotatedClass(Course.class).buildSessionFactory();
		
		String instNameP = "%Malik"; // Let's try and Get Prof. Bakshi'scourses
		
		// Get Session Instance from Factory
		
		Session sess = sFac.getCurrentSession();
		
		try
		{
			
			// Begin Transaction from Session
			
			sess.beginTransaction();
			
			org.hibernate.query.Query<Course> couQ = sess.createQuery(
			        "select c from Course c where c.instructor.fullName LIKE : instNameP", Course.class);
			if (couQ != null)
			{
				couQ.setParameter("instNameP", instNameP);
				List<Course> courses = couQ.getResultList();
				for (Course course : courses)
				{
					System.out.println(course);
				}
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
