package springsdemo1.POJOS;

import org.springframework.beans.factory.annotation.Autowired;

import springsdemo1.Interfaces.ICoach;
import springsdemo1.Interfaces.IFortuneService;

public class CricketCoach implements ICoach 
{
	//Dependency - Dependent Bean

	private IFortuneService fortuneSrv;
	
	
	private String email;
	
	private String team;
	
	
	public CricketCoach(IFortuneService fortuneSrv) 
	{
		this.fortuneSrv = fortuneSrv;
	}




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
