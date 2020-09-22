package root.demo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import root.POJOs.Student;
import root.POJOs.StudentGeneral;

public class DeleteStudentGeneral_BiDirectional1_1
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
			
			// Do action as per Transaction - get Student Detail for pdID =1 and delete it
			// The main Student Entity should not get deleted as Cascading for delete not enabled on Student General
			// dependant entity
			
			StudentGeneral stGen = sess.get(StudentGeneral.class, 4);
			if (stGen != null)
			{
				/*
				 * Direct Delete would cause an exception since mapped by referred property Root Entity has ALL
				 * Cascading that will retain the Association and will no allow delete, unless the association is
				 * explicitly broken before session txn commit
				 */
				
				/*
				 * Association broken explicitly by setting Dependent Entity as null once parent retrieved
				 */
				stGen.getStudent().setStudentGeneral(null);
				// Now trigger Dependent Delete - W/o an Association there would not be any exception
				
				sess.delete(stGen);
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
