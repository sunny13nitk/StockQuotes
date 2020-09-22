package scriptsengine.uploadengineSC.scContainer.implementations;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import modelframework.exceptions.EX_InvalidRelationException;
import modelframework.implementations.DependantObject;
import modelframework.implementations.MessagesFormatter;
import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengineSC.Metadata.definitions.SCSheetMetadata;
import scriptsengine.uploadengineSC.Metadata.services.interfaces.ISCWBMetadataSrv;
import scriptsengine.uploadengineSC.scContainer.definitions.SCScripDataContainer;
import scriptsengine.uploadengineSC.scContainer.interfaces.ISC_Scrip_DataCont_Srv;
import scriptsengine.uploadengineSC.scripSheetServices.interfaces.ISCExistsDB_Srv;

/*
 * CAchaeble Scrip Data Container Service
 */

@Service
public class SC_Scrip_DataCont_Srv implements ISC_Scrip_DataCont_Srv
{

	/**
	 * --------- AUTOWIRED BEANS -------------------------
	 */

	@Autowired
	private MessagesFormatter	msgFormatter;

	@Autowired
	private ISCExistsDB_Srv		scExSrv;

	@Autowired
	private ISCWBMetadataSrv		mdtSrv;

	/**
	 * --------- Redifinitions -------------------------
	 */

	@Override
	@Cacheable(cacheManager = "cacheManagerSCE", value = "SCscripsCache", keyGenerator = "SCKeyGen")
	public SCScripDataContainer get_ScripData(String SCCode) throws EX_General
	{

		SCScripDataContainer scDataCon = new SCScripDataContainer();

		if (SCCode != null && scExSrv != null && mdtSrv != null)
		{
			try
			{
				scDataCon.setScRoot(scExSrv.Get_ScripExisting_DB(SCCode));
				populateRelations(scDataCon);

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
		return scDataCon;
	}

	/*
	 * -------------------- PRIVATE METHODS --------------------
	 */

	@SuppressWarnings(
	{ "rawtypes", "unchecked"
	})
	private void populateRelations(SCScripDataContainer scDataCon) throws EX_General, EX_InvalidRelationException
	{
		if (scDataCon.getScRoot() != null)
		{
			// Loop through Sheets
			for ( SCSheetMetadata shmdt : mdtSrv.getWbMetadata().getSheetMetadata() )
			{
				if (!shmdt.isBaseSheet())
				{
					String relName = mdtSrv.getRelationNameforSheetName(shmdt.getSheetName());
					if (relName != null)
					{
						ArrayList<DependantObject> depObjs = scDataCon.getScRoot().getRelatedEntities(relName);
						if (depObjs != null)
						{
							if (depObjs.size() > 0)
							{
								SheetEntities shEnt = new SheetEntities(shmdt.getSheetName(), depObjs);
								scDataCon.getRelatedSheetEntities().add(shEnt);
							}
						}
					}
				}
			}

		}
	}

}
