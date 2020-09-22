package root.POJOS;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import root.Interfaces.ICoach;
import root.Interfaces.IFortuneService;

@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_SINGLETON)
@PropertySource("classpath:properties/sport.properties")
public class BasketBallCoach implements ICoach 
{
	//Dependency Bean
	@Autowired(required= false)
	private IFortuneService fortuneSrv;
	
	@Value("${foo.email}")
	private String email;
	
	@Value("${foo.team}")
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
