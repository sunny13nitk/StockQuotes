/**
 * 
 */
package modelframework.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import modelframework.cache.CacheQueryManager;
import modelframework.definitions.EntityManager;
import modelframework.definitions.KeyEntity;
import modelframework.definitions.Object_Info;
import modelframework.definitions.PrimaryKey;
import modelframework.exceptions.EX_InvalidObjectException;
import modelframework.exceptions.EX_NotRootException;
import modelframework.exceptions.EX_NullParamsException;
import modelframework.exceptions.EX_ParamCountMismatchException;
import modelframework.exceptions.EX_ParamInitializationException;
import modelframework.exposed.FrameworkManager;
import modelframework.implementations.RootObject;
import modelframework.interfaces.IEnumable;
import modelframework.interfaces.IQueryParamsValidator;
import modelframework.interfaces.IQueryService;
import modelframework.interfaces.ISimpleQueryService;
import modelframework.managers.ModelObjectFactory;
import modelframework.managers.ParamValidationManager;
import modelframework.types.TY_NameValue;
import modelframework.utilities.CglibHelper;
import modelframework.utilities.EnumGetBack;
import modelframework.utilities.PropertiesMapper;

/**
 * Simple Query Service Initlaized by the Name of the Root Object
 */
@Service("SimpleQueryService")
public class SimpleQueryService implements IQueryService, ISimpleQueryService
{
	@Autowired
	private FrameworkManager			frameworkManager;
	@Autowired
	private EntityManager			entManager;
	@Autowired
	private PropertiesMapper			propertiesMapper;
	private String					rootObjName;
	private String					tableName;
	private Object_Info				rootObjInfo;
	private String					dynpart;

	private ArrayList<TY_NameValue>	queryParams;
	private String					conditionSql;
	private PreparedStatement		prepStmt;
	private Connection				connection;
	@Autowired
	private ApplicationContext		ctxt;

	/**
	 * @return the dynpart
	 */
	@Override
	public String getDynpart()
	{
		return dynpart;
	}

	/**
	 * @param dynpart
	 *             the dynpart to set
	 */
	public void setDynpart(String dynpart)
	{
		this.dynpart = dynpart;
	}

	/*
	 * Initialilize Query Service
	 */
	@SuppressWarnings("static-access")
	@Override
	public void Initialize(String rootobjectName) throws EX_InvalidObjectException, EX_NotRootException, SQLException
	{
		if (rootobjectName != null && frameworkManager != null)
		{
			// Get Object Info from Model Loaded
			this.setRootObjInfo(frameworkManager.getObjectsInfoFactory().Get_ObjectInfo_byName(rootobjectName));
			if (this.getRootObjInfo() != null)
			{
				// Validate if the Object is a Root Object
				if (this.getRootObjInfo().getRoot_Metadata() != null)
				{
					this.setRootObjName(rootobjectName);

					// Get Table from ObjectInfo Root Metadata
					this.setTableName(this.getRootObjInfo().getRoot_Metadata().getTablename());
					this.conditionSql = "SELECT * FROM " + this.getTableName();
					this.queryParams = new ArrayList<TY_NameValue>();
					if (this.frameworkManager.getConnectionPool() != null)
					{
						this.connection = this.frameworkManager.getConnectionPool().getConnection();
					}
				}
				else
				{
					EX_NotRootException exnoRoot = new EX_NotRootException(new Object[]
					{ rootobjectName
					});
					frameworkManager.getMessageFormatter().generate_message_snippet(exnoRoot);
					throw exnoRoot;
				}

			}
			else
			{
				EX_InvalidObjectException exInvObj = new EX_InvalidObjectException(new Object[]
				{ rootobjectName
				});
				frameworkManager.getMessageFormatter().generate_message_snippet(exInvObj);
				throw exInvObj;

			}
		}

	}

	/**
	 * Execute the Query to directly get the extended RootObject Array List
	 * 
	 * @param whereCondition
	 *             - Where Clause of SQL Statement
	 * @param params
	 *             - Parameters ArrayList of type <Name,Value> Pair defined by
	 *             Type TY_NameValue
	 * @param resultClass
	 *             - The Class of Resultant ArrayList Object that has extended
	 *             RootObject
	 * @return - Casted Array List of resultClass
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
	@SuppressWarnings(
	{ "unchecked", "static-access"
	})
	@Override
	public <T> ArrayList<T> executeQuery(String whereCondition, ArrayList<TY_NameValue> params)
	          throws EX_NullParamsException, EX_ParamCountMismatchException, SQLException, EX_ParamInitializationException, IllegalAccessException,
	          IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException
	{

		ArrayList<T> result = new ArrayList<T>();
		try
		{
			/**
			 * Validate Query for Null and Parameter Count Mismatch
			 */
			if (whereCondition != null && params != null)
			{
				/**
				 * Get Param Validator BEan
				 */

				IQueryParamsValidator paramValidator = ParamValidationManager.getParamValidatorService("ParamValidatorService",
				          this.getRootObjName());
				paramValidator.validateQuery(whereCondition, params);

				this.setConditionSql(" WHERE " + whereCondition);
				this.setQueryParams(params);
				this.setDynpart(whereCondition);
			}

			/**
			 * Utilize Cache to avoid DB selects with same Parameters and where Condition
			 */

			CacheQueryManager cmBean = ctxt.getBean(CacheQueryManager.class);
			if (cmBean != null)
			{
				cmBean.initialize(this);
				ArrayList<KeyEntity<T>> queryResult = cmBean.getResults();

				if (queryResult != null)
				{
					for ( KeyEntity<T> t : queryResult )
					{
						// Create a Root Object Bean for each Row
						RootObject rootObj = (RootObject) ModelObjectFactory.createObjectbyClass(t.getPojoEntity().getClass());
						if (rootObj != null)
						{
							// synchronize Properties in Root object Bean by Pojo entity
							rootObj = propertiesMapper.addPropertiestoRootProxyBean(rootObj, (RootObject) t.getPojoEntity());
							CglibHelper cgHelper = new CglibHelper(rootObj);
							RootObject entrootObj = (RootObject) cgHelper.getTargetObject();
							if (entrootObj != null)
							{
								if (entManager.getRootMetadata(entrootObj) != null)
								{
									// synchronize Pkey Value in Root Object Bean Metadata for Related entities
									// mapping
									entManager.getRootMetadata(entrootObj).getPrimaryKey().setValue(t.getKey());
								}

								result.add((T) rootObj);
							}
						}
					}

				}
			}

		}

		/**
		 * Release all Resources
		 */
		finally
		{
			if (this.prepStmt != null)
			{
				if (this.prepStmt.isClosed() != true)
				{
					this.prepStmt.close();
				}
			}
			this.connection.close();
		}

		return result;
	}

	@SuppressWarnings(
	{ "unchecked", "static-access"
	})
	@Override
	public <T> ArrayList<T> executeQuery()
	          throws EX_NullParamsException, EX_ParamCountMismatchException, SQLException, EX_ParamInitializationException, IllegalAccessException,
	          IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException
	{

		ArrayList<T> result = new ArrayList<T>();
		try
		{

			/**
			 * Utilize Cache to avoid DB selects with same Parameters and where Condition
			 */

			CacheQueryManager cmBean = ctxt.getBean(CacheQueryManager.class);
			if (cmBean != null)
			{
				cmBean.initialize(this);
				ArrayList<KeyEntity<T>> queryResult = cmBean.getResults();

				if (queryResult != null)
				{
					for ( KeyEntity<T> t : queryResult )
					{
						// Create a Root Object Bean for each Row
						RootObject rootObj = (RootObject) ModelObjectFactory.createObjectbyClassDB(t.getPojoEntity().getClass());
						if (rootObj != null)
						{
							// synchronize Properties in Root object Bean by Pojo entity
							rootObj = propertiesMapper.addPropertiestoRootProxyBean(rootObj, (RootObject) t.getPojoEntity());
							CglibHelper cgHelper = new CglibHelper(rootObj);
							RootObject entrootObj = (RootObject) cgHelper.getTargetObject();
							if (entrootObj != null)
							{
								if (entManager.getRootMetadata(entrootObj) != null)
								{
									// synchronize Pkey Value in Root Object Bean Metadata for Related entities
									// mapping
									entManager.getRootMetadata(entrootObj).getPrimaryKey().setValue(t.getKey());
								}

								result.add((T) rootObj);
							}
						}
					}

				}
			}

		}

		/**
		 * Release all Resources
		 */
		finally
		{
			if (this.prepStmt != null)
			{
				if (this.prepStmt.isClosed() != true)
				{
					this.prepStmt.close();
				}
			}
			this.connection.close();
		}

		return result;
	}

	/**
	 * Syncronize Root Object Attributes from REsultSet using Setters in Object
	 * Info
	 * 
	 * @throws SQLException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	@Override
	@SuppressWarnings(
	{ "unchecked", "rawtypes"
	})
	public void syncronizeProperties(RootObject rootObj, ResultSet resultSet) throws SQLException, IllegalAccessException, IllegalArgumentException,
	          InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException
	{
		for ( Method setter : this.getRootObjInfo().getSetters() )
		{
			if (setter != null)
			{
				String setterparamType = setter.getParameterTypes()[0].getSimpleName();
				String paramName = setter.getName().substring(3);
				/**
				 * Ignore Application Context since Root Object needed to
				 * Implement ApplicationAwareInterface to support static
				 * methods in Root Object as Autowiring does not wires
				 * static attributes
				 */
				if (!paramName.equals("ApplicationContext"))
				{
					/**
					 * Get the value for this parameter as per it's type
					 */

					switch (setterparamType)
					{
					case "Int":
					case "Integer":
					case "int":
						int intValue = resultSet.getInt(paramName);
						// Invoke the Setter here itself on root Object
						setter.invoke(rootObj, intValue);
						break;

					case "Double":
					case "double":
						double doubleValue = resultSet.getDouble(paramName);
						// Invoke the Setter here itself on root Object
						setter.invoke(rootObj, doubleValue);
						break;

					case "String":
						String stringValue = resultSet.getString(paramName);
						// Invoke the Setter here itself on root Object
						setter.invoke(rootObj, stringValue);
						break;

					case "Boolean":
					case "boolean":
						boolean boolValue = resultSet.getBoolean(paramName);
						// Invoke the Setter here itself on root Object
						setter.invoke(rootObj, boolValue);
						break;

					case "Date":
					case "date":
						java.util.Date utilDate = resultSet.getDate(paramName);
						setter.invoke(rootObj, utilDate);

						break;

					default: // Enums that implement IEnumable Interface

						int enumInt = resultSet.getInt(paramName);
						Class enumtype = setter.getParameterTypes()[0];
						Object enumConv = EnumGetBack.lookupEnum(enumtype, enumInt);
						if (enumConv != null)
						{
							setter.invoke(rootObj, enumConv);
						}

						break;
					}
				}
			}
		}
	}

	/**
	 * Synchronize Pkey in Root Entity Metadata from REsultSet
	 */
	private void syncronizePkey(RootObject rootObj, ResultSet resultSet)
	{
		// Try to look out for PKey in result set
		if (entManager.getRootMetadata(rootObj) != null)
		{
			if (entManager.getRootMetadata(rootObj).getPrimaryKey() != null)
			{
				String pkeyType = null;
				/**
				 * Primary Key if Part of the Object would be a String only
				 */
				PrimaryKey pkey = entManager.getRootMetadata(rootObj).getPrimaryKey();
				if (pkey.getObjField() != null && this.getRootObjInfo().getRoot_Metadata().getAutokey() == false)
				{
					pkeyType = "String";
				}
				else if (pkey.getTableField() != null && this.getRootObjInfo().getRoot_Metadata().getAutokey() == true)
				{
					pkeyType = "Integer";
				}

				try
				{
					if (pkeyType == "String")
					{
						entManager.getRootMetadata(rootObj).getPrimaryKey().setValue(resultSet.getString(this.getRootObjInfo().getPKey_Name()));
					}
					else if (pkeyType == "Integer")
					{
						entManager.getRootMetadata(rootObj).getPrimaryKey().setValue(resultSet.getInt(this.getRootObjInfo().getPKey_Name()));
					}
				}
				catch (Exception e)
				{
					// Do Nothing
				}

			}
		}
	}

	/**
	 * Create the Prepared Statement
	 */
	@Override
	public void createPreparedStatement(Connection conn) throws SQLException
	{
		if (conn != null)
		{
			Method method = null;
			int i = 1;
			// First generate the Pstmt from Query String

			// First need to Consider User Aware Scenario
			if (this.getRootObjInfo().IsUserAware())
			{
				if (this.getQueryParams().size() > 0)
				{
					this.setConditionSql(" AND ( SYSUSER = ? )");
				}
				else if (this.getQueryParams().size() == 0)
				{
					this.setConditionSql("  WHERE  SYSUSER = ? ");
				}
			}

			this.prepStmt = conn.prepareStatement(this.getConditionSql());
			/**
			 * Do Parameters Assignent for Where Clause
			 */
			if (this.getPrepStmt() != null && this.getQueryParams().size() > 0)
			{

				for ( TY_NameValue ty_NameValue : queryParams )
				{
					method = this.getRootObjInfo().Get_Getter_for_FieldName(ty_NameValue.Name);
					if (method != null)
					{
						String retType = method.getReturnType().getSimpleName();
						switch (retType)
						{
						case "Int":
						case "Integer":
						case "int":
							this.prepStmt.setInt(i, (int) ty_NameValue.Value);
							break;

						case "Double":
						case "double":
							this.prepStmt.setDouble(i, (Double) ty_NameValue.Value);
							break;

						case "String":
							this.prepStmt.setString(i, (String) ty_NameValue.Value);
							break;

						case "Boolean":
							this.prepStmt.setBoolean(i, (Boolean) ty_NameValue.Value);
							break;

						default: // Enums that implement IEnumable
						         // Interface

							Object enum_obj = ty_NameValue.Value;
							if (enum_obj instanceof IEnumable)
							{
								IEnumable casted_obj = (IEnumable) enum_obj;
								this.prepStmt.setInt(i, casted_obj.Get_Value_From_Enums(enum_obj));
							}
							break;
						}
					}
					i++;
				}

			}
			if (this.getRootObjInfo().IsUserAware())
			{
				this.prepStmt.setString(i, this.frameworkManager.getUserManager().Get_LoggedUser());
			}
		}
	}

	/**
	 * Create the Prepared Statement
	 */
	@Override
	public void createPreparedStatement() throws SQLException
	{
		if (this.getConnection() != null)
		{
			Method method = null;
			int i = 1;
			// First generate the Pstmt from Query String

			// First need to Consider User Aware Scenario
			if (this.getRootObjInfo().IsUserAware())
			{
				if (this.getQueryParams().size() > 0)
				{
					this.setConditionSql(" AND ( SYSUSER = ? )");
				}
				else if (this.getQueryParams().size() == 0)
				{
					this.setConditionSql("  WHERE  SYSUSER = ? ");
				}
			}

			this.prepStmt = this.getConnection().prepareStatement(this.getConditionSql());
			/**
			 * Do Parameters Assignent for Where Clause
			 */
			if (this.getPrepStmt() != null && this.getQueryParams().size() > 0)
			{

				for ( TY_NameValue ty_NameValue : queryParams )
				{
					method = this.getRootObjInfo().Get_Getter_for_FieldName(ty_NameValue.Name);
					if (method != null)
					{
						String retType = method.getReturnType().getSimpleName();
						switch (retType)
						{
						case "Int":
						case "Integer":
						case "int":
							this.prepStmt.setInt(i, (int) ty_NameValue.Value);
							break;

						case "Double":
						case "double":
							this.prepStmt.setDouble(i, (Double) ty_NameValue.Value);
							break;

						case "String":
							this.prepStmt.setString(i, (String) ty_NameValue.Value);
							break;

						case "Boolean":
							this.prepStmt.setBoolean(i, (Boolean) ty_NameValue.Value);
							break;

						default: // Enums that implement IEnumable
						         // Interface

							Object enum_obj = ty_NameValue.Value;
							if (enum_obj instanceof IEnumable)
							{
								IEnumable casted_obj = (IEnumable) enum_obj;
								this.prepStmt.setInt(i, casted_obj.Get_Value_From_Enums(enum_obj));
							}
							break;
						}
					}
					i++;
				}

			}
			if (this.getRootObjInfo().IsUserAware())
			{
				this.prepStmt.setString(i, this.frameworkManager.getUserManager().Get_LoggedUser());
			}
		}
	}

	/**
	 * @return the prepStmt
	 */
	@Override
	public PreparedStatement getPrepStmt()
	{
		return prepStmt;
	}

	/**
	 * @return the queryParams
	 */
	@Override
	public ArrayList<TY_NameValue> getQueryParams()
	{
		return queryParams;
	}

	/**
	 * @param queryParams
	 *             the queryParams to set
	 */
	@Override
	public void setQueryParams(ArrayList<TY_NameValue> queryParams)
	{
		this.queryParams = queryParams;
	}

	/**
	 * @return the rootObjName
	 */
	@Override
	public String getRootObjName()
	{
		return rootObjName;
	}

	/**
	 * @param rootObjName
	 *             the rootObjName to set
	 */
	@Override
	public void setRootObjName(String rootObjName)
	{
		this.rootObjName = rootObjName;
	}

	/**
	 * @return the tableName
	 */
	@Override
	public String getTableName()
	{
		return tableName;
	}

	/**
	 * @param tableName
	 *             the tableName to set
	 */
	@Override
	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	/**
	 * @return the conditionSql
	 */
	@Override
	public String getConditionSql()
	{
		return conditionSql;
	}

	/**
	 * @param conditionSql
	 *             the conditionSql to set
	 */
	@Override
	public void setConditionSql(String conditionSql)
	{
		this.conditionSql += conditionSql;
	}

	/**
	 * @return the rootObjInfo
	 */
	@Override
	public Object_Info getRootObjInfo()
	{
		return rootObjInfo;
	}

	/**
	 * @param rootObjInfo
	 *             the rootObjInfo to set
	 */
	@Override
	public void setRootObjInfo(Object_Info rootObjInfo)
	{
		this.rootObjInfo = rootObjInfo;
	}

	/**
	 * @return the connection
	 */
	public Connection getConnection()
	{
		return connection;
	}

}
