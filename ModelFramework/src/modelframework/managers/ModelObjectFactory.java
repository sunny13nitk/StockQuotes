package modelframework.managers;

import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Level;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import modelframework.definitions.EntityManager;
import modelframework.definitions.EntityMetadata;
import modelframework.definitions.Object_Info;
import modelframework.enums.system.modelEnums;
import modelframework.exposed.FrameworkManager;
import modelframework.implementations.RootObject;
import modelframework.utilities.CglibHelper;
import modelframework.utilities.PropertiesMapper;

@Service("ObjectFactory")
public class ModelObjectFactory implements ApplicationContextAware
{

	private static ApplicationContext	Context;

	private static FrameworkManager	frameworkManager;
	private static MessageSource		messageSource;
	private static EntityManager		entityManager;
	private static PropertiesMapper	propertiesMapper;

	/*************************************************************************************************
	 * Create Root Object from Model Object Factory
	 * 
	 * @param ObjectName
	 *             - Name of Object to be Created - String
	 * @return
	 *         - Object Created
	 *************************************************************************************************/

	@SuppressWarnings("unchecked")
	public static <T> T createObjectbyName(String Objectname)
	{
		Object rootObj = null;
		if (Context != null && Objectname != null)
		{
			rootObj = createRootObject(Objectname);
		}

		return (T) rootObj;

	}

	/*************************************************************************************************
	 * Create Root Object from Model Object Factory
	 * 
	 * @param cls
	 *             - Class type of Bean to be created
	 * @return
	 *         - Object Created
	 *************************************************************************************************/

	@SuppressWarnings("unchecked")
	public static <T> T createObjectbyClass(Class<T> cls)
	{
		Object rootObj = null;
		if (Context != null && cls != null)
		{
			rootObj = createRootObject(cls);
		}

		return (T) rootObj;

	}

	@SuppressWarnings("unchecked")
	public static <T> T createObjectbyNameDB(String Objectname)
	{
		Object rootObj = null;
		if (Context != null && Objectname != null)
		{
			rootObj = createRootObjectDB(Objectname);
		}

		return (T) rootObj;

	}

	/*************************************************************************************************
	 * Create Root Object from Model Object Factory
	 * 
	 * @param cls
	 *             - Class type of Bean to be created
	 * @return
	 *         - Object Created
	 *************************************************************************************************/

	@SuppressWarnings("unchecked")
	public static <T> T createObjectbyClassDB(Class<T> cls)
	{
		Object rootObj = null;
		if (Context != null && cls != null)
		{
			rootObj = createRootObjectDB(cls);
		}

		return (T) rootObj;

	}

	/*************************************************************************************************
	 * Create Root Object from Model Object Factory
	 * 
	 * @param ObjectName
	 *             - Name of Object to be Created - String
	 * @return
	 *         - Object Created
	 *************************************************************************************************/
	@SuppressWarnings(
	{ "unchecked", "rawtypes", "static-access"
	})
	private static <T> T createRootObject(String Objectname)
	{
		// Start with Null Object
		RootObject Root_Obj = null;
		RootObject test_instance = null;
		// Framework Manager bound and Object name supplied
		if (frameworkManager != null && Objectname != null && entityManager != null)
		{
			if (Objectname.length() > 0)
			{
				// Get Object Information from Object Factory
				Object_Info obj_info = FrameworkManager.getObjectsInfoFactory().Get_ObjectInfo_byName(Objectname);
				if (obj_info != null)
				{
					// Create the Object of this Class
					Class obj_class = obj_info.getCurr_Obj_Class();
					if (obj_class != null)
					{

						try // Validate if the declared object is
						    // inherited from
						    // Root Object and can be instatiated as one
						{
							test_instance = (RootObject) obj_class.newInstance();
							if (test_instance != null && test_instance instanceof RootObject)
							{
								if (Context != null)
								{
									// Create Prototype BEan of
									// requested
									// Type

									Root_Obj = (RootObject) Context.getBean(Objectname, obj_class);

									// Create Entity Metadata
									EntityMetadata entityMetadata = new EntityMetadata(obj_info, modelEnums.entityMode.CREATE);
									// Set User Aware Mode if
									// Annotation UserAware
									// maintianed on Root Object
									entityMetadata.setUserAware(obj_info.IsUserAware());
									// Set Current User if Object is
									// UserAware
									if (entityMetadata.isUserAware())
									{
										entityMetadata.setCurrentUser(frameworkManager.getUserManager().Get_LoggedUser());
									}
									// Set Entity Object in Metadata

									try
									{
										CglibHelper cgHelper = new CglibHelper(Root_Obj);
										Object entity = cgHelper.getTargetObject();

										entityMetadata.setEntity(entity);

									}
									catch (Exception e) // Proxy Cast
									                    // to
									                    // Target Type
									                    // Error
									{
										frameworkManager.getLogger().log(Level.SEVERE,
										          messageSource.getMessage("ERR_PROXYCAST", new Object[]
												{ Objectname, e.getMessage()
												}, null, Locale.ENGLISH));
									}

									// Generate UUID for this Root Object to uniquely identify it through
									// sesssion
									entityMetadata.setSelfID(UUID.randomUUID().toString());
									// Since it is Root Object so ParentID/RootID would be null - no need to
									// set the
									// same

									// Entity Metadata is complete -
									// Add
									// to Entity Manager

									ArrayList<EntityMetadata> mdtColl = entityManager.getMetadataColl();

									if (mdtColl == null)
									{
										entityManager.setMetadataColl(new ArrayList<EntityMetadata>());
									}
									entityManager.getMetadataColl().add(entityMetadata);

									// Synchronize Keys - Not in case
									// of
									// Root Object Creation handled
									// with
									// Entity Metadata method
									// Generate Root Object Creation
									// Message in LOG
									frameworkManager.getLogger().log(Level.INFO, messageSource.getMessage("SUCC_ROOTCR", new Object[]
									{ Objectname
									}, null, Locale.ENGLISH));
								}
							}

						}
						catch (InstantiationException e)
						{
							frameworkManager.getLogger().log(Level.SEVERE, messageSource.getMessage("INSTANTIATION_FAILURE", new Object[]
							{ Objectname
							}, null, Locale.ENGLISH));
						}
						catch (IllegalAccessException e)
						{

							frameworkManager.getLogger().log(Level.SEVERE, messageSource.getMessage("ACCESS_FAILURE", new Object[]
							{ Objectname
							}, null, Locale.ENGLISH));
						}

					}
					else // Object Class not Found
					{
						frameworkManager.getLogger().log(Level.SEVERE, messageSource.getMessage("CLASS_NOT_FOUND", new Object[]
						{ Objectname
						}, null, Locale.ENGLISH));
					}
				}
				else // Invalid Object
				{

					frameworkManager.getLogger().log(Level.SEVERE, messageSource.getMessage("OBJECT_NOT_LOADED", new Object[]
					{ Objectname
					}, null, Locale.ENGLISH));

				}

			}
		}
		return (T) Root_Obj;
	}

	/*************************************************************************************************
	 * Create Root Object from Model Object Factory
	 * 
	 * @param cls
	 *             - Class type of Bean to be created
	 * @return
	 *         - Object Created
	 *************************************************************************************************/
	@SuppressWarnings(
	{ "unchecked", "rawtypes", "static-access"
	})
	private static <T> T createRootObject(Class<T> cls)
	{
		// Start with Null Object
		RootObject Root_Obj = null;
		RootObject test_instance = null;
		String Objectname = null;
		// Framework Manager bound and Object name supplied
		if (frameworkManager != null && cls != null && entityManager != null)
		{

			// Get Object Information from Object Factory
			Object_Info obj_info = FrameworkManager.getObjectsInfoFactory().Get_ObjectInfo_byClass(cls);
			if (obj_info != null)
			{
				Objectname = cls.getSimpleName();
				// Create the Object of this Class
				Class obj_class = obj_info.getCurr_Obj_Class();
				if (obj_class != null)
				{

					try // Validate if the declared object is
					    // inherited from
					    // Root Object and can be instatiated as one
					{
						test_instance = (RootObject) obj_class.newInstance();
						if (test_instance != null && test_instance instanceof RootObject)
						{
							if (Context != null)
							{
								// Create Prototype BEan of
								// requested
								// Type

								Root_Obj = (RootObject) Context.getBean(Objectname, obj_class);

								// Create Entity Metadata
								EntityMetadata entityMetadata = new EntityMetadata(obj_info, modelEnums.entityMode.CREATE);
								// Set User Aware Mode if
								// Annotation UserAware
								// maintianed on Root Object
								entityMetadata.setUserAware(obj_info.IsUserAware());
								// Set Current User if Object is
								// UserAware
								if (entityMetadata.isUserAware())
								{
									entityMetadata.setCurrentUser(frameworkManager.getUserManager().Get_LoggedUser());
								}
								// Set Entity Object in Metadata

								try
								{
									CglibHelper cgHelper = new CglibHelper(Root_Obj);
									Object entity = cgHelper.getTargetObject();

									entityMetadata.setEntity(entity);

								}
								catch (Exception e) // Proxy Cast
								                    // to
								                    // Target Type
								                    // Error
								{
									frameworkManager.getLogger().log(Level.SEVERE, messageSource.getMessage("ERR_PROXYCAST", new Object[]
									{ Objectname, e.getMessage()
									}, null, Locale.ENGLISH));
								}

								// Generate UUID for this Root Object to uniquely identify it through
								// sesssion
								entityMetadata.setSelfID(UUID.randomUUID().toString());
								// Since it is Root Object so ParentID/RootID would be null - no need to set the
								// same

								// Entity Metadata is complete -
								// Add
								// to Entity Manager

								ArrayList<EntityMetadata> mdtColl = entityManager.getMetadataColl();

								if (mdtColl == null)
								{
									entityManager.setMetadataColl(new ArrayList<EntityMetadata>());
								}
								entityManager.getMetadataColl().add(entityMetadata);

								// Synchronize Keys - Not in case
								// of
								// Root Object Creation handled
								// with
								// Entity Metadata method
								// Generate Root Object Creation
								// Message in LOG
								frameworkManager.getLogger().log(Level.INFO, messageSource.getMessage("SUCC_ROOTCR", new Object[]
								{ Objectname
								}, null, Locale.ENGLISH));
							}
						}

					}
					catch (InstantiationException e)
					{
						frameworkManager.getLogger().log(Level.SEVERE, messageSource.getMessage("INSTANTIATION_FAILURE", new Object[]
						{ Objectname
						}, null, Locale.ENGLISH));
					}
					catch (IllegalAccessException e)
					{

						frameworkManager.getLogger().log(Level.SEVERE, messageSource.getMessage("ACCESS_FAILURE", new Object[]
						{ Objectname
						}, null, Locale.ENGLISH));
					}

				}
				else // Object Class not Found
				{
					frameworkManager.getLogger().log(Level.SEVERE, messageSource.getMessage("CLASS_NOT_FOUND", new Object[]
					{ Objectname
					}, null, Locale.ENGLISH));
				}
			}
			else // Invalid Object
			{

				frameworkManager.getLogger().log(Level.SEVERE, messageSource.getMessage("OBJECT_NOT_LOADED", new Object[]
				{ Objectname
				}, null, Locale.ENGLISH));

			}

		}

		return (T) Root_Obj;
	}

	/*******************************************************************************
	 * SYSTEM METHOD!! Not recommended to be used externally - DO NOT USE!!
	 * Create-De-serialize a Root Object Bean fetching data for the same from
	 * DB
	 * 
	 * @param Objectname
	 *             - Root Object Name
	 * @return - Root Object Bean
	 ******************************************************************************/
	@SuppressWarnings(
	{ "unchecked", "rawtypes", "static-access"
	})
	private static <T> T createRootObjectDB(String Objectname)
	{
		// Start with Null Object
		RootObject Root_Obj = null;
		RootObject test_instance = null;
		// Framework Manager bound and Object name supplied
		if (frameworkManager != null && Objectname != null && entityManager != null)
		{
			if (Objectname.length() > 0)
			{
				// Get Object Information from Object Factory
				Object_Info obj_info = FrameworkManager.getObjectsInfoFactory().Get_ObjectInfo_byName(Objectname);
				if (obj_info != null)
				{
					// Create the Object of this Class
					Class obj_class = obj_info.getCurr_Obj_Class();
					if (obj_class != null)
					{

						try // Validate if the declared object is
						    // inherited from
						    // Root Object and can be instatiated as one
						{
							test_instance = (RootObject) obj_class.newInstance();
							if (test_instance != null && test_instance instanceof RootObject)
							{
								if (Context != null)
								{
									// Create Prototype BEan of
									// requested
									// Type

									Root_Obj = (RootObject) Context.getBean(Objectname, obj_class);

									// Create Entity Metadata
									EntityMetadata entityMetadata = new EntityMetadata(obj_info, modelEnums.entityMode.REFRESHED);
									// Set User Aware Mode if
									// Annotation UserAware
									// maintained on Root Object
									entityMetadata.setUserAware(obj_info.IsUserAware());
									// Set Current User if Object is
									// UserAware
									if (entityMetadata.isUserAware())
									{
										entityMetadata.setCurrentUser(frameworkManager.getUserManager().Get_LoggedUser());
									}
									// Set Entity Object in Metadata

									try
									{
										CglibHelper cgHelper = new CglibHelper(Root_Obj);
										Object entity = cgHelper.getTargetObject();

										entityMetadata.setEntity(entity);

									}
									catch (Exception e) // Proxy Cast
									                    // to
									                    // Target Type
									                    // Error
									{
										frameworkManager.getLogger().log(Level.SEVERE,
										          messageSource.getMessage("ERR_PROXYCAST", new Object[]
												{ Objectname, e.getMessage()
												}, null, Locale.ENGLISH));
									}

									// Generate UUID for this Root Object to uniquely identify it through
									// sesssion
									entityMetadata.setSelfID(UUID.randomUUID().toString());
									// Since it is Root Object so ParentID/RootID would be null - no need to
									// set the
									// same

									// Entity Metadata is complete -
									// Add
									// to Entity Manager

									ArrayList<EntityMetadata> mdtColl = entityManager.getMetadataColl();

									if (mdtColl == null)
									{
										entityManager.setMetadataColl(new ArrayList<EntityMetadata>());
									}
									entityManager.getMetadataColl().add(entityMetadata);

									// Synchronize Keys - Not in case
									// of
									// Root Object Creation handled
									// with
									// Entity Metadata method
									// Generate Root Object Creation
									// Message in LOG
									frameworkManager.getLogger().log(Level.INFO, messageSource.getMessage("SUCC_ROOTCR", new Object[]
									{ Objectname
									}, null, Locale.ENGLISH));
								}
							}

						}
						catch (InstantiationException e)
						{
							frameworkManager.getLogger().log(Level.SEVERE, messageSource.getMessage("INSTANTIATION_FAILURE", new Object[]
							{ Objectname
							}, null, Locale.ENGLISH));
						}
						catch (IllegalAccessException e)
						{

							frameworkManager.getLogger().log(Level.SEVERE, messageSource.getMessage("ACCESS_FAILURE", new Object[]
							{ Objectname
							}, null, Locale.ENGLISH));
						}

					}
					else // Object Class not Found
					{
						frameworkManager.getLogger().log(Level.SEVERE, messageSource.getMessage("CLASS_NOT_FOUND", new Object[]
						{ Objectname
						}, null, Locale.ENGLISH));
					}
				}
				else // Invalid Object
				{

					frameworkManager.getLogger().log(Level.SEVERE, messageSource.getMessage("OBJECT_NOT_LOADED", new Object[]
					{ Objectname
					}, null, Locale.ENGLISH));

				}

			}
		}
		return (T) Root_Obj;
	}

	/*******************************************************************************
	 * SYSTEM METHOD!! Not recommended to be used externally - DO NOT USE!!
	 * Create-De-serialize a Root Object Bean fetching data for the same from
	 * DB
	 * 
	 * @param cls
	 *             - Root Object Class
	 * @return - Root Object Bean
	 ******************************************************************************/
	private static <T> T createRootObjectDB(Class<T> cls)
	{
		// Start with Null Object
		RootObject Root_Obj = null;
		RootObject test_instance = null;
		String Objectname = null;
		// Framework Manager bound and Object name supplied
		if (frameworkManager != null && cls != null && entityManager != null)
		{

			// Get Object Information from Object Factory
			Object_Info obj_info = FrameworkManager.getObjectsInfoFactory().Get_ObjectInfo_byClass(cls);
			if (obj_info != null)
			{
				Objectname = cls.getSimpleName();
				// Create the Object of this Class
				Class obj_class = obj_info.getCurr_Obj_Class();
				if (obj_class != null)
				{

					try // Validate if the declared object is
					    // inherited from
					    // Root Object and can be instatiated as one
					{
						test_instance = (RootObject) obj_class.newInstance();
						if (test_instance != null && test_instance instanceof RootObject)
						{
							if (Context != null)
							{
								// Create Prototype BEan of
								// requested
								// Type

								Root_Obj = (RootObject) Context.getBean(Objectname, obj_class);

								// Create Entity Metadata
								EntityMetadata entityMetadata = new EntityMetadata(obj_info, modelEnums.entityMode.REFRESHED);
								// Set User Aware Mode if
								// Annotation UserAware
								// maintianed on Root Object
								entityMetadata.setUserAware(obj_info.IsUserAware());
								// Set Current User if Object is
								// UserAware
								if (entityMetadata.isUserAware())
								{
									entityMetadata.setCurrentUser(frameworkManager.getUserManager().Get_LoggedUser());
								}
								// Set Entity Object in Metadata

								try
								{
									CglibHelper cgHelper = new CglibHelper(Root_Obj);
									Object entity = cgHelper.getTargetObject();

									entityMetadata.setEntity(entity);

								}
								catch (Exception e) // Proxy Cast
								                    // to
								                    // Target Type
								                    // Error
								{
									frameworkManager.getLogger().log(Level.SEVERE, messageSource.getMessage("ERR_PROXYCAST", new Object[]
									{ Objectname, e.getMessage()
									}, null, Locale.ENGLISH));
								}

								// Generate UUID for this Root Object to uniquely identify it through
								// sesssion
								entityMetadata.setSelfID(UUID.randomUUID().toString());
								// Since it is Root Object so ParentID/RootID would be null - no need to set the
								// same

								// Entity Metadata is complete -
								// Add
								// to Entity Manager

								ArrayList<EntityMetadata> mdtColl = entityManager.getMetadataColl();

								if (mdtColl == null)
								{
									entityManager.setMetadataColl(new ArrayList<EntityMetadata>());
								}
								entityManager.getMetadataColl().add(entityMetadata);

								// Synchronize Keys - Not in case
								// of
								// Root Object Creation handled
								// with
								// Entity Metadata method
								// Generate Root Object Creation
								// Message in LOG
								frameworkManager.getLogger().log(Level.INFO, messageSource.getMessage("SUCC_ROOTCR", new Object[]
								{ Objectname
								}, null, Locale.ENGLISH));
							}
						}

					}
					catch (InstantiationException e)
					{
						frameworkManager.getLogger().log(Level.SEVERE, messageSource.getMessage("INSTANTIATION_FAILURE", new Object[]
						{ Objectname
						}, null, Locale.ENGLISH));
					}
					catch (IllegalAccessException e)
					{

						frameworkManager.getLogger().log(Level.SEVERE, messageSource.getMessage("ACCESS_FAILURE", new Object[]
						{ Objectname
						}, null, Locale.ENGLISH));
					}

				}
				else // Object Class not Found
				{
					frameworkManager.getLogger().log(Level.SEVERE, messageSource.getMessage("CLASS_NOT_FOUND", new Object[]
					{ Objectname
					}, null, Locale.ENGLISH));
				}
			}
			else // Invalid Object
			{

				frameworkManager.getLogger().log(Level.SEVERE, messageSource.getMessage("OBJECT_NOT_LOADED", new Object[]
				{ Objectname
				}, null, Locale.ENGLISH));

			}

		}

		return (T) Root_Obj;
	}

	@Override
	public void setApplicationContext(ApplicationContext ctxt) throws BeansException
	{
		Context = ctxt;
		frameworkManager = (FrameworkManager) Context.getBean("FrameworkManager");
		messageSource = (org.springframework.context.MessageSource) Context.getBean("messageSource");
		entityManager = (EntityManager) Context.getBean("EntityManager");
		propertiesMapper = (PropertiesMapper) Context.getBean("PropertiesMapper");
	}

}
