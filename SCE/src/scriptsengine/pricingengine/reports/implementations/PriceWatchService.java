package scriptsengine.pricingengine.reports.implementations;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.charts.AxisCrosses;
import org.apache.poi.ss.usermodel.charts.AxisPosition;
import org.apache.poi.ss.usermodel.charts.ChartAxis;
import org.apache.poi.ss.usermodel.charts.ChartDataSource;
import org.apache.poi.ss.usermodel.charts.DataSources;
import org.apache.poi.ss.usermodel.charts.LineChartData;
import org.apache.poi.ss.usermodel.charts.LineChartSeries;
import org.apache.poi.ss.usermodel.charts.ValueAxis;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTMarker;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTMarkerStyle;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.exceptions.EX_InvalidObjectException;
import modelframework.exceptions.EX_NotRootException;
import modelframework.implementations.GeneralMessage;
import modelframework.implementations.MessagesFormatter;
import modelframework.interfaces.IQueryService;
import modelframework.managers.QueryManager;
import scriptsengine.enums.SCEenums.direction;
import scriptsengine.pojos.OB_Scrip_BalSheet;
import scriptsengine.pojos.OB_Scrip_General;
import scriptsengine.pojos.OB_Scrip_Sector;
import scriptsengine.pricingengine.definitions.TY_PricesReturn;
import scriptsengine.pricingengine.definitions.TY_PwItemPPSrv;
import scriptsengine.pricingengine.reports.definitions.TY_PriceWatchItem;
import scriptsengine.pricingengine.reports.interfaces.IPriceWatchService;
import scriptsengine.pricingengine.services.interfaces.ISA_ScripPriceProjectionService;
import scriptsengine.reportsengine.interfaces.IXLSReportAware;
import scriptsengine.sectors.services.implementations.SectorsService;
import scriptsengine.sectors.services.interfaces.ISectorsService;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.services.interfaces.IScripDataService;
import scriptsengine.uploadengine.validations.interfaces.IScripExists;
import scriptsengine.utilities.implementations.DeltaCalcService;
import scriptsengine.utilities.implementations.XLSCellStyleGetter;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PriceWatchService implements IPriceWatchService, ApplicationContextAware
{
	@Autowired
	private MessagesFormatter						msgFormatter;

	@Autowired
	private DeltaCalcService							deltaSrv;
	@Autowired
	private SectorsService							secSrv;

	@Autowired
	private IScripExists							scExistsSrv;

	private ApplicationContext						appCtxt;
	private IScripDataService						scDataSrv;
	private ArrayList<TY_PriceWatchItem>				projPrices;
	private ArrayList<ISA_ScripPriceProjectionService>	ppSrvList;
	private ArrayList<OB_Scrip_General>				scripRootList;
	private ArrayList<OB_Scrip_Sector>					sectorsDetails;

	private final String							fileprefix		= "PriceWatch_";
	private final String							sheetName			= "Price Watch";
	private final String							dbSheetName		= "Dashboard";
	private final int								prTitleStart		= 8;
	private final int								prTitleEnd		= 11;
	private final int								prValueAdjStart	= 5;
	private final int								prValueAdjEnd		= 7;
	private final int								peTitleStart		= 2;
	private final int								peTitleEnd		= 4;
	private final int								perValueAdjStart	= 13;
	private final int								perValueAdjEnd		= 14;
	private final String							adjPEPriceSeries	= "Adj. PE Prices";
	private final String							avgPEPriceSeries	= "Avg. PE Prices";
	private final String							peCompSeries		= "PE Comparison";
	private final String							perReturnSeries	= "Return(s) Expected %";

	private final String[]							headers			=
	{ "Scrip Code", "Description", "Adjusted PE", "Average PE", "Sector PE", "Buy Price (Rs.)", "Avg. Price (Rs.)", "Sell Price (Rs.)",
	          "Buy Price (Rs.)", "Avg. Price (Rs.)", "Sell Price (Rs.)", "200 DMA", "Promoter Share", "Min. to Avg. %", "Avg. to Max %",
	          "PE Adjusted"
	};

	private final String[]							headersDB			=
	{ "Scrip Code", "Projected Prices for Adjusted and Average PE", "PE Comparison", "Exepcted Returns %"
	};

	/**
	 * @return the ppSrvList
	 */
	public ArrayList<ISA_ScripPriceProjectionService> getPpSrvList()
	{
		return ppSrvList;
	}

	@Override
	public ArrayList<TY_PriceWatchItem> computeProjectedPrices() throws EX_General
	{
		if (scDataSrv != null)
		{

			// Get the Root Object Name
			String rootObjName = getRootObjName();
			if (rootObjName != null)
			{
				try
				{
					IQueryService qs = (IQueryService) QueryManager.getQuerybyRootObjname(rootObjName);
					if (qs != null)
					{

						this.scripRootList = qs.executeQuery();
						if (scripRootList != null && deltaSrv != null)
						{
							if (scripRootList.size() > 0)
							{
								populateSectorDetails();
								for ( OB_Scrip_General scRoot : scripRootList )
								{
									/**
									 * Only proceed with Scrips which have balance sheets Maintained- Omit the
									 * Ones which don't have balance Sheets maintianed
									 */
									ArrayList<OB_Scrip_BalSheet> balSheetEntList = scRoot.getRelatedEntities("OB_Scrip_BalSheet_Rel");
									if (balSheetEntList != null)
									{
										if (balSheetEntList.size() > 0)
										{

											TY_PwItemPPSrv pwITemSrv = this.generatePWItem(scRoot);
											if (pwITemSrv != null)
											{

												this.projPrices.add(pwITemSrv.getPwItem());
												/**
												 * Store the Scrip Price Projection Service Instance in
												 * Order to
												 * render details and avoid recalculations
												 */
												this.ppSrvList.add(pwITemSrv.getScppSrv());
											}
										}

									}

								}

							}
						}
					}

				}

				catch (Exception e)
				{
					EX_General egen = new EX_General("ERR_PWWATCHCOMPU", new Object[]
					{ e.getMessage()
					});
					msgFormatter.generate_message_snippet(egen);
					throw egen;
				}
			}
		}

		return projPrices;

	}

	@Override
	public void computeandShowProjectedPrices_Console() throws EX_General
	{
		// DecimalFormat df = new DecimalFormat();
		// df.setMaximumFractionDigits(2);
		String hFormat = "%-5s%40s%20s%20s%20s%20s%20s%20s%20s%20s";
		String dFormat = "%-5s%40s%20.2f%20.2f%20.2f%20.2f%20.2f%20.2f%20.2f%20s";
		String scCode = "Scrip Code";
		String scDesc = "Description";
		String avgPEPrice = "Price Avg. PE";
		String maxPEPrice = "Price Max. PE";
		String minPEPrice = "Price Min. PE";
		String deltaavgMin = "Delta MinAvg";
		String deltaavgMax = "Delta AvgMax";
		String scripAvgPE = "Scrip Avg. PE";
		String sectorAvgPE = "Sector Avg. PE";
		String peAdjusted = "PE Adjusted";
		if (this.projPrices.size() == 0)
		{
			computeProjectedPrices();
		}

		System.out.println(
		          "_________________________________________________________________________________________________________________________________________________________________________________________");
		System.out.println(
		          "                                                                                               PRICE WATCH REPORT                                                                        ");
		System.out.println(
		          "_________________________________________________________________________________________________________________________________________________________________________________________");
		System.out.format(hFormat, scCode, scDesc, avgPEPrice, maxPEPrice, minPEPrice, deltaavgMin, deltaavgMax, scripAvgPE, sectorAvgPE,
		          peAdjusted);
		System.out.println();
		System.out.println(
		          "_________________________________________________________________________________________________________________________________________________________________________________________");

		for ( TY_PriceWatchItem pwItem : projPrices )
		{

			System.out.format(dFormat, pwItem.getScCode(), pwItem.getScDesc(), pwItem.getProjPrices().getAvgPE_PP(),
			          pwItem.getProjPrices().getMaxPE_PP(), pwItem.getProjPrices().getMinPE_PP(), pwItem.getDeltaAvgMin(), pwItem.getDeltaAvgMax(),
			          pwItem.getAvgPE(), pwItem.getSecPE(), pwItem.getProjPrices().isPeAdjusted());
			System.out.println();
			System.out.println(
			          "---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public void computeandDLProjectedPrices_XLS(String filepath) throws EX_General
	{

		if (filepath != null)
		{
			/**
			 * Prepare fileName
			 */
			// Write the workbook in file system
			FileOutputStream outStream;
			try
			{
				outStream = new FileOutputStream(new File(filepath + prepareFileName()));

				if (outStream != null)
				{
					XSSFWorkbook wbCtxt = new XSSFWorkbook();
					if (wbCtxt != null)
					{
						XSSFSheet sheet = wbCtxt.createSheet(sheetName);
						if (sheet != null)
						{
							/**
							 * Compute Projected Prices
							 */
							computeProjectedPrices();

							generatePWSheet(wbCtxt, sheet, false);

							generateDashBoard(wbCtxt);

						}

						// finally write the workbook context to physical file
						wbCtxt.write(outStream);
						outStream.close();
						wbCtxt.close();
						// Write Success Message in Log
						GeneralMessage msgGen = new GeneralMessage("PWDLSUCC", new Object[]
						{ filepath
						});
						msgFormatter.generate_message_snippet(msgGen);
					}

				}

			}
			catch (Exception e)
			{
				/*
				 * EX_General egen = new EX_General("ERR_PWWATCHCOMPU", new Object[]
				 * { e.getMessage()
				 * });
				 * msgFormatter.generate_message_snippet(egen);
				 * throw egen;
				 */
			}
		}

	}

	@Override
	public void generateDetailedReport_XLS(String filepath) throws EX_General
	{
		if (filepath != null)
		{
			/**
			 * Prepare fileName
			 */
			// Write the workbook in file system
			FileOutputStream outStream;
			try
			{
				outStream = new FileOutputStream(new File(filepath + prepareFileName()));

				if (outStream != null)
				{
					XSSFWorkbook wbCtxt = new XSSFWorkbook();
					if (wbCtxt != null)
					{
						XSSFSheet sheet = wbCtxt.createSheet(sheetName);
						if (sheet != null)
						{
							computeProjectedPrices();
							generatePWSheet(wbCtxt, sheet, true);
							generateDashBoard(wbCtxt);

						}

						// Get the PPsrv for each Scrip
						for ( ISA_ScripPriceProjectionService ppSrv : this.getPpSrvList() )
						{
							if (ppSrv instanceof IXLSReportAware)
							{
								((IXLSReportAware) ppSrv).generateXLSReport(wbCtxt);
							}

						}
						// finally write the workbook context to physical file
						wbCtxt.write(outStream);
						outStream.close();
						wbCtxt.close();
						// Write Success Message in Log
						GeneralMessage msgGen = new GeneralMessage("PWDLSUCC", new Object[]
						{ filepath
						});
						msgFormatter.generate_message_snippet(msgGen);

					}
				}
			}
			catch (Exception e)
			{

			}
		}
	}

	@Override
	public TY_PriceWatchItem computeProjectedPriceforScripdesc(String scripDescStartsWith) throws EX_General
	{
		TY_PriceWatchItem pwItem = null;
		if (scDataSrv != null)
		{

			// Get the Root Object Name
			String rootObjName = getRootObjName();
			if (rootObjName != null && scripDescStartsWith != null)
			{
				try
				{
					if (scExistsSrv != null)
					{
						this.scripRootList.add(scExistsSrv.getScripRootbyDescStartingwith(scripDescStartsWith));
					}

					if (deltaSrv != null)
					{
						if (scripRootList.size() > 0)
						{
							populateSectorDetails(scripRootList.get(0).getscSector());

							TY_PwItemPPSrv pwITemSrv = this.generatePWItem(scripRootList.get(0));
							if (pwITemSrv != null)
							{

								pwItem = pwITemSrv.getPwItem();
								this.projPrices.add(pwItem);

								/**
								 * Store the Scrip Price Projection Service Instance in Order to
								 * render details and avoid recalculations
								 */
								this.ppSrvList.add(pwITemSrv.getScppSrv());
							}

						}
					}

				}

				catch (Exception e)
				{
					EX_General egen = new EX_General("ERR_PWWATCHCOMPU", new Object[]
					{ e.getMessage()
					});
					msgFormatter.generate_message_snippet(egen);
					throw egen;
				}
			}
		}

		return pwItem;
	}

	@Override
	public TY_PriceWatchItem computeProjectedPriceforScCode(String scCode) throws EX_General
	{
		TY_PriceWatchItem pwItem = null;
		if (scDataSrv != null)
		{

			// Get the Root Object Name
			String rootObjName = getRootObjName();
			if (rootObjName != null && scCode != null)
			{
				try
				{
					if (scExistsSrv != null)
					{
						this.scripRootList.add(scExistsSrv.getScripRootbyCode(scCode));
					}

					if (deltaSrv != null)
					{
						if (scripRootList.size() > 0)
						{
							populateSectorDetails(scripRootList.get(0).getscSector());

							TY_PwItemPPSrv pwITemSrv = this.generatePWItem(scripRootList.get(0));
							if (pwITemSrv != null)
							{

								pwItem = pwITemSrv.getPwItem();
								this.projPrices.add(pwItem);

								/**
								 * Store the Scrip Price Projection Service Instance in Order to
								 * render details and avoid recalculations
								 */
								this.ppSrvList.add(pwITemSrv.getScppSrv());
							}

						}
					}

				}

				catch (Exception e)
				{
					EX_General egen = new EX_General("ERR_PWWATCHCOMPU", new Object[]
					{ e.getMessage()
					});
					msgFormatter.generate_message_snippet(egen);
					throw egen;
				}
			}
		}

		return pwItem;
	}

	@Override
	public void setApplicationContext(ApplicationContext ctxt) throws BeansException
	{
		this.appCtxt = ctxt;
		if (appCtxt != null)
		{
			this.scDataSrv = appCtxt.getBean(IScripDataService.class);
			if (scDataSrv.getWBMetaData() == null)
			{
				try
				{
					scDataSrv.initialize();

				}
				catch (EX_General e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	private String getRootObjName()
	{
		String rootObjName = null;

		if (scDataSrv != null)
		{
			rootObjName = scDataSrv.getWBMetaData().getSheetsMetadata().stream().filter(x -> x.getBasesheet() == true).findFirst().get()
			          .getBobjName();
		}

		return rootObjName;
	}

	/**
	 * 
	 */
	public PriceWatchService()
	{
		super();
		this.projPrices = new ArrayList<TY_PriceWatchItem>();
		this.sectorsDetails = new ArrayList<OB_Scrip_Sector>();
		this.ppSrvList = new ArrayList<ISA_ScripPriceProjectionService>();
		this.scripRootList = new ArrayList<OB_Scrip_General>();
	}

	private String prepareFileName()
	{

		String fileName = null;

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		String date = (dateFormat.format(cal.getTime()));

		fileName = fileprefix + date + ".xlsx";

		return fileName;

	}

	private void populateSectorDetails() throws EX_General
	{
		if (this.sectorsDetails != null)
		{
			if (this.sectorsDetails.size() == 0)
			{
				if (this.secSrv == null)
				{
					secSrv = (SectorsService) appCtxt.getBean(ISectorsService.class);
				}
				if (this.secSrv != null)
				{

					try
					{
						this.sectorsDetails = secSrv.getAllSectors();
					}
					catch (EX_InvalidObjectException | EX_NotRootException | SQLException e)
					{
						EX_General egen = new EX_General("ERRGETSECTORS", new Object[]
						{ e.getMessage()
						});
						msgFormatter.generate_message_snippet(egen);
						throw egen;
					}

				}

			}
		}
	}

	private void populateSectorDetails(String sector) throws EX_General
	{
		if (this.sectorsDetails != null)
		{
			if (this.sectorsDetails.size() == 0)
			{
				if (this.secSrv == null)
				{
					secSrv = (SectorsService) appCtxt.getBean(ISectorsService.class);
				}
				if (this.secSrv != null)
				{

					try
					{
						this.sectorsDetails.add(secSrv.getSectorbyCode(sector));
					}
					catch (Exception e)
					{
						EX_General egen = new EX_General("ERRGETSECTORS", new Object[]
						{ e.getMessage()
						});
						msgFormatter.generate_message_snippet(egen);
						throw egen;
					}

				}

			}
		}
	}

	private void generatePWSheet(XSSFWorkbook wbCtxt, XSSFSheet sheet, boolean hlinkEnabled)
	{
		int c = 0, i = 1;
		XSSFCreationHelper helper = wbCtxt.getCreationHelper();
		XSSFHyperlink link = null;
		// Create Header Row
		Row row = sheet.createRow(0);
		if (row != null)
		{
			for ( String header : headers )
			{
				Cell cell = row.createCell(c);
				cell.setCellValue(header);
				cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleHeaderBlueUline, wbCtxt, CellType.STRING));
				c++;

			}
		}

		// Populate Price Watch Sheet with Data
		for ( TY_PriceWatchItem pwItem : projPrices )
		{
			row = sheet.createRow(i);
			c = 0;
			while (c <= 15)
			{
				Cell cell = row.createCell(c);

				switch (c)
				{
					case 0:
						cell.setCellValue(pwItem.getScCode());
						if (hlinkEnabled)
						{
							link = helper.createHyperlink(HyperlinkType.DOCUMENT);
							String tgAddress = "'" + pwItem.getScCode() + "'" + "!A1";
							link.setAddress(tgAddress);
							cell.setHyperlink(link);
							cell.setCellStyle(
							          XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleKeyGoldHlink, wbCtxt, CellType.STRING));
						}
						else
						{
							cell.setCellStyle(
							          XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleKeyGold, wbCtxt, CellType.STRING));
						}
						c++;
						break;
					case 1:
						cell.setCellValue(pwItem.getScDesc());
						c++;
						break;

					case 2:
						cell.setCellType(CellType.NUMERIC);
						cell.setCellValue(pwItem.getAvgPE());
						cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleLightGray, wbCtxt, CellType.NUMERIC));
						c++;
						break;

					case 3:
						cell.setCellType(CellType.NUMERIC);
						cell.setCellValue(pwItem.getUnadj_avgPE());
						cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleNoFill, wbCtxt, CellType.NUMERIC));
						c++;
						break;

					case 4:
						cell.setCellType(CellType.NUMERIC);
						cell.setCellValue(pwItem.getSecPE());
						cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleTeal, wbCtxt, CellType.NUMERIC));
						c++;

						break;

					case 5:
						cell.setCellType(CellType.NUMERIC);
						cell.setCellValue(pwItem.getProjPrices().getMinPE_PP());
						cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleLightGray, wbCtxt, CellType.NUMERIC));
						c++;
						break;
					case 6:
						cell.setCellType(CellType.NUMERIC);
						cell.setCellValue(pwItem.getProjPrices().getAvgPE_PP());
						cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleLightGray, wbCtxt, CellType.NUMERIC));
						c++;
						break;
					case 7:
						cell.setCellType(CellType.NUMERIC);
						cell.setCellValue(pwItem.getProjPrices().getMaxPE_PP());
						cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleLightGray, wbCtxt, CellType.NUMERIC));
						c++;
						break;

					case 8:
						cell.setCellType(CellType.NUMERIC);
						cell.setCellValue(pwItem.getUnadj_projPrices().getMinPE_PP());
						cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleNoFill, wbCtxt, CellType.NUMERIC));
						c++;
						break;
					case 9:
						cell.setCellType(CellType.NUMERIC);
						cell.setCellValue(pwItem.getUnadj_projPrices().getAvgPE_PP());
						cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleNoFill, wbCtxt, CellType.NUMERIC));
						c++;
						break;
					case 10:
						cell.setCellType(CellType.NUMERIC);
						cell.setCellValue(pwItem.getUnadj_projPrices().getMaxPE_PP());
						cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleNoFill, wbCtxt, CellType.NUMERIC));
						c++;
						break;

					case 11:
						cell.setCellType(CellType.NUMERIC);
						cell.setCellValue(pwItem.getDMA_200());
						cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleTeal, wbCtxt, CellType.NUMERIC));
						c++;
						break;

					case 12:
						cell.setCellType(CellType.NUMERIC);
						cell.setCellValue(pwItem.getPromoterHolding());
						cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleLightBlue, wbCtxt, CellType.NUMERIC));
						c++;
						break;

					case 13:
						cell.setCellType(CellType.NUMERIC);
						cell.setCellValue(pwItem.getDeltaAvgMin());
						cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleLightGray, wbCtxt, CellType.NUMERIC));
						c++;
						break;

					case 14:
						cell.setCellType(CellType.NUMERIC);
						cell.setCellValue(pwItem.getDeltaAvgMax());
						cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleLightGray, wbCtxt, CellType.NUMERIC));
						c++;

						break;

					case 15:
						cell.setCellType(CellType.BOOLEAN);
						cell.setCellValue(pwItem.getProjPrices().isPeAdjusted());
						cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleTeal, wbCtxt, CellType.BOOLEAN));
						c++;
						i++;
						break;

					default:
						break;
				}
			}

		}

		for ( int j = 0; j < headers.length; j++ )
		{
			sheet.autoSizeColumn(j);
		}
		// Freeze top Row first column
		sheet.createFreezePane(1, 1);

	}

	/**
	 * ------------------ Generate Dashboard-----------------------------------------------
	 * 
	 * @param wbCtxt
	 *             ------------------------------------------------------------------------------------
	 */
	private void generateDashBoard(XSSFWorkbook wbCtxt)
	{
		if (wbCtxt != null)
		{
			XSSFCreationHelper helper = wbCtxt.getCreationHelper();
			XSSFHyperlink link = null;
			XSSFSheet pwSheetCtxt = wbCtxt.getSheet(sheetName);
			if (pwSheetCtxt != null)
			{
				// Create a new DashBoard Sheet
				XSSFSheet dbSheet = wbCtxt.createSheet(dbSheetName);
				if (dbSheet != null)
				{
					/**
					 * Iterate through each row of PW Sheet to generate dashboard Sheet Charts
					 */
					// Get iterator to all the rows in PW sheet
					Iterator<Row> rowIterator = pwSheetCtxt.iterator();
					int rownum = 0, colnum = 0, rowhead = 0;
					String scCode = null;

					// dbHrow.setHeight((short) 1000);
					/**
					 * Prepare Header Row
					 */
					Row dbHrow = dbSheet.createRow(rowhead);
					if (dbHrow != null)
					{
						for ( String header : headersDB )
						{

							Cell cell = dbHrow.createCell(colnum);
							cell.setCellValue(header);
							XSSFCellStyle styleRetH = XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleHeaderBlueUline, wbCtxt,
							          CellType.STRING);
							styleRetH.setWrapText(true);
							cell.setCellStyle(styleRetH);

							colnum++;
						}
					}

					while (rowIterator.hasNext())
					{
						Row row = rowIterator.next();
						if (rownum >= 1) // Ignore header row
						{
							if (row != null)
							{
								// Get Scrip Code from PW sheet
								Cell scCell = row.getCell(0);
								if (scCell != null)
								{
									scCode = scCell.getStringCellValue();
									if (scCode != null)
									{
										colnum = 0;
										// Create a Subsequent Row in Dashboard Sheet
										Row dbrow = dbSheet.createRow(rownum);
										if (dbrow != null)
										{
											dbrow.setHeight((short) 4500);
											// ----Scrip Code
											Cell dbcell = dbrow.createCell(colnum);
											dbcell.setCellValue(scCode);
											link = helper.createHyperlink(HyperlinkType.DOCUMENT);
											String tgAddress = "'" + scCode + "'" + "!A1";
											link.setAddress(tgAddress);
											dbcell.setHyperlink(link);
											XSSFCellStyle styleRet = XLSCellStyleGetter.getCellStyleforCode(
											          XLSCellStyleGetter.styleBottomThickBorderLGrayBold, wbCtxt, CellType.STRING);
											dbcell.setCellStyle(styleRet);

											colnum++;
											// --- Scrip Code

											// ---Prices
											// Chart-----------------------------------------------------------------------------------
											/* Create a drawing canvas on the worksheet */
											Cell dbcelllchart = dbrow.createCell(colnum);
											XSSFCellStyle styleRetchart = XLSCellStyleGetter.getCellStyleforCode(
											          XLSCellStyleGetter.styleNoFillBorderBottomThick, wbCtxt, CellType.STRING);
											dbcelllchart.setCellStyle(styleRetchart);

											XSSFDrawing xlsx_drawing = dbSheet.createDrawingPatriarch();
											/* Define anchor points in the worksheet to position the chart */
											XSSFClientAnchor anchor = xlsx_drawing.createAnchor(10 * Units.EMU_PER_PIXEL,
											          10 * Units.EMU_PER_PIXEL, 520 * Units.EMU_PER_PIXEL, 280 * Units.EMU_PER_PIXEL,
											          colnum, rownum, colnum, rownum);

											/* Create the chart object based on the anchor point */
											XSSFChart my_line_chart = xlsx_drawing.createChart(anchor);

											/*
											 * Define legends for the line chart and set the position of the
											 * legend
											 */
											// XSSFChartLegend legend = my_line_chart.getOrCreateLegend();
											// legend.setPosition(LegendPosition.BOTTOM);
											/* Create data for the chart */
											/* Define chart AXIS */
											ChartAxis bottomAxis = my_line_chart.getChartAxisFactory()
											          .createCategoryAxis(AxisPosition.BOTTOM);
											// bottomAxis.setVisible(false);

											ValueAxis leftAxis = my_line_chart.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
											leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
											// leftAxis.setVisible(false);
											LineChartData data = my_line_chart.getChartDataFactory().createLineChartData();
											/* Define Data sources for the chart */
											/* Set the right cell range that contain values for the chart */
											/* Pass the worksheet and cell range address as inputs */
											/*
											 * Cell Range Address is defined as First row, last row, first
											 * column, last column
											 */
											ChartDataSource<String> xs = DataSources.fromStringCellRange(pwSheetCtxt,
											          new CellRangeAddress(0, 0, prTitleStart, prTitleEnd));
											ChartDataSource<Number> ys1 = DataSources.fromNumericCellRange(pwSheetCtxt,
											          new CellRangeAddress(rownum, rownum, prValueAdjStart, prValueAdjEnd));

											ChartDataSource<Number> ys2 = DataSources.fromNumericCellRange(pwSheetCtxt,
											          new CellRangeAddress(rownum, rownum, prTitleStart, prTitleEnd));

											/* Add chart data sources as data to the chart */
											LineChartSeries adjPEPrices = data.addSeries(xs, ys1);
											adjPEPrices.setTitle(adjPEPriceSeries);

											LineChartSeries avgPEPrices = data.addSeries(xs, ys2);
											avgPEPrices.setTitle(avgPEPriceSeries);

											/* Plot the chart with the inputs from data and chart axis */
											my_line_chart.plot(data, new ChartAxis[]
											{ bottomAxis, leftAxis
											});

											CTPlotArea plotArea = my_line_chart.getCTChart().getPlotArea();

											CTMarker ctMarker = CTMarker.Factory.newInstance();
											ctMarker.setSymbol(CTMarkerStyle.Factory.newInstance());
											CTBoolean ctBool = CTBoolean.Factory.newInstance();
											ctBool.setVal(false);

											for ( CTLineSer ser : plotArea.getLineChartArray()[0].getSerArray() )
											{
												// Don't smoothen and don't put markers
												ser.setMarker(ctMarker);
												ser.setSmooth(ctBool);

											}
											colnum++;

											// ---Prices
											// Chart----------------------------------------------------------------------------------

											// ---PE Chart ----------------------------

											Cell dbcelllPEchart = dbrow.createCell(colnum);

											dbcelllPEchart.setCellStyle(styleRetchart);

											XSSFDrawing xlsx_drawingPE = dbSheet.createDrawingPatriarch();
											/* Define anchor points in the worksheet to position the chart */
											XSSFClientAnchor anchorPE = xlsx_drawingPE.createAnchor(10 * Units.EMU_PER_PIXEL,
											          10 * Units.EMU_PER_PIXEL, 300 * Units.EMU_PER_PIXEL, 280 * Units.EMU_PER_PIXEL,
											          colnum, rownum, colnum, rownum);

											/* Create the chart object based on the anchor point */
											XSSFChart my_line_chartPE = xlsx_drawingPE.createChart(anchorPE);
											/* Create data for the chart */
											/* Define chart AXIS */
											ChartAxis bottomAxisPE = my_line_chartPE.getChartAxisFactory()
											          .createCategoryAxis(AxisPosition.BOTTOM);
											// bottomAxis.setVisible(false);

											ValueAxis leftAxisPE = my_line_chartPE.getChartAxisFactory()
											          .createValueAxis(AxisPosition.LEFT);
											leftAxisPE.setCrosses(AxisCrosses.AUTO_ZERO);
											// leftAxis.setVisible(false);
											LineChartData dataPE = my_line_chartPE.getChartDataFactory().createLineChartData();
											/* Define Data sources for the chart */
											/* Set the right cell range that contain values for the chart */
											/* Pass the worksheet and cell range address as inputs */
											/*
											 * Cell Range Address is defined as First row, last row, first
											 * column, last column
											 */
											ChartDataSource<String> xsPE = DataSources.fromStringCellRange(pwSheetCtxt,
											          new CellRangeAddress(0, 0, peTitleStart, peTitleEnd));
											ChartDataSource<Number> ysPE = DataSources.fromNumericCellRange(pwSheetCtxt,
											          new CellRangeAddress(rownum, rownum, peTitleStart, peTitleEnd));

											/* Add chart data sources as data to the chart */
											LineChartSeries peComp = dataPE.addSeries(xsPE, ysPE);
											peComp.setTitle(peCompSeries);

											/* Plot the chart with the inputs from data and chart axis */
											my_line_chartPE.plot(dataPE, new ChartAxis[]
											{ bottomAxisPE, leftAxisPE
											});

											CTPlotArea plotAreaPE = my_line_chartPE.getCTChart().getPlotArea();

											for ( CTLineSer ser : plotAreaPE.getLineChartArray()[0].getSerArray() )
											{
												// Don't smoothen and don't put markers
												ser.setMarker(ctMarker);
												ser.setSmooth(ctBool);

											}
											colnum++;

											// ----PE Chart --------------------------

											// ---Percentages Chart ----------------------------

											Cell dbcelllPerchart = dbrow.createCell(colnum);

											dbcelllPerchart.setCellStyle(styleRetchart);

											XSSFDrawing xlsx_drawingPer = dbSheet.createDrawingPatriarch();
											/* Define anchor points in the worksheet to position the chart */
											XSSFClientAnchor anchorPer = xlsx_drawingPer.createAnchor(10 * Units.EMU_PER_PIXEL,
											          10 * Units.EMU_PER_PIXEL, 300 * Units.EMU_PER_PIXEL, 280 * Units.EMU_PER_PIXEL,
											          colnum, rownum, colnum, rownum);

											/* Create the chart object based on the anchor point */
											XSSFChart my_line_chartPer = xlsx_drawingPer.createChart(anchorPer);
											/* Create data for the chart */
											/* Define chart AXIS */
											ChartAxis bottomAxisPer = my_line_chartPer.getChartAxisFactory()
											          .createCategoryAxis(AxisPosition.BOTTOM);
											// bottomAxis.setVisible(false);

											ValueAxis leftAxisPer = my_line_chartPer.getChartAxisFactory()
											          .createValueAxis(AxisPosition.LEFT);
											leftAxisPer.setCrosses(AxisCrosses.AUTO_ZERO);
											// leftAxis.setVisible(false);
											LineChartData dataPer = my_line_chartPer.getChartDataFactory().createLineChartData();
											/* Define Data sources for the chart */
											/* Set the right cell range that contain values for the chart */
											/* Pass the worksheet and cell range address as inputs */
											/*
											 * Cell Range Address is defined as First row, last row, first
											 * column, last column
											 */
											ChartDataSource<String> xsPer = DataSources.fromStringCellRange(pwSheetCtxt,
											          new CellRangeAddress(0, 0, perValueAdjStart, perValueAdjEnd));
											ChartDataSource<Number> ysPer = DataSources.fromNumericCellRange(pwSheetCtxt,
											          new CellRangeAddress(rownum, rownum, perValueAdjStart, perValueAdjEnd));

											/* Add chart data sources as data to the chart */
											LineChartSeries perComp = dataPer.addSeries(xsPer, ysPer);
											perComp.setTitle(perReturnSeries);

											/* Plot the chart with the inputs from data and chart axis */
											my_line_chartPer.plot(dataPer, new ChartAxis[]
											{ bottomAxisPer, leftAxisPer
											});

											CTPlotArea plotAreaPer = my_line_chartPer.getCTChart().getPlotArea();

											for ( CTLineSer ser : plotAreaPer.getLineChartArray()[0].getSerArray() )
											{
												// Don't smoothen and don't put markers
												ser.setMarker(ctMarker);
												ser.setSmooth(ctBool);

											}
											colnum++;

											// ----Percentages Chart --------------------------

										}
									}

								}
							}
						}
						rownum++;
					}
				}

				dbSheet.setColumnWidth(0, 5500);
				dbSheet.setColumnWidth(1, 20000);
				dbSheet.setColumnWidth(2, 14000);
				dbSheet.setColumnWidth(3, 14000);
				// Freeze top Row first column
				dbSheet.createFreezePane(1, 1);

			}
		}
	}

	/**********************************************************************
	 * Generate Price Watch Item for Scrip Root Object
	 * 
	 * @param scRoot
	 *             - SCrip Root Object
	 * @return - Price Watch Item
	 *         *
	 * @throws EX_General
	 *              *******************************************************************
	 */
	private TY_PwItemPPSrv generatePWItem(OB_Scrip_General scRoot) throws EX_General
	{
		TY_PwItemPPSrv pwItem_ppSrv = null;
		;

		TY_PriceWatchItem pwItem = null;

		if (scRoot != null)
		{
			double deltaPE = 0;
			pwItem = new TY_PriceWatchItem();
			pwItem_ppSrv = new TY_PwItemPPSrv();
			pwItem.setScCode(scRoot.getscCode());
			pwItem.setScDesc(scRoot.getscName());

			if (appCtxt != null)
			{
				ISA_ScripPriceProjectionService scppSrv = appCtxt.getBean(ISA_ScripPriceProjectionService.class);
				if (scppSrv != null)
				{

					ArrayList<TY_PricesReturn> pricesReturn = scppSrv.getPriceProjectionsforScrip(scRoot.getscCode());

					if (pricesReturn != null)
					{
						pwItem.setDMA_200(scppSrv.getDMA_200());
						pwItem.setPromoterHolding(scppSrv.getPromoterHolding());
						if (scppSrv.getAvgPE() != null)
						{
							pwItem.setAvgPE(scppSrv.getAvgPE().getAvgPE() * scppSrv.getLastNp_FVR().getFVR());
							pwItem.setUnadj_avgPE(scppSrv.getAvgPE().getAvgPE_unadjusted() * scppSrv.getLastNp_FVR().getFVR());
							deltaPE = deltaSrv.getDeltaPercentage(pwItem.getAvgPE(), pwItem.getUnadj_avgPE());
						}

						pwItem.getProjPrices().setAvgPE_PP(pricesReturn.get(0).getProjectedPrices().getAvgPE_PP());
						if (deltaPE > 0)
						{
							pwItem.getUnadj_projPrices().setAvgPE_PP(
							          deltaSrv.adjustNumberbyPercentage(pwItem.getProjPrices().getAvgPE_PP(), deltaPE, direction.INCREASE));
						}
						else
						{
							pwItem.getUnadj_projPrices().setAvgPE_PP(pricesReturn.get(0).getProjectedPrices().getAvgPE_PP());
						}

						pwItem.getProjPrices().setMaxPE_PP(pricesReturn.get(0).getProjectedPrices().getMaxPE_PP());
						if (deltaPE > 0)
						{
							pwItem.getUnadj_projPrices().setMaxPE_PP(
							          deltaSrv.adjustNumberbyPercentage(pwItem.getProjPrices().getMaxPE_PP(), deltaPE, direction.INCREASE));
						}
						else
						{
							pwItem.getUnadj_projPrices().setMaxPE_PP(pricesReturn.get(0).getProjectedPrices().getMaxPE_PP());
						}

						pwItem.getProjPrices().setMinPE_PP(pricesReturn.get(0).getProjectedPrices().getMinPE_PP());
						if (deltaPE > 0)
						{
							pwItem.getUnadj_projPrices().setMinPE_PP(
							          deltaSrv.adjustNumberbyPercentage(pwItem.getProjPrices().getMinPE_PP(), deltaPE, direction.INCREASE));
						}
						else
						{
							pwItem.getUnadj_projPrices().setMinPE_PP(pricesReturn.get(0).getProjectedPrices().getMinPE_PP());
						}

						pwItem.getProjPrices().setPeAdjusted(pricesReturn.get(0).getProjectedPrices().isPeAdjusted());

						pwItem.setDeltaAvgMin(
						          deltaSrv.getDeltaPercentage(pwItem.getProjPrices().getMinPE_PP(), pwItem.getProjPrices().getAvgPE_PP()));
						pwItem.setDeltaAvgMax(
						          deltaSrv.getDeltaPercentage(pwItem.getProjPrices().getAvgPE_PP(), pwItem.getProjPrices().getMaxPE_PP()));

						pwItem.setSecPE(this.sectorsDetails.stream().filter(x -> x.getSector().equals(scRoot.getscSector())).findFirst().get()
						          .getAvgPE());

					}
					pwItem_ppSrv.setPwItem(pwItem);
					pwItem_ppSrv.setScppSrv(scppSrv);

				}
			}
		}
		return pwItem_ppSrv;
	}

}
