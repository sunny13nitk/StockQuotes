/**
 * 
 */
package modelframework.interfaces;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;

import modelframework.exceptions.EX_InvalidObjectException;
import modelframework.exceptions.EX_InvalidRelationException;
import modelframework.exceptions.EX_NotRootException;
import modelframework.exceptions.EX_NullParamsException;
import modelframework.exceptions.EX_ParamCountMismatchException;
import modelframework.exceptions.EX_ParamInitializationException;
import modelframework.types.TY_NameValue;

/**
 * Interface for Query Service
 * Base Methods for Query Definition
 */
public interface IQueryService
{
	/**
	 * Initilaize the Query to Set Table/Object/ObjectInfo etc by passing Root Object Name
	 * 
	 * @param rootobjectName
	 *             - Name of the Root Object
	 * @throws EX_InvalidObjectException
	 * @throws EX_NotRootException
	 * @throws SQLException
	 * 
	 */
	public void Initialize(String rootobjectName) throws EX_InvalidObjectException, EX_NotRootException, SQLException;

	/**
	 * Execute the Query to directly get the extended RootObject Array List
	 * 
	 * @param whereCondition
	 *             - Where Clause of SQL Statement
	 * @param params
	 *             - Parameters ArrayList of type <Name,Value> Pair defined by Type TY_NameValue
	 * @param resultClass
	 *             - The Class of Resultant ArrayList Object that has extended RootObject
	 * @return
	 *         - Casted Array List of resultClass
	 * @throws EX_NullParamsException
	 * @throws EX_ParamCountMismatchException
	 * @throws SQLException
	 * @throws EX_ParamInitializationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public <T> ArrayList<T> executeQuery(String whereCondition, ArrayList<TY_NameValue> params) throws EX_NullParamsException,
	          EX_ParamCountMismatchException, SQLException, EX_ParamInitializationException, IllegalAccessException, IllegalArgumentException,
	          InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException, EX_InvalidRelationException;

	public <T> ArrayList<T> executeQuery() throws EX_NullParamsException, EX_ParamCountMismatchException, SQLException,
	          EX_ParamInitializationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException,
	          NoSuchMethodException, SecurityException, EX_InvalidRelationException;
}
