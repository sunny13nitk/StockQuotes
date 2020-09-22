/**
 * 
 */
package modelframework.definitions;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import modelframework.enums.system.modelEnums;
import modelframework.enums.system.modelEnums.entityMode;
import modelframework.enums.system.modelEnums.objectType;
import modelframework.exceptions.EX_PKeynotSpecified;
import modelframework.implementations.DependantObject;
import modelframework.implementations.GeneralMessage;
import modelframework.implementations.RootObject;
import modelframework.utilities.CglibHelper;

@Component("EntityManager")
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS) // CGLIB based Proxy
/**
 * entity Manager Class
 *
 */
public class EntityManager
{
	public ArrayList<EntityMetadata> metadataColl;

	/**
	 * @return the metadataColl
	 */
	public ArrayList<EntityMetadata> getMetadataColl()
	{
		return metadataColl;
	}

	/**
	 * @param metadataColl
	 *             the metadataColl to set
	 */
	public void setMetadataColl(ArrayList<EntityMetadata> metadataColl)
	{
		this.metadataColl = metadataColl;
	}

	/**
	 * 
	 */
	public EntityManager()
	{
		this.metadataColl = new ArrayList<EntityMetadata>();
	}

	/**
	 * Return Root Object from Entity Manager
	 * 
	 * @return - Root Object
	 */
	public RootObject getRootObject()
	{
		RootObject rootObj = null;
		if (this.metadataColl != null)
		{
			if (this.metadataColl.size() > 0)
			{
				try
				{
					rootObj = (RootObject) this.metadataColl.stream().filter(x -> x.getObjectType().equals(objectType.ROOT_OBJECT)).findFirst()
					          .get().getEntity();
				}
				catch (NoSuchElementException NE)
				{
					// Do Nothing
				}
			}
		}

		return rootObj;
	}

	/************************************************
	 * Return Root Object from Entity Manager
	 * matching the current Root Object
	 * Comparison by Entity
	 * 
	 * @param invoker
	 *             - Root Object Instance
	 * @return - Root Object
	 ************************************************/
	public RootObject getRootObject(Object invoker)
	{
		RootObject rootObj = null;
		if (this.metadataColl != null)
		{
			if (this.metadataColl.size() > 0)
			{
				try
				{
					rootObj = (RootObject) this.metadataColl.stream().filter(x -> x.getObjectType().equals(objectType.ROOT_OBJECT))
					          .filter(x -> x.getEntity().equals(invoker)).findFirst().get().getEntity();
				}
				catch (NoSuchElementException NE)
				{
					// Do Nothing
				}
			}
		}

		return rootObj;
	}

	/*******************************************************************************
	 * Get the selfID for an Object Instance if any Matching in Entity Manager
	 * 
	 * @param invoker
	 *             - Object Entity to Search for
	 * @return selfID - self UUID for the Object in Entity Manager
	 * 
	 *******************************************************************************/
	public String getselfID(Object invoker)
	{
		String selfID = null;
		if (this.metadataColl != null)
		{
			if (this.metadataColl.size() > 0)
			{
				try
				{
					selfID = this.metadataColl.stream().filter(x -> x.getEntity().equals(invoker)).findFirst().get().getSelfID();
				}
				catch (NoSuchElementException NE)
				{
					// Do Nothing
				}
			}
		}

		return selfID;
	}

	/*******************************************************************************
	 * Get the selfID for an Object Instance if any Matching in Entity Manager
	 * 
	 * @param invoker
	 *             - Object Entity to Search for
	 * @param oType
	 *             - Tyoe of Object Root/Dependant
	 * @return selfID - self UUID for the Object in Entity Manager
	 * 
	 *******************************************************************************/
	public String getselfID(Object invoker, objectType oType)
	{
		String selfID = null;
		if (this.metadataColl != null)
		{
			if (this.metadataColl.size() > 0)
			{
				try
				{
					selfID = this.metadataColl.stream().filter(x -> x.getObjectType().equals(oType)).filter(x -> x.getEntity().equals(invoker))
					          .findFirst().get().getSelfID();
				}
				catch (NoSuchElementException NE)
				{

					// Try to Find by Removing Cglib Wrapper
					CglibHelper cgHelper = new CglibHelper(invoker);
					Object entity = cgHelper.getTargetObject();

					try
					{
						selfID = this.metadataColl.stream().filter(x -> x.getObjectType().equals(oType))
						          .filter(x -> x.getEntity().equals(entity)).findFirst().get().getSelfID();
					}
					catch (NoSuchElementException E)
					{
						// Do Nothing
					}
				}
			}
		}

		return selfID;
	}

	/**************************************************************************
	 * Get Parent Id for the Object if any
	 * 
	 * @param invoker
	 *             - Object for which parent entity is sought
	 * @return - parent UUID
	 **************************************************************************/
	public String getparentID(Object invoker)
	{
		String parentID = null;
		/**
		 * Need more than one to have parent metadata
		 */
		if (this.metadataColl.size() > 1)
		{
			try
			{
				parentID = this.metadataColl.stream().filter(x -> x.getEntity().equals(invoker)).findFirst().get().getParentID();
			}
			catch (NoSuchElementException NE)
			{
				// Do Nothing
			}
		}

		return parentID;
	}

	/**
	 * GEt Root Object entity Metadata from Entity Manager by passing root Object Instance
	 * 
	 * @param rootObj
	 *             - Root Object Instance
	 * @return - Root Entity Metadata
	 */
	public EntityMetadata getRootMetadata(RootObject rootObj)
	{
		EntityMetadata entMdt = null;
		if (this.metadataColl != null && rootObj != null)
		{
			if (this.metadataColl.size() > 0)
			{
				try
				{
					// first get SelfId for Root Object
					String selfID = this.getselfID(rootObj, objectType.ROOT_OBJECT);
					if (selfID != null)
					{
						entMdt = this.metadataColl.stream().filter(x -> x.getSelfID().equals(selfID)).findFirst().get();
					}

				}
				catch (NoSuchElementException NE)
				{
					// Do Nothing
				}
			}
		}

		return entMdt;
	}

	/**
	 * GEt Dependant Object entity Metadata from Entity Manager by passing dependant Object Instance
	 * 
	 * @param depObj
	 *             - Dependant Object Instance
	 * @return - Dependant Entity Metadata
	 */
	public EntityMetadata getDependantMetadata(DependantObject depObj)
	{
		EntityMetadata entMdt = null;
		if (this.metadataColl != null && depObj != null)
		{
			if (this.metadataColl.size() > 0)
			{
				try
				{
					// first get SelfId for Root Object
					String selfID = this.getselfID(depObj, objectType.DEPENDANT_OBJECT);
					if (selfID != null)
					{
						entMdt = this.metadataColl.stream().filter(x -> x.getSelfID().equals(selfID)).findFirst().get();
					}

				}
				catch (NoSuchElementException NE)
				{
					// Do Nothing
				}
			}
		}

		return entMdt;
	}

	/*******************************************************************************************
	 * Returns - List of EntityMetadata relevant for Current Root Object from Entity Manager
	 * for further processing as needed.
	 * Includes Root Object EntityMetadata as 1st row in collection
	 * 
	 * @param invoker
	 *             - Root Object instance
	 * @return - RElevant entity Metadata Collection
	 ******************************************************************************************/
	public ArrayList<EntityMetadata> getEntityMetadataColl_RootObject(RootObject invoker)
	{
		ArrayList<EntityMetadata> rootEntMdtColl = new ArrayList<EntityMetadata>();

		if (invoker != null)
		{
			if (invoker instanceof RootObject)
			{
				if (this.metadataColl.size() > 0)
				{
					/**
					 * First get entity metadata for Root invoker
					 */
					try
					{
						EntityMetadata rootMdt = this.metadataColl.stream().filter(x -> x.getEntity().equals(invoker)).findFirst().get();
						if (rootMdt != null)
						{
							// Get Self Id of Root
							String rootID = rootMdt.getSelfID();
							if (rootID != null)
							{

								rootEntMdtColl = this.metadataColl.stream().filter(x -> x.getRootID() != null)
								          .filter(x -> x.getRootID().equals(rootID)).collect(Collectors.toCollection(ArrayList::new));

							}

							if (rootEntMdtColl != null)
							{
								rootEntMdtColl.add(0, rootMdt);
							}

						}
					}
					catch (NoSuchElementException NE)
					{
						// Do Nothing
					}

				}
			}
		}

		return rootEntMdtColl;
	}

	/*********************************************************************************************
	 * Generate Prepared Statements for Passed Connection through Meatadata Objects contained
	 * 
	 * @param Conn
	 *             - SQL Connection
	 * @throws EX_PKeynotSpecified
	 *              - PKey declared as part of Object but not maintained
	 * @throws SQLException
	 *              - - Any SQL error
	 *********************************************************************************************/
	public void generate_PreparedStatements(Connection Conn, List<EntityMetadata> mdtColl) throws EX_PKeynotSpecified, SQLException
	{
		if (Conn != null)
		{

			for ( EntityMetadata entityMetadata : mdtColl )
			{
				entityMetadata.generate_PreparedStatement(Conn);

				// Execute Prepared Statemnt and syncronize keys
				// if (entityMetadata.getEntityMode() == entityMode.CREATE)
				// {
				execute_synchronize(entityMetadata);
				// }
				// Simply Execute the prepared statements in case of Change and Delete- No key syncronization needed
				// else
				// {
				if (entityMetadata.getEntityMode() == entityMode.DELETE || entityMetadata.getEntityMode() == entityMode.CHANGE)
				{
					entityMetadata.getPreparedStatement().executeUpdate();
					/**
					 * For Delete Entity Mode - Only deleting the Root Entity should trigger an cascading
					 * delete on
					 * the child entities - NO need to further process child entities in this case
					 * Root Object will always be the first entity metadata to be processed
					 */
					if (entityMetadata.getEntityMode() == entityMode.DELETE && entityMetadata.getObjectType() == objectType.ROOT_OBJECT)
					     // No futher Child entity metadata processing needed
					     return;
				}
				// }
			}
		}
	}

	/**
	 * Execute Prepared Statment for Metadata and trigger synchronization
	 * 
	 * @param entMdt
	 *             - EntityMetadata - Currently Assigned a genearated Pstmt
	 * @throws SQLException
	 * @throws EX_PKeynotSpecified
	 *              - Primary Key not found while creating Child Entities
	 */
	@SuppressWarnings(
	{ "static-access", "rawtypes"
	})
	private void execute_synchronize(EntityMetadata entMdt) throws SQLException, EX_PKeynotSpecified
	{
		/**
		 * Execute the Prepared Statement and Syncronize Keys and relevant Fkey
		 * Attribute of Child Relation
		 */

		if (entMdt.isKeyGen() != true)
		{
			/**
			 * Execute Prepared Statement if it is a Create/Update/Delete Scenario
			 */
			if (entMdt.getEntityMode() == entityMode.CREATE || entMdt.getEntityMode() == entityMode.CHANGE
			          || entMdt.getEntityMode() == entityMode.DELETE)
			{
				entMdt.getPreparedStatement().executeUpdate();
			}

			// Start with Syncronization
			if (entMdt.getObjectType() == objectType.ROOT_OBJECT)
			{

				// Get Key for Root Object
				String propPkey = entMdt.getPrimaryKey().getObjField();
				if (propPkey != null)
				{
					/**
					 * If the Primary key is not part of Changed Properties of Lower Hierarchy
					 * Relations
					 * Add to Change Property the value
					 */
					Object pkeyValue = entMdt.getPrimaryKey().getValue();
					for ( EntityMetadata entityMetadata : metadataColl )
					{
						/**
						 * Only for Lower Hierarchy Relations in Create/Change Mode as you might just open/edit a
						 * root and add a relation at later time
						 */
						if (entityMetadata.getHierarchy() == 1 && (entityMetadata.getEntityMode() == entityMode.CREATE
						          || entityMetadata.getEntityMode() == entityMode.CHANGE) && entityMetadata.getParentID() != null)

						{
							if (entityMetadata.getParentID().equals(entMdt.getSelfID()))
							{
								boolean propFound = entityMetadata.getChangedProperties().stream()
								          .anyMatch(x -> x.getFieldName().equals(propPkey));
								if (propFound != true)
								{
									/**
									 * Calling Program has not set the value
									 * Framework needs to set the same
									 */
									// Get the value from Current Entity Metadata Changed
									// Properties
									try
									{
										if (pkeyValue == null)
										{
											pkeyValue = entMdt.getChangedProperties().stream()
											          .filter(x -> x.getFieldName().equals(propPkey)).findFirst().get().getValue();
										}
									}
									catch (NoSuchElementException NE)
									{
										// Primary Key Not Set in Changed Properties of Parent
										// Relation
										// Neither it is updated in Pkey of the Root Object
										// Invalid Key Exception
										// ERR_NOPKEY=Primary Key {0} must be set to Save Entity
										// Instance of Type {1}
										GeneralMessage msgReset = new GeneralMessage("ERR_NOPKEY", new Object[]
										{ propPkey, entMdt.getObjectName()
										});
										this.getRootObject().getFW_Manager().getMessageFormatter().generate_message_snippet(msgReset);
										throw new EX_PKeynotSpecified(new Object[]
										{ propPkey, entMdt.getObjectName()
										});
									}

									// Get Object Info First
									Object_Info objInfo = this.getRootObject().getFW_Manager().getObjectsInfoFactory()
									          .Get_ObjectInfo_byName(entityMetadata.getObjectName());
									if (objInfo != null)
									{
										// Get the property DataType First from Object Info
										/**
										 * Get Parameter Type from Corrsponding Setter Method
										 */
										Method setMethodKeyProp = objInfo.Get_Setter_for_FieldName(propPkey);
										if (setMethodKeyProp != null)
										{
											Class[] typesarray = setMethodKeyProp.getParameterTypes();
											String propType = typesarray[0].getSimpleName();
											entityMetadata.getChangedProperties().add(new Changed_Property(propPkey, propType, pkeyValue));
										}
									}
								}
							}
						}
					}
				}

			}

			else if (entMdt.getObjectType() == objectType.DEPENDANT_OBJECT)
			{
				// Get Key for Root Object
				String propPkey = entMdt.getPrimaryKey().getObjField();
				if (propPkey != null)
				{
					/**
					 * If the Primary key is not part of Changed Properties of Lower Hierarchy
					 * Relations
					 * Add to Change Property the value
					 */
					Object pkeyValue = entMdt.getPrimaryKey().getValue();

					// Add to Changed Properties Collection of all Hierarchy + 1 relations
					// matching
					// their parent dependant object with current
					// entity Metadata entity (dependant Object)
					/**
					 * This will inject this property in Child Relations during creation of
					 * their Prepared Statements only in case the dependant entity is a new
					 * Entity - i.e Create mode
					 * Also Set the Foreign Key in Lower Hierarchy relations for Completeness
					 */
					int currHrcy = entMdt.getHierarchy();
					for ( EntityMetadata entityMetadata : metadataColl )
					{
						if (entityMetadata.getHierarchy() == (currHrcy + 1) && entityMetadata.getObjectType() == objectType.DEPENDANT_OBJECT
						          && (entityMetadata.getEntityMode() == entityMode.CREATE
						                    || entityMetadata.getEntityMode() == entityMode.CHANGE)
						          && entityMetadata.getParentID().equals(entMdt.getSelfID()))
						// Ony Update in relevant Dependant Entity Metadata Object(s) and not all lower Hierarchy
						// ones
						{
							DependantObject depObj_lowerhrcy = (DependantObject) entityMetadata.getEntity();
							if (depObj_lowerhrcy.getParentDependant().equals(entMdt.getEntity()))
							{
								// Get the value from Current Entity Metadata Changed
								// Properties
								try
								{
									if (pkeyValue == null)
									{
										pkeyValue = entMdt.getChangedProperties().stream().filter(x -> x.getFieldName().equals(propPkey))
										          .findFirst().get().getValue();
									}
								}
								catch (NoSuchElementException NE)
								{
									// Primary Key Not Set in Changed Properties of Parent
									// Relation
									// Neither it is updated in Pkey of the Root Object
									// Invalid Key Exception
									// ERR_NOPKEY=Primary Key {0} must be set to Save Entity
									// Instance of Type {1}
									GeneralMessage msgReset = new GeneralMessage("ERR_NOPKEY", new Object[]
									{ propPkey, entMdt.getObjectName()
									});
									this.getRootObject().getFW_Manager().getMessageFormatter().generate_message_snippet(msgReset);
									throw new EX_PKeynotSpecified(new Object[]
									{ propPkey, entMdt.getObjectName()
									});
								}

								// Get Object Info First
								Object_Info objInfo = this.getRootObject().getFW_Manager().getObjectsInfoFactory()
								          .Get_ObjectInfo_byName(entityMetadata.getObjectName());
								if (objInfo != null)
								{
									// Get the property DataType First from Object Info
									/**
									 * Get Parameter Type from Corrsponding Setter Method
									 */
									Method setMethodKeyProp = objInfo.Get_Setter_for_FieldName(propPkey);
									if (setMethodKeyProp != null)
									{
										Class[] typesarray = setMethodKeyProp.getParameterTypes();
										String propType = typesarray[0].getSimpleName();

										entityMetadata.getChangedProperties().add(new Changed_Property(propPkey, propType, pkeyValue));
										entityMetadata.getForeignKey().setValue(pkeyValue);
										break;

									}
								}

							}

						}

					}
				}

			}

		}

		else if (entMdt.isKeyGen() == true)
		{

			int keyGenerated = 0;
			int rows_affected = entMdt.getPreparedStatement().executeUpdate();
			modelEnums.entityMode entModePrev = entMdt.getEntityMode();

			entMdt.setEntityMode(entityMode.REFRESHED);
			/**
			 * If Key Generation is set to true - need to get key and propogate it
			 */
			if (rows_affected == 1)
			{
				ResultSet rs = null;

				// Test IF COndition - to be removed in case of DUMP while SAVING
				if (entModePrev == entityMode.CREATE)
				{
					rs = entMdt.getPreparedStatement().getGeneratedKeys();
					while (rs.next())
					{
						keyGenerated = rs.getInt(1);
					}
					// close the resultset - no longer needed in further execution
					rs.close();
				}

				// Test ELSE COndition - to be removed in case of DUMP while SAVING
				else
				{
					keyGenerated = (int) entMdt.getPrimaryKey().getValue();
				}
				/**
				 * If OBject is Root Object
				 * This key needs to be set in
				 * - Primary Key of Root Object
				 * - All the level 1 Hierarchies of this Root as Changed Property there
				 * since there can be only 1 RootObject in an Entity Manager
				 */

				if (entMdt.getObjectType() == objectType.ROOT_OBJECT)
				{
					entMdt.getPrimaryKey().setValue(keyGenerated);

					// Get Primary Key Name
					String propPkey = entMdt.getPrimaryKey().getTableField();
					if (propPkey != null)
					{
						// Add to Changed Properties Collection of all Hierarchy 1 relations
						/**
						 * This will inject this property in Child Relations during creation of
						 * their
						 * Prepared
						 * Statements
						 */
						for ( EntityMetadata entityMetadata : metadataColl )
						{
							if (entityMetadata.getHierarchy() == 1
							          && (entityMetadata.getEntityMode() == entityMode.CREATE
							                    || entityMetadata.getEntityMode() == entityMode.CHANGE)
							          && entityMetadata.getParentID() != null)
							{
								if (entityMetadata.getParentID().equals(entMdt.getSelfID()))
								{
									entityMetadata.getChangedProperties().add(new Changed_Property(propPkey, "Integer", keyGenerated));
								}

							}

						}
					}
				}

				if (entMdt.getObjectType() == objectType.DEPENDANT_OBJECT)
				{
					entMdt.getPrimaryKey().setValue(keyGenerated);

					// Get Primary Key Name
					String propPkey = entMdt.getPrimaryKey().getTableField();
					if (propPkey != null)
					{
						// Add to Changed Properties Collection of all Hierarchy + 1 relations
						// matching
						// their parent dependant object with current
						// entity Metadata entity (dependant Object)
						/**
						 * This will inject this property in Child Relations during creation of
						 * their Prepared Statements only in case the dependant entity is a new
						 * Entity - i.e Create mode
						 * Also Set the Foreign Key in Lower Hierarchy relations for Completeness
						 */
						int currHrcy = entMdt.getHierarchy();
						for ( EntityMetadata entityMetadata : metadataColl )
						{
							if (entityMetadata.getHierarchy() == (currHrcy + 1)
							          && entityMetadata.getObjectType() == objectType.DEPENDANT_OBJECT
							          && (entityMetadata.getEntityMode() == entityMode.CREATE
							                    || entityMetadata.getEntityMode() == entityMode.CHANGE)
							          && entityMetadata.getParentID().equals(entMdt.getSelfID()))
							{
								DependantObject depObj_lowerhrcy = (DependantObject) entityMetadata.getEntity();
								if (depObj_lowerhrcy.getParentDependant().equals(entMdt.getEntity()))
								{
									entityMetadata.getChangedProperties().add(new Changed_Property(propPkey, "Integer", keyGenerated));
									entityMetadata.getForeignKey().setValue(keyGenerated);
								}

							}

						}
					}

				}

			}
		}
	}

}
