package root.demo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import root.POJOs.Course;
import root.POJOs.Instructor;
import root.POJOs.Reviews;
import root.POJOs.Student;
import root.POJOs.StudentGeneral;

public class AddCoursesforStudent
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
			
			int courseId = 5;
			int stId1    = 5;
			// int stId2 = 6;
			
			// Do action as per Transaction - Save Here
			Course course = sess.get(Course.class, courseId);
			if (course != null)
			{
				Student std1 = sess.get(Student.class, stId1);
				// Student std2 = sess.get(Student.class, stId2);
				
				course.addStudent(std1);
				// course.addStudent(std2);
				
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
