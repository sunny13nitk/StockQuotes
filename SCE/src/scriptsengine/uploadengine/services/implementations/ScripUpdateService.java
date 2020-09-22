package scriptsengine.uploadengine.services.implementations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.definitions.EntityMetadata;
import modelframework.definitions.Object_Info;
import modelframework.exceptions.EX_InvalidObjectException;
import modelframework.exceptions.EX_InvalidRelationException;
import modelframework.exceptions.EX_NotRootException;
import modelframework.exceptions.EX_NullParamsException;
import modelframework.exceptions.EX_ParamCountMismatchException;
import modelframework.exceptions.EX_ParamInitializationException;
import modelframework.exposed.FrameworkManager;
import modelframework.implementations.DependantObject;
import modelframework.implementations.GeneralMessage;
import modelframework.implementations.MessagesFormatter;
import modelframework.implementations.RootObject;
import modelframework.utilities.CglibHelper;
import modelframework.utilities.PropertiesMapper;
import scriptsengine.pojos.OB_Scrip_General;
import scriptsengine.uploadengine.JAXB.definitions.SheetMetadata;
import scriptsengine.uploadengine.definitions.SheetEntFilter;
import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.services.interfaces.IDBQueryFilter;
import scriptsengine.uploadengine.services.interfaces.IScripBaseSrv;
import scriptsengine.uploadengine.services.interfaces.IScripUpdate;
import scriptsengine.uploadengine.validations.implementations.ScripExistsService;

@Service("ScripUpdateService")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ScripUpdateService implements IScripUpdate, ApplicationContextAware
{

	private IScripBaseSrv			baseService;

	@Autowired
	private MessagesFormatter		msgFormatter;

	@Autowired
	private ScripSheetMetadataService	scMdtSrv;

	@Autowired
	private ScripExistsService		scExSrv;

	// Root Object for Scrip
	private OB_Scrip_General			scripRoot;

	private ArrayList<SheetEntFilter>	sheetFilters;

	private ApplicationContext		appCtxt;

	/**
	 * @return the sheetFilters
	 */
	public ArrayList<SheetEntFilter> getSheetFilters()
	{
		return sheetFilters;
	}

	/**
	 * @param sheetFilters
	 *             the sheetFilters to set
	 */
	public void setSheetFilters(ArrayList<SheetEntFilter> sheetFilters)
	{
		this.sheetFilters = sheetFilters;
	}

	/**
	 * @return the scripRoot
	 */
	public OB_Scrip_General getScripRoot()
	{
		return scripRoot;
	}

	/**
	 * @param scripRoot
	 *             the scripRoot to set
	 */
	public void setScripRoot(OB_Scrip_General scripRoot)
	{
		this.scripRoot = scripRoot;
	}

	@Override
	public IScripBaseSrv getBaseService()
	{

		return baseService;
	}

	@Override
	public void setBaseService(IScripBaseSrv baseSrv)
	{
		this.baseService = baseSrv;

	}

	@Override
	public void updateScrip() throws EX_General
	{
		// 1. Generate Root Scrip

		genRootScrip();

		// 2. Process Each Sheet beans to get Query Filters

		getSheetEntFilters();

		// 3. Process entities
		processEntities();

		// 4. Save the Root Object
		if (this.scripRoot.Save())
		{
			GeneralMessage scSucc = new GeneralMessage("SCUPDSUCC", new Object[]
			{ scripRoot.getscCode()
			});
			msgFormatter.generate_message_snippet(scSucc);
		}

	}

	@SuppressWarnings("rawtypes")
	private void processEntities() throws EX_General
	{
		if (baseService.getWbUpdateValdSrv().getWbEntities() != null)
		{
			if (baseService.getWbUpdateValdSrv().getWbEntities().size() > 0)
			{

				for ( SheetEntities shEntities : baseService.getWbUpdateValdSrv().getWbEntities() )
				{
					// Get Sheet Metadata for each of the Sheet - Determine if base Sheet
					SheetMetadata shMdt = scMdtSrv.getSheetMdtbySheetName(shEntities.getSheetName());
					if (shMdt != null)
					{
						// Base Sheet will have no filters
						if (shMdt.getBasesheet())
						{
							// Set Properties in Root Object via Properties Mapper
							try
							{
								this.scripRoot.lock();
								this.scripRoot.switchtoChangeMode();
								this.scripRoot = PropertiesMapper.setPropertiesforRootProxyBeanUpdateMode(scripRoot,
								          (RootObject) shEntities.getSheetEntityList().get(0));
							}
							catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
							{
								EX_General egen = new EX_General("ERRUPDSCROOT", new Object[]
								{ baseService.getWbUpdateValdSrv().getScripCode(), e.getMessage()
								});
								msgFormatter.generate_message_snippet(egen);
								throw egen;
							}
						}
						else // Non Base Sheets having dependant relations
						{
							if (this.sheetFilters != null)
							{

								if (this.sheetFilters.size() > 0)
								{
									try
									{
										// Get relevant SheetEntfilter
										SheetEntFilter shEntFlt = this.sheetFilters.stream()
										          .filter(x -> x.getSheetName().equals(shEntities.getSheetName())).findFirst().get();
										if (shEntFlt.getFilter() != null)
										{
											// Filter Exists - get related entities with Filter
											try
											{
												ArrayList<DependantObject> depEntities = scripRoot
												          .getRelatedEntitieswithFilter(shEntFlt.getRelName(), shEntFlt.getFilter());
												processDepEntities(shEntities, depEntities, shEntFlt, shMdt);

											}
											catch (EX_InvalidRelationException e)
											{
												EX_General egen = new EX_General("ERRRELSCRIP", new Object[]
												{ shEntFlt.getRelName(), baseService.getWbUpdateValdSrv().getScripCode(),
												          baseService.getFpValidationSrv().getFilePath(), e.getMessage()
												});
												msgFormatter.generate_message_snippet(egen);
												throw egen;
											}
										}
										else
										{
											// No Filter Exists - get related entities without Filter
											try
											{
												ArrayList<DependantObject> depEntities = scripRoot
												          .getRelatedEntities(shEntFlt.getRelName());
												processDepEntities(shEntities, depEntities, shEntFlt, shMdt);

											}
											catch (EX_InvalidRelationException e)
											{
												EX_General egen = new EX_General("ERRRELSCRIP", new Object[]
												{ shEntFlt.getRelName(), baseService.getWbUpdateValdSrv().getScripCode(),
												          baseService.getFpValidationSrv().getFilePath(), e.getMessage()
												});
												msgFormatter.generate_message_snippet(egen);
												throw egen;
											}
										}

									}
									catch (NoSuchElementException e)
									{
										// do nothing must be a dummy sheet not mapped to a relation e.g.
										// 'Prices'
									}
								}
							}

						}
					}
				}
			}
		}

	}

	@SuppressWarnings(
	{ "rawtypes", "unchecked"
	})
	private void processDepEntities(SheetEntities shEntities, ArrayList<DependantObject> depEntities, SheetEntFilter shEntFlt, SheetMetadata shMdt)
	          throws EX_General
	{
		/**
		 * Only one dependent Entity and also Collection not
		 * supported for Sheet - Clear Cut Update Scenario
		 */

		if (depEntities.size() == 1 && shMdt.getCollection() == false && shEntities.getSheetEntityList().get(0) != null)
		{
			try
			{
				depEntities.get(0).switchtoChangeMode();
				PropertiesMapper.setPropertiesforDependantProxyBeanUpdateMode(depEntities.get(0),
				          (DependantObject) shEntities.getSheetEntityList().get(0));
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				EX_General egen = new EX_General("ERRUPDSCDEP", new Object[]
				{ shEntFlt.getRelName(), baseService.getWbUpdateValdSrv().getScripCode(), baseService.getFpValidationSrv().getFilePath(),
				          e.getMessage()
				});
				msgFormatter.generate_message_snippet(egen);
				throw egen;
			}

		}

		// Create Single Dependant Entity Scenario
		else if (depEntities.size() == 0 && shMdt.getCollection() == false && shEntities.getSheetEntityList().get(0) != null
		          && shEntities.getSheetEntityList().size() == 1)
		{
			// Dependant Object Bean
			DependantObject depObjBean;
			try
			{
				depObjBean = this.scripRoot.Create_RelatedEntity(shEntFlt.getRelName());
			}
			catch (EX_InvalidRelationException e1)
			{
				EX_General egen = new EX_General("ERRCRSCDEP", new Object[]
				{ shMdt.getBobjName(), baseService.getWbUpdateValdSrv().getScripCode(), e1.getMessage()
				});
				msgFormatter.generate_message_snippet(egen);
				throw egen;
			}

			// Set Properties in Dependant Object via Properties
			// Mapper
			try
			{
				depObjBean = PropertiesMapper.setPropertiesforDependantProxyBean(depObjBean,
				          (DependantObject) shEntities.getSheetEntityList().get(0));
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				EX_General egen = new EX_General("ERRCRSCDEP", new Object[]
				{ shMdt.getBobjName(), baseService.getWbUpdateValdSrv().getScripCode(), e.getMessage()
				});
				msgFormatter.generate_message_snippet(egen);
				throw egen;
			}
		}

		// Single New Dependant Create Case
		else if (depEntities.size() == 0 && shMdt.getCollection() && shEntities.getSheetEntityList().size() >= 1)
		{
			// Dependant Object Bean
			DependantObject depObjBean;
			try
			{
				depObjBean = this.scripRoot.Create_RelatedEntity(shEntFlt.getRelName());
			}
			catch (EX_InvalidRelationException e1)
			{
				EX_General egen = new EX_General("ERRCRSCDEP", new Object[]
				{ shMdt.getBobjName(), baseService.getWbUpdateValdSrv().getScripCode(), e1.getMessage()
				});
				msgFormatter.generate_message_snippet(egen);
				throw egen;
			}

			// Set Properties in Dependant Object via Properties
			// Mapper
			try
			{
				depObjBean = PropertiesMapper.setPropertiesforDependantProxyBean(depObjBean,
				          (DependantObject) shEntities.getSheetEntityList().get(0));
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				EX_General egen = new EX_General("ERRCRSCDEP", new Object[]
				{ shMdt.getBobjName(), baseService.getWbUpdateValdSrv().getScripCode(), e.getMessage()
				});
				msgFormatter.generate_message_snippet(egen);
				throw egen;
			}
		}

		// Multiple Db Entities found, Collection Supported and at least one sheet Entity to Compare
		// Can be create/update Scenario
		else if (depEntities.size() >= 1 && shMdt.getCollection() && shEntities.getSheetEntityList().size() >= 1)
		{

			/**
			 * If size of dependant Entities and SheetEntity List is same
			 * All Entities is case of Update
			 */
			if (depEntities.size() == shEntities.getSheetEntityList().size())
			{
				/**
				 * Update Dependant entities with Sheet Entities
				 * Even the order of occurance would be same since Dependant Entities are fetched from keys in order
				 * of Sheet Entities
				 */
				for ( int i = 0; i < depEntities.size(); i++ )
				{
					try
					{
						depEntities.get(i).switchtoChangeMode();
						PropertiesMapper.setPropertiesforDependantProxyBeanUpdateMode(depEntities.get(i),
						          (DependantObject) shEntities.getSheetEntityList().get(i));
					}
					catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
					{
						EX_General egen = new EX_General("ERRUPDSCDEP", new Object[]
						{ shEntFlt.getRelName(), baseService.getWbUpdateValdSrv().getScripCode(),
						          baseService.getFpValidationSrv().getFilePath(), e.getMessage()
						});
						msgFormatter.generate_message_snippet(egen);
						throw egen;
					}
				}
			}

			/**
			 * Loop on Sheet Entities one by one getting the keys and compare the keys with each of depEntities
			 */
			else // Not All Update Case
			{
				ArrayList<DependantObject> depshEntities = shEntities.getSheetEntityList();
				for ( DependantObject depshEnt : depshEntities )
				{
					boolean matchfound = false;
					ArrayList<Object> depShEntKeys = getKeyValuesforDependantObject(depshEnt, shEntFlt.getKeys());

					// Loop on Dependant Entities to compare keys for each of entities
					for ( DependantObject depEnt : depEntities )
					{
						// No futher looping on dependent entities for the same Sheet Entity
						if (matchfound == false)
						{
							ArrayList<Object> depEntKeys = getKeyValuesforDependantObject(depEnt, shEntFlt.getKeys());
							if (depShEntKeys.equals(depEntKeys))
							{
								// Update Scenario
								matchfound = true;
								try
								{
									depEnt.switchtoChangeMode();
									PropertiesMapper.setPropertiesforDependantProxyBeanUpdateMode(depEnt, depshEnt);

								}
								catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
								{
									EX_General egen = new EX_General("ERRUPDSCDEP", new Object[]
									{ shEntFlt.getRelName(), baseService.getWbUpdateValdSrv().getScripCode(),
									          baseService.getFpValidationSrv().getFilePath(), e.getMessage()
									});
									msgFormatter.generate_message_snippet(egen);
									throw egen;
								}

							}
						}

					}
					// Create Scenario
					if (matchfound == false)
					{
						// Dependant Object Bean
						DependantObject depObjBean;
						try
						{
							depObjBean = this.scripRoot.Create_RelatedEntity(shEntFlt.getRelName());
						}
						catch (EX_InvalidRelationException e1)
						{
							EX_General egen = new EX_General("ERRCRSCDEP", new Object[]
							{ shMdt.getBobjName(), baseService.getWbUpdateValdSrv().getScripCode(), e1.getMessage()
							});
							msgFormatter.generate_message_snippet(egen);
							throw egen;
						}

						// Set Properties in Dependant Object via Properties
						// Mapper
						try
						{
							depObjBean = PropertiesMapper.setPropertiesforDependantProxyBean(depObjBean, depshEnt);
						}
						catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
						{
							EX_General egen = new EX_General("ERRCRSCDEP", new Object[]
							{ shMdt.getBobjName(), baseService.getWbUpdateValdSrv().getScripCode(), e.getMessage()
							});
							msgFormatter.generate_message_snippet(egen);
							throw egen;
						}
					}
				}

			}
		}

	}

	private ArrayList<Object> getKeyValuesforDependantObject(DependantObject depObj, ArrayList<String> keys) throws EX_General

	{
		ArrayList<Object> keyVals = null;
		if (depObj != null && keys != null)
		{
			if (keys.size() > 0)
			{
				Object_Info depobjinfo = FrameworkManager.getObjectsInfoFactory().Get_ObjectInfo_byClass(depObj.getClass());
				if (depobjinfo == null)
				{
					// CGLib Proxy Cast - Get the target
					// Get the Target Part of Root Bean
					CglibHelper cgHelper = new CglibHelper(depObj);
					DependantObject entdepObj = (DependantObject) cgHelper.getTargetObject();
					depobjinfo = FrameworkManager.getObjectsInfoFactory().Get_ObjectInfo_byClass(entdepObj.getClass());
				}
				if (depobjinfo != null)
				{
					keyVals = new ArrayList<Object>();
					for ( int x = 1; x <= keys.size(); x++ )
					{
						Method mGetter = depobjinfo.Get_Getter_for_FieldName(keys.get(x - 1));
						if (mGetter != null)
						{
							// Get the value for the parameter from POJO
							Object value;
							try
							{
								value = mGetter.invoke(depObj, new Object[] {});
								if (value != null)
								{
									keyVals.add(value);
								}
							}
							catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
							{
								EX_General egen = new EX_General("ERRUPDSCDEP", new Object[]
								{ depobjinfo.getClass().getName(), baseService.getWbUpdateValdSrv().getScripCode(),
								          baseService.getFpValidationSrv().getFilePath(), e.getMessage()
								});
								msgFormatter.generate_message_snippet(egen);
								throw egen;
							}

						}
					}
				}

			}
		}

		return keyVals;
	}

	private void genRootScrip() throws EX_General
	{
		if (scExSrv != null)
		{
			try
			{
				this.scripRoot = scExSrv.getScripRootbyCode(baseService.getWbUpdateValdSrv().getScripCode());
				// Testing
				ArrayList<EntityMetadata> root_entMdtColl = scripRoot.getEntityManager().getEntityMetadataColl_RootObject(scripRoot);
				for ( EntityMetadata entityMetadata : root_entMdtColl )
				{
					System.out.println(entityMetadata.getObjectName() + "-" + entityMetadata.getEntityMode());
				}

			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException | NoSuchMethodException
			          | SecurityException | EX_InvalidObjectException | EX_NotRootException | SQLException | EX_NullParamsException
			          | EX_ParamCountMismatchException | EX_ParamInitializationException | EX_InvalidRelationException e)
			{
				EX_General egen = new EX_General("SCRIPEXISTERROR", new Object[]
				{ e.getMessage()
				});
				msgFormatter.generate_message_snippet(egen);
				throw egen;
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private void getSheetEntFilters() throws EX_General
	{
		if (appCtxt != null && scMdtSrv != null)
		{
			if (baseService.getWbUpdateValdSrv().getWbEntities() != null)
			{
				if (baseService.getWbUpdateValdSrv().getWbEntities().size() > 0)
				{
					this.sheetFilters = new ArrayList<SheetEntFilter>();
					for ( SheetEntities shEntities : baseService.getWbUpdateValdSrv().getWbEntities() )
					{
						// Get Sheet Metadata for each of the Sheet - Determine if base Sheet
						SheetMetadata shMdt = scMdtSrv.getSheetMdtbySheetName(shEntities.getSheetName());
						if (shMdt != null)
						{
							// Base Sheet will have no filters
							if (!shMdt.getBasesheet())
							{
								// Instantiate Db query filter Prototype Bean
								IDBQueryFilter qyFilterBean = appCtxt.getBean(IDBQueryFilter.class);
								if (qyFilterBean != null)
								{
									try
									{
										SheetEntFilter shentFlt = qyFilterBean.getEntFilterforSheetEntities(scripRoot, shEntities);
										if (shentFlt != null)
										{
											this.sheetFilters.add(shentFlt);
										}
									}
									catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
									{
										EX_General egen = new EX_General("ERRGENFILTER", new Object[]
										{ baseService.getWbUpdateValdSrv().getScripCode(), shEntities.getSheetName(), e.getMessage()
										});
										msgFormatter.generate_message_snippet(egen);
										throw egen;
									}
								}
							}
						}
					}
				}
			}
		}

	}

	@Override
	public void setApplicationContext(ApplicationContext ctxt) throws BeansException
	{
		this.appCtxt = ctxt;

	}

}
