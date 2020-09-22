package modelframework.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import modelframework.definitions.EntityMetadata;
import modelframework.implementations.DependantObject;
import modelframework.implementations.MessagesFormatter;
import modelframework.implementations.RootObject;

/**
 * 
 * Aspect to Gets Key(s) - Primary/Foreign for Objects Root and Dependant as these are not the part of Objects POJOS
 * itself specfically in the case of auto generated Keys
 *
 */
@Component
@Aspect
public class KeyGetterAspect
{
	@Autowired
	private MessagesFormatter msgFormatter;

	/**
	 * --------------------------------------------------------------------------
	 * Around Advice to retrive the Integer Primary key for an Dependant Object
	 * 
	 * @param pjp
	 *             - Root or Dependant Object
	 * @return - Integer Primary Keyy
	 *         ---------------------------------------------------------------------------
	 */
	@Around("target_RootDependant() && getPrimaryKey()")
	public int getPrimaryKey_Int(ProceedingJoinPoint pjp)
	{

		int pKey = 0;
		if (pjp != null)
		{
			if (pjp.getTarget() instanceof DependantObject)
			{
				DependantObject depObj = (DependantObject) pjp.getTarget();
				if (depObj != null)
				{
					RootObject parentRoot = depObj.getParentRoot();
					if (parentRoot != null)
					{
						if (parentRoot.getEntityManager() != null)
						{
							EntityMetadata entMdt = parentRoot.getEntityManager().getDependantMetadata(depObj);
							if (entMdt != null)
							{
								if (entMdt.getPrimaryKey() != null)
								{
									if (entMdt.getPrimaryKey().getValue() != null)
									{
										pKey = (int) entMdt.getPrimaryKey().getValue();
									}
								}
							}
						}
					}
				}
			}
		}

		return pKey;

	}

	/********************************************************************************************************************
	 **************************************** POINTCUT DEFINITIONS SECTION ********************************************
	 ********************************************************************************************************************/

	@Pointcut("target(modelframework.implementations.RootObject) || target(modelframework.implementations.DependantObject)  )")
	public void target_RootDependant()
	{

	}

	@Pointcut("execution( public int *.getPrimaryKey_Int())")
	public void getPrimaryKey()
	{

	}
}
