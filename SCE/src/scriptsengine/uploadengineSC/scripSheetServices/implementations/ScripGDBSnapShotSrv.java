package scriptsengine.uploadengineSC.scripSheetServices.implementations;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.implementations.MessagesFormatter;
import modelframework.interfaces.IQueryService;
import modelframework.managers.QueryManager;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengineSC.Metadata.services.interfaces.ISCWBMetadataSrv;
import scriptsengine.uploadengineSC.entities.EN_SC_General;
import scriptsengine.uploadengineSC.scripSheetServices.definitions.SCSheetValStamps;
import scriptsengine.uploadengineSC.scripSheetServices.interfaces.IScripDBSnapShotSrv;
import scriptsengine.uploadengineSC.scripSheetServices.interfaces.IScripGDBSnapShotSrv;

@Service("ScripGDBSnapShotSrv")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ScripGDBSnapShotSrv implements IScripGDBSnapShotSrv
{

	@Autowired
	private MessagesFormatter	msgFormatter;

	@Autowired
	private IScripDBSnapShotSrv	scDBSSSrv;

	@Autowired
	private ISCWBMetadataSrv		wbMdtSrv;

	@Override
	public ArrayList<SCSheetValStamps> getScripsDBSnapShot() throws EX_General
	{

		ArrayList<SCSheetValStamps> scSheetValStamps = null;

		if (wbMdtSrv != null && scDBSSSrv != null)
		{

			// 1. Get the Scrips saved in DB
			try
			{
				IQueryService qs = (IQueryService) QueryManager.getQuerybyRootObjname(wbMdtSrv.getMetadataforBaseSheet().getBobjName());

				if (qs != null)
				{

					ArrayList<EN_SC_General> scRoots = qs.executeQuery();
					if (scRoots != null)
					{
						if (scRoots.size() > 0)
						{
							scSheetValStamps = new ArrayList<SCSheetValStamps>();

							for ( EN_SC_General scRoot : scRoots )
							{
								scSheetValStamps.add(scDBSSSrv.getScripLatestValsbyScCode(scRoot));
							}
						}
					}

				}
			}
			catch (Exception e)
			{
				EX_General egen = new EX_General("ERR_SCDBSS_LOAD", new Object[]
				{ e.getMessage()
				});
				msgFormatter.generate_message_snippet(egen);
				throw egen;
			}

		}

		return scSheetValStamps;
	}

}
