package scriptsengine.uploadengine.templates.implementations;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.exceptions.EX_InvalidObjectException;
import modelframework.exceptions.EX_InvalidRelationException;
import modelframework.exceptions.EX_NotRootException;
import modelframework.exceptions.EX_NullParamsException;
import modelframework.exceptions.EX_ParamCountMismatchException;
import modelframework.exceptions.EX_ParamInitializationException;
import modelframework.implementations.GeneralMessage;
import modelframework.implementations.MessagesFormatter;
import modelframework.types.TY_NameValue;
import scriptsengine.uploadengine.JAXB.definitions.SheetMetadata;
import scriptsengine.uploadengine.JAXB.definitions.WorkbookMetadata;
import scriptsengine.uploadengine.definitions.SheetDBSnapShot;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.services.interfaces.IScripDBSnapShot;
import scriptsengine.uploadengine.services.interfaces.IScripDataService;
import scriptsengine.uploadengine.templates.interfaces.IScripUpdateTemplate;
import scriptsengine.uploadengine.templates.sheetTemplates.interfaces.ISheetCreateTemplate;
import scriptsengine.uploadengine.validations.implementations.ScripExistsService;

/*
 * Service for Scrip Create Template Generation
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ScripUpdateTemplateService implements IScripUpdateTemplate, ApplicationContextAware
{

	@Autowired
	private IScripDataService	scDataSrv;

	@Autowired
	private MessagesFormatter	msgFormatter;

	@Autowired
	private ScripExistsService	scExSrv;

	private IScripDBSnapShot		scSnapShotSrv;

	private WorkbookMetadata		wbMdt;

	private XSSFWorkbook		wbCtxt;

	private ApplicationContext	appCtxt;

	private final String		wbName				= "UpdateTemplate.xlsx";

	private final String		sheetTemplateSrv_Suffix	= "SheetTmpCreateSrv";

	private final String		genericSheetTmplSrv		= "GenericSheetTmplScCreationSrv";

	private final String		SSShetName			= "DBSnapShot";

	private final String		dataMaintained			= "Data Maintained";
	private final String		dataNotMaintained		= "Data Not Maintained";

	private String[]			headers				=
	{ "Sheet Name", "Data Status", "Key(s) Combination", "Latest Value(s) for Keys Specified"
	};

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
	public void generateTemplateforScripUpdation(String filePath, String scripCode) throws IOException, EX_General
	{
		if (filePath != null)
		{
			// Write the workbook in file system
			FileOutputStream outStream = new FileOutputStream(new File(filePath + scripCode + wbName));
			if (outStream != null)
			{
				wbCtxt = new XSSFWorkbook();

				if (wbCtxt != null && scDataSrv != null)
				{
					if (scDataSrv.getWBMetaData() != null)
					{
						this.wbMdt = scDataSrv.getWBMetaData();

						/**
						 * Get the DB snapshot Bean for the Scrip
						 */

						scSnapShotSrv = appCtxt.getBean(IScripDBSnapShot.class);
						if (scSnapShotSrv != null)
						{
							// Get DB snapshot for Scrip
							scSnapShotSrv.getScripDBSnapshot(scripCode);
							if (this.scSnapShotSrv.getScDBsnapshot() != null)
							{
								// Prepare the SnapShot sheet
								prepareShapshotSheet();
							}
						}

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

	private void prepareShapshotSheet()
	{

		XSSFSheet sheet = wbCtxt.createSheet(this.SSShetName);
		if (sheet != null)
		{
			XSSFCellStyle styleKey = wbCtxt.createCellStyle();
			styleKey.setBorderBottom(CellStyle.BORDER_THICK);
			styleKey.setBottomBorderColor(IndexedColors.BLUE.getIndex());
			styleKey.setBorderLeft(CellStyle.BORDER_THICK);
			styleKey.setLeftBorderColor(IndexedColors.GREEN.getIndex());

			int c = 0, i = 1;
			// Create Header Row
			Row row = sheet.createRow(0);
			if (row != null)
			{
				for ( String header : headers )
				{
					Cell cell = row.createCell(c);
					cell.setCellValue(header);
					cell.setCellStyle(styleKey);
					c++;

				}
			}

			// Data Populate for Sheets DB Snapshot
			for ( SheetDBSnapShot shSS : this.scSnapShotSrv.getScDBsnapshot().getSheetsDBSS() )
			{
				row = sheet.createRow(i);
				c = 0;
				String keys = "";
				String vals = "";
				while (c < 4)
				{
					if (c == 0)
					{
						Cell cell = row.createCell(c);
						cell.setCellValue(shSS.getSheetName());
					}
					else if (c == 1)
					{
						Cell cell = row.createCell(c);
						if (shSS.isDataMaintained())
						{
							cell.setCellValue(dataMaintained);
						}
						else
						{
							cell.setCellValue(dataNotMaintained);

						}

					}
					else if (c == 2)
					{
						Cell cell = row.createCell(c);
						if (shSS.getKeyVals() != null)
						{
							int itr = 1;
							for ( TY_NameValue nameval : shSS.getKeyVals() )
							{

								if (itr > 1)
								{
									keys += ", ";
								}
								keys += nameval.Name;
								itr++;
							}
							cell.setCellValue(keys);
						}

					}
					else if (c == 3)
					{
						Cell cell = row.createCell(c);
						if (shSS.getKeyVals() != null)
						{
							int itr = 1;
							for ( TY_NameValue nameval : shSS.getKeyVals() )
							{

								if (itr > 1)
								{
									vals += ", ";
								}
								vals += nameval.Value.toString();
								itr++;

							}
							cell.setCellValue(vals);
						}
					}
					c++;

				}
				i++;
			}
		}
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		sheet.autoSizeColumn(3);
	}

	@Override
	public void generateTemplateforScripUpdationbyscripDesc(String filePath, String scripDescPattern) throws IOException, EX_General
	{
		// First get Scrip Code from DB and then proceed with usual Scrip Caode and Filepath method
		if (scripDescPattern != null)
		{
			if (scExSrv != null)
			{
				try
				{
					String scCode = scExSrv.getScripRootbyDescStartingwith(scripDescPattern).getscCode();
					if (scCode != null)
					{
						this.generateTemplateforScripUpdation(filePath, scCode);
					}
				}
				catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException
				          | NoSuchMethodException | SecurityException | EX_InvalidObjectException | EX_NotRootException | SQLException
				          | EX_NullParamsException | EX_ParamCountMismatchException | EX_ParamInitializationException
				          | EX_InvalidRelationException e)
				{
					EX_General egen = new EX_General("SCRIPEXISTERROR", new Object[]
					{ e.getMessage()
					});
					msgFormatter.generate_message_snippet(egen);
					throw egen;
				}
			}
		}
	}

}
