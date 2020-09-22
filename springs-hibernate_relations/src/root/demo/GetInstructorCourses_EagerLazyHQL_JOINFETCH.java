package root.demo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import root.POJOs.Course;
import root.POJOs.Instructor;

public class GetInstructorCourses_EagerLazyHQL_JOINFETCH
{
	
	public static void main(
	        String[] args
	)
	{
		// Create Session Factory
		SessionFactory sFac = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Instructor.class)
		        .addAnnotatedClass(Course.class).buildSessionFactory();
		
		int        InstId = 1;
		Instructor inst   = null;
		// Get Session Instance from Factory
		
		Session sess = sFac.getCurrentSession();
		
		try
		{
			
			// Begin Transaction from Session
			
			sess.beginTransaction();
			
			Query<Instructor> insQ = sess.createQuery(
			        "" + "select i from Instructor i JOIN FETCH i.courses " + "where i.instrId  =: InstID",
			        Instructor.class);
			if (insQ != null)
			{
				insQ.setParameter("InstID", InstId);
				inst = insQ.getSingleResult();
			}
			
			// Commit the Txn
			sess.getTransaction().commit();
			
			sess.close(); // Closing the Session after GEtting Instructor -Loading courses also in memory
			
			if (inst != null)
			{
				System.out.println(inst);
				// Now get the Courses on Demand even after Session close- Fetch from App Memory
				System.out.println(inst.getCourses());
				
			}
			
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
