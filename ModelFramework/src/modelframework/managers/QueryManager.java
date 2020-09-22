/**
 * 
 */
package modelframework.managers;

import java.sql.SQLException;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import modelframework.exceptions.EX_InvalidObjectException;
import modelframework.exceptions.EX_NotRootException;
import modelframework.interfaces.IQueryService;
import modelframework.interfaces.ISimpleQueryService;

/**
 * Query Manager Service To get handle to Query Services to retrive data from DB
 * for Root Objects
 */
@Service("QueryManager")
public class QueryManager implements ApplicationContextAware
{

	private static ApplicationContext Context;

	/********************************************************************************************
	 * Get Simple Query Service Bean by Name of Root Object
	 * 
	 * @param RootObjname
	 *             - Name of Root Object
	 * @return - Simple Query Service Bean
	 * @throws EX_InvalidObjectException
	 *              - Object used for Invoking is invalid/not loaded in Model
	 * @throws EX_NotRootException
	 *              - Object used for Invoking is not a Root Object
	 * @throws SQLException
	 *              - Any Sql Exception during Connection Initialization
	 ********************************************************************************************/
	public static ISimpleQueryService getQuerybyRootObjname(String RootObjname) throws EX_InvalidObjectException, EX_NotRootException, SQLException
	{
		IQueryService baseQ = null;
		ISimpleQueryService simpleQ = null;
		if (RootObjname != null && Context != null)
		{
			/**
			 * start with base Query to Initialize
			 */
			baseQ = (IQueryService) Context.getBean("SimpleQueryService");

			baseQ.Initialize(RootObjname);
			// Cast to Simple query before returning
			simpleQ = (ISimpleQueryService) baseQ;

		}

		return simpleQ;
	}

	@Override
	public void setApplicationContext(ApplicationContext ctxt) throws BeansException
	{
		Context = ctxt;

	}

}
