package scriptsengine.uploadengine.updateIdentifiers.services.implementations;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import modelframework.exceptions.EX_InvalidObjectException;
import modelframework.exceptions.EX_InvalidRelationException;
import modelframework.exceptions.EX_NotRootException;
import modelframework.exceptions.EX_NullParamsException;
import modelframework.exceptions.EX_ParamCountMismatchException;
import modelframework.exceptions.EX_ParamInitializationException;
import modelframework.implementations.MessagesFormatter;
import modelframework.interfaces.IQueryService;
import modelframework.managers.QueryManager;
import scriptsengine.pojos.OB_Scrip_General;
import scriptsengine.uploadengine.definitions.ScripDBSnaphot;
import scriptsengine.uploadengine.definitions.SheetDBSnapShot;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.managers.ScripDataManager;
import scriptsengine.uploadengine.services.implementations.ScripSheetMetadataService;
import scriptsengine.uploadengine.services.interfaces.IScripDBSnapShot;
import scriptsengine.uploadengine.services.interfaces.IScripDataService;
import scriptsengine.uploadengine.templates.implementations.ScripUpdateTemplateService;
import scriptsengine.uploadengine.updateIdentifiers.interfaces.IDetermineUpdateEligibility;
import scriptsengine.uploadengine.updateIdentifiers.services.interfaces.IScripMassUpdateSrv;

/**
 * 
 * Scrip Mass Update Service - Update the Scrips in one Go/ Generate Update Templates for Eligible Scrips
 */
@Service
public class ScripMassUpdateSrv implements IScripMassUpdateSrv, ApplicationContextAware
{
	@Autowired
	private MessagesFormatter			msgFormatter;
	@Autowired
	private ScripSheetMetadataService		shtMdtSrv;
	@Autowired
	private ScripUpdateTemplateService		scUpdTmplSrv;

	@Autowired
	private IScripDBSnapShot				dbSSSrv;

	private IScripDataService			scDataSrv;

	private ApplicationContext			appCtxt;

	private ArrayList<OB_Scrip_General>	scripRootList;

	private String						rootObjName;

	/**
	 * Generate Templates for Eligible Scrips
	 */
	@Override
	public void generateTemplatesforScripsPendingUpdate(String filepath) throws EX_General
	{
		if (this.scDataSrv != null && shtMdtSrv != null && scUpdTmplSrv != null)
		{
			if (rootObjName == null)
			{
				rootObjName = shtMdtSrv.getRootObjectName();
			}

			if (rootObjName != null)
			{
				try
				{
					IQueryService qs = (IQueryService) QueryManager.getQuerybyRootObjname(rootObjName);
					if (qs != null)
					{
						// Get Scrip Root List
						this.scripRootList = qs.executeQuery();
						if (scripRootList != null)
						{
							if (scripRootList.size() > 0)
							{
								for ( OB_Scrip_General ob_Scrip_General : scripRootList )
								{
									if (dbSSSrv != null)
									{
										// Get Scrip Db Snapshot for Each Scrip
										ScripDBSnaphot scdbSS = dbSSSrv.getScripDBSnapshot(ob_Scrip_General.getscCode());
										if (scdbSS != null)
										{
											// Determine Update Eligibility for scrip DB Snapshot Object
											try
											{
												if (determineUpdateEligibility(scdbSS))
												{
													// Update Eligibility Determined - Generate Update
													// Template at specified Path

													scUpdTmplSrv.generateTemplateforScripUpdation(filepath,
													          ob_Scrip_General.getscCode());
												}
											}
											catch (EX_General | IOException e)
											{
												EX_General egen = new EX_General("ERR_UPD_ELIG_SCRIP", new Object[]
												{ ob_Scrip_General.getscCode(), e.getMessage()
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
				catch (EX_InvalidObjectException | EX_NotRootException | SQLException | IllegalAccessException | IllegalArgumentException
				          | InvocationTargetException | InstantiationException | NoSuchMethodException | SecurityException
				          | EX_NullParamsException | EX_ParamCountMismatchException | EX_ParamInitializationException
				          | EX_InvalidRelationException e)
				{
					EX_General egen = new EX_General("ERR_MASSUPD", new Object[]
					{ e.getMessage()
					});
					msgFormatter.generate_message_snippet(egen);
					throw egen;
				}

			}

		}
	}

	/**
	 * Update Scrips from Folder Specified
	 */
	@Override
	public void updateScripsfromFilePath(String folder) throws EX_General
	{
		if (this.scDataSrv != null && shtMdtSrv != null && folder != null)
		{
			try
			{
				List<Path> fileNames = Files.walk(Paths.get(folder)).filter(Files::isRegularFile).collect(Collectors.toList());
				if (fileNames != null)
				{
					if (fileNames.size() > 0)
					{
						for ( Path path : fileNames )
						{
							scDataSrv.updateScripfromXls_WB(path.toString());
						}
					}
				}
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public void setApplicationContext(ApplicationContext ctxt) throws BeansException
	{
		if (ctxt != null)
		{
			this.appCtxt = ctxt;
			ScripDataManager scDataMgr = ctxt.getBean(ScripDataManager.class);
			if (scDataMgr != null)
			{
				try
				{
					this.scDataSrv = ScripDataManager.getScripDataService();
				}
				catch (EX_General e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	// ------------------------------ PRIVATE SECTION-----------------------------------

	private boolean determineUpdateEligibility(ScripDBSnaphot scdbSS) throws EX_General
	{
		if (scdbSS != null)
		{
			// Determine updateReqdProcessorBean for each sheet
			for ( SheetDBSnapShot sheetDBSS : scdbSS.getSheetsDBSS() )
			{
				String beanName = shtMdtSrv.getUpdateReqdProcessBeanforSheetName(sheetDBSS.getSheetName());
				if (beanName != null)
				{
					IDetermineUpdateEligibility updEligBean = (IDetermineUpdateEligibility) appCtxt.getBean(beanName);
					if (updEligBean != null)
					{
						try
						{
							boolean isupdateEligible = false;
							isupdateEligible = updEligBean.isEligibleforUpdate(sheetDBSS.getKeyVals());
							if (isupdateEligible == true)
								return isupdateEligible;
						}
						catch (EX_General e)
						{
							EX_General egen = new EX_General("ERR_UPD_ELIG_SHEET", new Object[]
							{ sheetDBSS.getSheetName(), e.getMessage()
							});
							msgFormatter.generate_message_snippet(egen);
							throw egen;
						}
					}
				}

			}
		}
		return false;
	}

}
