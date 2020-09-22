package springsdemo1.POJOS;


import springsdemo1.Interfaces.IFortuneService;

public class HappyFortuneSrv implements IFortuneService
{

	@Override
	public String getFortune() 
	{
		
		return "Today is a Good Day!";
	}

}
