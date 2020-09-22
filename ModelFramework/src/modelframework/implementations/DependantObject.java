package modelframework.implementations;

import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Level;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import modelframework.JAXB.definitions.objschemas.Dependant_Object_Defn;
import modelframework.JAXB.definitions.objschemas.ObjectSchema;
import modelframework.definitions.EntityMetadata;
import modelframework.definitions.Object_Info;
import modelframework.enums.system.modelEnums.entityMode;
import modelframework.exceptions.EX_InvalidRelationException;
import modelframework.exposed.FrameworkManager;
import modelframework.types.TY_Filter;
import modelframework.utilities.CglibHelper;

/*********************************************************************************
 * System Class - Main Class to hold Dependant Objects Information - DO NOT CHANGE
 *********************************************************************************/
@Component
public class DependantObject implements ApplicationContextAware
{
	private FrameworkManager		FrameworkManager;

	private MessageSource		MessageSource;

	private ApplicationContext	Context;

	// Parent Root Object Reference
	private RootObject			parentRoot;

	// Parent Dependant Object Reference
	private DependantObject		parentDependant;

	public FrameworkManager getFW_Manager()
	{
		return FrameworkManager;
	}

	/**
	 * @return the parent -- cannot be set from Dependant - Read Only property Can be set only from Root
	 *         Object
	 */
	public RootObject getParentRoot()
	{
		return parentRoot;
	}

	/**
	 * @return the parentDependant
	 */
	public DependantObject getParentDependant()
	{
		return parentDependant;
	}

	/**
	 * @param parentDependant
	 *             the parentDependant to set
	 */
	public void Inject_ParentDependant(DependantObject parentDependant)
	{
		this.parentDependant = parentDependant;
	}

	public void Inject_Parent_RootObject(RootObject parent)
	{
		this.parentRoot = parent;
	}

	public DependantObject()
	{

	}

	/*************************************************************************************************
	 * Create Related Entity for Depenadant Object passing the Relation Name
	 * 
	 * @param Relationname
	 *             - String Name of Relation with Dependant Object
	 * @return - Dependant Object Bean Instance
	 * @throws EX_InvalidRelationException
	 *********************************************************************************************/
	@SuppressWarnings(
	{ "static-access", "unchecked"
	})
	public DependantObject Create_RelatedEntity(String Relationname) throws EX_InvalidRelationException
	{
		DependantObject depObj = null;
		DependantObject test_instance = null;
		// Get Parent's Root Object Schema
		ObjectSchema RootSchema = FrameworkManager.getObjectSchemaFactory()
		          .Get_Schema_byRootObjName(this.getParentRoot().getClass().getSimpleName());
		// Relation name provided and Root Object already assigned/injected to parent dependant object
		// which has Root Object Schema hierarchy loaded
		if (Relationname != null && this.parentRoot != null && RootSchema != null)
		{
			// Get the Relations details if it is valid
			Dependant_Object_Defn relDetails = RootSchema.getRelationDetails(Relationname);
			if (relDetails != null)
			{
				// 2. Get the Dependant Object Name from relation details
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

							// Validate if the declared object is inherited from Dependant
							// Object and can be instatiated as one

							try
							{
								test_instance = (DependantObject) obj_class.newInstance();
							}
							catch (InstantiationException | IllegalAccessException e)
							{
								FrameworkManager.getLogger().log(Level.SEVERE, MessageSource.getMessage("INSTANTIATION_FAILURE", new Object[]
								{ depobjName
								}, null, Locale.ENGLISH));
							}
							if (test_instance != null && test_instance instanceof DependantObject)
							{
								if (Context != null && this.getParentRoot().getEntityManager() != null)
								{
									// Create Prototype BEan of requested Type
									depObj = (DependantObject) Context.getBean(depobjName, obj_class);

									Object root = this.getParentRoot();
									String rootID = this.getParentRoot().getEntityManager().getselfID(root);

									if (root != null)
									{
										// Set the Root Object Parent - Target Unproxied Object
										depObj.Inject_Parent_RootObject(this.getParentRoot());

									}
									// Set Current Oject as Parent Dependant Object after
									// getting Target Object
									try
									{
										CglibHelper cgHelper = new CglibHelper(this);
										Object parentDep = cgHelper.getTargetObject();

										depObj.Inject_ParentDependant((DependantObject) parentDep);
									}

									catch (Exception e) // Proxy Cast to Target Type Error
									{
										FrameworkManager.getLogger().log(Level.SEVERE,
										          MessageSource.getMessage("ERR_PROXYCAST", new Object[]
												{ depobjName, e.getMessage()
												}, null, Locale.ENGLISH));
									}
									// Create Entity Metadata
									EntityMetadata entMdt = new EntityMetadata(relDetails, entityMode.CREATE);
									// Set Entity Object in Metadata
									try
									{
										CglibHelper cgHelper = new CglibHelper(depObj);
										Object entity = cgHelper.getTargetObject();

										entMdt.setEntity(entity);

										// Also set the User Aware Property as derived
										// from Parent Root ObjectInfo Class
										Object_Info rootobjInfo = FrameworkManager.getObjectsInfoFactory()
										          .Get_ObjectInfo_byName((this.getParentRoot().getClass().getSimpleName()));
										if (rootobjInfo != null)
										{
											entMdt.setUserAware(rootobjInfo.IsUserAware());
										}

										// Set Current User if Object is UserAware
										if (entMdt.isUserAware())
										{
											entMdt.setCurrentUser(FrameworkManager.getUserManager().Get_LoggedUser());
										}

										/**
										 * UUIDS set
										 * selfID - New UUID
										 * parentID - parent Object ID
										 * rootID - parentRoot ID
										 */
										String parentID = this.parentRoot.getEntityManager().getselfID(this);
										if (parentID != null)
										{
											entMdt.setSelfID(UUID.randomUUID().toString());
											entMdt.setParentID(parentID);
											entMdt.setRootID(rootID);
										}

										// Entity Metadata is complete - Add to Entity
										// Manager

										ArrayList<EntityMetadata> mdtColl = this.parentRoot.getEntityManager().getMetadataColl();
										if (mdtColl != null)
										{
											this.parentRoot.getEntityManager().getMetadataColl().add(entMdt);
										}

										// Generate Dependant Object Creation Message in
										// LOG
										FrameworkManager.getLogger().log(Level.INFO, MessageSource.getMessage("SUCC_DEPCR", new Object[]
										{ depobjName, this.getClass().getSimpleName()
										}, null, Locale.ENGLISH));

									}
									catch (Exception e) // Proxy Cast to Target Type Error
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
				// ERR_INVALIDREL= Invalid Relation {0} requested for Base Type {1}!
				EX_InvalidRelationException exInvRel = new EX_InvalidRelationException(new Object[]
				{ Relationname, RootSchema.getRootObjectMetadata().getObjectname()
				});
				FrameworkManager.getMessageFormatter().generate_message_snippet(exInvRel);
				throw exInvRel;

			}
		}

		return depObj;
	}

	/*************************************************************************************************
	 * * SYSTEM METHOD!! Not to be User Externally!!! DO NOT USE. USE Create_RelatedEntity method instead!!
	 * Create Related Entity for Depenadant Object passing the Relation Name
	 * 
	 * @param Relationname
	 *             - String Name of Relation with Dependant Object
	 * @return - Dependant Object Bean Instance
	 * @throws EX_InvalidRelationException
	 *********************************************************************************************/
	@SuppressWarnings(
	{ "static-access", "unchecked"
	})
	public DependantObject Create_RelatedEntityDB(String Relationname) throws EX_InvalidRelationException
	{
		DependantObject depObj = null;
		DependantObject test_instance = null;
		// Get Parent's Root Object Schema
		ObjectSchema RootSchema = FrameworkManager.getObjectSchemaFactory()
		          .Get_Schema_byRootObjName(this.getParentRoot().getClass().getSimpleName());
		// Relation name provided and Root Object already assigned/injected to parent dependant object
		// which has Root Object Schema hierarchy loaded
		if (Relationname != null && this.parentRoot != null && RootSchema != null)
		{
			// Get the Relations details if it is valid
			Dependant_Object_Defn relDetails = RootSchema.getRelationDetails(Relationname);
			if (relDetails != null)
			{
				// 2. Get the Dependant Object Name from relation details
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

							// Validate if the declared object is inherited from Dependant
							// Object and can be instatiated as one

							try
							{
								test_instance = (DependantObject) obj_class.newInstance();
							}
							catch (InstantiationException | IllegalAccessException e)
							{
								FrameworkManager.getLogger().log(Level.SEVERE, MessageSource.getMessage("INSTANTIATION_FAILURE", new Object[]
								{ depobjName
								}, null, Locale.ENGLISH));
							}
							if (test_instance != null && test_instance instanceof DependantObject)
							{
								if (Context != null && this.getParentRoot().getEntityManager() != null)
								{
									// Create Prototype BEan of requested Type
									depObj = (DependantObject) Context.getBean(depobjName, obj_class);

									Object root = this.getParentRoot();
									String rootID = this.getParentRoot().getEntityManager().getselfID(root);

									if (root != null)
									{
										// Set the Root Object Parent - Target Unproxied Object
										depObj.Inject_Parent_RootObject(this.getParentRoot());

									}
									// Set Current Oject as Parent Dependant Object after
									// getting Target Object
									try
									{
										CglibHelper cgHelper = new CglibHelper(this);
										Object parentDep = cgHelper.getTargetObject();

										depObj.Inject_ParentDependant((DependantObject) parentDep);
									}

									catch (Exception e) // Proxy Cast to Target Type Error
									{
										FrameworkManager.getLogger().log(Level.SEVERE,
										          MessageSource.getMessage("ERR_PROXYCAST", new Object[]
												{ depobjName, e.getMessage()
												}, null, Locale.ENGLISH));
									}
									// Create Entity Metadata
									EntityMetadata entMdt = new EntityMetadata(relDetails, entityMode.REFRESHED);
									// Set Entity Object in Metadata
									try
									{
										CglibHelper cgHelper = new CglibHelper(depObj);
										Object entity = cgHelper.getTargetObject();

										entMdt.setEntity(entity);

										// Also set the User Aware Property as derived
										// from Parent Root ObjectInfo Class
										Object_Info rootobjInfo = FrameworkManager.getObjectsInfoFactory()
										          .Get_ObjectInfo_byName((this.getParentRoot().getClass().getSimpleName()));
										if (rootobjInfo != null)
										{
											entMdt.setUserAware(rootobjInfo.IsUserAware());
										}

										// Set Current User if Object is UserAware
										if (entMdt.isUserAware())
										{
											entMdt.setCurrentUser(FrameworkManager.getUserManager().Get_LoggedUser());
										}

										/**
										 * UUIDS set
										 * selfID - New UUID
										 * parentID - parent Object ID
										 * rootID - parentRoot ID
										 */
										String parentID = this.parentRoot.getEntityManager().getselfID(this);
										if (parentID != null)
										{
											entMdt.setSelfID(UUID.randomUUID().toString());
											entMdt.setParentID(parentID);
											entMdt.setRootID(rootID);
										}

										// Entity Metadata is complete - Add to Entity
										// Manager

										ArrayList<EntityMetadata> mdtColl = this.parentRoot.getEntityManager().getMetadataColl();
										if (mdtColl != null)
										{
											this.parentRoot.getEntityManager().getMetadataColl().add(entMdt);
										}

										// Generate Dependant Object Creation Message in
										// LOG
										FrameworkManager.getLogger().log(Level.INFO, MessageSource.getMessage("SUCC_DEPCR", new Object[]
										{ depobjName, this.getClass().getSimpleName()
										}, null, Locale.ENGLISH));

									}
									catch (Exception e) // Proxy Cast to Target Type Error
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
				// ERR_INVALIDREL= Invalid Relation {0} requested for Base Type {1}!
				EX_InvalidRelationException exInvRel = new EX_InvalidRelationException(new Object[]
				{ Relationname, RootSchema.getRootObjectMetadata().getObjectname()
				});
				FrameworkManager.getMessageFormatter().generate_message_snippet(exInvRel);
				throw exInvRel;

			}
		}

		return depObj;
	}

	/***************************************************************************
	 * Get Related Entities for the Dependant Object by specifying the relation name
	 * Handled via RelatedEntities Aspect
	 * 
	 * @param relationName
	 *             - Relation Name
	 * @return - Array List of Entities
	 * @throws EX_InvalidRelationException
	 ***************************************************************************/
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

	/*********************************************************************************
	 * Delete the Dependant Object
	 * Dealt Within Lock Aspect
	 * 
	 * @return - boolean Value true/false accordingly lock could be deleted or not
	 *********************************************************************************/
	public boolean delete()
	{
		return null != null;
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

	/**
	 * -------------------------------------------------------------------------------------------------------
	 * Get the Integer Primary Key for the Object from Entity Manager - Dealt within Aspect - KeyGetter Aspect
	 * ----------------------------------------------------------------------------------------------------
	 */
	public int getPrimaryKey_Int()
	{
		return 0;
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException
	{
		if (context != null)
		{
			this.FrameworkManager = (FrameworkManager) context.getBean("FrameworkManager");
			this.MessageSource = (org.springframework.context.MessageSource) context.getBean("messageSource");
			this.Context = context;
		}

	}

}
