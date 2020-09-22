package root.Aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import root.annotations.POJO;

@Aspect
@Component
public class AnalyticsAspect
{
	
	// ADVICES
	
	/**
	 * Advice to Analyze any DAO call to record the elapsed time to execute the DB Operations
	 * 
	 * @param pjp
	 * @throws Throwable
	 */
	@Around(
	    "allDAOMethods()"
	)
	public Object analyseDAOCalls(
	        ProceedingJoinPoint pjp
	) throws Throwable
	{
		Object result = null;
		if (pjp != null)
		{
			String DAOName    = pjp.getTarget().getClass().getSimpleName();
			String methodName = pjp.getSignature().getName();
			System.out.println();
			System.out.println("- Analytics Aspect -");
			System.out.println("DAO Name :" + DAOName);
			System.out.println("Method Name : " + methodName);
			
			POJO pjAnn = pjp.getTarget().getClass().getAnnotation(root.annotations.POJO.class);
			if (pjAnn != null)
			{
				System.out.println("POJO - " + pjAnn.EntityName());
			}
			
			System.out.println("-------Execution Time (milliSecs)--------");
			long before = System.currentTimeMillis();
			result = pjp.proceed();
			long end      = System.currentTimeMillis();
			long duration = end - before;
			System.out.println("Time elapsed: " + duration);
			
		}
		
		return result;
	}
	
	// POINTCUTS
	/*
	 * Any DAO Method containing 0 or any parameters returning anything
	 */
	@Pointcut(
	    "execution(* root.DAO.impl.*.*(..))"
	)
	public void allDAOMethods(
	)
	{
	}
	
}
