package root.demo;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import root.POJOs.Student;

public class QueryStudent
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
			
			// Do action as per Transaction - Get by Query
			
			// 01- Get All
			List<Student> studentsAll = sess.createQuery("from Student").getResultList();
			System.out.println("All Students --------");
			displayStudents(studentsAll);
			System.out.println("End of List --------");
			
			// 02 - Get Last Name Bhardwaj
			studentsAll = sess.createQuery("from Student s where s.lastName = 'Bhardwaj'").getResultList();
			System.out.println("All Bhardwajs --------");
			displayStudents(studentsAll);
			System.out.println("End of List --------");
			
			// 02 - Get yahoo email Students
			studentsAll = sess.createQuery("from Student s where s.email LIKE '%yahoo%'").getResultList();
			System.out.println("All Yahoo Email Students --------");
			displayStudents(studentsAll);
			System.out.println("End of List --------");
			
			// Commit the Txn
			sess.getTransaction().commit();
			
		} finally // Close the Session Factory
		{
			sFac.close();
		}
		
	}
	
	public static void displayStudents(
	        List<Student> students
	)
	{
		for (Student student : students)
		{
			System.out.println(student);
		}
	}
	
}
