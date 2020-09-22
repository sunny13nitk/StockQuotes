/**
 * 
 */
package modelframework.definitions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import modelframework.JAXB.definitions.objschemas.Dependant_Object_Defn;
import modelframework.enums.system.modelEnums;
import modelframework.exceptions.EX_PKeynotSpecified;
import modelframework.exposed.FrameworkManager;
import modelframework.interfaces.IEnumable;
import modelframework.types.TY_NameValue;

/**
 * Entity Metadata Class - Holds the Entities Information as a package for a
 * Root Object System Class - Do NOT Change
 */

public class EntityMetadata
{
	private String						selfID;
	private String						parentID;
	private String						rootID;
	private String						objectName;
	private modelEnums.objectType			objectType;
	private modelEnums.entityMode			entityMode;
	private int						hierarchy;
	private PrimaryKey					primaryKey;
	private ForeignKey					foreignKey;
	private Object						entity;
	private PreparedStatement			preparedStatement;
	private ArrayList<Changed_Property>	changedProperties;
	private boolean					keyGen;
	private boolean					userAware;
	private String						currentUser;

	/**
	 * @return the selfID
	 */
	public String getSelfID()
	{
		return selfID;
	}

	/**
	 * @param selfID
	 *             the selfID to set
	 */
	public void setSelfID(String selfID)
	{
		this.selfID = selfID;
	}

	/**
	 * @return the parentID
	 */
	public String getParentID()
	{
		return parentID;
	}

	/**
	 * @param parentID
	 *             the parentID to set
	 */
	public void setParentID(String parentID)
	{
		this.parentID = parentID;
	}

	/**
	 * @return the rootID
	 */
	public String getRootID()
	{
		return rootID;
	}

	/**
	 * @param rootID
	 *             the rootID to set
	 */
	public void setRootID(String rootID)
	{
		this.rootID = rootID;
	}

	/**
	 * @return the objectName
	 */
	public String getObjectName()
	{
		return objectName;
	}

	/**
	 * @param objectName
	 *             the objectName to set
	 */
	public void setObjectName(String objectName)
	{
		this.objectName = objectName;
	}

	/**
	 * @return the objectType
	 */
	public modelEnums.objectType getObjectType()
	{
		return objectType;
	}

	/**
	 * @param objectType
	 *             the objectType to set
	 */
	public void setObjectType(modelEnums.objectType objectType)
	{
		this.objectType = objectType;
	}

	/**
	 * @return the entityMode
	 */
	public modelEnums.entityMode getEntityMode()
	{
		return entityMode;
	}

	/**
	 * @param entityMode
	 *             the entityMode to set
	 */
	public void setEntityMode(modelEnums.entityMode entityMode)
	{
		this.entityMode = entityMode;
	}

	/**
	 * @return the hierarchy
	 */
	public int getHierarchy()
	{
		return hierarchy;
	}

	/**
	 * @param hierarchy
	 *             the hierarchy to set
	 */
	public void setHierarchy(int hierarchy)
	{
		this.hierarchy = hierarchy;
	}

	/**
	 * @return the primaryKey
	 */
	public PrimaryKey getPrimaryKey()
	{
		return primaryKey;
	}

	/**
	 * @param primaryKey
	 *             the primaryKey to set
	 */
	public void setPrimaryKey(PrimaryKey primaryKey)
	{
		this.primaryKey = primaryKey;
	}

	/**
	 * @return the entity
	 */
	public Object getEntity()
	{
		return entity;
	}

	/**
	 * @return the foreignKey
	 */
	public ForeignKey getForeignKey()
	{
		return foreignKey;
	}

	/**
	 * @param foreignKey
	 *             the foreignKey to set
	 */
	public void setForeignKey(ForeignKey foreignKey)
	{
		this.foreignKey = foreignKey;
	}

	/**
	 * @param entity
	 *             the entity to set
	 */
	public void setEntity(Object entity)
	{
		this.entity = entity;
	}

	/**
	 * @return the preparedStatement
	 */
	public PreparedStatement getPreparedStatement()
	{
		return preparedStatement;
	}

	/**
	 * @param preparedStatement
	 *             the preparedStatement to set
	 */
	public void setPreparedStatement(PreparedStatement preparedStatement)
	{
		this.preparedStatement = preparedStatement;
	}

	/**
	 * @return the changedProperties
	 */
	public ArrayList<Changed_Property> getChangedProperties()
	{
		return changedProperties;
	}

	/**
	 * @param changedProperties
	 *             the changedProperties to set
	 */
	public void setChangedProperties(ArrayList<Changed_Property> changedProperties)
	{
		this.changedProperties = changedProperties;
	}

	/**
	 * @return the keyGen
	 */
	public boolean isKeyGen()
	{
		return keyGen;
	}

	/**
	 * @param keyGen
	 *             the keyGen to set
	 */
	public void setKeyGen(boolean keyGen)
	{
		this.keyGen = keyGen;
	}

	/**
	 * @return the userAware
	 */
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
	 * @return the currentUser
	 */
	public String getCurrentUser()
	{
		return currentUser;
	}

	/**
	 * @param currentUser
	 *             the currentUser to set
	 */
	public void setCurrentUser(String currentUser)
	{
		this.currentUser = currentUser;
	}

	/**
	 * Blank constructor
	 */
	public EntityMetadata()
	{
		// Initialize Change Properties Collection
		this.changedProperties = new ArrayList<Changed_Property>();

	}

	/***********************************************************************************
	 * Create Entity Metadata using ObjectInfo & Entity Mode
	 * 
	 * @param objInfo
	 *             - Object Information
	 * @param mode
	 *             - Entity Mode
	 ***********************************************************************************/
	public EntityMetadata(Object_Info objInfo, modelEnums.entityMode mode)
	{
		if (objInfo != null)
		{
			// Object Name
			this.objectName = objInfo.getObject_Name();

			// Entity Mode
			this.entityMode = mode;

			// Object Type & Hierarchy
			if (objInfo.getRoot_Metadata() != null)
			{
				this.objectType = modelframework.enums.system.modelEnums.objectType.ROOT_OBJECT;
				// Hierarchy = 0 for Root Object Always
				this.hierarchy = 0;

				// Keep the Key Value Initially null
				this.primaryKey = new PrimaryKey(objInfo.getRoot_Metadata().getKeyObjField(), objInfo.getRoot_Metadata().getKeyTableField(),
				          null);

				this.foreignKey = null; // Root Object has no foreign Key
			}
			else if (objInfo.getDep_Metadata() != null)
			{
				this.objectType = modelframework.enums.system.modelEnums.objectType.DEPENDANT_OBJECT;
				// Get Hierarchy for Dependant
				this.hierarchy = objInfo.getDep_Metadata().getHierarchy();

				// Primary Key - Keep value null initially
				this.primaryKey = new PrimaryKey(objInfo.getDep_Metadata().getKeyObjField(), objInfo.getDep_Metadata().getKeyTableField(), null);

				// Foreign Key - Keep value null initially
				this.foreignKey = new ForeignKey(objInfo.getDep_Metadata().getForeignkeyname(), null);
			}

			this.changedProperties = new ArrayList<Changed_Property>();

		}
	}

	/*********************************************************************************
	 * Create Entity Metadata for a Dependant Object passing relation details
	 * and mode
	 * 
	 * @param relDetails
	 *             - Relation Details
	 * @param mode
	 *             - Entity Mode Enum
	 *********************************************************************************/
	public EntityMetadata(Dependant_Object_Defn relDetails, modelEnums.entityMode mode)
	{
		if (relDetails != null)
		{
			this.objectType = modelframework.enums.system.modelEnums.objectType.DEPENDANT_OBJECT;
			this.entityMode = mode;
			this.objectName = relDetails.getDepobjname();
			this.hierarchy = relDetails.getHierarchy();
			this.changedProperties = new ArrayList<Changed_Property>();
			this.primaryKey = new PrimaryKey(relDetails.getKeyObjField(), relDetails.getKeyTableField(), null);
			this.foreignKey = new ForeignKey(relDetails.getForeignkeyname(), null);

		}
	}

	/*********************************************************************************************
	 * Generate Prepared Statements for Passed Connection through Meatadata
	 * Objects contained
	 * 
	 * @param Conn
	 *             - SQL Connection
	 * @throws EX_PKeynotSpecified
	 *              - PKey declared as part of Object but not maintained
	 * @throws SQLException
	 *              - Any SQL error
	 *********************************************************************************************/

	@SuppressWarnings("incomplete-switch")
	public void generate_PreparedStatement(Connection Conn) throws EX_PKeynotSpecified, SQLException
	{
		if (Conn != null)
		{
			switch (this.getEntityMode())
			{
			case CREATE:
				generate_CreateStatement(Conn, this);
				break;

			case CHANGE:
				generate_UpdateStatement(Conn, this);
				break;

			case DELETE:
				generate_DeleteStatement(Conn, this);
				break;
			}
		}
	}

	/***************************************************************************************
	 * Generate Create Prepared Statement
	 * 
	 * @param Conn
	 * @param entMdt
	 * @throws EX_PKeynotSpecified
	 * @throws SQLException
	 ***************************************************************************************/
	private void generate_CreateStatement(Connection Conn, EntityMetadata entMdt) throws EX_PKeynotSpecified, SQLException
	{

		/**
		 * Some/All Properties at least ought to be Set before
		 */
		if (this.getChangedProperties().size() > 0)
		{
			/**
			 * Primary Key in case present in Object(not autogenerated) must
			 * be set in Changed Properties - otherwise data to be saved
			 * would be inconsistent
			 */
			if (this.getPrimaryKey().getObjField() != null)
			{
				try
				{
					Changed_Property objkepProp = this.getChangedProperties().stream()
					          .filter(x -> x.getFieldName().equals(this.getPrimaryKey().getObjField())).findFirst().get();
					if (objkepProp != null)
					{
						if (objkepProp.getValue() == null)
						{

							EX_PKeynotSpecified exPkey = new EX_PKeynotSpecified(new Object[]
							{ this.getPrimaryKey().getObjField(), this.getObjectName()
							});
							throw exPkey;
						}
					}
				}
				catch (NoSuchElementException NE)
				{
					// Entity Has Primary Key to be Set in Object but the
					// same is Not
					// found in Entity Meatadata
					EX_PKeynotSpecified exPkey = new EX_PKeynotSpecified(new Object[]
					{ this.getPrimaryKey().getObjField(), this.getObjectName()
					});
					throw exPkey;
				}
			}

			/**
			 * Prepare Changed Properties Collection from Changed Properties
			 * in Entity Metadata
			 */
			ArrayList<TY_NameValue> CH_Props = new ArrayList<TY_NameValue>();
			for ( Changed_Property chProp : this.getChangedProperties() )
			{
				TY_NameValue nameVal = new TY_NameValue(chProp.getFieldName(), chProp.getValue());
				CH_Props.add(nameVal);
			}
			/**
			 * Generate the Query_Insert Object after getting the Object Info
			 * for ObjectName
			 */
			Object_Info Obj_Info = FrameworkManager.getObjectsInfoFactory().Get_ObjectInfo_byName(this.getObjectName());
			Query_Insert insertQuery = new Query_Insert(Obj_Info, CH_Props, entMdt);
			if (insertQuery != null)
			{
				PreparedStatement pstmt = null;
				/**
				 * Generate the Prepared statement cosidering Key generation
				 * property of Entity Metadata
				 */
				if (this.isKeyGen() == true && Conn != null)
				{
					pstmt = Conn.prepareStatement(insertQuery.getInsert_Query_Text(), java.sql.Statement.RETURN_GENERATED_KEYS);
				}
				else if (this.isKeyGen() == false && Conn != null)
				{
					pstmt = Conn.prepareStatement(insertQuery.getInsert_Query_Text());
				}
				/**
				 * Set the placeholders in Prepared Statement
				 */
				if (pstmt != null)
				{
					int i = 1;
					for ( Changed_Property chgProp : this.getChangedProperties() )
					{

						switch (chgProp.getDataType())
						{
						case "Int":
						case "int":
						case "Integer":
							pstmt.setInt(i, (int) chgProp.getValue());
							break;

						case "Double":
						case "double":
							pstmt.setDouble(i, (Double) chgProp.getValue());
							break;

						case "String":
							pstmt.setString(i, (String) chgProp.getValue());
							break;

						case "Boolean":
						case "boolean":
							pstmt.setBoolean(i, (Boolean) chgProp.getValue());
							break;

						case "Date":
						case "date":
							java.util.Date utilDate = (java.util.Date) chgProp.getValue();
							java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
							pstmt.setDate(i, sqlDate);
							break;

						default: // Enums that implement IEnumable
						         // Interface

							Object enum_obj = chgProp.getValue();
							if (enum_obj instanceof IEnumable)
							{
								IEnumable casted_obj = (IEnumable) enum_obj;
								pstmt.setInt(i, casted_obj.Get_Value_From_Enums(enum_obj));
							}

							break;

						}
						i++;
					}

					if (this.isUserAware() == true)
					{
						pstmt.setString(i, getCurrentUser());
					}

					/**
					 * Finally Set the Prepared Statement in Entity
					 * Metadata
					 */
					this.setPreparedStatement(pstmt);

				}

			}
		}

	}

	/***************************************************************************************
	 * Generate Create Prepared Statement
	 * 
	 * @param Conn
	 * @param entMdt
	 * @throws EX_PKeynotSpecified
	 * @throws SQLException
	 ***************************************************************************************/

	private void generate_UpdateStatement(Connection Conn, EntityMetadata entMdt) throws EX_PKeynotSpecified, SQLException
	{

		/**
		 * Some/All Properties at least ought to be Set before
		 */
		if (this.getChangedProperties().size() > 0)
		{

			/**
			 * Prepare Changed Properties Collection from Changed Properties
			 * in Entity Metadata
			 */
			ArrayList<TY_NameValue> CH_Props = new ArrayList<TY_NameValue>();
			for ( Changed_Property chProp : this.getChangedProperties() )
			{
				TY_NameValue nameVal = new TY_NameValue(chProp.getFieldName(), chProp.getValue());
				CH_Props.add(nameVal);
			}
			/**
			 * Generate the Query_Update Object after getting the Object Info
			 * for ObjectName
			 */
			Object_Info Obj_Info = FrameworkManager.getObjectsInfoFactory().Get_ObjectInfo_byName(this.getObjectName());
			Query_Update updateQuery = new Query_Update(Obj_Info, CH_Props);
			if (updateQuery != null)
			{
				PreparedStatement pstmt = null;
				/**
				 * Generate the Prepared statement cosidering Key generation
				 * property of Entity Metadata
				 */

				if (Conn != null)
				{
					pstmt = Conn.prepareStatement(updateQuery.getUpdate_Query_Text());
				}
				/**
				 * Set the placeholders in Prepared Statement
				 */
				if (pstmt != null)
				{
					int i = 1;
					for ( Changed_Property chgProp : this.getChangedProperties() )
					{

						switch (chgProp.getDataType())
						{
						case "Int":
						case "int":
						case "Integer":
							pstmt.setInt(i, (int) chgProp.getValue());
							break;

						case "Double":
						case "double":
							pstmt.setDouble(i, (Double) chgProp.getValue());
							break;

						case "String":
							pstmt.setString(i, (String) chgProp.getValue());
							break;

						case "Boolean":
						case "boolean":
							pstmt.setBoolean(i, (Boolean) chgProp.getValue());
							break;

						case "Date":
						case "date":
							java.util.Date utilDate = (java.util.Date) chgProp.getValue();
							java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
							pstmt.setDate(i, sqlDate);
							break;

						default: // Enums that implement IEnumable
						         // Interface

							Object enum_obj = chgProp.getValue();
							if (enum_obj instanceof IEnumable)
							{
								IEnumable casted_obj = (IEnumable) enum_obj;
								pstmt.setInt(i, casted_obj.Get_Value_From_Enums(enum_obj));
							}

							break;

						}
						i++;
					}

					if (this.getPrimaryKey() != null)
					{
						String pkeyType = null;
						if (this.getPrimaryKey().getObjField() != null)
						{
							pkeyType = "String";
							pstmt.setString(i, (String) this.getPrimaryKey().getValue());
						}
						else if (this.getPrimaryKey().getTableField() != null)
						{
							pkeyType = "Integer";
							pstmt.setInt(i, (int) this.getPrimaryKey().getValue());
						}

					}

					/**
					 * Finally Set the Prepared Statement in Entity
					 * Metadata
					 */
					this.setPreparedStatement(pstmt);

				}

			}
		}

	}

	/***************************************************************************************
	 * Generate Create Prepared Statement
	 * 
	 * @param Conn
	 * @param entMdt
	 * @throws EX_PKeynotSpecified
	 * @throws SQLException
	 ***************************************************************************************/

	private void generate_DeleteStatement(Connection Conn, EntityMetadata entMdt) throws EX_PKeynotSpecified, SQLException
	{
		if (entMdt.getObjectName() != null)
		{
			Object_Info Obj_Info = FrameworkManager.getObjectsInfoFactory().Get_ObjectInfo_byName(this.getObjectName());
			if (Obj_Info != null)
			{
				Obj_Info.Upload_Delete_Query();
				if (Obj_Info.getDelete_Query().getDeleteQuery_Text() != null)
				{
					if (Obj_Info.getDelete_Query().getDeleteQuery_Text().length() > 0)
					{
						PreparedStatement pstmt = null;
						/**
						 * Generate the Prepared statement cosidering Key generation
						 * property of Entity Metadata
						 */

						if (Conn != null)
						{
							pstmt = Conn.prepareStatement(Obj_Info.getDelete_Query().getDeleteQuery_Text());
						}
						/**
						 * Set the placeholders in Prepared Statement
						 */
						if (pstmt != null)
						{
							if (this.getPrimaryKey() != null)
							{
								String pkeyType = null;
								if (this.getPrimaryKey().getObjField() != null)
								{
									pkeyType = "String";
									pstmt.setString(1, (String) this.getPrimaryKey().getValue());
								}
								else if (this.getPrimaryKey().getTableField() != null)
								{
									pkeyType = "Integer";
									pstmt.setInt(1, (int) this.getPrimaryKey().getValue());
								}

							}
							/**
							 * Finally Set the Prepared Statement in Entity
							 * Metadata
							 */
							this.setPreparedStatement(pstmt);
						}
					}
				}
			}
		}

	}
}
