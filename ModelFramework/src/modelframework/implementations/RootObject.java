package modelframework.implementations;

import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Level;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import modelframework.JAXB.definitions.objschemas.Dependant_Object_Defn;
import modelframework.JAXB.definitions.objschemas.ObjectSchema;
import modelframework.annotations.Saveable;
import modelframework.definitions.EntityManager;
import modelframework.definitions.EntityMetadata;
import modelframework.definitions.Object_Info;
import modelframework.enums.system.modelEnums.entityMode;
import modelframework.enums.system.modelEnums.objectType;
import modelframework.exceptions.EX_InvalidRelationException;
import modelframework.exceptions.EX_ParentEntitynotFound;
import modelframework.exposed.FrameworkManager;
import modelframework.types.TY_Filter;
import modelframework.utilities.CglibHelper;

/*********************************************************************************
 * System Class - Main Class to hold Root Objects Information - DO NOT CHANGE
 *********************************************************************************/
@Component
public class RootObject implements ApplicationContextAware
{

	private static FrameworkManager	FrameworkManager;

	private static MessageSource		MessageSource;

	private static ApplicationContext	Context;

	@Autowired
	private EntityManager			entityManager;

	/**
	 * @return the entityManager
	 */
	public EntityManager getEntityManager()
	{
		return entityManager;
	}

	public FrameworkManager getFW_Manager()
	{
		return FrameworkManager;
	}

	/**
	 * @return the context
	 */
	public static ApplicationContext getContext()
	{
		return Context;
	}

	public RootObject()
	{

	}

	/*************************************************************************************************
	 * Create Related Entity for Root Object passing the Relation Name
	 * 
	 * @param relationName
	 *             - String Name of Relation with Root Object
	 * @return - Dependent Object Bean Instance
	 * @throws EX_InvalidRelationException
	 *********************************************************************************************/
	@SuppressWarnings(
	{ "unchecked", "static-access"
	})
	public DependantObject Create_RelatedEntity(String relationName) throws EX_InvalidRelationException
	{
		ObjectSchema RootSchema = FrameworkManager.getObjectSchemaFactory().Get_Schema_byRootObjName(this.getClass().getSimpleName());
		DependantObject depObj = null;
		DependantObject test_instance = null;
		if (relationName != null)
		{
			// 1. Validate the Relation w.r.t Root Object from RootSchema
			if (RootSchema != null)
			{
				// Get the Relations details if it is valid
				Dependant_Object_Defn relDetails = RootSchema.getRelationDetails(relationName);
				if (relDetails != null)
				{
					// 2. Get the Dependant Object Name from relation
					// details
					String depobjName = relDetails.getDepobjname();
					if (depobjName != null)
					{
						// 3. Get Object info details for the Object
						// Get Object Information from Object Factory
						Object_Info obj_info = FrameworkManager.getObjectsInfoFactory().Get_ObjectInfo_byName(depobjName);
						if (obj_info != null)
						{
							// Create the Object of this Class
							@SuppressWarnings("rawtypes")
							Class obj_class = obj_info.getCurr_Obj_Class();
							if (obj_class != null)
							{

								// Validate if the declared object is
								// inherited from Dependant Object and
								// can be
								// Instantiated as one

								try
								{
									test_instance = (DependantObject) obj_class.newInstance();
								}
								catch (InstantiationException | IllegalAccessException e)
								{
									FrameworkManager.getLogger().log(Level.SEVERE,
									          MessageSource.getMessage("INSTANTIATION_FAILURE", new Object[]
											{ depobjName
											}, null, Locale.ENGLISH));
								}
								if (test_instance != null && test_instance instanceof DependantObject)
								{
									if (Context != null && entityManager != null)
									{
										// Create Prototype BEan of
										// requested Type
										depObj = (DependantObject) Context.getBean(depobjName, obj_class);
										// Set the Root Object Parent
										// - Target Unproxied Object
										depObj.Inject_Parent_RootObject(this.getEntityManager().getRootObject(this));

										// Create Entity Metadata
										EntityMetadata entMdt = new EntityMetadata(relDetails, entityMode.CREATE);
										// Set Entity Object in
										// Metadata

										try
										{
											CglibHelper cgHelper = new CglibHelper(depObj);
											Object entity = cgHelper.getTargetObject();

											entMdt.setEntity(entity);

											// Set User Aware on
											// Child Relation
											// hierarchy 1 if Root
											// has it
											Object_Info parentobjInfo = FrameworkManager.getObjectsInfoFactory()
											          .Get_ObjectInfo_byName(relDetails.getRootobjname());
											if (parentobjInfo != null)
											{

												entMdt.setUserAware(parentobjInfo.IsUserAware());
											}

											// Set Current User if
											// Object is UserAware
											if (entMdt.isUserAware())
											{
												entMdt.setCurrentUser(FrameworkManager.getUserManager().Get_LoggedUser());
											}

											/**
											 * Add the UUID in Dependant Entity Metadata
											 * selfID - New UUID
											 * parentID - Root UUID
											 * rootID - RootUUID
											 */
											String selfID = this.entityManager.getselfID(this, objectType.ROOT_OBJECT);
											if (selfID != null)
											{
												entMdt.setParentID(selfID);
												entMdt.setSelfID(UUID.randomUUID().toString());
												entMdt.setRootID(selfID);
											}
											else
											{
												/**
												 * ERR_NOPARENT=No Parent Entity of type - {0} could be
												 * found for requested relation - {1}
												 */
												EX_ParentEntitynotFound noParent = new EX_ParentEntitynotFound(new Object[]
												{ RootSchema.getRootObjectMetadata().getObjectname(), relationName
												});
												FrameworkManager.getMessageFormatter().generate_message_snippet(noParent);
												throw noParent;
											}

											// Entity Metadata is
											// complete - Add to
											// Entity Manager
											ArrayList<EntityMetadata> mdtColl = entityManager.getMetadataColl();
											if (mdtColl != null)
											{
												entityManager.getMetadataColl().add(entMdt);
											}

											// Generate Dependant
											// Object Creation
											// Message in LOG
											FrameworkManager.getLogger().log(Level.INFO,
											          MessageSource.getMessage("SUCC_DEPCR", new Object[]
													{ depobjName, RootSchema.getRootObjectMetadata().getObjectname()
													}, null, Locale.ENGLISH));

										}
										catch (Exception e) // Proxy
										                    // Cast to
										                    // Target
										                    // Type
										                    // Error
										{
											FrameworkManager.getLogger().log(Level.SEVERE,
											          MessageSource.getMessage("ERR_PROXYCAST", new Object[]
													{ depobjName, e.getMessage()
													}, null, Locale.ENGLISH));
										}
									}
								}

							}

						}
					}
				}
				else // Invalid Relation Error Handling
				{
					// Throw Invalid Relation Exception
					// ERR_INVALIDREL= Invalid Relation {0} requested for
					// Base Type {1}!
					EX_InvalidRelationException exInvRel = new EX_InvalidRelationException(new Object[]
					{ relationName, RootSchema.getRootObjectMetadata().getObjectname()
					});
					FrameworkManager.getMessageFormatter().generate_message_snippet(exInvRel);
					throw exInvRel;

				}
			}
		}

		return depObj;
	}

	/*************************************************************************************************
	 * SYSTEM METHOD!! Not to be User Externally!!! DO NOT USE. USE Create_RelatedEntity method instead!!
	 * Create Related Entity for Root Object passing the Relation Name
	 * 
	 * @param relationName
	 *             - String Name of Relation with Root Object
	 * @return - Dependent Object Bean Instance
	 * @throws EX_InvalidRelationException
	 *********************************************************************************************/
	@SuppressWarnings(
	{ "unchecked", "static-access"
	})
	public DependantObject Create_RelatedEntityDB(String relationName) throws EX_InvalidRelationException
	{
		ObjectSchema RootSchema = FrameworkManager.getObjectSchemaFactory().Get_Schema_byRootObjName(this.getClass().getSimpleName());
		DependantObject depObj = null;
		DependantObject test_instance = null;
		if (relationName != null)
		{
			// 1. Validate the Relation w.r.t Root Object from RootSchema
			if (RootSchema != null)
			{
				// Get the Relations details if it is valid
				Dependant_Object_Defn relDetails = RootSchema.getRelationDetails(relationName);
				if (relDetails != null)
				{
					// 2. Get the Dependant Object Name from relation
					// details
					String depobjName = relDetails.getDepobjname();
					if (depobjName != null)
					{
						// 3. Get Object info details for the Object
						// Get Object Information from Object Factory
						Object_Info obj_info = FrameworkManager.getObjectsInfoFactory().Get_ObjectInfo_byName(depobjName);
						if (obj_info != null)
						{
							// Create the Object of this Class
							@SuppressWarnings("rawtypes")
							Class obj_class = obj_info.getCurr_Obj_Class();
							if (obj_class != null)
							{

								// Validate if the declared object is
								// inherited from Dependant Object and
								// can be
								// Instantiated as one

								try
								{
									test_instance = (DependantObject) obj_class.newInstance();
								}
								catch (InstantiationException | IllegalAccessException e)
								{
									FrameworkManager.getLogger().log(Level.SEVERE,
									          MessageSource.getMessage("INSTANTIATION_FAILURE", new Object[]
											{ depobjName
											}, null, Locale.ENGLISH));
								}
								if (test_instance != null && test_instance instanceof DependantObject)
								{
									if (Context != null && entityManager != null)
									{
										// Create Prototype BEan of
										// requested Type
										depObj = (DependantObject) Context.getBean(depobjName, obj_class);
										// Set the Root Object Parent
										// - Target Unproxied Object
										depObj.Inject_Parent_RootObject(this.getEntityManager().getRootObject(this));

										// Create Entity Metadata
										EntityMetadata entMdt = new EntityMetadata(relDetails, entityMode.REFRESHED);
										// Set Entity Object in
										// Metadata

										try
										{
											CglibHelper cgHelper = new CglibHelper(depObj);
											Object entity = cgHelper.getTargetObject();

											entMdt.setEntity(entity);

											// Set User Aware on
											// Child Relation
											// hierarchy 1 if Root
											// has it
											Object_Info parentobjInfo = FrameworkManager.getObjectsInfoFactory()
											          .Get_ObjectInfo_byName(relDetails.getRootobjname());
											if (parentobjInfo != null)
											{

												entMdt.setUserAware(parentobjInfo.IsUserAware());
											}

											// Set Current User if
											// Object is UserAware
											if (entMdt.isUserAware())
											{
												entMdt.setCurrentUser(FrameworkManager.getUserManager().Get_LoggedUser());
											}

											/**
											 * Add the UUID in Dependant Entity Metadata
											 * selfID - New UUID
											 * parentID - Root UUID
											 * rootID - RootUUID
											 */
											String selfID = this.entityManager.getselfID(this, objectType.ROOT_OBJECT);
											if (selfID != null)
											{
												entMdt.setParentID(selfID);
												entMdt.setSelfID(UUID.randomUUID().toString());
												entMdt.setRootID(selfID);
											}
											else
											{
												/**
												 * ERR_NOPARENT=No Parent Entity of type - {0} could be
												 * found for requested relation - {1}
												 */
												EX_ParentEntitynotFound noParent = new EX_ParentEntitynotFound(new Object[]
												{ RootSchema.getRootObjectMetadata().getObjectname(), relationName
												});
												FrameworkManager.getMessageFormatter().generate_message_snippet(noParent);
												throw noParent;
											}

											// Entity Metadata is
											// complete - Add to
											// Entity Manager
											ArrayList<EntityMetadata> mdtColl = entityManager.getMetadataColl();
											if (mdtColl != null)
											{
												entityManager.getMetadataColl().add(entMdt);
											}

											// Generate Dependant
											// Object Creation
											// Message in LOG
											FrameworkManager.getLogger().log(Level.INFO,
											          MessageSource.getMessage("SUCC_DEPCR", new Object[]
													{ depobjName, RootSchema.getRootObjectMetadata().getObjectname()
													}, null, Locale.ENGLISH));

										}
										catch (Exception e) // Proxy
										                    // Cast to
										                    // Target
										                    // Type
										                    // Error
										{
											FrameworkManager.getLogger().log(Level.SEVERE,
											          MessageSource.getMessage("ERR_PROXYCAST", new Object[]
													{ depobjName, e.getMessage()
													}, null, Locale.ENGLISH));
										}
									}
								}

							}

						}
					}
				}
				else // Invalid Relation Error Handling
				{
					// Throw Invalid Relation Exception
					// ERR_INVALIDREL= Invalid Relation {0} requested for
					// Base Type {1}!
					EX_InvalidRelationException exInvRel = new EX_InvalidRelationException(new Object[]
					{ relationName, RootSchema.getRootObjectMetadata().getObjectname()
					});
					FrameworkManager.getMessageFormatter().generate_message_snippet(exInvRel);
					throw exInvRel;

				}
			}
		}

		return depObj;
	}

	/**************************************************************************************************
	 * Get Related Entities for the Root Object by specifying the relation name
	 * Handled via RelatedEntities Aspect
	 * 
	 * @param relationName
	 *             - Relation Name
	 * @return - Array List of Entities
	 * @throws EX_InvalidRelationException
	 **************************************************************************************************/
	public <T> ArrayList<T> getRelatedEntities(String relationName) throws EX_InvalidRelationException
	{
		return null;
	}

	/**************************************************************************************************
	 * Get Related Entities for the Root Object by specifying the relation name and filter TY_Filter
	 * Handled via RelatedEntities Aspect
	 * 
	 * @param relationName
	 *             - String Name of Relation
	 * @param filter
	 *             - Filter of type TY_Filter
	 * @return - Beans of related Entities result
	 * @throws EX_InvalidRelationException
	 **************************************************************************************************/
	public <T> ArrayList<T> getRelatedEntitieswithFilter(String relationName, TY_Filter filter) throws EX_InvalidRelationException
	{
		return null;
	}

	/*********************************************************************************
	 * Save the Root Object
	 * 
	 * @return - Boolean Saved
	 *********************************************************************************/
	@Saveable
	@CacheEvict(value = "searchResults", allEntries = true)
	public boolean Save()
	{
		boolean saved = false;

		return saved;
	}

	/*********************************************************************************
	 * Lock the Root Object for Edit Process
	 * Dealt Within Lock Aspect
	 * 
	 * @return - boolean Value true/false accordingly lock could be obtained or not
	 *********************************************************************************/
	public boolean lock()
	{
		return null != null;
	}

	/*********************************************************************************
	 * Delete the Root Object
	 * Dealt Within Lock Aspect
	 * 
	 * @return - boolean Value true/false accordingly lock could be deleted or not
	 *********************************************************************************/
	public boolean delete()
	{
		return null != null;
	}

	/********************************************************************************
	 * Get the Current Lock Details for the Object
	 * Dealt Within Lock Aspect
	 * 
	 * @return - String Message with Current Lock Details and Time
	 *********************************************************************************/
	public String getCurrentLockDetails()
	{
		String msg = null;

		return msg;
	}

	/********************************************************************************
	 * Switch the entity to change mode for editing properties
	 * 
	 * @return - boolean value true if change mode enabled or false otherwise
	 *********************************************************************************/

	public boolean switchtoChangeMode()
	{
		boolean ischangeable = false;

		return ischangeable;
	}

	/********************************************************************************
	 * Determine if Object is in Locked Mode
	 * 
	 * @return - boolean value true if lock mode enabled or false otherwise
	 *********************************************************************************/

	public boolean isLocked()
	{
		boolean islocked = false;

		return islocked;
	}

	/********************************************************************************
	 * Determine if Object is in Change Mode
	 * 
	 * @return - boolean value true if change mode enabled or false otherwise
	 *********************************************************************************/

	public boolean isChangebale()
	{
		boolean ischangeable = false;

		return ischangeable;
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException
	{
		if (context != null)
		{
			FrameworkManager = (FrameworkManager) context.getBean("FrameworkManager");
			MessageSource = (org.springframework.context.MessageSource) context.getBean("messageSource");
			Context = context;

		}

	}

}
