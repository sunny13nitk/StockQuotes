package root.Aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect
{
	
	// ADVICES -------------
	@Before(
	    "addAccount()"
	)
	public void b4AccountAddAdvice(
	        JoinPoint jp
	
	)
	{
		System.out.println("==>> Before Adding an Account Advice Triggered!");
		if (jp != null)
		{
			System.out.println("Join Point Name - " + jp.getTarget());
			
		}
		System.out.println();
	}
	
	@Before(
	    "anyDAOAddMethod()"
	)
	public void b4anyAddDAOAdvice(
	        JoinPoint jp
	
	)
	{
		System.out.println("==>> Before Adding on any DAO method starting with add!");
		if (jp != null)
		{
			System.out.println("Join Point Name - " + jp.getTarget());
		}
		System.out.println();
	}
	
	// POINTCUTS ----------
	
	// PointCut Expression
	@Pointcut(
	    "execution (public void addAccount())"
	)
	// -- Add Account PointCut Signature
	public void addAccount(
	)
	{
	}
	
	// - add Method on any DAO in DAO Package
	@Pointcut(
	    "execution(public * root.DAO.*.add*(..) )"
	)
	public void anyDAOAddMethod(
	)
	{
		
	}
}
