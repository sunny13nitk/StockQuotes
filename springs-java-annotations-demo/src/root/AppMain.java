package root;



import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import root.Interfaces.ICoach;
import root.POJOS.BasketBallCoach;

public class AppMain
{

	public static void main(String[] args)
	{
		
		//Instantiate the Application Context Container- Load the Bean Config(XML) File
		
	AnnotationConfigApplicationContext appCtxt = 
			new AnnotationConfigApplicationContext(RootConfig.class);
		
		//Retrieve the Bean from Container
		if(appCtxt != null)
		{
			
			ICoach myCoach = appCtxt.getBean(BasketBallCoach.class);
			if(myCoach != null)
			{
				//Call Methods on the Bean
				System.out.println(myCoach.getDailyWorkout());
				System.out.println(myCoach.getDailyFortune());
				System.out.println(myCoach.getTeam());
				System.out.println(myCoach.getEmail());
				
			}
			
			
			
			//Close the Context Container
			appCtxt.close();
		}
		
		
		
		
		
	}

}
