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

import modelframework.JAXB.definitions.objschemas.Dependant_Object_Defn;
import modelframework.JAXB.definitions.objschemas.ObjectSchema;
import modelframework.cache.CacheQueryManager;
import modelframework.definitions.EntityManager;
import modelframework.definitions.EntityMetadata;
import modelframework.definitions.KeyEntity;
import modelframework.definitions.Object_Info;
import modelframework.definitions.PrimaryKey;
import modelframework.exceptions.EX_InvalidObjectException;
import modelframework.exceptions.EX_InvalidRelationException;
import modelframework.exceptions.EX_NotRootException;
import modelframework.exceptions.EX_NullParamsException;
import modelframework.exceptions.EX_ParamCountMismatchException;
import modelframework.exceptions.EX_ParamInitializationException;
import modelframework.exposed.FrameworkManager;
import modelframework.implementations.DependantObject;
import modelframework.implementations.RootObject;
import modelframework.interfaces.IEnumable;
import modelframework.interfaces.IQueryParamsValidator;
import modelframework.interfaces.IQueryService;
import modelframework.interfaces.IRelQueryService;
import modelframework.managers.ParamValidationManager;
import modelframework.types.TY_NameValue;
import modelframework.usermanager.managers.UserManager;
import modelframework.utilities.CglibHelper;
import modelframework.utilities.EnumGetBack;
import modelframework.utilities.PropertiesMapper;

@Service("RelationsQueryService")
public class RelationsQueryService implements IRelQueryService, IQueryService
{
	@Autowired
	private UserManager				userManager;
	@Autowired
	private FrameworkManager			frameworkManager;
	@Autowired
	private EntityManager			entManager;
	@Autowired
	private ApplicationContext		ctxt;
	@Autowired
	private PropertiesMapper			propertiesMapper;

	private Object_Info				rootObjInfo;
	private Connection				conn;
	private PreparedStatement		pStmnt;
	private String					selTable;
	private String					fkeyName;
	private Object					pkeyValue;
	private String					queryStr;
	private String					dynqpart;
	private boolean				userAware;
	private String					pkeyType;
	private ObjectSchema			RootSchema;
	private Dependant_Object_Defn		relDetails;
	private ArrayList<TY_NameValue>	params;
	private Boolean				filterMode;
	/**
	 * Can be Root or Dependant
	 */
	private Object					invoker;
	/**
	 * DO Not close if Connection is inherited; Caller will close the connection
	 */
	private boolean				connInherited;

	/**
	 * @return the invoker
	 */
	public Object getInvoker()
	{
		return invoker;
	}

	/**
	 * @param invoker
	 *             the invoker to set
	 */
	public void setInvoker(Object invoker)
	{
		this.invoker = invoker;
	}

	/**
	 * @return the rootSchema
	 */
	public ObjectSchema getRootSchema()
	{
		return RootSchema;
	}

	/**
	 * @param rootSchema
	 *             the rootSchema to set
	 */
	public void setRootSchema(ObjectSchema rootSchema)
	{
		RootSchema = rootSchema;
	}

	/**
	 * @return the relDetails
	 */
	@Override
	public Dependant_Object_Defn getRelDetails()
	{
		return relDetails;
	}

	/**
	 * @param relDetails
	 *             the relDetails to set
	 */
	public void setRelDetails(Dependant_Object_Defn relDetails)
	{
		this.relDetails = relDetails;
	}

	/**
	 * @return the rootObjInfo
	 */
	public Object_Info getRootObjInfo()
	{
		return rootObjInfo;
	}

	/**
	 * @param rootObjInfo
	 *             the rootObjInfo to set
	 */
	public void setRootObjInfo(Object_Info rootObjInfo)
	{
		this.rootObjInfo = rootObjInfo;
	}

	/**
	 * @return the connInherited
	 */
	public boolean isConnInherited()
	{
		return connInherited;
	}

	/**
	 * @param connInherited
	 *             the connInherited to set
	 */
	public void setConnInherited(boolean connInherited)
	{
		this.connInherited = connInherited;
	}

	/**
	 * @return the pkeyType
	 */
	public String getPkeyType()
	{
		return pkeyType;
	}

	/**
	 * @param pkeyType
	 *             the pkeyType to set
	 */
	public void setPkeyType(String pkeyType)
	{
		this.pkeyType = pkeyType;
	}

	/**
	 * @return the userAware
	 */
	@Override
	public boolean isUserAware()
	{
		return userAware;
	}

	/**
	 * @param userAware
	 *             the userAware to set
	 */
	public void setUserAware(boolean userAware)
	{
		this.userAware = userAware;
	}

	/**
	 * @return the conn
	 */
	public Connection getConn()
	{
		return conn;
	}

	/**
	 * @param conn
	 *             the conn to set
	 */
	public void setConn(Connection conn)
	{
		this.conn = conn;
	}

	/**
	 * @return the pStmnt
	 */
	@Override
	public PreparedStatement getpStmnt()
	{
		return pStmnt;
	}

	/**
	 * @param pStmnt
	 *             the pStmnt to set
	 */
	public void setpStmnt(PreparedStatement pStmnt)
	{
		this.pStmnt = pStmnt;
	}

	/**
	 * @return the selTable
	 */
	public String getSelTable()
	{
		return selTable;
	}

	/**
	 * @param selTable
	 *             the selTable to set
	 */
	public void setSelTable(String selTable)
	{
		this.selTable = selTable;
	}

	/**
	 * @return the fkeyName
	 */
	public String getFkeyName()
	{
		return fkeyName;
	}

	/**
	 * @param fkeyName
	 *             the fkeyName to set
	 */
	public void setFkeyName(String fkeyName)
	{
		this.fkeyName = fkeyName;
	}

	/**
	 * @return the pkeyValue
	 */
	@Override
	public Object getPkeyValue()
	{
		return pkeyValue;
	}

	/**
	 * @param pkeyValue
	 *             the pkeyValue to set
	 */
	public void setPkeyValue(Object pkeyValue)
	{
		this.pkeyValue = pkeyValue;
	}

	/**
	 * @return the queryStr
	 */
	public String getQueryStr()
	{
		return queryStr;
	}

	/**
	 * @param queryStr
	 *             the queryStr to set
	 */
	public void setQueryStr(String queryStr)
	{
		this.queryStr = queryStr;
	}

	/**
	 * @return the params
	 */
	@Override
	public ArrayList<TY_NameValue> getParams()
	{
		return params;
	}

	/**
	 * @param params
	 *             the params to set
	 */
	public void setParams(ArrayList<TY_NameValue> params)
	{
		this.params = params;
	}

	/**
	 * @return the dynqpart
	 */
	@Override
	public String getDynqpart()
	{
		return dynqpart;
	}

	/**
	 * @param dynqpart
	 *             the dynqpart to set
	 */
	public void setDynqpart(String dynqpart)
	{
		this.dynqpart = dynqpart;
	}

	/**
	 * @return the filterMode
	 */
	@Override
	public Boolean isFilterON()
	{
		return filterMode;
	}

	/**
	 * @param filterMode
	 *             the filterMode to set
	 */
	public void setFilterMode(Boolean filterMode)
	{
		this.filterMode = filterMode;
	}

	@Override
	public void Initialize(Object invoker, String relName, Boolean filterMode) throws EX_InvalidRelationException, SQLException
	{
		if (invoker != null && relName != null)
		{
			this.setFilterMode(filterMode);
			if (filterMode == false)
			{
				/**
				 * Always Reset Params and dyn Part and Query String to Clear buffer in case of No filter Mode
				 * For Filter Mode these would always be passed
				 */
				this.params = null;
				this.dynqpart = null;
				this.queryStr = null;
			}
			if (invoker instanceof RootObject)
			{
				setInvoker(invoker);
				initializeRoot((RootObject) invoker, relName);
				this.setConnInherited(false);
			}

			if (invoker instanceof DependantObject)
			{
				setInvoker(invoker);
				initializeDependant((DependantObject) invoker, relName);
				this.setConnInherited(false);
			}

		}

	}

	@Override
	public void Initialize(Object invoker, String relName, Connection conn)
	{

	}

	@SuppressWarnings(
	{ "unchecked", "static-access"
	})
	@Override
	public <T> ArrayList<T> executeQuery(String whereCondition, ArrayList<TY_NameValue> params) throws SQLException, EX_InvalidRelationException,
	          IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException,
	          SecurityException, EX_ParamCountMismatchException, EX_ParamInitializationException, EX_NullParamsException
	{

		/**
		 * Don't forget to Update Foreign Key Value from Root Pkey once the
		 * Dependant Object is Created Also Update Dependant Object Primary
		 * Key once the Data is returned from REsulset
		 */

		/*
		 * Validate the Query parameters First
		 */

		/**
		 * Get Param Validator BEan
		 */
		ArrayList<T> result = new ArrayList<T>();
		if (whereCondition != null && params != null)
		{

			try
			{
				IQueryParamsValidator paramValidator = ParamValidationManager.getParamValidatorService("ParamValidatorService",
				          this.getInvoker().getClass().getSimpleName());
				paramValidator.validateQuery(whereCondition, params);
				// Set the params after Validation in the Object
				this.setParams(params);
				// Set the query dynamic where condition as whereCondition
				this.setDynqpart(whereCondition);
				// Generate query String in filter Mode
				generateQueryStrfilterMode();
				// Generate Prepared statement in Filter Mode
				generatePreparedStmtforFilterMode();

				if (relDetails != null)
				{
					if (relDetails.getRelationname() != null)
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
									DependantObject depObj = null;
									// Create a Dependant Object Bean for each Row for Root Object Invoker
									if (invoker instanceof RootObject)
									{
										depObj = ((RootObject) invoker).Create_RelatedEntityDB(relDetails.getRelationname());
									}
									// Create a Dependant Object Bean for each Row for Dependant Object Invoker
									else if (invoker instanceof DependantObject)
									{
										depObj = ((DependantObject) invoker).Create_RelatedEntityDB(relDetails.getRelationname());
									}

									if (depObj != null)
									{
										// synchronize Properties in Dependant object Bean by Pojo entity
										depObj = propertiesMapper.addPropertiestoDependantProxyBean(depObj,
										          (DependantObject) t.getPojoEntity());
										CglibHelper cgHelper = new CglibHelper(depObj);
										DependantObject entdepObj = (DependantObject) cgHelper.getTargetObject();
										/**
										 * Synchronize Pkey in Root Entity Metadata from
										 * REsultSet
										 */
										syncronizePkeyFkey(entdepObj, t.getKey());

										/**
										 * Add to Result Collection
										 */
										result.add((T) depObj);
									}
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
				if (this.pStmnt != null)
				{
					if (this.pStmnt.isClosed() != true)
					{
						this.pStmnt.close();
					}
				}
				if (isConnInherited() == false)
				{
					this.conn.close();
				}
			}
		}

		return result;
	}

	@SuppressWarnings(
	{ "unchecked", "static-access"
	})
	@Override
	public <T> ArrayList<T> executeQuery() throws SQLException, EX_InvalidRelationException, IllegalAccessException, IllegalArgumentException,
	          InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException
	{
		/**
		 * Don't forget to Update Foreign Key Value from Root Pkey once the
		 * Dependant Object is Created Also Update Dependant Object Primary
		 * Key once the Data is returned from REsulset
		 */

		ArrayList<T> result = new ArrayList<T>();

		try
		{
			if (relDetails != null)
			{
				if (relDetails.getRelationname() != null)
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
								DependantObject depObj = null;
								// Create a Dependant Object Bean for each Row for Root Object Invoker
								if (invoker instanceof RootObject)
								{
									depObj = ((RootObject) invoker).Create_RelatedEntityDB(relDetails.getRelationname());
								}
								// Create a Dependant Object Bean for each Row for Dependant Object Invoker
								else if (invoker instanceof DependantObject)
								{
									depObj = ((DependantObject) invoker).Create_RelatedEntityDB(relDetails.getRelationname());
								}

								if (depObj != null)
								{
									// synchronize Properties in Dependant object Bean by Pojo entity
									depObj = propertiesMapper.addPropertiestoDependantProxyBean(depObj, (DependantObject) t.getPojoEntity());
									CglibHelper cgHelper = new CglibHelper(depObj);
									DependantObject entdepObj = (DependantObject) cgHelper.getTargetObject();
									/**
									 * Synchronize Pkey in Root Entity Metadata from
									 * REsultSet
									 */
									syncronizePkeyFkey(entdepObj, t.getKey());

									/**
									 * Add to Result Collection
									 */
									result.add((T) depObj);
								}
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
			if (this.pStmnt != null)
			{
				if (this.pStmnt.isClosed() != true)
				{
					this.pStmnt.close();
				}
			}
			if (isConnInherited() == false)
			{
				this.conn.close();
			}
		}

		return result;

	}

	@SuppressWarnings("static-access")
	private void initializeRoot(RootObject rootObj, String relName) throws EX_InvalidRelationException, SQLException
	{
		/**
		 * Get the relation details from root RootSchema & Validate the
		 * Relation
		 */

		{

			RootSchema = FrameworkManager.getObjectSchemaFactory().Get_Schema_byRootObjName(rootObj.getClass().getSimpleName());
			relDetails = RootSchema.getRelationDetails(relName);
			if (relDetails != null)
			{
				this.setSelTable(relDetails.getTablename());
				this.setFkeyName(relDetails.getForeignkeyname());
			}

			EntityMetadata rootMdt = entManager.getRootMetadata(rootObj);
			if (rootMdt != null)
			{
				this.setPkeyValue(rootMdt.getPrimaryKey().getValue());
				this.setRootObjInfo(frameworkManager.getObjectsInfoFactory().Get_ObjectInfo_byName(rootMdt.getObjectName()));
				this.setUserAware(rootMdt.isUserAware());

				PrimaryKey pkey = entManager.getRootMetadata(rootObj).getPrimaryKey();
				if (pkey.getObjField() != null && this.getRootObjInfo().getRoot_Metadata().getAutokey() == false)
				{
					pkeyType = "String";
				}
				else if (pkey.getTableField() != null && this.getRootObjInfo().getRoot_Metadata().getAutokey() == true)
				{
					pkeyType = "Integer";
				}

				/**
				 * Generate Prepared statement on Initialization only in case of Filter Mode Off
				 * Otherwise prepared statmetn in Filter Mode would be generated while execution of Query with
				 * Filter
				 */
				/*
				 * if (isFilterON() == false)
				 * {
				 * generateQueryStr();
				 * generatePreparedStmt();
				 * }
				 */
			}

			else
			{
				// Invalid Relation Error Handling
				{
					// Throw Invalid Relation Exception
					// ERR_INVALIDREL= Invalid Relation {0} requested for
					// Base Type {1}!

					EX_InvalidRelationException exInvRel = new EX_InvalidRelationException(new Object[]
					{ relName, entManager.getRootMetadata(rootObj).getObjectName()
					});
					rootObj.getFW_Manager().getMessageFormatter().generate_message_snippet(exInvRel);
					throw exInvRel;

				}

			}

		}
	}

	@SuppressWarnings("static-access")
	private void initializeDependant(DependantObject depObj, String relName) throws EX_InvalidRelationException, SQLException
	{
		/**
		 * Get the relation details from root RootSchema & Validate the
		 * Relation
		 */

		if (depObj != null)
		{
			RootObject rootObj = depObj.getParentRoot();
			if (rootObj != null)
			{
				RootSchema = FrameworkManager.getObjectSchemaFactory().Get_Schema_byRootObjName(rootObj.getClass().getSimpleName());
				relDetails = RootSchema.getRelationDetails(relName);
				if (relDetails != null)
				{
					this.setSelTable(relDetails.getTablename());
					this.setFkeyName(relDetails.getForeignkeyname());
				}

				EntityMetadata depMdt = entManager.getDependantMetadata(depObj);
				if (depMdt != null)
				{
					this.setPkeyValue(depMdt.getPrimaryKey().getValue());
					this.setRootObjInfo(frameworkManager.getObjectsInfoFactory().Get_ObjectInfo_byName(rootObj.getClass().getSimpleName()));
					this.setUserAware(depMdt.isUserAware());

					PrimaryKey pkey = entManager.getDependantMetadata(depObj).getPrimaryKey();
					if (pkey.getObjField() != null && relDetails.getAutokey() == false)
					{
						pkeyType = "String";
					}
					else if (pkey.getTableField() != null && relDetails.getAutokey() == true)
					{
						pkeyType = "Integer";
					}

					/**
					 * Generate Prepared statement on Initialization only in case of Filter Mode Off
					 * Otherwise prepared statmetn in Filter Mode would be generated while execution of Query with
					 * Filter
					 */
					/*
					 * if (isFilterON() == false)
					 * {
					 * generateQueryStr();
					 * generatePreparedStmt();
					 * }
					 */

				}

				else
				{
					// Invalid Relation Error Handling
					{
						// Throw Invalid Relation Exception
						// ERR_INVALIDREL= Invalid Relation {0} requested for
						// Base Type {1}!

						EX_InvalidRelationException exInvRel = new EX_InvalidRelationException(new Object[]
						{ relName, entManager.getDependantMetadata(depObj).getObjectName()
						});
						depObj.getFW_Manager().getMessageFormatter().generate_message_snippet(exInvRel);
						throw exInvRel;

					}

				}
			}
		}

	}

	@Override
	@SuppressWarnings("unused")
	public void generateQueryStr()
	{
		/**
		 * No Additional conditions and Parameters
		 */
		if (this.params == null)
		{
			// No where condition addendum
			String query = "SELECT * FROM " + getSelTable() + " WHERE " + getFkeyName() + " = ?";

			if (isUserAware())
			{
				query += " AND ( SYSUSER = ? )";
			}

			this.setQueryStr(query);

		}

	}

	@Override
	public void generateQueryStrfilterMode()
	{
		if (this.params != null && this.params.size() > 0)
		{

			String query = "SELECT * FROM " + getSelTable() + " WHERE " + getFkeyName() + " = ?";

			if (isUserAware())
			{
				query += " AND ( SYSUSER = ? )";
			}

			/**
			 * Will always be true since query string would be always initialized in Initilaize() call to the
			 * RelationsQueryManager
			 * Here needs just update of the query String
			 */

			if (this.getDynqpart() != null)
			{
				String woparamQStr = query;
				woparamQStr += " AND ( ";
				woparamQStr += this.getDynqpart();
				woparamQStr += " )";

				this.setQueryStr(woparamQStr);
			}
		}

	}

	@Override
	@SuppressWarnings("static-access")
	public void generatePreparedStmt() throws SQLException
	{
		if (this.getConn() == null)
		{
			this.setConn(frameworkManager.getConnectionPool().getConnection());
		}
		else
		{
			if (this.getConn().isClosed())
			{
				this.setConn(frameworkManager.getConnectionPool().getConnection());
			}
		}

		PreparedStatement pstmt = this.getConn().prepareStatement(this.getQueryStr());
		if (pstmt != null)
		{
			if (this.getParams() == null)
			{
				if (pkeyType == "String")
				{
					pstmt.setString(1, (String) getPkeyValue());
				}
				else if (pkeyType == "Integer")
				{
					pstmt.setInt(1, (int) getPkeyValue());
				}

				if (isUserAware())
				{
					pstmt.setString(2, userManager.Get_LoggedUser());
				}
			}

			this.setpStmnt(pstmt);
		}
	}

	@Override
	@SuppressWarnings("static-access")
	public void generatePreparedStmtforFilterMode() throws SQLException
	{
		Method method = null;
		int i = 1;
		if (this.getConn() == null)
		{
			this.setConn(frameworkManager.getConnectionPool().getConnection());
		}
		else
		{
			if (this.getConn().isClosed())
			{
				this.setConn(frameworkManager.getConnectionPool().getConnection());
			}
		}

		PreparedStatement pstmt = this.getConn().prepareStatement(this.getQueryStr());
		if (pstmt != null)
		{
			if (this.getParams() != null)
			{
				if (pkeyType == "String")
				{
					pstmt.setString(i, (String) getPkeyValue());
					i++;
				}
				else if (pkeyType == "Integer")
				{
					pstmt.setInt(i, (int) getPkeyValue());
					i++;
				}

				if (isUserAware())
				{
					pstmt.setString(i, userManager.Get_LoggedUser());
					i++;
				}

				if (this.getParams().size() > 0)
				{
					if (relDetails != null)
					{
						Object_Info objinfoInvoker = frameworkManager.getObjectsInfoFactory().Get_ObjectInfo_byName(relDetails.getDepobjname());
						for ( TY_NameValue ty_NameValue : this.getParams() )
						{
							method = objinfoInvoker.Get_Getter_for_FieldName(ty_NameValue.Name);
							if (method != null)
							{
								String retType = method.getReturnType().getSimpleName();
								switch (retType)
								{
								case "Int":
								case "Integer":
								case "int":
									pstmt.setInt(i, (int) ty_NameValue.Value);
									break;

								case "Double":
								case "double":
									pstmt.setDouble(i, (Double) ty_NameValue.Value);
									break;

								case "String":
									pstmt.setString(i, (String) ty_NameValue.Value);
									break;

								case "Boolean":
									pstmt.setBoolean(i, (Boolean) ty_NameValue.Value);
									break;

								default: // Enums that implement IEnumable
								         // Interface

									Object enum_obj = ty_NameValue.Value;
									if (enum_obj instanceof IEnumable)
									{
										IEnumable casted_obj = (IEnumable) enum_obj;
										pstmt.setInt(i, casted_obj.Get_Value_From_Enums(enum_obj));
									}
									break;
								}
							}
							i++;
						}
					}
				}

			}

			this.setpStmnt(pstmt);
		}
	}

	@Override
	@SuppressWarnings(
	{ "unchecked", "rawtypes", "static-access"
	})
	public void syncronizeProperties(DependantObject depObj, ResultSet resultSet) throws SQLException, IllegalAccessException,
	          IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException
	{
		for ( Method setter : frameworkManager.getObjectsInfoFactory().Get_ObjectInfo_byClass(depObj.getClass()).getSetters() )
		{
			if (setter != null)
			{
				String setterparamType = setter.getParameterTypes()[0].getSimpleName();
				String paramName = setter.getName().substring(3);
				/**
				 * Ignore Application Context since Dependant Object needed to
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
					case "int":
					case "Integer":
						int intValue = resultSet.getInt(paramName);
						// Invoke the Setter here itself on root Object
						setter.invoke(depObj, intValue);
						break;

					case "Double":
					case "double":
						double doubleValue = resultSet.getDouble(paramName);
						// Invoke the Setter here itself on root Object
						setter.invoke(depObj, doubleValue);
						break;

					case "String":
						String stringValue = resultSet.getString(paramName);
						// Invoke the Setter here itself on root Object
						setter.invoke(depObj, stringValue);
						break;

					case "Boolean":
					case "boolean":
						boolean boolValue = resultSet.getBoolean(paramName);
						// Invoke the Setter here itself on root Object
						setter.invoke(depObj, boolValue);
						break;

					case "Date":
					case "date":
						java.util.Date utilDate = resultSet.getDate(paramName);
						setter.invoke(depObj, utilDate);

						break;

					default: // Enums that implement IEnumable Interface

						int enumInt = resultSet.getInt(paramName);
						Class enumtype = setter.getParameterTypes()[0];
						Object enumConv = EnumGetBack.lookupEnum(enumtype, enumInt);
						if (enumConv != null)
						{
							setter.invoke(depObj, enumConv);
						}

						break;
					}
				}
			}
		}
	}

	@Override

	public void syncronizePkeyFkey(DependantObject depObj, Object pKey)
	{

		/**
		 * PKEY Set in Dependant Object from Result Set
		 */
		// Try to look out for PKey in result set
		if (entManager.getDependantMetadata(depObj) != null)
		{
			if (entManager.getDependantMetadata(depObj).getPrimaryKey() != null)
			{

				try
				{

					entManager.getDependantMetadata(depObj).getPrimaryKey().setValue(pKey);

				}
				catch (Exception e)
				{
					// Do Nothing
				}

			}
		}

		/**
		 * FKEY Set in Dependant Object from Parent Invoker Entity Metadata Primary Key
		 */
		if (entManager.getDependantMetadata(depObj) != null)
		{
			if (entManager.getDependantMetadata(depObj).getForeignKey() != null)
			{
				if (entManager.getDependantMetadata(depObj).getForeignKey().getObjField() != null)
				{
					// Get Entity Metadata for Invoker from entity Manager
					if (invoker != null)
					{
						Object FkeyVal = null;
						if (invoker instanceof RootObject)
						{
							FkeyVal = entManager.getRootMetadata((RootObject) invoker).getPrimaryKey().getValue();
						}
						else if (invoker instanceof DependantObject)
						{
							FkeyVal = entManager.getDependantMetadata((DependantObject) invoker).getPrimaryKey().getValue();
						}

						if (FkeyVal != null)
						{
							entManager.getDependantMetadata(depObj).getForeignKey().setValue(FkeyVal);
						}
					}
				}
			}
		}

	}

	@Override
	public void Initialize(String rootobjectName) throws EX_InvalidObjectException, EX_NotRootException, SQLException
	{
		// TODO Auto-generated method stub

	}

}
