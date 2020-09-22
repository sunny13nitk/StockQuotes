package root.POJOS;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import root.Interfaces.ICoach;
import root.Interfaces.IFortuneService;

@Component
@PropertySource("classpath:properties/sport.properties")
public class CricketCoach implements ICoach 
{
	//Dependency - Dependent Bean
	@Autowired
	private IFortuneService fortuneSrv;
	
	@Value("${foo.email}")
	private String email;
	
	@Value("${foo.team}")
	private String team;
		
	
	@Override
	public String getDailyWorkout()
	{
		
		return ("Practise in Nets for 30 Mins.");
	}

	@Override
	public String getDailyFortune()
	{
		// TODO Auto-generated method stub
		return this.fortuneSrv.getFortune();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}



	public IFortuneService getFortuneSrv() {
		return fortuneSrv;
	}



	public void setFortuneSrv(IFortuneService fortuneSrv) {
		this.fortuneSrv = fortuneSrv;
	}
	
	
	
	

}
