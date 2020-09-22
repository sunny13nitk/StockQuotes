package modelframework.aspects;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

import modelframework.exceptions.EX_InvalidRelationException;
import modelframework.exceptions.EX_NullParamsException;
import modelframework.exceptions.EX_ParamCountMismatchException;
import modelframework.exceptions.EX_ParamInitializationException;
import modelframework.implementations.DependantObject;
import modelframework.implementations.RootObject;
import modelframework.interfaces.IRelQueryService;
import modelframework.managers.RelationsQueryManager;
import modelframework.types.TY_Filter;

/**
 * 
 * Related Entities ASpect - to Get Related Entities using RElation NAme for a
 * Root/Dependant Object Acts around getRelatedEntities by issuing an Advice
 */

@Aspect
@Service
public class RelatedEntitiesAspect
{

	/******************************************************************************
	 * Advice for Normal Get Related Entities method without a condition
	 * addendum
	 * 
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 ******************************************************************************/
	@Around("getrelatedEntities() && args(relationName) && target_RootDependant()")
	public <T> ArrayList<T> getRelatedEntities(ProceedingJoinPoint pjp, String relationName)
	          throws EX_InvalidRelationException, SQLException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
	          InstantiationException, NoSuchMethodException, SecurityException
	{
		ArrayList<T> entities = new ArrayList<T>();

		if (pjp != null && relationName != null)
		{
			/*************************************************************************************************
			 * Root Object
			 *************************************************************************************************/
			if (pjp.getTarget() instanceof RootObject)
			{

				RootObject rootObj = (RootObject) pjp.getTarget();
				if (rootObj != null)
				{
					/**
					 * Get a handle to RelationQueryService by RelationQueryManager
					 * to get the relations data
					 */
					IRelQueryService relQ = RelationsQueryManager.getRelQuerybyRelation(rootObj, relationName);
					if (relQ != null)
					{
						entities = relQ.executeQuery();
					}
				}

			}

			/*************************************************************************************************
			 * Dependant Object
			 *************************************************************************************************/
			else if (pjp.getTarget() instanceof DependantObject)
			{

				DependantObject depObj = (DependantObject) pjp.getTarget();
				if (depObj != null)
				{
					/**
					 * Get a handle to RelationQueryService by RelationQueryManager
					 * to get the relations data
					 */
					IRelQueryService relQ = RelationsQueryManager.getRelQuerybyRelation(depObj, relationName);
					if (relQ != null)
					{
						entities = relQ.executeQuery();
					}
				}

			}
		}

		return entities;
	}

	/**
	 * 
	 * @param pjp
	 * @param relationName
	 * @param filter
	 * @return
	 * @throws EX_InvalidRelationException
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws EX_ParamCountMismatchException
	 * @throws EX_ParamInitializationException
	 * @throws EX_NullParamsException
	 */
	@Around("getRelatedEntitieswithFilter() && args(relationName, filter) && target_RootDependant()")
	public <T> ArrayList<T> getRelatedEntitieswithFilter(ProceedingJoinPoint pjp, String relationName, TY_Filter filter)
	          throws EX_InvalidRelationException, SQLException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
	          InstantiationException, NoSuchMethodException, SecurityException, EX_ParamCountMismatchException, EX_ParamInitializationException,
	          EX_NullParamsException
	{
		ArrayList<T> entities = new ArrayList<T>();

		if (pjp != null && relationName != null && filter != null)
		{
			/*************************************************************************************************
			 * Root Object
			 *************************************************************************************************/
			if (pjp.getTarget() instanceof RootObject)
			{

				RootObject rootObj = (RootObject) pjp.getTarget();
				if (rootObj != null)
				{
					/**
					 * Get a handle to RelationQueryService by RelationQueryManager
					 * to get the relations data
					 */
					IRelQueryService relQ = RelationsQueryManager.getRelQuerybyRelationwithFilter(rootObj, relationName);
					if (relQ != null)
					{
						if (filter.getFilterParams() != null && filter.getWhereCondn() != null)
						{
							entities = relQ.executeQuery(filter.getWhereCondn(), filter.getFilterParams());
						}
					}
				}

			}

			/*************************************************************************************************
			 * Dependant Object
			 *************************************************************************************************/
			else if (pjp.getTarget() instanceof DependantObject)
			{

				DependantObject depObj = (DependantObject) pjp.getTarget();
				if (depObj != null)
				{
					/**
					 * Get a handle to RelationQueryService by RelationQueryManager
					 * to get the relations data
					 */
					IRelQueryService relQ = RelationsQueryManager.getRelQuerybyRelationwithFilter(depObj, relationName);
					if (relQ != null)
					{
						entities = relQ.executeQuery(filter.getWhereCondn(), filter.getFilterParams());
					}
				}

			}
		}

		return entities;
	}

	/********************************************************************************************
	 * POINTCUTS DEFINITIONS
	 ********************************************************************************************/

	/**
	 * PointCut Definition Execution of getRelatedEntities from package
	 * "implementations"
	 */

	@Pointcut("execution(* modelframework.implementations.*.getRelatedEntities(*))")
	public void getrelatedEntities()
	{

	}

	@Pointcut("execution(* modelframework.implementations.*.getRelatedEntitieswithFilter(String, * ))")
	public void getRelatedEntitieswithFilter()
	{

	}

	@Pointcut("target(modelframework.implementations.RootObject) || target(modelframework.implementations.DependantObject)  )")
	public void target_RootDependant()
	{

	}

}
