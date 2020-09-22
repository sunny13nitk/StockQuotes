/**
 * 
 */
package modelframework.interfaces;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import modelframework.definitions.Object_Info;
import modelframework.implementations.RootObject;
import modelframework.types.TY_NameValue;

/**
 * Simple Query Service to fetch Dta from a Root Object Table Only
 *
 */
public interface ISimpleQueryService
{

	public String getRootObjName();

	public void setRootObjName(String rootObjName);

	public String getTableName();

	public void setTableName(String tableName);

	public String getConditionSql();

	public void setConditionSql(String conditionSql);

	public Object_Info getRootObjInfo();

	public String getDynpart();

	public void setRootObjInfo(Object_Info rootObjInfo);

	public void setQueryParams(ArrayList<TY_NameValue> params);

	public ArrayList<TY_NameValue> getQueryParams();

	public PreparedStatement getPrepStmt();

	public void createPreparedStatement() throws SQLException;

	public void createPreparedStatement(Connection conn) throws SQLException;

	public void syncronizeProperties(RootObject rootObj, ResultSet resultSet) throws SQLException, IllegalAccessException, IllegalArgumentException,
	          InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException;

}
