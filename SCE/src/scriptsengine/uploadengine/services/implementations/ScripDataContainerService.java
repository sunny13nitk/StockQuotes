package scriptsengine.uploadengine.services.implementations;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import modelframework.exceptions.EX_InvalidRelationException;
import modelframework.implementations.DependantObject;
import modelframework.implementations.MessagesFormatter;
import scriptsengine.uploadengine.JAXB.definitions.SheetMetadata;
import scriptsengine.uploadengine.definitions.ScripDataContainer;
import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.managers.ScripDataManager;
import scriptsengine.uploadengine.services.interfaces.IScripDataContainerSrv;
import scriptsengine.uploadengine.services.interfaces.IScripDataService;
import scriptsengine.uploadengine.services.interfaces.IScripSheetMetadata;
import scriptsengine.uploadengine.validations.implementations.ScripExistsService;

@Service
public class ScripDataContainerService implements IScripDataContainerSrv
{

	@Autowired
	private ScripExistsService	scExSrv;

	@Autowired
	private MessagesFormatter	msgFormatter;

	private IScripDataService	scDataSrv;
	private IScripSheetMetadata	shMdtSrv;

	@Override
	@Cacheable(cacheManager = "cacheManagerSCE", value = "scripsCache")
	public ScripDataContainer getScripDetailsfromDB(String scCode) throws EX_General
	{
		System.out.println("Hitting DB to get Scrip details for  " + scCode);
		ScripDataContainer scDataCont = new ScripDataContainer();

		if (scCode != null)
		{
			if (scExSrv != null)
			{
				try
				{

					scDataSrv = ScripDataManager.getScripDataService();
					if (scDataSrv != null)
					{
						shMdtSrv = scDataSrv.getContext().getBean(IScripSheetMetadata.class);
					}

					scDataCont.setScRoot(scExSrv.getScripRootbyCode(scCode));
					processRelations(scDataCont);

				}
				catch (Exception e)
				{
					EX_General egen = new EX_General("SCRIPEXISTERROR", new Object[]
					{ e.getMessage()
					});
					msgFormatter.generate_message_snippet(egen);
					throw egen;
				}
			}

		}

		return scDataCont;
	}

	/**
	 * Get Related entities for scrip
	 * 
	 * @throws EX_InvalidRelationException
	 */
	@SuppressWarnings(
	{ "rawtypes", "unchecked"
	})
	private void processRelations(ScripDataContainer scDataCont) throws EX_InvalidRelationException
	{
		if (this.scDataSrv != null && shMdtSrv != null)
		{
			if (this.scDataSrv.getWBMetaData() != null)
			{
				// Looping on the WB Metadata to get relation details
				for ( SheetMetadata shMdt : this.scDataSrv.getWBMetaData().getSheetsMetadata() )
				{
					if (!shMdt.getBasesheet())
					{
						String relName = shMdtSrv.getRelationNameforSheetName(shMdt.getSheetName());
						if (relName != null)
						{
							ArrayList<DependantObject> depObjs = scDataCont.getScRoot().getRelatedEntities(relName);
							if (depObjs != null)
							{
								if (depObjs.size() > 0)
								{
									SheetEntities shEnt = new SheetEntities(shMdt.getSheetName(), depObjs);
									scDataCont.getRelatedSheetEntities().add(shEnt);
								}
							}
						}
					}
				}
			}
		}

	}

	/**
	 * Cache will only be cleared in case of Split/Bonus Declaration by Bonus/Split Service
	 * This service will be autowired there and on split/bonus settlement this method of autowired service will be
	 * called to clear cache
	 */
	@Override
	@CacheEvict(value = "scripsCache", allEntries = true)
	public void clearCache()
	{
		// Do noting only Cache eviction implicitly triggered

	}

}
