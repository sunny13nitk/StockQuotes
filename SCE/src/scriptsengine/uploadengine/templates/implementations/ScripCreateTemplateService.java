package scriptsengine.uploadengine.templates.implementations;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.implementations.GeneralMessage;
import modelframework.implementations.MessagesFormatter;
import scriptsengine.uploadengine.JAXB.definitions.SheetMetadata;
import scriptsengine.uploadengine.JAXB.definitions.WorkbookMetadata;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.services.interfaces.IScripDataService;
import scriptsengine.uploadengine.templates.interfaces.IScripCreateTemplate;
import scriptsengine.uploadengine.templates.sheetTemplates.interfaces.ISheetCreateTemplate;

/*
 * Service for Scrip Create Template Generation
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ScripCreateTemplateService implements IScripCreateTemplate, ApplicationContextAware
{

	@Autowired
	private IScripDataService	scDataSrv;

	@Autowired
	private MessagesFormatter	msgFormatter;

	private WorkbookMetadata		wbMdt;

	private XSSFWorkbook		wbCtxt;

	private ApplicationContext	appCtxt;

	private final String		wbName				= "CreateScripTemplate.xlsx";

	private final String		sheetTemplateSrv_Suffix	= "SheetTmpCreateSrv";

	private final String		genericSheetTmplSrv		= "GenericSheetTmplScCreationSrv";

	/**
	 * @return the wbMdt
	 */
	public WorkbookMetadata getWbMdt()
	{
		return wbMdt;
	}

	/**
	 * @return the wbCtxt
	 */
	public XSSFWorkbook getWbCtxt()
	{
		return wbCtxt;
	}

	@Override
	public void setApplicationContext(ApplicationContext ctxt) throws BeansException
	{
		this.appCtxt = ctxt;

	}

	@SuppressWarnings("resource")
	@Override
	public void generateTemplateforScripCreation(String filePath) throws IOException, EX_General
	{
		if (filePath != null)
		{
			// Write the workbook in file system
			FileOutputStream outStream = new FileOutputStream(new File(filePath + wbName));
			if (outStream != null)
			{
				wbCtxt = new XSSFWorkbook();

				if (wbCtxt != null && scDataSrv != null)
				{
					if (scDataSrv.getWBMetaData() != null)
					{
						this.wbMdt = scDataSrv.getWBMetaData();
						/**
						 * Trigger Sheet Create Template Services
						 */
						for ( SheetMetadata shMdt : wbMdt.getSheetsMetadata() )
						{
							if (shMdt.getSheetName() != null && appCtxt != null)
							{
								/**
								 * Try for specific Sheet Create template
								 */
								ISheetCreateTemplate spSheetTmplCrBean = null;
								try
								{
									spSheetTmplCrBean = (ISheetCreateTemplate) appCtxt
									          .getBean(shMdt.getSheetName() + sheetTemplateSrv_Suffix);
								}
								catch (Exception ex)
								{
									// Do Nothing
								}
								if (spSheetTmplCrBean != null)
								{
									try
									{
										spSheetTmplCrBean.createSheet(wbCtxt, shMdt);
									}
									catch (EX_General e)
									{
										EX_General egen = new EX_General("ERRTMPLSHCR", new Object[]
										{ shMdt.getSheetName(), e.getMessage()
										});
										msgFormatter.generate_message_snippet(egen);
										throw egen;
									}
								}
								else
								{
									/**
									 * Invoke Fallback Option with generic Template Bean
									 */
									ISheetCreateTemplate genSheetTmplCrBean = (ISheetCreateTemplate) appCtxt
									          .getBean(this.genericSheetTmplSrv);
									if (genSheetTmplCrBean != null)
									{
										try
										{
											genSheetTmplCrBean.createSheet(wbCtxt, shMdt);
										}
										catch (EX_General e)
										{
											EX_General egen = new EX_General("ERRTMPLSHCR", new Object[]
											{ shMdt.getSheetName(), e.getMessage()
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

				// finally write the workbook context to physical file
				wbCtxt.write(outStream);
				outStream.close();
				// Write Success Message in Log
				GeneralMessage msgGen = new GeneralMessage("TEMPLSUCC", new Object[]
				{ filePath
				});
				msgFormatter.generate_message_snippet(msgGen);

			}

		}

	}

}
