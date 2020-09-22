package root;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import root.Config.DemoConfig;
import root.DAO.AccountDAO;
import root.DAO.MemberDAO;

public class MainApp
{
	
	public static void main(
	        String[] args
	)
	{
		// Instantiate the Spring Container from Config Class
		
		AnnotationConfigApplicationContext ctxt = new AnnotationConfigApplicationContext(DemoConfig.class);
		
		if (ctxt != null)
		{
			// Get DEsired Bean
			
			AccountDAO acDAO = ctxt.getBean("accountDAO", AccountDAO.class);
			
			MemberDAO memDAO = ctxt.getBean(MemberDAO.class);
			
			// Invoke business Method
			if (acDAO != null)
			{
				acDAO.addAccount();
			}
			
			if (memDAO != null)
			{
				memDAO.addMember();
			}
			
			// Close the Context Container
			ctxt.close();
		}
	}
	
}
