package root.Aspects;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import root.annotations.DBMark;
import root.annotations.POJO;

@Aspect
@Component
public class DBMarkAspect
{
	
	// ADVICE
	
	/*
	 * Register In Log for DB Mark Specific Methods - Read the Annotation Properties to Display the Operation and
	 * Message with appropriate jp args
	 */
	@AfterReturning(
	    "forDBMarkAnnotation()"
	)
	public void registeronDBMark(
	        JoinPoint jp
	)
	{
		if (jp != null)
		{
			/*
			 * Later to be saved in DB with Advice Name, DAO Name, Method Name, Args concatenated and separated by '|'
			 * in String, NumRows
			 */
			String DAOName    = jp.getTarget().getClass().getSimpleName();
			String methodName = jp.getSignature().getName();
			System.out.println();
			System.out.println("- DB Mark ASpect -");
			System.out.println("DAO Name :" + DAOName);
			System.out.println("Method Name : " + methodName);
			
			POJO pjAnn = jp.getTarget().getClass().getAnnotation(root.annotations.POJO.class);
			if (pjAnn != null)
			{
				System.out.println("POJO - " + pjAnn.EntityName());
			}
			
			DBMark annDBMark = (DBMark) this.getAnnotationforJPbyAnnType(jp, root.annotations.DBMark.class);
			if (annDBMark != null)
			{
				System.out.println("DB Operation Type: " + annDBMark.dbOperation().toString());
				
				System.out.println("--DB Operation Message --");
				String msg = new String();
				if (jp.getArgs() != null)
				{
					for (Object arg : jp.getArgs())
					{
						msg += arg.toString();
					}
				}
				
				System.out.println(annDBMark.msg().replace("?", msg));
				
			}
		}
	}
	
	// POINTCUT
	
	/*
	 * For @DBMark Annotation handling
	 */
	@Pointcut(
	    "@annotation(root.annotations.DBMark)"
	)
	public void forDBMarkAnnotation(
	)
	{
		
	}
	
	private Annotation getAnnotationforObjbyAnnType(
	        Object obj, final Class<? extends Annotation> annotation
	)
	{
		Annotation ann = null;
		
		if (obj != null)
		{
			
			Class<?> klass = obj.getClass();
			
			final ArrayList<Method> allMethods = new ArrayList<Method>(Arrays.asList(klass.getDeclaredMethods()));
			for (final Method method : allMethods)
			{
				if (method.isAnnotationPresent(annotation))
				{
					ann = method.getAnnotation(annotation);
					// TODO process annotInstance
					// methods.add(method);
				}
			}
			
		}
		
		return ann;
	}
	
	/*
	 * Get Annotation for a Join or Proceeding JoinPoint for a fully Qualified Annotation Class For Multiple Instances
	 * of same Annotation over various Jp.Target- Class Methods, only the one where out of the signature of target
	 * Methods matches the signature of invoked JP/PJP , the annotation is returned e.g. @DBMark on both save and delete
	 * method in DAO class ; but at runtime if delte method is trigerred the Annotation on Delete Method is only
	 * returned
	 */
	private Annotation getAnnotationforJPbyAnnType(
	        Object jp, final Class<? extends Annotation> annotation
	        )
	{
		Annotation ann = null;
		
		if (jp != null)
		{
			if (jp instanceof JoinPoint || jp instanceof ProceedingJoinPoint)
			{
				JoinPoint jpCast = (JoinPoint) jp; // Join point in Super Interface of PJP so will cast to more generic
				
				Class<?> klass = jpCast.getTarget().getClass();
				
				final ArrayList<Method> allMethods = new ArrayList<Method>(Arrays.asList(klass.getDeclaredMethods()));
				for (final Method method : allMethods)
				{
					if (method.isAnnotationPresent(annotation))
					{
						if (method.getName().equals(jpCast.getSignature().getName()))
						{
							/*
							 * Looking Specifically for Join Point Method if the Class has multiple same Annotations
							 */
							ann = method.getAnnotation(annotation);
						}
						
					}
				}
				
			}
			
		}
		
		return ann;
	}
}
