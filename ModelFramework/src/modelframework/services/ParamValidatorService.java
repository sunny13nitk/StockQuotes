package modelframework.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import modelframework.exceptions.EX_NullParamsException;
import modelframework.exceptions.EX_ParamCountMismatchException;
import modelframework.exceptions.EX_ParamInitializationException;
import modelframework.exposed.FrameworkManager;
import modelframework.interfaces.IQueryParamsValidator;
import modelframework.types.TY_NameValue;

/**
 * 
 * Parameter Validation for
 * Null params in case Query Condition expects parameters
 * Number of Parameters differing in Query Condition and Parameters specified
 * Any Blank/Null vlaue in Parameters Name-Value Pair
 */
@Service("ParamValidatorService")
public class ParamValidatorService implements IQueryParamsValidator
{
	/**
	 * Evaluate Null Parameters in where Clause for any Placeholder
	 * 
	 * @return boolean paramError- Param error in case param should be provided
	 * @throws EX_NullParamsException
	 */

	@Autowired
	private FrameworkManager	frameworkManager;
	private String			objName;

	/**
	 * @return the objName
	 */
	public String getObjName()
	{
		return objName;
	}

	/**
	 * @param objName
	 *             the objName to set
	 */
	public void setObjName(String objName)
	{
		this.objName = objName;
	}

	@Override
	public void Initialize(String objName)
	{
		if (objName != null)
		{
			this.setObjName(objName);
		}

	}

	/*******************************************************************************
	 * Trigger Validation
	 * 
	 * @param whereCondition
	 *             - String Sql Where Clause with parameters as "?"
	 * @param params
	 *             - ArrayList of Name-Value Pair TY_NameValue
	 * @throws EX_ParamCountMismatchException
	 *              - Parameter Count Mismatch between query and wherecondition
	 * @throws EX_ParamInitializationException
	 *              - Parameters specified have null or blank name value pairs
	 * @throws EX_NullParamsException
	 *              - Paramters are null while conditons expects them
	 ********************************************************************************/
	@Override
	public void validateQuery(String whereCondition, ArrayList<TY_NameValue> params)
	          throws EX_ParamCountMismatchException, EX_ParamInitializationException, EX_NullParamsException
	{
		if (whereCondition != null)
		{
			/**
			 * If No Parameters Sepcified and Query has "?" placeholder for
			 * params trigger NulParameterException
			 */
			if ((params != null && params.size() == 0) || (params == null))
			{
				evalnullParams(whereCondition);

			}

			/**
			 * if Parameters Specified compare count of "?" with number of
			 * params if not Matching throw ParamCountMismatchException
			 */
			else
			{
				int numParams;
				if ((numParams = evalcountParams(whereCondition)) != params.size())
				{
					// throw ParamCountMismatchException
					// Cannot Execute Query. Parameters Count Mismatch.
					// Actual paraemters provided
					// are {0} while {1} are defined!
					EX_ParamCountMismatchException excountParam = new EX_ParamCountMismatchException(new Object[]
					{ params.size(), numParams
					});
					frameworkManager.getMessageFormatter().generate_message_snippet(excountParam);
					throw excountParam;
				}
				/**
				 * Check for Blank Parameters/Values if any
				 */
				evalParams(params);

			}
		}

	}

	@Override
	public void evalnullParams(String whereCondn) throws EX_NullParamsException
	{

		if (whereCondn != null)
		{
			if (whereCondn.contains("?"))
			{
				// throw null Param Exception
				EX_NullParamsException exnullParam = new EX_NullParamsException(new Object[]
				{ this.getObjName()
				});
				frameworkManager.getMessageFormatter().generate_message_snippet(exnullParam);
				throw exnullParam;
			}
		}

	}

	/**
	 * Evaluate Number of Parameters in where Clause for Placeholders
	 * 
	 * @return paramcount- used for Param error in case param count differs
	 *         from placeholder count
	 */
	@Override
	public int evalcountParams(String whereCondn)
	{
		int numParams = 0;

		for ( int i = 0; i < whereCondn.length(); i++ )
		{
			if (whereCondn.charAt(i) == '?')
			{
				numParams++;
			}
		}

		return numParams;
	}

	/**
	 * Evaluate Null Parameters in Parameter Name Value Pair List for any null
	 * Name/Value
	 * 
	 * @return boolean paramError- Param error in case param should be provided
	 * @throws EX_ParamInitializationException
	 */
	@Override
	public void evalParams(ArrayList<TY_NameValue> params) throws EX_ParamInitializationException
	{

		if (params != null)
		{
			int idx = 1;
			for ( TY_NameValue ty_NameValue : params )
			{
				if (ty_NameValue.Value == null || ty_NameValue.Name == null)
				{

					// throw ERR_PARAMBLANK= Parameters initilization
					// error!! Parameters at Index
					// {0} for Query for Object{1} has
					// null/blank Name/Value Pair!
					EX_ParamInitializationException exiniParam = new EX_ParamInitializationException(new Object[]
					{ idx, this.getObjName()
					});
					frameworkManager.getMessageFormatter().generate_message_snippet(exiniParam);
					throw exiniParam;
				}
				else
				{
					if (ty_NameValue.Name.length() == 0)
					{
						// throw ERR_PARAMBLANK= Parameters initilization
						// error!! Parameters at
						// Index
						// {0} for Query for Object{1} has
						// null/blank Name/Value Pair!
						EX_ParamInitializationException exiniParam = new EX_ParamInitializationException(new Object[]
						{ idx, this.getObjName()
						});
						frameworkManager.getMessageFormatter().generate_message_snippet(exiniParam);
						throw exiniParam;
					}
				}
				idx++;
			}

		}

	}

}
