package root.busslogic.utilities;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

public class UtilAnnotation
{
	public static Annotation
	        
	        getAnnotationforObjbyAnnType(
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
					
				}
			}
			
		}
		
		return ann;
	}
	
	/*
	 * Get Annotation for a Joinpoint or Proceeding JoinPoint for a fully Qualified Annotation Class For Multiple
	 * Instances of same Annotation over various Jp.Target- Class Methods, only the one where out of the signature of
	 * target Methods matches the signature of invoked JP/PJP , the annotation is returned e.g. @DBMark on both save and
	 * delete method in DAO class ; but at runtime if delete method is triggered the Annotation on Delete Method is only
	 * returned
	 */
	public static Annotation getAnnotationforJPbyAnnType(
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
