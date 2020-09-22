package springsdemo1;

public class AppMain
{
	
	public static void main(
	        String[] args
	)
	{
		
		String moneySyr = UtilDecimaltoMoneyString.getMoneyStringforDecimal(54237634.89, 2);
		System.out.println(moneySyr);
		
		/*
		 * BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); String encodedPassword =
		 * passwordEncoder.encode("test123"); System.out.println(encodedPassword);
		 */
		
		/*
		 * //Instantiate the Application Context Container- Load the Bean Config(XML) File
		 * ClassPathXmlApplicationContext appCtxt = new ClassPathXmlApplicationContext("appCtxt.xml"); //Retrieve the
		 * Bean from Container if(appCtxt != null) { So basically w.o touching the code, just configure the right beans
		 * in Config XML to trigger appropriate bean specific Coach methods ICoach myCoach =
		 * appCtxt.getBean(ICoach.class); if(myCoach != null) { //Call Methods on the Bean
		 * System.out.println(myCoach.getDailyWorkout()); System.out.println(myCoach.getDailyFortune());
		 * System.out.println(myCoach.getTeam()); System.out.println(myCoach.getEmail()); } //Close the Context
		 * Container appCtxt.close(); }
		 */
		
	}
	
}
