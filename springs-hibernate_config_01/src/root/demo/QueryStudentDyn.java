package root.demo;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import root.POJOs.Student;

public class QueryStudentDyn
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
			
			String lv_lastName = "Bhardwaj";
			
			// Do action as per Transaction - Get by Query
			
			// 01- Get by Last Name Parameter
			
			Query stbyLname_Q = sess.createQuery("from Student where upper(lastName) =:lv_lastName", Student.class);
			if (stbyLname_Q != null)
			{
				stbyLname_Q.setParameter("lv_lastName", lv_lastName.toUpperCase());
			}
			
			List<Student> studentsAll = stbyLname_Q.getResultList();
			displayStudents(studentsAll);
			
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
