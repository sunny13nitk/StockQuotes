package root.demo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import root.POJOs.Student;
import root.POJOs.StudentGeneral;

public class GetStudentfromStudentGeneral_BiDirectional1_1
{
	
	public static void main(
	        String[] args
	)
	{
		// Create Session Factory
		SessionFactory sFac = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Student.class)
		        .addAnnotatedClass(StudentGeneral.class).buildSessionFactory();
		
		// Get Session Instance from Factory
		
		Session sess = sFac.getCurrentSession();
		
		try
		{
			
			// Begin Transaction from Session
			
			sess.beginTransaction();
			
			// Do action as per Transaction - get Student Detail for pdID =1
			
			StudentGeneral stGen = sess.get(StudentGeneral.class, 1);
			if (stGen != null)
			{
				System.out.println(stGen);
				System.out.println("Parent Student Entity ----");
				System.out.println(stGen.getStudent());
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
