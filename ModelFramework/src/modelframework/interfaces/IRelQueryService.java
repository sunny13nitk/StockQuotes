package modelframework.interfaces;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import modelframework.JAXB.definitions.objschemas.Dependant_Object_Defn;
import modelframework.exceptions.EX_InvalidRelationException;
import modelframework.exceptions.EX_NullParamsException;
import modelframework.exceptions.EX_ParamCountMismatchException;
import modelframework.exceptions.EX_ParamInitializationException;
import modelframework.implementations.DependantObject;
import modelframework.types.TY_NameValue;

/**
 * 
 * Relations Query Interface to cater to Root/Dependant Realtional Navigation
 *
 */
@Service
public interface IRelQueryService
{
	/**
	 * Initliaze the Relations Query by passing Invoker (Root/Depndant) Object
	 * Instance and Relation Name
	 * 
	 * @param invoker
	 *             - Root/Dependant Object Instance for whhich realtion is
	 *             sought
	 * @param relName
	 *             - StringName of Relation
	 * @throws EX_InvalidRelationException
	 * @throws SQLException
	 */
	public void Initialize(Object invoker, String relName, Boolean filterMode) throws EX_InvalidRelationException, SQLException;

	/**
	 * Initliaze the Relations Query by passing Invoker (Root/Depndant) Object
	 * Instance/Relation Name and Connection to execute al Selects in a single
	 * LUW for performance enhancements
	 * 
	 * @param invoker
	 *             - Root/Dependant Object Instance for whhich realtion is
	 *             sought
	 * @param relName
	 *             - StringName of Relation
	 * @param conn
	 *             - Connection if Already Created to execute in a single LUW
	 */
	public void Initialize(Object invoker, String relName, Connection conn);

	/**
	 * Execute the Query to directly get the filtered extended DependantObject
	 * Array List, Considering Logged in User as per Root UserAwareness
	 * 
	 * @param whereCondition
	 *             - Where Clause of SQL Statement for Relational Query
	 *             filtering Note: Primary Key of Parent Relation is implicitly
	 *             filtered by framework.
	 * @param params
	 *             - Parameters ArrayList of type <Name,Value> Pair defined by
	 *             Type TY_NameValue
	 * @param resultClass
	 *             - The Class of Resultant ArrayList Object that has extended
	 *             DependantObject
	 * @return - Casted Array List of resultClass
	 * @throws SQLException
	 * @throws EX_InvalidRelationException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws EX_NullParamsException
	 * @throws EX_ParamInitializationException
	 * @throws EX_ParamCountMismatchException
	 */
	public <T> ArrayList<T> executeQuery(String whereCondition, ArrayList<TY_NameValue> params) throws SQLException, EX_InvalidRelationException,
	          IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException,
	          SecurityException, EX_ParamCountMismatchException, EX_ParamInitializationException, EX_NullParamsException;

	/**
	 * Execute the Query to directly get the unfiltered extended
	 * DependantObject Array List, Considering Logged in User as per Root
	 * UserAwareness
	 * 
	 * @return - Casted Array List of resultClass
	 * @throws SQLException
	 * @throws EX_InvalidRelationException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public <T> ArrayList<T> executeQuery() throws SQLException, EX_InvalidRelationException, IllegalAccessException, IllegalArgumentException,
	          InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException;

	public Dependant_Object_Defn getRelDetails();

	public Object getPkeyValue();

	public boolean isUserAware();

	public String getDynqpart();

	public ArrayList<TY_NameValue> getParams();

	public Boolean isFilterON();

	public void generateQueryStr();

	public void generateQueryStrfilterMode();

	public void generatePreparedStmt() throws SQLException;

	public void generatePreparedStmtforFilterMode() throws SQLException;

	public void syncronizeProperties(DependantObject depObj, ResultSet resultSet) throws SQLException, IllegalAccessException,
	          IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException;

	public void syncronizePkeyFkey(DependantObject depObj, Object pKey);

	public PreparedStatement getpStmnt();

}
