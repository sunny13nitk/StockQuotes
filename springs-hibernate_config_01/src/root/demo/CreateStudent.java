package root.demo;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import root.POJOs.Student;

public class CreateStudent
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
			Student newStudent = new Student("Nidhi", "Singh", "nids@yahoo.com");
			
			String dateStr  = "15/01/1988";
			Date   utilDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateStr);
			
			newStudent.setDOB(utilDate);
			
			// Begin Transaction from Session
			
			sess.beginTransaction();
			
			// Do action as per Transaction - Save Here
			
			sess.save(newStudent);
			
			// Commit the Txn
			sess.getTransaction().commit();
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally // Close the Session Factory
		{
			sFac.close();
		}
		
	}
	
}
