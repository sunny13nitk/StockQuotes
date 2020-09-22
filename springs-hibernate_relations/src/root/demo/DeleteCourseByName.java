package root.demo;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import root.POJOs.Course;
import root.POJOs.Instructor;
import root.POJOs.Reviews;
import root.POJOs.Student;
import root.POJOs.StudentGeneral;

/**
 * This should not delete the Instructor as Delete is not Cascaded
 */
public class DeleteCourseByName
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
			
			String courseToBeDeleted = "%Economics";
			// Begin Transaction from Session
			
			sess.beginTransaction();
			
			// Do action as per Transaction - Design query to get Course Entity by CourseName
			
			Query courseQ = sess.createQuery("from Course where courseName LIKE :courseToBeDeleted");
			courseQ.setParameter("courseToBeDeleted", courseToBeDeleted);
			
			Course selCourse = (Course) courseQ.getSingleResult();
			if (selCourse != null)
			{
				sess.delete(selCourse); // Course Deletion should not trigger Instructor Deletion
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
