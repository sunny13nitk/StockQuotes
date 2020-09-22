package root.demo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import root.POJOs.Course;
import root.POJOs.Instructor;
import root.POJOs.Reviews;
import root.POJOs.Student;
import root.POJOs.StudentGeneral;

public class CreateCourse_Reviews
{
	
	public static void main(
	        String[] args
	)
	{
		// Create Session Factory
		SessionFactory sFac = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Reviews.class)
		        .addAnnotatedClass(Course.class).addAnnotatedClass(Instructor.class).addAnnotatedClass(Student.class)
		        .addAnnotatedClass(StudentGeneral.class).buildSessionFactory();
		
		// Get Session Instance from Factory
		
		Session sess = sFac.getCurrentSession();
		
		try
		{
			
			// Begin Transaction from Session
			
			sess.beginTransaction();
			
			int courseId = 2;
			
			// Do action as per Transaction - Save Here
			Course course = sess.get(Course.class, courseId);
			if (course != null)
			{
				Reviews Review1 = new Reviews("Nice Course on Promoters Integrity");
				Reviews Review2 = new Reviews("Nice Course on Cash Flow Analysis");
				course.addReview(Review1);
				course.addReview(Review2);
				// sess.save(Review1);
				// sess.save(Review2);
				sess.save(course);
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
