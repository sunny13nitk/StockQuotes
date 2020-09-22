package scriptsengine.uploadengineSC.scripSheetServices.implementations;

import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.implementations.MessagesFormatter;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.services.implementations.WBFilepathService;
import scriptsengine.uploadengine.validations.implementations.FilepathValidationService;
import scriptsengine.uploadengineSC.scripSheetServices.definitions.SC_XLS_CrUpd_Mode;
import scriptsengine.uploadengineSC.scripSheetServices.interfaces.ISCDetCrUpd_Mode;
import scriptsengine.uploadengineSC.scripSheetServices.interfaces.IXLS_Scrip_Create_Srv;
import scriptsengine.uploadengineSC.scripSheetServices.interfaces.IXLS_Scrip_Update_Srv;
import scriptsengine.uploadengineSC.scripSheetServices.interfaces.IXLS_Scrip_Upload_Srv;

@Service("XLS_Scrip_Upload_Srv")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class XLS_Scrip_Upload_Srv implements IXLS_Scrip_Upload_Srv
{

	@Autowired
	private MessagesFormatter		msgFormatter;

	@Autowired
	private IXLS_Scrip_Create_Srv		scCRSrv;

	@Autowired
	private IXLS_Scrip_Update_Srv		scUPDSrv;

	@Autowired
	private ISCDetCrUpd_Mode			modeDetSrv;

	@Autowired
	private FilepathValidationService	fpValSrv;

	@Autowired
	private WBFilepathService		wbfpSrv;

	/**
	 * ---------------- Interface Methods ----------------------------------
	 */
	@Override
	public boolean Upload_Scrip_from_XLS_Filepath(String FilePath) throws EX_General
	{
		boolean uploaded = false;

		if (FilePath != null)
		{
			if (fpValSrv != null)
			{
				try
				{
					if (fpValSrv.validateFilePath(FilePath))
					{
						if (wbfpSrv != null)
						{
							XSSFWorkbook wbCtxt = wbfpSrv.getWBcontextfromFilepath(FilePath);
							if (wbCtxt != null)
							{
								uploaded = this.Upload_Scrip_from_XLS_WBCtxt(wbCtxt);
							}
						}
					}
				}
				catch (IOException e)
				{
					EX_General egen = new EX_General("FILENOTFOUND", new Object[]
					{ FilePath
					});
					msgFormatter.generate_message_snippet(egen);
					throw egen;
				}
			}
		}

		return uploaded;
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public boolean Upload_Scrip_from_XLS_WBCtxt(XSSFWorkbook wbCtxt) throws EX_General
	{
		boolean uploaded = false;

		if (wbCtxt != null)
		{
			// 1. Get the Scrip Create/Update Mode
			SC_XLS_CrUpd_Mode modeDet = modeDetSrv.getModeforWB(wbCtxt);
			if (modeDet != null)
			{
				switch (modeDet.getMode())

				{
					case CREATE:
						if (scCRSrv != null)
						{
							uploaded = scCRSrv.Create_Scrip_WbContext(wbCtxt);
						}
						break;

					case UPDATE:
						if (scUPDSrv != null)
						{
							uploaded = scUPDSrv.Update_Scrip_WbContext(wbCtxt);
						}

						break;
				}
			}

		}

		return uploaded;
	}

}
