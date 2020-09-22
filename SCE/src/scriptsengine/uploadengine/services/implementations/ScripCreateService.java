package scriptsengine.uploadengine.services.implementations;

import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.exceptions.EX_InvalidRelationException;
import modelframework.implementations.DependantObject;
import modelframework.implementations.GeneralMessage;
import modelframework.implementations.MessagesFormatter;
import modelframework.implementations.RootObject;
import modelframework.managers.ModelObjectFactory;
import modelframework.utilities.PropertiesMapper;
import scriptsengine.pojos.OB_Scrip_General;
import scriptsengine.uploadengine.JAXB.definitions.SheetMetadata;
import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.services.interfaces.IScripBaseSrv;
import scriptsengine.uploadengine.services.interfaces.IScripCreate;

/**
 * 
 * Scrip Creation Service - Prototype Bean
 * Multiple people can upload multiple Scrips at same time
 * Uses Scrip base Service Instance for operations common to create/update
 */

@Service("ScripCreateService")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ScripCreateService implements IScripCreate
{

	private IScripBaseSrv		baseService;

	@Autowired
	private MessagesFormatter	msgFormatter;

	// Root Object for Scrip
	private OB_Scrip_General		scripRoot;

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

	@SuppressWarnings("rawtypes")
	@Override
	public void createScrip() throws EX_General
	{
		// 1. Get wb Entities from WB Create Validation Service
		if (this.baseService.getWbCreateValdSrv() != null)
		{
			if (this.baseService.getWbCreateValdSrv().getWbEntities() != null)
			{
				if (this.baseService.getWbCreateValdSrv().getWbEntities().size() > 0)
				{
					// Loop through each Sheet
					for ( SheetEntities shEnt : this.baseService.getWbCreateValdSrv().getWbEntities() )
					{
						if (baseService.getWbCreateValdSrv().getScMdtSrv() != null)
						{
							SheetMetadata shMdt = (baseService.getWbCreateValdSrv().getScMdtSrv())
							          .getSheetMdtbySheetName(shEnt.getSheetName());
							if (shMdt != null)
							{
								if (shMdt.getBasesheet())
								{
									try
									{
										CreateRootObject(shEnt, shMdt);
									}
									catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
									{
										EX_General egen = new EX_General("ERRCRSCROOT", new Object[]
										{ baseService.getWbCreateValdSrv().getScripCode(), e.getMessage()
										});
										msgFormatter.generate_message_snippet(egen);
										throw egen;
									}
								}
								else
								{
									try
									{
										CreateDependantObject(shEnt, shMdt);
									}
									catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
									          | EX_InvalidRelationException e)
									{
										EX_General egen = new EX_General("ERRCRSCDEP", new Object[]
										{ shMdt.getBobjName(), baseService.getWbCreateValdSrv().getScripCode(), e.getMessage()
										});
										msgFormatter.generate_message_snippet(egen);
										throw egen;
									}
								}
							}

						}
					}
					// All sheets looped through - Save the Scrip Now
					if (scripRoot != null)
					{
						if (scripRoot.Save())
						{
							// Populate Message in Message Container
							GeneralMessage msgReset = new GeneralMessage("SCCRSUCC", new Object[]
							{ scripRoot.getscCode()
							});
							msgFormatter.generate_message_snippet(msgReset);
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private void CreateRootObject(SheetEntities shEnt, SheetMetadata shMdt)
	          throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		if (shEnt != null && shMdt != null)
		{
			if (shEnt.getSheetEntityList() != null)
			{
				if (shEnt.getSheetEntityList().size() > 0)
				{
					// Create Root Object
					this.scripRoot = ModelObjectFactory.createObjectbyName(shMdt.getBobjName());
					// Set Properties in Root Object via Properties Mapper
					this.scripRoot = PropertiesMapper.setPropertiesforRootProxyBean(scripRoot, (RootObject) shEnt.getSheetEntityList().get(0));
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private void CreateDependantObject(SheetEntities shEnt, SheetMetadata shMdt)
	          throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, EX_InvalidRelationException
	{
		if (shEnt != null && shMdt != null)
		{
			if (shEnt.getSheetEntityList() != null)
			{
				if (shEnt.getSheetEntityList().size() > 0)
				{
					// Get the relation Name
					String relName = baseService.getWbCreateValdSrv().getScMdtSrv().getRelationNameforSheetName(shEnt.getSheetName());
					/**
					 * Proceed only if a valid relation Name could be established w.r.t Root Object
					 * There can be several sheets like Prices which do not have a direct relation w.r.t Root
					 * Object but are handled via X Sheet Validator implementations
					 */

					if (relName != null)
					{
						// Loop through each dependent entity
						for ( Object obj : shEnt.getSheetEntityList() )
						{
							if (obj instanceof DependantObject)
							{
								// Dependant Object POJO
								DependantObject depObj = (DependantObject) obj;

								// Dependant Object Bean
								DependantObject depObjBean = this.scripRoot.Create_RelatedEntity(relName);

								// Set Properties in Dependant Object via Properties Mapper
								depObjBean = PropertiesMapper.setPropertiesforDependantProxyBean(depObjBean, depObj);
							}

						}
					}

				}
			}
		}
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

}
