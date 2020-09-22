package mypackage.Executions;

import java.util.ArrayList;

import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import mypackage.Interfaces.IExecutable;
import scriptsengine.uploadengine.services.implementations.WBFilepathService;
import scriptsengine.uploadengineSC.Metadata.services.interfaces.ISCWBMetadataSrv;
import scriptsengine.uploadengineSC.scContainer.definitions.SCScripDataContainer;
import scriptsengine.uploadengineSC.scContainer.interfaces.ISC_Scrip_DataCont_Srv;
import scriptsengine.uploadengineSC.tools.interfaces.IHeadersDeltaGetSrv;

@Service("SCHeaderDeltaGetSrvExe")
public class SCHeaderDeltaGetSrvExe implements IExecutable
{
	@Autowired
	private IHeadersDeltaGetSrv	hdrDeltaSrv;

	@Autowired
	private ISCWBMetadataSrv		wbMdtSrv;

	@Autowired
	private WBFilepathService	wbfpSrv;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(ApplicationContext appctxt) throws Exception
	{
		if (appctxt != null)
		{
			String ScCode = "AVANTI";

			String filePath = "C://WBConfig//Avanti Feeds.xlsx";
			String sheetName = "Analysis";

			String sheetName2 = "Data Sheet";

			XSSFWorkbook wbCtxt = null;

			if (wbfpSrv != null)
			{
				wbCtxt = wbfpSrv.getWBcontextfromFilepath(filePath);
			}

			ISC_Scrip_DataCont_Srv scDataConSrv = appctxt.getBean(ISC_Scrip_DataCont_Srv.class);
			if (scDataConSrv != null && wbCtxt != null && wbMdtSrv != null)
			{
				SCScripDataContainer scDataCon = scDataConSrv.get_ScripData(ScCode);
				if (scDataCon != null)
				{
					System.out.println("Data REtrieved for - " + scDataCon.getScRoot().getSCName());

					ArrayList<T> balsheetENList = scDataCon.getEntitiesforSheet("Analysis").getSheetEntityList();
					if (balsheetENList != null)
					{
						if (hdrDeltaSrv != null)
						{
							ArrayList<T> deltaHeaders = hdrDeltaSrv.getHeadersDelta(wbCtxt.getSheet(sheetName), balsheetENList,
							          wbMdtSrv.getMetadataforSheet(sheetName));
							if (deltaHeaders != null)
							{
								for ( Object t : deltaHeaders )
								{
									System.out.println(t.toString());
								}
								System.out.println(deltaHeaders.size());
							}

						}
					}

					ArrayList<T> qtrENList = scDataCon.getEntitiesforSheet(sheetName2).getSheetEntityList();
					if (qtrENList != null)
					{
						if (hdrDeltaSrv != null)
						{
							ArrayList<T> deltaHeaders = hdrDeltaSrv.getHeadersDelta(wbCtxt.getSheet(sheetName2), qtrENList,
							          wbMdtSrv.getMetadataforSheet(sheetName2));
							if (deltaHeaders != null)
							{
								for ( Object t : deltaHeaders )
								{
									System.out.println(t.toString());
								}
								System.out.println(deltaHeaders.size());
							}

						}
					}

				}

			}

		}

	}

}
