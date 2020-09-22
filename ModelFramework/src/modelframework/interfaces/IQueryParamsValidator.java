package modelframework.interfaces;

import java.util.ArrayList;

import modelframework.exceptions.EX_NullParamsException;
import modelframework.exceptions.EX_ParamCountMismatchException;
import modelframework.exceptions.EX_ParamInitializationException;
import modelframework.types.TY_NameValue;

public interface IQueryParamsValidator
{

	/**
	 * Initialize by passing Object Name - String
	 * 
	 * @param objName
	 *             - Object Name- String
	 */
	public void Initialize(String objName);

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
	public void validateQuery(String whereCondition, ArrayList<TY_NameValue> params)
	          throws EX_ParamCountMismatchException, EX_ParamInitializationException, EX_NullParamsException;

	/**
	 * Evaluate Null Parameters in where Clause for any Placeholder
	 * 
	 * @throws EX_NullParamsException
	 */
	public void evalnullParams(String whereCondn) throws EX_NullParamsException;

	/**
	 * Evaluate Number of Parameters in where Clause for Placeholders
	 * 
	 * @return paramcount- used for Param error in case param count differs
	 *         from placeholder count
	 */
	public int evalcountParams(String whereCondn);

	/**
	 * Evaluate Null Parameters in Parameter Name Value Pair List for any null
	 * Name/Value
	 * 
	 * @param params
	 *             - Name Value Pair ArrayList
	 * @throws EX_ParamInitializationException
	 * 
	 */
	public void evalParams(ArrayList<TY_NameValue> params) throws EX_ParamInitializationException;
}
