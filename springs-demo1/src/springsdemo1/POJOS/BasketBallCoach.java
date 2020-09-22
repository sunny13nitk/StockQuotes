package springsdemo1.POJOS;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;

import springsdemo1.Interfaces.ICoach;
import springsdemo1.Interfaces.IFortuneService;

public class BasketBallCoach implements ICoach 
{
	//Dependency Bean
	@Autowired
	private IFortuneService fortuneSrv;
	
	private String email;
	
	private String team;
	
	
	@Override
	public String getDailyWorkout() 
	{
		// TODO Auto-generated method stub
		return "Practise Dribbling for 20 min";
	}

	
	public void setFortuneSrv(IFortuneService fortuneSrv)
	{
		System.out.println("Setter Injection for Fortune Service");
		this.fortuneSrv = fortuneSrv;
	}
	@Override
	public String getDailyFortune()
	{
		return this.fortuneSrv.getFortune();
	}

	public BasketBallCoach() 
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public IFortuneService getFortuneSrv() {
		return fortuneSrv;
	}


	
	

	public void setEmail(String email) {
		this.email = email;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	@Override
	public String getTeam() 
	{
		// TODO Auto-generated method stub
		return this.team;
	}

	@Override
	public String getEmail() {
		// TODO Auto-generated method stub
		return this.email;
	}
	
	@PostConstruct
	private void afterBeanInstantation()
	{
		System.out.println("Triggered after Bean Initialization");
		System.out.println(this.toString());
		System.out.println("Verifying properties Injection");
		System.out.println(this.getEmail() + this.getTeam());
		System.out.println("Verifying collaborations Injection");
		System.out.println(this.getDailyFortune());
		System.out.println();
	}
	
	@PreDestroy
	private void beforedestroy()
	{
		System.out.println("Before Bean destruction");
	}
	
	
	
	
}
