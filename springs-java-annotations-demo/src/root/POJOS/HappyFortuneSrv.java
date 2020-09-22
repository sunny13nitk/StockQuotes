package root.POJOS;


import org.springframework.stereotype.Component;

import root.Interfaces.IFortuneService;

@Component
public class HappyFortuneSrv implements IFortuneService
{

	@Override
	public String getFortune() 
	{
		
		return "Today is a Good Day!";
	}

}
