package scriptsengine.uploadengineSC.scripSheetServices.implementations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.definitions.Object_Info;
import modelframework.exceptions.EX_InvalidRelationException;
import modelframework.exposed.FrameworkManager;
import modelframework.implementations.DependantObject;
import modelframework.implementations.MessagesFormatter;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengineSC.Metadata.definitions.SCSheetMetadata;
import scriptsengine.uploadengineSC.Metadata.services.interfaces.ISCWBMetadataSrv;
import scriptsengine.uploadengineSC.entities.EN_SC_General;
import scriptsengine.uploadengineSC.scripSheetServices.definitions.SCSheetValStamps;
import scriptsengine.uploadengineSC.scripSheetServices.definitions.SheetValStamp;
import scriptsengine.uploadengineSC.scripSheetServices.interfaces.ISCExistsDB_Srv;
import scriptsengine.uploadengineSC.scripSheetServices.interfaces.IScripDBSnapShotSrv;

@Service("ScripDBSnapShotSrv")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ScripDBSnapShotSrv implements IScripDBSnapShotSrv
{

	@Autowired
	private MessagesFormatter	msgFormatter;

	@Autowired
	private ISCExistsDB_Srv		scExSrv;

	@Autowired
	private ISCWBMetadataSrv		wbMdtSrv;

	@Autowired
	private FrameworkManager		frameworkManager;

	private EN_SC_General		scRoot;

	/**
	 * ---------------- INTERFACE IMPLEMENTATIONS ----------------------------
	 */

	@Override
	public SCSheetValStamps getScripLatestValsbyScCode(String ScCode) throws EX_General
	{
		SCSheetValStamps scValStamps = null;

		if (ScCode != null && scExSrv != null)
		{
			this.scRoot = scExSrv.Get_ScripExisting_DB(ScCode);
			if (scRoot != null)
			{
				try
				{
					scValStamps = getSnapShotfromScRoot();
				}
				catch (Exception e)
				{
					EX_General egen = new EX_General("ERR_SC_DBSTAMP", new Object[]
					{ ScCode, e.getMessage()
					});
					msgFormatter.generate_message_snippet(egen);
					throw egen;
				}
			}
		}

		return scValStamps;
	}

	@Override
	public SCSheetValStamps getScripLatestValsbyScDesc(String ScDescBeginsWith) throws EX_General
	{
		SCSheetValStamps scValStamps = null;

		if (ScDescBeginsWith != null && scExSrv != null)
		{
			this.scRoot = scExSrv.Get_ScripExisting_DB_DescSW(ScDescBeginsWith);
			if (scRoot != null)
			{
				try
				{
					scValStamps = getSnapShotfromScRoot();
				}
				catch (Exception e)
				{
					EX_General egen = new EX_General("ERR_SC_DBSTAMP", new Object[]
					{ ScDescBeginsWith, e.getMessage()
					});
					msgFormatter.generate_message_snippet(egen);
					throw egen;
				}
			}
		}

		return scValStamps;
	}

	@Override
	public SCSheetValStamps getScripLatestValsbyScCode(EN_SC_General scRoot) throws EX_General
	{
		SCSheetValStamps scValStamps = null;

		if (scRoot != null)
		{
			this.scRoot = scRoot;
			if (scRoot != null)
			{
				try
				{
					scValStamps = getSnapShotfromScRoot();
				}
				catch (Exception e)
				{
					EX_General egen = new EX_General("ERR_SC_DBSTAMP", new Object[]
					{ scRoot.getSCCode(), e.getMessage()
					});
					msgFormatter.generate_message_snippet(egen);
					throw egen;
				}
			}
		}

		return scValStamps;
	}

	/**
	 * ----------------- PRIVATE METHODS -------------------------
	 * 
	 * @throws EX_InvalidRelationException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * 
	 */

	private SCSheetValStamps getSnapShotfromScRoot()
	          throws EX_General, EX_InvalidRelationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		SCSheetValStamps scValStamps = null;
		String headerFld = null;

		if (wbMdtSrv != null)
		{
			scValStamps = new SCSheetValStamps();

			scValStamps.setScCode(scRoot.getSCCode());

			// For UpdateHeaderDelta Mode sheets

			for ( SCSheetMetadata shMdt : wbMdtSrv.getWbMetadata().getSheetMetadata() )
			{
				headerFld = null;
				if (shMdt.isUpdHeaderDeltaMode())
				{
					SheetValStamp shvalStamp = new SheetValStamp();
					shvalStamp.setSheetName(shMdt.getSheetName());
					headerFld = shMdt.getHeadScanConfig().getObjField();
					String relName = wbMdtSrv.getRelationNameforSheetName(shMdt.getSheetName());
					if (relName != null)
					{

						// Get Object Information from Object Factory
						Object_Info objInfo = FrameworkManager.getObjectsInfoFactory().Get_ObjectInfo_byName(shMdt.getBobjName());

						ArrayList<DependantObject> relEntList = scRoot.getRelatedEntities(relName);
						if (relEntList != null)
						{
							if (relEntList.size() > 0)
							{
								// Get Last Entity
								DependantObject depEntity = relEntList.get((relEntList.size() - 1));
								if (depEntity != null)
								{
									// GEt the Getter for Current Object
									Method getM = objInfo.Get_Getter_for_FieldName(headerFld);
									if (getM != null)
									{
										// Invoke getter for Last entity in Related Entities Collection
										shvalStamp.setValue(getM.invoke(depEntity, new Object[] {}));
										scValStamps.getSheetVals().add(shvalStamp);
									}

								}
							}
						}
					}

				}
			}

		}
		return scValStamps;
	}

}
