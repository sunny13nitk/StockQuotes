package scriptsengine.reportsengine.implementations;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.charts.AxisCrosses;
import org.apache.poi.ss.usermodel.charts.AxisPosition;
import org.apache.poi.ss.usermodel.charts.ChartAxis;
import org.apache.poi.ss.usermodel.charts.ChartDataSource;
import org.apache.poi.ss.usermodel.charts.DataSources;
import org.apache.poi.ss.usermodel.charts.LegendPosition;
import org.apache.poi.ss.usermodel.charts.LineChartData;
import org.apache.poi.ss.usermodel.charts.LineChartSeries;
import org.apache.poi.ss.usermodel.charts.ValueAxis;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.charts.XSSFChartLegend;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTMarker;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTMarkerStyle;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTTitle;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTTx;
import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import modelframework.implementations.GeneralMessage;
import modelframework.implementations.MessagesFormatter;
import scriptsengine.reportsengine.definitions.TY_ColStats;
import scriptsengine.reportsengine.definitions.Ty_SectorAvgStats;
import scriptsengine.reportsengine.definitions.Ty_SectorCount;
import scriptsengine.reportsengine.definitions.Ty_SectorXlsRowPositioner;
import scriptsengine.reportsengine.interfaces.IXLSReportGenerator;
import scriptsengine.reportsengine.repDS.definitions.TY_Attr_Container;
import scriptsengine.reportsengine.repDS.definitions.TY_ScRoot_AttrContainers;
import scriptsengine.statistics.JAXB.definitions.StatsAttrDetails;
import scriptsengine.statistics.JAXB.interfaces.IStatsSrvConfigMetadata;
import scriptsengine.statistics.definitions.TY_Attr_ScKeyFigsContainer;
import scriptsengine.statistics.definitions.TY_AttribSingleVal;
import scriptsengine.statistics.definitions.TY_Sector_AttrContainers;
import scriptsengine.statistics.definitions.TY_scCodeKeyFigure;
import scriptsengine.statistics.interfaces.IMShareScripAnalytics;
import scriptsengine.statistics.interfaces.IScripAnalysisSrv;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.utilities.implementations.XLSCellStyleGetter;
import scriptsengine.utilities.interfaces.IFileNameService;

@Service("ScripAnalysisXLSSrv")
public class ScripAnalysisXLSSrv implements IXLSReportGenerator
{
	@Autowired
	private MessagesFormatter		msgFormatter;

	@Autowired
	private IStatsSrvConfigMetadata	attrMdtSrv;

	@Autowired
	private IFileNameService			flNameSrv;

	@Autowired
	private IMShareScripAnalytics		mShareSrv;

	private final String			constant_Sector			= "Sector";
	private final String			constant_ScCode			= "Scrip Code";
	private final String			constant_fnamePrefix		= "ScripAnalysis_";
	private final String			constant_Summary			= "Summary";
	private final String			constant_Average			= "Averages";
	private final String			constant_oVW				= "_OVW";
	private final String[]			constant_secSheetChartsTitles	= new String[]
	{ "Revenue(s) and Profit(s) CAGR", "Earnings Per Share and Average Profit Margins", "Dividends Distribution and Capex Borrowings",
	          "Inventory Management and Avg. Reserves Provisions Ratio ", "Debt Positioning Over Years",
	          "Average. Price Deviation and Shareholding Patterns"
	};

	private final String[]			constant_ChartTitles		= new String[]
	{ "CAGR Sales", "CAGR Profit", "CAGR Profit Margin", "CAGR EPS", "Average EPS", "Avg. Profit Margin", "CAGR Dividend", "Avg. Eff. Dividend",
	          "Avg. Capex Borrow Ratio", "CAGR Inventory Turnover Ratio", "Average Inventory Turnover Ratio", "Average Reserves Provisions Ratio",
	          "CAGR Debt Equity Ratio", "Average Debt Equity Ratio", "CAGR  Debt", "Deviation from Avg. Price", "FII Holding %",
	          "Promoter Shareholding %",

	};

	private final String			constant_RDS				= " - Comparitive Distribution over Years";
	private final String			constant_Years				= "Years";
	/************** Column Numbers for Chart fields hardcoded ***********************/
	private final int				colnumScripCode			= 0;
	private final int				colnumCAGR_Sales			= 2;
	private final int				colnumCAGR_Profit			= 3;
	private final int				colnumCAGR_NPM				= 6;
	private final int				colnumCAGR_EPS				= 4;
	private final int				colnumAvg_EPS				= 5;
	private final int				colnumAvg_NPM				= 7;
	private final int				colnumCAGR_DIV				= 17;
	private final int				colnumAvg_DIV				= 18;
	private final int				colnumAvg_CBR				= 13;
	private final int				colnumCAGR_ITR				= 8;
	private final int				colnumAvg_ITR				= 9;
	private final int				colnumAvg_RPR				= 16;
	private final int				colnumCAGR_DER				= 10;
	private final int				colnumAvg_DER				= 11;
	private final int				colnumCAGR_Debt			= 12;
	private final int				colnumCAGR_PDev			= 20;
	private final int				colnumFIIHold				= 21;
	private final int				colnumPromHold				= 22;

	/************** Column Numbers for Chart fields hardcoded ***********************/
	private final int				totalColSpread				= 56000;

	@Override
	public void triggerXLSReportGeneration(String filePath, Object srvObj) throws EX_General
	{
		IScripAnalysisSrv scASrv = null;
		XSSFWorkbook wbCtxt = null;
		if (srvObj instanceof IScripAnalysisSrv)
		{
			scASrv = (IScripAnalysisSrv) srvObj;
			if (scASrv != null)
			{
				// Perform basic checks
				performBasicChecks(scASrv);
				// Write the workbook in file system
				FileOutputStream outStream;
				try
				{
					String fname = flNameSrv.getFileNameDate(constant_fnamePrefix, filePath, true);
					outStream = new FileOutputStream(new File(fname));
					if (outStream != null)
					{
						wbCtxt = new XSSFWorkbook();
						processWB(wbCtxt, scASrv);
					}

					// finally write the workbook context to physical file
					wbCtxt.write(outStream);
					outStream.close();
					// Write Success Message in Log
					GeneralMessage msgGen = new GeneralMessage("SA_XLS_SUCC", new Object[]
					{ filePath
					});
					msgFormatter.generate_message_snippet(msgGen);

				}
				catch (Exception e)
				{
					EX_General egen = new EX_General("ERR_CR_WB", new Object[]
					{ filePath, e.getMessage()
					});
					msgFormatter.generate_message_snippet(egen);
					throw egen;
				}
			}
		}

	}

	@Override
	public void triggerXLSReportGeneration(XSSFWorkbook wbctxt, Object srvObj) throws EX_General
	{
		IScripAnalysisSrv scASrv = null;
		XSSFWorkbook wbCtxt = null;
		if (srvObj instanceof IScripAnalysisSrv)
		{
			scASrv = (IScripAnalysisSrv) srvObj;
			if (scASrv != null)
			{
				// Perform basic checks
				performBasicChecks(scASrv);
				processWB(wbCtxt, scASrv);

			}
		}

	}

	/**
	 * ------------------------------------------------------------------
	 * Perform Basic Checks
	 * -----------------------------------------------------------------
	 */
	@Override
	public void performBasicChecks(Object srvObj) throws EX_General
	{
		if (srvObj instanceof IScripAnalysisSrv)
		{
			if (((IScripAnalysisSrv) srvObj).getScRootAttContainersList() != null)
			{
				if (((IScripAnalysisSrv) srvObj).getScRootAttContainersList().size() == 0)
				{
					EX_General egen = new EX_General("ERR_NODATA_SCA", null);
					msgFormatter.generate_message_snippet(egen);
					throw egen;
				}
			}
			if (((IScripAnalysisSrv) srvObj).getScSectors() != null)
			{
				if (((IScripAnalysisSrv) srvObj).getScSectors().size() == 0)
				{
					EX_General egen = new EX_General("ERR_NODATA_SCA", null);
					msgFormatter.generate_message_snippet(egen);
					throw egen;
				}
			}
		}

	}

	/**
	 * ------------------------------------------------ PRIVATE SECTION--------------------------
	 */

	/**
	 * ------------------------------------------------------------------------
	 * Process Work Book
	 * 
	 * @param wbCtxt
	 *             - Work book Context
	 *             ------------------------------------------------------------------------
	 * @throws EX_General
	 */
	private void processWB(XSSFWorkbook wbCtxt, IScripAnalysisSrv scASrv) throws EX_General
	{
		createSummarySheet(wbCtxt, scASrv);
		createSectorChartsSheets(wbCtxt, scASrv);
	}

	/**
	 * ----------------------------------------------------------
	 * Create WorkBook Summary Sheet
	 * 
	 * @param wbCtxt
	 *             - Work book Context
	 *             ----------------------------------------------------------
	 * @throws EX_General
	 */
	private void createSummarySheet(XSSFWorkbook wbCtxt, IScripAnalysisSrv scASrv) throws EX_General
	{
		if (wbCtxt != null)
		{
			XSSFSheet sumSheet = wbCtxt.createSheet(constant_Summary);
			if (sumSheet != null)
			{
				// Create Header Row
				Row row = sumSheet.createRow(0);
				if (row != null)
				{
					generateSummaryHeaders(sumSheet, wbCtxt, row, scASrv);
					populateData(sumSheet, wbCtxt, scASrv);
				}
				sumSheet.createFreezePane(2, 1);
			}
		}
	}

	/**
	 * -----------------------------------------------------------------------
	 * Generate Summary Sheet Headers
	 * 
	 * @param row
	 *             - Row
	 * @param scASrv
	 *             - SCASrv Implementation Instance
	 *             ---------------------------------------------------------------------
	 * @throws EX_General
	 */
	private void generateSummaryHeaders(XSSFSheet sumSheet, XSSFWorkbook wbCtxt, Row row, IScripAnalysisSrv scASrv) throws EX_General
	{
		int c = 0;
		if (row != null && sumSheet != null && attrMdtSrv != null)
		{
			if (scASrv != null)
			{
				if (scASrv.getScRootAttContainersList() != null)
				{
					if (scASrv.getScRootAttContainersList().size() > 0)
					{
						TY_ScRoot_AttrContainers first_scRootAttrContainers = scASrv.getScRootAttContainersList().get(0);
						if (first_scRootAttrContainers != null)
						{
							XSSFCellStyle styleRet = XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleHeaderBlueUline, wbCtxt,
							          CellType.STRING);
							styleRet.setWrapText(true);
							styleRet.setVerticalAlignment(VerticalAlignment.CENTER);

							// Scrip Code Cell
							Cell cellscCode = row.createCell(c);
							sumSheet.setColumnWidth(c, 5200);
							cellscCode.setCellValue(constant_ScCode);
							cellscCode.setCellStyle(styleRet);
							c++;

							// Sector Cell
							Cell cellswc = row.createCell(c);
							sumSheet.setColumnWidth(c, 8200);
							cellswc.setCellValue(constant_Sector);
							cellswc.setCellStyle(styleRet);
							c++;

							/*
							 * Other Cells - First for Attribute Containers - Attribute Containers Scan to get
							 * Attributes
							 */

							for ( TY_Attr_Container attrContainer : first_scRootAttrContainers.getAttrContainers() )
							{
								StatsAttrDetails attrDetails = attrMdtSrv.getattrDetailsbyAttrName(attrContainer.getAttrName());
								if (attrDetails != null)
								{
									if (attrDetails.isDeltaON())
									{
										Cell cellAttrCont = row.createCell(c);
										sumSheet.setColumnWidth(c, 5200);
										cellAttrCont.setCellValue(attrContainer.getAttrLabel());
										cellAttrCont.setCellStyle(styleRet);
										c++;
									}

									/*
									 * Add Average Cell for Attribute in case Configured in SS.xml for
									 * attribute
									 */
									if (attrDetails.isAvgON())
									{
										Cell cellAttrContAvg = row.createCell(c);
										sumSheet.setColumnWidth(c, 5200);
										cellAttrContAvg.setCellValue("Avg. " + attrDetails.getAttrName());
										cellAttrContAvg.setCellStyle(styleRet);
										c++;
									}
								}
							}

							/*
							 * Other Cells for Single Value Containers
							 */
							for ( TY_AttribSingleVal singleValContainer : first_scRootAttrContainers.getSingleValContainers() )
							{
								Cell cellAttrCont = row.createCell(c);
								sumSheet.setColumnWidth(c, 5200);
								cellAttrCont.setCellValue(singleValContainer.getAttrLabel());
								cellAttrCont.setCellStyle(styleRet);
								c++;
							}

						}

					}
				}
			}

		}
	}

	/**
	 * --------------------------------------- Populate Data in Summary Sheet------
	 * 
	 * @param sumSheet
	 *             - Summary Sheet Reference
	 * @param wbCtxt
	 *             - Work Book Context
	 * @param scASrv
	 *             - Scrip Analysis Service Reference
	 * @throws EX_General
	 *              ----------------------------------------------------------------------------
	 */
	private void populateData(XSSFSheet sumSheet, XSSFWorkbook wbCtxt, IScripAnalysisSrv scASrv) throws EX_General
	{
		// Some Initializations
		int rowNum = 1;
		TY_ScRoot_AttrContainers first_scRootAttrContainers = null;
		ArrayList<TY_ScRoot_AttrContainers> scRootAttrContainersList = new ArrayList<TY_ScRoot_AttrContainers>();
		ArrayList<TY_ScRoot_AttrContainers> secSp_scRootAttrContainersList = new ArrayList<TY_ScRoot_AttrContainers>();

		if (sumSheet != null && scASrv != null)
		{
			if (scASrv.getSectorsCount() != null)
			{
				if (scASrv.getSectorsCount().size() > 0)
				{
					scRootAttrContainersList = scASrv.getScRootAttContainersList();
					first_scRootAttrContainers = scRootAttrContainersList.get(0);

					if (scRootAttrContainersList != null && first_scRootAttrContainers != null)
					{
						// Start looping at Unique Sectors
						for ( Ty_SectorCount secCount : scASrv.getSectorsCount() )
						{
							/**
							 * Get Sector Specific Attribute containers List
							 */

							secSp_scRootAttrContainersList = scRootAttrContainersList.stream()
							          .filter(x -> x.getScRoot().getscSector().equals(secCount.getSector()))
							          .collect(Collectors.toCollection(ArrayList::new));
							if (secSp_scRootAttrContainersList != null)
							{
								populateDataforSector(secSp_scRootAttrContainersList, first_scRootAttrContainers, rowNum, sumSheet, wbCtxt,
								          secCount, scASrv);

								// Adjust RowNum
								rowNum = rowNum + secCount.getNumScrips() + 1; // Num of Scrips in Sector +
								                                               // 1 row For
								// Averages + Next Row to Create New
								// Row

							}
						}
					}
				}
			}
		}

	}

	/**
	 * --------------------------------------------- Create Sector Specific Rows in Summary Sheet--------------------
	 * 
	 * @param secSp_scRootAttrContainersList
	 *             - Sector Specific SCRoot Containers List to be created together
	 * @param first_scRootAttrContainers
	 *             - Reference Sc Root Container for Attributes scan
	 * @param rowNum
	 *             - Roe to Start from in Summary sheet
	 * @param sumSheet
	 *             - Summary Sheet Reference
	 * @param wbCtxt
	 *             - Work Book Context
	 * @param secCount
	 *             - Sector Count Type
	 *             -------------------------------------------------------------------------------------------------
	 * @throws EX_General
	 */
	private void populateDataforSector(ArrayList<TY_ScRoot_AttrContainers> secSp_scRootAttrContainersList,
	          TY_ScRoot_AttrContainers first_scRootAttrContainers, int rowNum, XSSFSheet sumSheet, XSSFWorkbook wbCtxt, Ty_SectorCount secCount,
	          IScripAnalysisSrv scASrv) throws EX_General
	{

		int initRowNum = rowNum;
		XSSFHyperlink link = null;
		XSSFCreationHelper helper = wbCtxt.getCreationHelper();
		ArrayList<TY_Attr_Container> attrSpAttrContainers = new ArrayList<TY_Attr_Container>();

		// Some Styles
		XSSFCellStyle styleRetNoFill = XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleNoFill, wbCtxt, CellType.NUMERIC);
		styleRetNoFill.setWrapText(true);
		styleRetNoFill.setVerticalAlignment(VerticalAlignment.CENTER);

		XSSFCellStyle styleRetHlink = XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleKeyGoldHlink, wbCtxt, CellType.STRING);
		styleRetNoFill.setWrapText(true);
		styleRetNoFill.setVerticalAlignment(VerticalAlignment.CENTER);

		XSSFCellStyle styleRetSec = XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleYellowFillBoldBlack, wbCtxt, CellType.STRING);
		styleRetNoFill.setWrapText(true);

		/**
		 * You have sector specific Data Now start creating rows in Sheet
		 */
		for ( TY_ScRoot_AttrContainers ScRoot_AttrContainers : secSp_scRootAttrContainersList )
		{
			Row row = sumSheet.createRow(rowNum);
			if (row != null)
			{
				int c = 0;
				// Scrip Code Cell
				Cell cellscCode = row.createCell(c);
				sumSheet.setColumnWidth(c, 5200);
				cellscCode.setCellValue(ScRoot_AttrContainers.getScRoot().getscCode());
				cellscCode.setCellStyle(styleRetHlink);
				c++;

				// Sector Cell
				Cell cellswc = row.createCell(c);
				sumSheet.setColumnWidth(c, 8200);
				cellswc.setCellValue(secCount.getSector());
				link = helper.createHyperlink(HyperlinkType.DOCUMENT);
				String tgAddress = "'" + secCount.getSector() + "'" + "!A1";
				link.setAddress(tgAddress);
				cellswc.setHyperlink(link);
				cellswc.setCellStyle(styleRetSec);
				c++;

				/*
				 * Other Cells - First for Attribute Containers - Attribute Containers Scan to get
				 * Attributes
				 */

				for ( TY_Attr_Container attrContainer : first_scRootAttrContainers.getAttrContainers() )
				{
					// Get Details of Each atribute one by one
					StatsAttrDetails attrDetails = attrMdtSrv.getattrDetailsbyAttrName(attrContainer.getAttrName());
					if (attrDetails != null)
					{
						/**
						 * Get the Current Attribute Container from each sector Specific SCRootAttribContainers
						 */
						TY_Attr_Container currAttrContainer = ScRoot_AttrContainers.getAttrContainers().stream()
						          .filter(x -> x.getAttrName().equals(attrDetails.getAttrName())).findFirst().get();
						if (currAttrContainer != null)
						{
							/**
							 * Scan the properties and prepare cells accordingly
							 */
							if (attrDetails.isDeltaON())
							{
								Cell cellAttrCont = row.createCell(c);
								sumSheet.setColumnWidth(c, 5200);
								cellAttrCont.setCellType(CellType.NUMERIC);
								cellAttrCont.setCellValue(currAttrContainer.getDeltaAvg());
								cellAttrCont.setCellStyle(styleRetNoFill);
								c++;
							}

							/*
							 * Add Average Cell for Attribute in case Configured in SS.xml for
							 * attribute
							 */
							if (attrDetails.isAvgON())
							{
								Cell cellAttrContAvg = row.createCell(c);
								sumSheet.setColumnWidth(c, 5200);
								cellAttrContAvg.setCellType(CellType.NUMERIC);
								cellAttrContAvg.setCellValue(currAttrContainer.getAvg());
								cellAttrContAvg.setCellStyle(styleRetNoFill);
								c++;
							}

						}
					}
				}
				/*
				 * Other Cells for Single Value Containers
				 */
				for ( TY_AttribSingleVal singleValContainer : ScRoot_AttrContainers.getSingleValContainers() )
				{
					Cell cellAttrCont = row.createCell(c);
					sumSheet.setColumnWidth(c, 5200);

					switch (singleValContainer.getAttrType())
					{
					case "Int":
					case "int":
					case "Integer":
						cellAttrCont.setCellType(CellType.NUMERIC);
						cellAttrCont.setCellValue((int) singleValContainer.getValue());
						break;

					case "Double":
					case "double":
						cellAttrCont.setCellType(CellType.NUMERIC);
						cellAttrCont.setCellValue((double) singleValContainer.getValue());
						break;

					case "String":
						cellAttrCont.setCellType(CellType.STRING);
						cellAttrCont.setCellValue((String) singleValContainer.getValue());
						break;

					case "Boolean":
					case "boolean":
						cellAttrCont.setCellType(CellType.BOOLEAN);
						cellAttrCont.setCellValue((Boolean) singleValContainer.getValue());
						break;

					default: // Enums that implement IEnumable
					         // Interface

						cellAttrCont.setCellType(CellType.STRING);
						cellAttrCont.setCellValue((String) singleValContainer.getValue());

						break;
					}

					cellAttrCont.setCellStyle(styleRetNoFill);
					c++;
				}
			}

			rowNum++;
		}

		// Merge Sectors Cells
		// Merges the cells -- Only if there are more than 1 Scrip(s) in a sector
		if (secCount.getNumScrips() > 1)
		{
			Row firstRow = sumSheet.getRow(initRowNum);
			if (firstRow != null)
			{
				CellRangeAddress cellRangeAddress = new CellRangeAddress(initRowNum, (initRowNum + secCount.getNumScrips() - 1), 1, 1);
				sumSheet.addMergedRegion(cellRangeAddress);

				// Creates the cell
				Cell cell = CellUtil.createCell(firstRow, 1, secCount.getSector());

				// Sets the allignment to the created cell
				CellUtil.setVerticalAlignment(cell, VerticalAlignment.CENTER);

			}
		}
		/**
		 * Populate Averages for this sector
		 */
		populateAveragesforSector(secCount, rowNum, sumSheet, wbCtxt, first_scRootAttrContainers, scASrv);
	}

	/**
	 * ---------------------------------- Populate Sector Averages Row ---------------------------------------
	 * 
	 * @param secCount
	 *             - Sector Count stats type
	 * @param rowNum
	 *             - row num in which average details are to be created
	 * @param sumSheet
	 *             - Summary Sheet Reference
	 * @param wbCtxt
	 *             - Work Book Context for Styles Creation
	 * @param first_scRootAttrContainers
	 *             - Reference Sc Root Container for Attributes scan
	 *             ---------------------------------------------------------------------------------------
	 * @throws EX_General
	 */
	private void populateAveragesforSector(Ty_SectorCount secCount, int rowNum, XSSFSheet sumSheet, XSSFWorkbook wbCtxt,
	          TY_ScRoot_AttrContainers first_scRootAttrContainers, IScripAnalysisSrv scASrv) throws EX_General
	{
		int c = 1;
		// Some Styles
		XSSFCellStyle styleRet = XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleTotalBlueGrayItalic, wbCtxt, CellType.NUMERIC);
		styleRet.setWrapText(true);
		styleRet.setVerticalAlignment(VerticalAlignment.CENTER);

		Row row = sumSheet.createRow(rowNum);
		if (row != null)
		{
			// Scrip Code Cell
			Cell cellscCode = row.createCell(c);
			sumSheet.setColumnWidth(c, 5200);
			cellscCode.setCellValue(constant_Average);
			cellscCode.setCellStyle(styleRet);
			c++;

			// Get the Average Stats from SCA Srv for the Current Sector
			if (scASrv.getSectorsAvgStats() != null)
			{
				ArrayList<Ty_SectorAvgStats> secAvgStatsList = scASrv.getSectorsAvgStats().stream()
				          .filter(x -> x.getSector().equals(secCount.getSector())).collect(Collectors.toCollection(ArrayList::new));
				if (secAvgStatsList != null)
				{
					// Attribute Containers Scan to get Attributes
					for ( TY_Attr_Container attrContainer : first_scRootAttrContainers.getAttrContainers() )
					{
						StatsAttrDetails attrDetails = attrMdtSrv.getattrDetailsbyAttrName(attrContainer.getAttrName());
						if (attrDetails != null)
						{
							// Get the Stats Structure/Data for the current Attribute

							Ty_SectorAvgStats secAvgStats = secAvgStatsList.stream()
							          .filter(x -> x.getAttrName().equals(attrDetails.getAttrName())).findFirst().get();

							if (secAvgStats != null)
							{
								// CAGR Delta
								if (attrDetails.isAvgDeltasecSpON())
								{
									Cell cellAttrCont = row.createCell(c);
									sumSheet.setColumnWidth(c, 5200);
									cellAttrCont.setCellType(CellType.NUMERIC);
									cellAttrCont.setCellValue(secAvgStats.getAvgDeltaAvg());
									cellAttrCont.setCellStyle(styleRet);
									c++;
								}

								// Average of Averages
								if (attrDetails.isSelfsecSpON())
								{
									Cell cellAttrCont = row.createCell(c);
									sumSheet.setColumnWidth(c, 5200);
									cellAttrCont.setCellType(CellType.NUMERIC);
									cellAttrCont.setCellValue(secAvgStats.getAvgAvg());
									cellAttrCont.setCellStyle(styleRet);
									c++;
								}
							}
						}
					}

				}

			}
		}

	}

	/**
	 * ----------------------------------------- Create Sector Specific Charts----------------------------------
	 * 
	 * @param wbCtxt
	 *             - Wb Context
	 * @param scASrv
	 *             - Scrip Analysis Service Instance
	 *             ---------------------------------------------------------------------------------------------
	 * @throws EX_General
	 */
	private void createSectorChartsSheets(XSSFWorkbook wbCtxt, IScripAnalysisSrv scASrv) throws EX_General
	{
		/**
		 * First we need to get XLS row Positioners for Specific Sectors from Summary Sheet for Sectors that have more
		 * than 1 scrip(s)
		 */

		ArrayList<Ty_SectorXlsRowPositioner> secRowPositions = null;
		secRowPositions = getRowPostionsforSectors(scASrv);
		if (secRowPositions != null && secRowPositions.size() > 0)
		{
			/**
			 * One Sheet Each for Each Sector - Overview Sheet wil be created before traversing to next Sector Sheet
			 * iteration
			 */

			for ( Ty_SectorXlsRowPositioner SectorXlsRowPositioner : secRowPositions )
			{
				XSSFSheet secSheet = wbCtxt.createSheet(SectorXlsRowPositioner.getSectorName());
				if (secSheet != null)
				{
					createSectorSheet(secSheet, wbCtxt, scASrv, SectorXlsRowPositioner);
					// Also Create OverView Sheet for Market Share Comparisons
					String oVWSheetName = SectorXlsRowPositioner.getSectorName() + constant_oVW;
					XSSFSheet secoVWSheet = wbCtxt.createSheet(oVWSheetName);
					if (secoVWSheet != null)
					{
						createSectoroVWSheet(secoVWSheet, wbCtxt, scASrv, SectorXlsRowPositioner.getSectorName(),
						          SectorXlsRowPositioner.getToRow() - SectorXlsRowPositioner.getFromRow());
					}

				}
			}

		}

	}

	/**
	 * -------------------------------- Get Row Positions for Sectors from Scrip Analysisi Service Instance -----------
	 * 
	 * @param scASrv
	 *             - Scrip Analysis Service Instance
	 * @return - Sector Row Positions List
	 *         ---------------------------------------------------------------------------------------------------
	 */
	private ArrayList<Ty_SectorXlsRowPositioner> getRowPostionsforSectors(IScripAnalysisSrv scASrv)
	{
		ArrayList<Ty_SectorXlsRowPositioner> secRowPositions = new ArrayList<Ty_SectorXlsRowPositioner>();

		if (scASrv.getSectorsCount() != null)
		{

			int numrows = 2; // actual scrip data starts with 2 because of header in summary sheet
			int defaultRowAddendum = 1; // 1 for Average

			for ( Ty_SectorCount secCountStat : scASrv.getSectorsCount() )
			{

				if (secCountStat.getNumScrips() < 2)
				{

					numrows += secCountStat.getNumScrips() + defaultRowAddendum;

				}
				else if (secCountStat.getNumScrips() >= 2)
				{

					Ty_SectorXlsRowPositioner rowPositioner = new Ty_SectorXlsRowPositioner();
					rowPositioner.setSectorName(secCountStat.getSector());
					rowPositioner.setFromRow(numrows); // starts with 2
					rowPositioner.setToRow(rowPositioner.getFromRow() + secCountStat.getNumScrips() - 1);
					numrows += secCountStat.getNumScrips() + defaultRowAddendum;
					secRowPositions.add(rowPositioner);
				}

			}
		}

		return secRowPositions;

	}

	/**
	 * ------------------------------------------------ Create Sectors Charts Sheet-----------------------
	 * 
	 * @param secSheet
	 *             - Sectors Sheet Instance
	 *             ---------------------------------------------------------------------------------------
	 */
	private void createSectorSheet(XSSFSheet secSheet, XSSFWorkbook wbCtxt, IScripAnalysisSrv scASrv,
	          Ty_SectorXlsRowPositioner SectorXlsRowPositioner)
	{
		XSSFCellStyle styleHeader = XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleHeaderBlueUline, wbCtxt, CellType.STRING);
		styleHeader.setWrapText(true);
		styleHeader.setVerticalAlignment(VerticalAlignment.CENTER);

		XSSFCellStyle styleNoFill = XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleNoFill, wbCtxt, CellType.STRING);
		styleNoFill.setWrapText(true);
		styleNoFill.setVerticalAlignment(VerticalAlignment.CENTER);

		/**
		 * Get Summary Sheet Reference to utilize the data
		 */
		XSSFSheet sumSheet = wbCtxt.getSheet(constant_Summary);

		if (sumSheet != null)
		{

			// Create Header Row - Revenue(s) and Profit(s) CAGR
			Row row = secSheet.createRow(0);
			if (row != null)
			{
				PrepareMergedTitlesforRow(row, constant_secSheetChartsTitles[0], 3, styleHeader, secSheet);
			}

			// Revenue(s) and Profit(s) CAGR Charts Row
			int rowNum = 1;
			row = secSheet.createRow(rowNum);
			if (row != null)
			{
				plotCAGRRevProfitCharts(row, secSheet, sumSheet, styleNoFill, SectorXlsRowPositioner);
			}
			rowNum++;

			// Create Header Row - Earnings Per Share and Average NPM
			row = secSheet.createRow(rowNum);
			if (row != null)
			{
				PrepareMergedTitlesforRow(row, constant_secSheetChartsTitles[1], 3, styleHeader, secSheet);
			}

			// EPS and Average Net. Profit Margin
			rowNum++;
			row = secSheet.createRow(rowNum);
			if (row != null)
			{
				plotCAGREPSAvgNpm(row, secSheet, sumSheet, styleNoFill, SectorXlsRowPositioner);
			}
			rowNum++;

			// Create Header Row - Dividends Distribution and Capex Borrowngs
			row = secSheet.createRow(rowNum);
			if (row != null)
			{
				PrepareMergedTitlesforRow(row, constant_secSheetChartsTitles[2], 3, styleHeader, secSheet);
			}

			// Dividend and Capes Borrowings Ratio
			rowNum++;
			row = secSheet.createRow(rowNum);
			if (row != null)
			{
				plotCAGRDivCapexBW(row, secSheet, sumSheet, styleNoFill, SectorXlsRowPositioner);
			}
			rowNum++;

			// Create Header Row - Inventory Management and Avg. Reserves Provisions Ratio
			row = secSheet.createRow(rowNum);
			if (row != null)
			{
				PrepareMergedTitlesforRow(row, constant_secSheetChartsTitles[3], 3, styleHeader, secSheet);
			}

			// Inventory Management and Avg. Reserves Provisions Ratio
			rowNum++;
			row = secSheet.createRow(rowNum);
			if (row != null)
			{
				plotCAGRInvRPR(row, secSheet, sumSheet, styleNoFill, SectorXlsRowPositioner);
			}
			rowNum++;

			// Create Header Row - Debt Positioning Over Years
			row = secSheet.createRow(rowNum);
			if (row != null)
			{
				PrepareMergedTitlesforRow(row, constant_secSheetChartsTitles[4], 3, styleHeader, secSheet);
			}

			// Debt Positioning Over Years
			rowNum++;
			row = secSheet.createRow(rowNum);
			if (row != null)
			{
				plotCAGRDebt(row, secSheet, sumSheet, styleNoFill, SectorXlsRowPositioner);
			}
			rowNum++;

			// Create Header Row - Average. Price Deviation and Shareholding Patternsrs
			row = secSheet.createRow(rowNum);
			if (row != null)
			{
				PrepareMergedTitlesforRow(row, constant_secSheetChartsTitles[5], 3, styleHeader, secSheet);
			}

			// Average. Price Deviation and Shareholding Patterns
			rowNum++;
			row = secSheet.createRow(rowNum);
			if (row != null)
			{
				plotPDevSHP(row, secSheet, sumSheet, styleNoFill, SectorXlsRowPositioner);
			}
			rowNum++;

			// Sectors Charts Sheets Column widhts Spread Evenly
			secSheet.setColumnWidth(0, 19000);
			secSheet.setColumnWidth(1, 19000);
			secSheet.setColumnWidth(2, 19000);
		}
	}

	/**
	 * -------------------------------Prepare Merged Cells Title Row ---------------------------------------
	 * 
	 * @param row
	 *             - Row Instance
	 * @param Title
	 *             - Title Text
	 * @param colSpread
	 *             - Column Spread
	 *             ------------------------------------------------------------------------------------------
	 */
	private void PrepareMergedTitlesforRow(Row row, String Title, int colSpread, XSSFCellStyle styleRet, XSSFSheet secSheet)
	{

		if (row != null && colSpread >= 2)
		{

			for ( int c = 0; c < colSpread; c++ )
			{
				Cell cell = row.createCell(c);
				cell.setCellStyle(styleRet);

			}

			// Merge Rows for Columns
			CellRangeAddress cellRangeAddress = new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, colSpread);
			secSheet.addMergedRegion(cellRangeAddress);

			// Creates the cell
			Cell cell = CellUtil.createCell(row, 0, Title);

			// Sets the allignment to the created cell
			CellUtil.setVerticalAlignment(cell, VerticalAlignment.CENTER);

		}
	}

	/**
	 * --------------------------------------- Plot CAGR REvenue Profit Charts --------------------------------
	 * 
	 * @param row
	 *             - Row Instance
	 * @param secSheet
	 *             - Sector Sheet
	 * @param sumSheet
	 *             - Summary Sheet
	 * @param styleNoFill
	 *             - No Fill Plain Style
	 * @param SectorXlsRowPositioner
	 *             - Sector Xls Row Positioner Instance for Current Sector
	 *             ---------------------------------------------------------------------------------
	 */
	private void plotCAGRRevProfitCharts(Row row, XSSFSheet secSheet, XSSFSheet sumSheet, XSSFCellStyle styleNoFill,
	          Ty_SectorXlsRowPositioner SectorXlsRowPositioner)
	{

		if (row != null && secSheet != null && sumSheet != null && SectorXlsRowPositioner != null)
		{
			int colnum = 0;
			row.setHeight((short) 4500);

			CTMarker ctMarker = CTMarker.Factory.newInstance();
			ctMarker.setSymbol(CTMarkerStyle.Factory.newInstance());
			CTBoolean ctBool = CTBoolean.Factory.newInstance();
			ctBool.setVal(false);

			// -----CAGR REvenue/Sales ----------------------------
			Cell dbcelllchartEPSCAGR = row.createCell(colnum);
			dbcelllchartEPSCAGR.setCellStyle(styleNoFill);

			// Create Drawing Object
			XSSFDrawing xlsx_drawing = secSheet.createDrawingPatriarch();

			/* Define anchor points in the worksheet to position the chart */
			XSSFClientAnchor anchor = xlsx_drawing.createAnchor(10 * Units.EMU_PER_PIXEL, 10 * Units.EMU_PER_PIXEL, 520 * Units.EMU_PER_PIXEL,
			          280 * Units.EMU_PER_PIXEL, colnum, row.getRowNum(), colnum, row.getRowNum());

			/* Create the chart object based on the anchor point */
			XSSFChart my_line_chartCAGRSales = xlsx_drawing.createChart(anchor);

			// my_line_chartCAGRSales.setTitle(constant_ChartTitles[0]);

			/* Define chart AXIS */
			ChartAxis bottomAxis_chartCAGRSales = my_line_chartCAGRSales.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);

			ValueAxis leftAxis_chartCAGRSales = my_line_chartCAGRSales.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
			leftAxis_chartCAGRSales.setCrosses(AxisCrosses.AUTO_ZERO);

			LineChartData data_chartCAGRSales = my_line_chartCAGRSales.getChartDataFactory().createLineChartData();

			ChartDataSource<String> xs_chartCAGRSales = DataSources.fromStringCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumScripCode, colnumScripCode));
			ChartDataSource<Number> ys1_chartCAGRSales = DataSources.fromNumericCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumCAGR_Sales, colnumCAGR_Sales));

			/* Add chart data sources as data to the chart */
			LineChartSeries CAGRSalesSeries = data_chartCAGRSales.addSeries(xs_chartCAGRSales, ys1_chartCAGRSales);

			CTChart ctChart = my_line_chartCAGRSales.getCTChart();
			CTTitle title = ctChart.addNewTitle();
			title.addNewOverlay().setVal(false);
			CTTx tx = title.addNewTx();
			CTTextBody rich = tx.addNewRich();
			rich.addNewBodyPr(); // body properties must exist, but can be empty
			CTTextParagraph para = rich.addNewP();
			CTRegularTextRun r = para.addNewR();
			r.setT(constant_ChartTitles[0]);

			/* Plot the chart with the inputs from data and chart axis */
			my_line_chartCAGRSales.plot(data_chartCAGRSales, new ChartAxis[]
			{ bottomAxis_chartCAGRSales, leftAxis_chartCAGRSales
			});

			CTPlotArea plotAreaCAGRSales = my_line_chartCAGRSales.getCTChart().getPlotArea();

			for ( CTLineSer ser : plotAreaCAGRSales.getLineChartArray()[0].getSerArray() )
			{
				// Don't smoothen and don't put markers
				ser.setMarker(ctMarker);
				ser.setSmooth(ctBool);

			}
			colnum++;

			// -----CAGR Profit ----------------------------
			Cell dbcelllchartProfitCAGR = row.createCell(colnum);
			dbcelllchartProfitCAGR.setCellStyle(styleNoFill);

			// Create Drawing Object
			XSSFDrawing xlsx_drawingCAGRProfit = secSheet.createDrawingPatriarch();

			/* Define anchor points in the worksheet to position the chart */
			XSSFClientAnchor anchorCAGRProfit = xlsx_drawing.createAnchor(10 * Units.EMU_PER_PIXEL, 10 * Units.EMU_PER_PIXEL,
			          520 * Units.EMU_PER_PIXEL, 280 * Units.EMU_PER_PIXEL, colnum, row.getRowNum(), colnum, row.getRowNum());

			/* Create the chart object based on the anchor point */
			XSSFChart my_line_chartCAGRProfit = xlsx_drawingCAGRProfit.createChart(anchorCAGRProfit);

			/* Define chart AXIS */
			ChartAxis bottomAxis_chartCAGRProfit = my_line_chartCAGRProfit.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);

			ValueAxis leftAxis_chartCAGRProfit = my_line_chartCAGRProfit.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
			leftAxis_chartCAGRProfit.setCrosses(AxisCrosses.AUTO_ZERO);

			LineChartData data_chartCAGRProfit = my_line_chartCAGRProfit.getChartDataFactory().createLineChartData();

			ChartDataSource<String> xs_chartCAGRProfit = DataSources.fromStringCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumScripCode, colnumScripCode));
			ChartDataSource<Number> ys1_chartCAGRProfit = DataSources.fromNumericCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumCAGR_Profit, colnumCAGR_Profit));

			/* Add chart data sources as data to the chart */
			LineChartSeries CAGRProfitSeries = data_chartCAGRProfit.addSeries(xs_chartCAGRProfit, ys1_chartCAGRProfit);

			CTChart ctChartCAGRProfit = my_line_chartCAGRProfit.getCTChart();
			CTTitle titleCAGRProfit = ctChartCAGRProfit.addNewTitle();
			titleCAGRProfit.addNewOverlay().setVal(false);
			CTTx txCAGRProfit = titleCAGRProfit.addNewTx();
			CTTextBody richCAGRProfit = txCAGRProfit.addNewRich();
			richCAGRProfit.addNewBodyPr(); // body properties must exist, but can be empty
			CTTextParagraph paraCAGRProfit = richCAGRProfit.addNewP();
			CTRegularTextRun rCAGRPrfit = paraCAGRProfit.addNewR();
			rCAGRPrfit.setT(constant_ChartTitles[1]);

			/* Plot the chart with the inputs from data and chart axis */
			my_line_chartCAGRProfit.plot(data_chartCAGRProfit, new ChartAxis[]
			{ bottomAxis_chartCAGRProfit, leftAxis_chartCAGRProfit
			});

			CTPlotArea plotAreaCAGRProfit = my_line_chartCAGRProfit.getCTChart().getPlotArea();

			for ( CTLineSer ser : plotAreaCAGRProfit.getLineChartArray()[0].getSerArray() )
			{
				// Don't smoothen and don't put markers
				ser.setMarker(ctMarker);
				ser.setSmooth(ctBool);

			}

			colnum++;

			// -----CAGR NPM ----------------------------
			Cell dbcelllchartNPMCAGR = row.createCell(colnum);
			dbcelllchartNPMCAGR.setCellStyle(styleNoFill);

			// Create Drawing Object
			XSSFDrawing xlsx_drawingCAGRNPM = secSheet.createDrawingPatriarch();

			/* Define anchor points in the worksheet to position the chart */
			XSSFClientAnchor anchorCAGRNPM = xlsx_drawing.createAnchor(10 * Units.EMU_PER_PIXEL, 10 * Units.EMU_PER_PIXEL,
			          520 * Units.EMU_PER_PIXEL, 280 * Units.EMU_PER_PIXEL, colnum, row.getRowNum(), colnum, row.getRowNum());

			/* Create the chart object based on the anchor point */
			XSSFChart my_line_chartCAGRNPM = xlsx_drawingCAGRNPM.createChart(anchorCAGRNPM);

			/* Define chart AXIS */
			ChartAxis bottomAxis_chartCAGRNPM = my_line_chartCAGRNPM.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);

			ValueAxis leftAxis_chartCAGRNPM = my_line_chartCAGRNPM.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
			leftAxis_chartCAGRNPM.setCrosses(AxisCrosses.AUTO_ZERO);

			LineChartData data_chartCAGRNPM = my_line_chartCAGRNPM.getChartDataFactory().createLineChartData();

			ChartDataSource<String> xs_chartCAGRNPM = DataSources.fromStringCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumScripCode, colnumScripCode));
			ChartDataSource<Number> ys1_chartCAGRNPM = DataSources.fromNumericCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumCAGR_NPM, colnumCAGR_NPM));

			/* Add chart data sources as data to the chart */
			LineChartSeries CAGRNPMSeries = data_chartCAGRNPM.addSeries(xs_chartCAGRNPM, ys1_chartCAGRNPM);

			CTChart ctChartCAGRNPM = my_line_chartCAGRNPM.getCTChart();
			CTTitle titleCAGRNPM = ctChartCAGRNPM.addNewTitle();
			titleCAGRNPM.addNewOverlay().setVal(false);
			CTTx txCAGRNPM = titleCAGRNPM.addNewTx();
			CTTextBody richCAGRNPM = txCAGRNPM.addNewRich();
			richCAGRNPM.addNewBodyPr(); // body properties must exist, but can be empty
			CTTextParagraph paraCAGRNPM = richCAGRNPM.addNewP();
			CTRegularTextRun rCAGRNPM = paraCAGRNPM.addNewR();
			rCAGRNPM.setT(constant_ChartTitles[2]);

			/* Plot the chart with the inputs from data and chart axis */
			my_line_chartCAGRNPM.plot(data_chartCAGRNPM, new ChartAxis[]
			{ bottomAxis_chartCAGRNPM, leftAxis_chartCAGRNPM
			});

			CTPlotArea plotAreaCAGRNPM = my_line_chartCAGRNPM.getCTChart().getPlotArea();

			for ( CTLineSer ser : plotAreaCAGRNPM.getLineChartArray()[0].getSerArray() )
			{
				// Don't smoothen and don't put markers
				ser.setMarker(ctMarker);
				ser.setSmooth(ctBool);

			}

			colnum++;

		}
	}

	/**
	 * --------------------------------------- Plot CAGR EPS Average NPM Charts --------------------------------
	 * 
	 * @param row
	 *             - Row Instance
	 * @param secSheet
	 *             - Sector Sheet
	 * @param sumSheet
	 *             - Summary Sheet
	 * @param styleNoFill
	 *             - No Fill Plain Style
	 * @param SectorXlsRowPositioner
	 *             - Sector Xls Row Positioner Instance for Current Sector
	 *             ---------------------------------------------------------------------------------
	 */
	private void plotCAGREPSAvgNpm(Row row, XSSFSheet secSheet, XSSFSheet sumSheet, XSSFCellStyle styleNoFill,
	          Ty_SectorXlsRowPositioner SectorXlsRowPositioner)
	{

		if (row != null && secSheet != null && sumSheet != null && SectorXlsRowPositioner != null)
		{
			int colnum = 0;
			row.setHeight((short) 4500);

			CTMarker ctMarker = CTMarker.Factory.newInstance();
			ctMarker.setSymbol(CTMarkerStyle.Factory.newInstance());
			CTBoolean ctBool = CTBoolean.Factory.newInstance();
			ctBool.setVal(false);

			// -----CAGR EPS ----------------------------
			Cell dbcelllchartEPSCAGR = row.createCell(colnum);
			dbcelllchartEPSCAGR.setCellStyle(styleNoFill);

			// Create Drawing Object
			XSSFDrawing xlsx_drawing = secSheet.createDrawingPatriarch();

			/* Define anchor points in the worksheet to position the chart */
			XSSFClientAnchor anchor = xlsx_drawing.createAnchor(10 * Units.EMU_PER_PIXEL, 10 * Units.EMU_PER_PIXEL, 520 * Units.EMU_PER_PIXEL,
			          280 * Units.EMU_PER_PIXEL, colnum, row.getRowNum(), colnum, row.getRowNum());

			/* Create the chart object based on the anchor point */
			XSSFChart my_line_chartEPSCAGR = xlsx_drawing.createChart(anchor);

			// my_line_chartEPSCAGR.setTitle(constant_ChartTitles[0]);

			/* Define chart AXIS */
			ChartAxis bottomAxis_chartEPSCAGR = my_line_chartEPSCAGR.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);

			ValueAxis leftAxis_chartEPSCAGR = my_line_chartEPSCAGR.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
			leftAxis_chartEPSCAGR.setCrosses(AxisCrosses.AUTO_ZERO);

			LineChartData data_chartEPSCAGR = my_line_chartEPSCAGR.getChartDataFactory().createLineChartData();

			ChartDataSource<String> xs_chartEPSCAGR = DataSources.fromStringCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumScripCode, colnumScripCode));
			ChartDataSource<Number> ys1_chartEPSCAGR = DataSources.fromNumericCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumCAGR_EPS, colnumCAGR_EPS));

			/* Add chart data sources as data to the chart */
			LineChartSeries EPSCAGRSeries = data_chartEPSCAGR.addSeries(xs_chartEPSCAGR, ys1_chartEPSCAGR);

			CTChart ctChart = my_line_chartEPSCAGR.getCTChart();
			CTTitle title = ctChart.addNewTitle();
			title.addNewOverlay().setVal(false);
			CTTx tx = title.addNewTx();
			CTTextBody rich = tx.addNewRich();
			rich.addNewBodyPr(); // body properties must exist, but can be empty
			CTTextParagraph para = rich.addNewP();
			CTRegularTextRun r = para.addNewR();
			r.setT(constant_ChartTitles[3]);

			/* Plot the chart with the inputs from data and chart axis */
			my_line_chartEPSCAGR.plot(data_chartEPSCAGR, new ChartAxis[]
			{ bottomAxis_chartEPSCAGR, leftAxis_chartEPSCAGR
			});

			CTPlotArea plotAreaCAGRSales = my_line_chartEPSCAGR.getCTChart().getPlotArea();

			for ( CTLineSer ser : plotAreaCAGRSales.getLineChartArray()[0].getSerArray() )
			{
				// Don't smoothen and don't put markers
				ser.setMarker(ctMarker);
				ser.setSmooth(ctBool);

			}
			colnum++;

			// -----Avg EPS ----------------------------
			Cell dbcelllchartAvgEPS = row.createCell(colnum);
			dbcelllchartAvgEPS.setCellStyle(styleNoFill);

			// Create Drawing Object
			XSSFDrawing xlsx_drawingAvgEPS = secSheet.createDrawingPatriarch();

			/* Define anchor points in the worksheet to position the chart */
			XSSFClientAnchor anchorAvgEPS = xlsx_drawing.createAnchor(10 * Units.EMU_PER_PIXEL, 10 * Units.EMU_PER_PIXEL,
			          520 * Units.EMU_PER_PIXEL, 280 * Units.EMU_PER_PIXEL, colnum, row.getRowNum(), colnum, row.getRowNum());

			/* Create the chart object based on the anchor point */
			XSSFChart my_line_chartAvgEPS = xlsx_drawingAvgEPS.createChart(anchorAvgEPS);

			/* Define chart AXIS */
			ChartAxis bottomAxis_chartAvgEPS = my_line_chartAvgEPS.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);

			ValueAxis leftAxis_chartAvgEPS = my_line_chartAvgEPS.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
			leftAxis_chartAvgEPS.setCrosses(AxisCrosses.AUTO_ZERO);

			LineChartData data_chartAvgEPS = my_line_chartAvgEPS.getChartDataFactory().createLineChartData();

			ChartDataSource<String> xs_chartAvgEPS = DataSources.fromStringCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumScripCode, colnumScripCode));
			ChartDataSource<Number> ys1_chartAvgEPS = DataSources.fromNumericCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumAvg_EPS, colnumAvg_EPS));

			/* Add chart data sources as data to the chart */
			LineChartSeries AvgEPSSeries = data_chartAvgEPS.addSeries(xs_chartAvgEPS, ys1_chartAvgEPS);

			CTChart ctChartAvgEPS = my_line_chartAvgEPS.getCTChart();
			CTTitle titleAvgEPS = ctChartAvgEPS.addNewTitle();
			titleAvgEPS.addNewOverlay().setVal(false);
			CTTx txAvgEPS = titleAvgEPS.addNewTx();
			CTTextBody richAvgEPS = txAvgEPS.addNewRich();
			richAvgEPS.addNewBodyPr(); // body properties must exist, but can be empty
			CTTextParagraph paraAvgEPS = richAvgEPS.addNewP();
			CTRegularTextRun rCAGRPrfit = paraAvgEPS.addNewR();
			rCAGRPrfit.setT(constant_ChartTitles[4]);

			/* Plot the chart with the inputs from data and chart axis */
			my_line_chartAvgEPS.plot(data_chartAvgEPS, new ChartAxis[]
			{ bottomAxis_chartAvgEPS, leftAxis_chartAvgEPS
			});

			CTPlotArea plotAreaAvgEPS = my_line_chartAvgEPS.getCTChart().getPlotArea();

			for ( CTLineSer ser : plotAreaAvgEPS.getLineChartArray()[0].getSerArray() )
			{
				// Don't smoothen and don't put markers
				ser.setMarker(ctMarker);
				ser.setSmooth(ctBool);

			}

			colnum++;

			// -----Avg NPM ----------------------------
			Cell dbcelllchartAvgNPM = row.createCell(colnum);
			dbcelllchartAvgNPM.setCellStyle(styleNoFill);

			// Create Drawing Object
			XSSFDrawing xlsx_drawingAvgNPM = secSheet.createDrawingPatriarch();

			/* Define anchor points in the worksheet to position the chart */
			XSSFClientAnchor anchorAvgNPM = xlsx_drawing.createAnchor(10 * Units.EMU_PER_PIXEL, 10 * Units.EMU_PER_PIXEL,
			          520 * Units.EMU_PER_PIXEL, 280 * Units.EMU_PER_PIXEL, colnum, row.getRowNum(), colnum, row.getRowNum());

			/* Create the chart object based on the anchor point */
			XSSFChart my_line_chartAvgNPM = xlsx_drawingAvgNPM.createChart(anchorAvgNPM);

			/* Define chart AXIS */
			ChartAxis bottomAxis_chartAvgNPM = my_line_chartAvgNPM.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);

			ValueAxis leftAxis_chartAvgNPM = my_line_chartAvgNPM.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
			leftAxis_chartAvgNPM.setCrosses(AxisCrosses.AUTO_ZERO);

			LineChartData data_chartAvgNPM = my_line_chartAvgNPM.getChartDataFactory().createLineChartData();

			ChartDataSource<String> xs_chartAvgNPM = DataSources.fromStringCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumScripCode, colnumScripCode));
			ChartDataSource<Number> ys1_chartAvgNPM = DataSources.fromNumericCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumAvg_NPM, colnumAvg_NPM));

			/* Add chart data sources as data to the chart */
			LineChartSeries AvgNPMSeries = data_chartAvgNPM.addSeries(xs_chartAvgNPM, ys1_chartAvgNPM);

			CTChart ctChartAvgNPM = my_line_chartAvgNPM.getCTChart();
			CTTitle titleAvgNPM = ctChartAvgNPM.addNewTitle();
			titleAvgNPM.addNewOverlay().setVal(false);
			CTTx txAvgNPM = titleAvgNPM.addNewTx();
			CTTextBody richAvgNPM = txAvgNPM.addNewRich();
			richAvgNPM.addNewBodyPr(); // body properties must exist, but can be empty
			CTTextParagraph paraAvgNPM = richAvgNPM.addNewP();
			CTRegularTextRun rAvgNPM = paraAvgNPM.addNewR();
			rAvgNPM.setT(constant_ChartTitles[5]);

			/* Plot the chart with the inputs from data and chart axis */
			my_line_chartAvgNPM.plot(data_chartAvgNPM, new ChartAxis[]
			{ bottomAxis_chartAvgNPM, leftAxis_chartAvgNPM
			});

			CTPlotArea plotAreaAvgNPM = my_line_chartAvgNPM.getCTChart().getPlotArea();

			for ( CTLineSer ser : plotAreaAvgNPM.getLineChartArray()[0].getSerArray() )
			{
				// Don't smoothen and don't put markers
				ser.setMarker(ctMarker);
				ser.setSmooth(ctBool);

			}

			colnum++;

		}
	}

	/**
	 * --------------------------------------- Plot CAGR dividend and Capex Borrowings Ratio Charts
	 * 
	 * @param row
	 *             - Row Instance
	 * @param secSheet
	 *             - Sector Sheet
	 * @param sumSheet
	 *             - Summary Sheet
	 * @param styleNoFill
	 *             - No Fill Plain Style
	 * @param SectorXlsRowPositioner
	 *             - Sector Xls Row Positioner Instance for Current Sector
	 *             ---------------------------------------------------------------------------------
	 */
	private void plotCAGRDivCapexBW(Row row, XSSFSheet secSheet, XSSFSheet sumSheet, XSSFCellStyle styleNoFill,
	          Ty_SectorXlsRowPositioner SectorXlsRowPositioner)
	{

		if (row != null && secSheet != null && sumSheet != null && SectorXlsRowPositioner != null)
		{
			int colnum = 0;
			row.setHeight((short) 4500);

			CTMarker ctMarker = CTMarker.Factory.newInstance();
			ctMarker.setSymbol(CTMarkerStyle.Factory.newInstance());
			CTBoolean ctBool = CTBoolean.Factory.newInstance();
			ctBool.setVal(false);

			// -----CAGR Dividend ----------------------------
			Cell dbcelllchartDivCAGR = row.createCell(colnum);
			dbcelllchartDivCAGR.setCellStyle(styleNoFill);

			// Create Drawing Object
			XSSFDrawing xlsx_drawing = secSheet.createDrawingPatriarch();

			/* Define anchor points in the worksheet to position the chart */
			XSSFClientAnchor anchor = xlsx_drawing.createAnchor(10 * Units.EMU_PER_PIXEL, 10 * Units.EMU_PER_PIXEL, 520 * Units.EMU_PER_PIXEL,
			          280 * Units.EMU_PER_PIXEL, colnum, row.getRowNum(), colnum, row.getRowNum());

			/* Create the chart object based on the anchor point */
			XSSFChart my_line_chartDivCAGR = xlsx_drawing.createChart(anchor);

			// my_line_chartDivCAGR.setTitle(constant_ChartTitles[0]);

			/* Define chart AXIS */
			ChartAxis bottomAxis_chartDivCAGR = my_line_chartDivCAGR.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);

			ValueAxis leftAxis_chartDivCAGR = my_line_chartDivCAGR.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
			leftAxis_chartDivCAGR.setCrosses(AxisCrosses.AUTO_ZERO);

			LineChartData data_chartDivCAGR = my_line_chartDivCAGR.getChartDataFactory().createLineChartData();

			ChartDataSource<String> xs_chartDivCAGR = DataSources.fromStringCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumScripCode, colnumScripCode));
			ChartDataSource<Number> ys1_chartDivCAGR = DataSources.fromNumericCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumCAGR_DIV, colnumCAGR_DIV));

			/* Add chart data sources as data to the chart */
			LineChartSeries DivCAGRSeries = data_chartDivCAGR.addSeries(xs_chartDivCAGR, ys1_chartDivCAGR);

			CTChart ctChart = my_line_chartDivCAGR.getCTChart();
			CTTitle title = ctChart.addNewTitle();
			title.addNewOverlay().setVal(false);
			CTTx tx = title.addNewTx();
			CTTextBody rich = tx.addNewRich();
			rich.addNewBodyPr(); // body properties must exist, but can be empty
			CTTextParagraph para = rich.addNewP();
			CTRegularTextRun r = para.addNewR();
			r.setT(constant_ChartTitles[6]);

			/* Plot the chart with the inputs from data and chart axis */
			my_line_chartDivCAGR.plot(data_chartDivCAGR, new ChartAxis[]
			{ bottomAxis_chartDivCAGR, leftAxis_chartDivCAGR
			});

			CTPlotArea plotAreaCAGRSales = my_line_chartDivCAGR.getCTChart().getPlotArea();

			for ( CTLineSer ser : plotAreaCAGRSales.getLineChartArray()[0].getSerArray() )
			{
				// Don't smoothen and don't put markers
				ser.setMarker(ctMarker);
				ser.setSmooth(ctBool);

			}
			colnum++;

			// -----Avg Effective DPS ----------------------------
			Cell dbcelllchartAvgDPS = row.createCell(colnum);
			dbcelllchartAvgDPS.setCellStyle(styleNoFill);

			// Create Drawing Object
			XSSFDrawing xlsx_drawingAvgDPS = secSheet.createDrawingPatriarch();

			/* Define anchor points in the worksheet to position the chart */
			XSSFClientAnchor anchorAvgDPS = xlsx_drawing.createAnchor(10 * Units.EMU_PER_PIXEL, 10 * Units.EMU_PER_PIXEL,
			          520 * Units.EMU_PER_PIXEL, 280 * Units.EMU_PER_PIXEL, colnum, row.getRowNum(), colnum, row.getRowNum());

			/* Create the chart object based on the anchor point */
			XSSFChart my_line_chartAvgDPS = xlsx_drawingAvgDPS.createChart(anchorAvgDPS);

			/* Define chart AXIS */
			ChartAxis bottomAxis_chartAvgDPS = my_line_chartAvgDPS.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);

			ValueAxis leftAxis_chartAvgDPS = my_line_chartAvgDPS.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
			leftAxis_chartAvgDPS.setCrosses(AxisCrosses.AUTO_ZERO);

			LineChartData data_chartAvgDPS = my_line_chartAvgDPS.getChartDataFactory().createLineChartData();

			ChartDataSource<String> xs_chartAvgDPS = DataSources.fromStringCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumScripCode, colnumScripCode));
			ChartDataSource<Number> ys1_chartAvgDPS = DataSources.fromNumericCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumAvg_DIV, colnumAvg_DIV));

			/* Add chart data sources as data to the chart */
			LineChartSeries AvgDPSSeries = data_chartAvgDPS.addSeries(xs_chartAvgDPS, ys1_chartAvgDPS);

			CTChart ctChartAvgDPS = my_line_chartAvgDPS.getCTChart();
			CTTitle titleAvgDPS = ctChartAvgDPS.addNewTitle();
			titleAvgDPS.addNewOverlay().setVal(false);
			CTTx txAvgDPS = titleAvgDPS.addNewTx();
			CTTextBody richAvgDPS = txAvgDPS.addNewRich();
			richAvgDPS.addNewBodyPr(); // body properties must exist, but can be empty
			CTTextParagraph paraAvgDPS = richAvgDPS.addNewP();
			CTRegularTextRun rCAGRPrfit = paraAvgDPS.addNewR();
			rCAGRPrfit.setT(constant_ChartTitles[7]);

			/* Plot the chart with the inputs from data and chart axis */
			my_line_chartAvgDPS.plot(data_chartAvgDPS, new ChartAxis[]
			{ bottomAxis_chartAvgDPS, leftAxis_chartAvgDPS
			});

			CTPlotArea plotAreaAvgDPS = my_line_chartAvgDPS.getCTChart().getPlotArea();

			for ( CTLineSer ser : plotAreaAvgDPS.getLineChartArray()[0].getSerArray() )
			{
				// Don't smoothen and don't put markers
				ser.setMarker(ctMarker);
				ser.setSmooth(ctBool);

			}

			colnum++;

			// -----Avg Capex Borrowings Ratio ----------------------------
			Cell dbcelllchartAvgCapexBW = row.createCell(colnum);
			dbcelllchartAvgCapexBW.setCellStyle(styleNoFill);

			// Create Drawing Object
			XSSFDrawing xlsx_drawingAvgCapexBW = secSheet.createDrawingPatriarch();

			/* Define anchor points in the worksheet to position the chart */
			XSSFClientAnchor anchorAvgCapexBW = xlsx_drawing.createAnchor(10 * Units.EMU_PER_PIXEL, 10 * Units.EMU_PER_PIXEL,
			          520 * Units.EMU_PER_PIXEL, 280 * Units.EMU_PER_PIXEL, colnum, row.getRowNum(), colnum, row.getRowNum());

			/* Create the chart object based on the anchor point */
			XSSFChart my_line_chartAvgCapexBW = xlsx_drawingAvgCapexBW.createChart(anchorAvgCapexBW);

			/* Define chart AXIS */
			ChartAxis bottomAxis_chartAvgCapexBW = my_line_chartAvgCapexBW.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);

			ValueAxis leftAxis_chartAvgCapexBW = my_line_chartAvgCapexBW.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
			leftAxis_chartAvgCapexBW.setCrosses(AxisCrosses.AUTO_ZERO);

			LineChartData data_chartAvgCapexBW = my_line_chartAvgCapexBW.getChartDataFactory().createLineChartData();

			ChartDataSource<String> xs_chartAvgCapexBW = DataSources.fromStringCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumScripCode, colnumScripCode));
			ChartDataSource<Number> ys1_chartAvgCapexBW = DataSources.fromNumericCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumAvg_CBR, colnumAvg_CBR));

			/* Add chart data sources as data to the chart */
			LineChartSeries AvgCapexBWSeries = data_chartAvgCapexBW.addSeries(xs_chartAvgCapexBW, ys1_chartAvgCapexBW);

			CTChart ctChartAvgCapexBW = my_line_chartAvgCapexBW.getCTChart();
			CTTitle titleAvgCapexBW = ctChartAvgCapexBW.addNewTitle();
			titleAvgCapexBW.addNewOverlay().setVal(false);
			CTTx txAvgCapexBW = titleAvgCapexBW.addNewTx();
			CTTextBody richAvgCapexBW = txAvgCapexBW.addNewRich();
			richAvgCapexBW.addNewBodyPr(); // body properties must exist, but can be empty
			CTTextParagraph paraAvgCapexBW = richAvgCapexBW.addNewP();
			CTRegularTextRun rAvgCapexBW = paraAvgCapexBW.addNewR();
			rAvgCapexBW.setT(constant_ChartTitles[8]);

			/* Plot the chart with the inputs from data and chart axis */
			my_line_chartAvgCapexBW.plot(data_chartAvgCapexBW, new ChartAxis[]
			{ bottomAxis_chartAvgCapexBW, leftAxis_chartAvgCapexBW
			});

			CTPlotArea plotAreaAvgCapexBW = my_line_chartAvgCapexBW.getCTChart().getPlotArea();

			for ( CTLineSer ser : plotAreaAvgCapexBW.getLineChartArray()[0].getSerArray() )
			{
				// Don't smoothen and don't put markers
				ser.setMarker(ctMarker);
				ser.setSmooth(ctBool);

			}

			colnum++;

		}
	}

	/**
	 * --------------------------------------- Plot CAGR Inventory turnover and Reserves Provisions Ratio Charts
	 * 
	 * @param row
	 *             - Row Instance
	 * @param secSheet
	 *             - Sector Sheet
	 * @param sumSheet
	 *             - Summary Sheet
	 * @param styleNoFill
	 *             - No Fill Plain Style
	 * @param SectorXlsRowPositioner
	 *             - Sector Xls Row Positioner Instance for Current Sector
	 *             ---------------------------------------------------------------------------------
	 */
	private void plotCAGRInvRPR(Row row, XSSFSheet secSheet, XSSFSheet sumSheet, XSSFCellStyle styleNoFill,
	          Ty_SectorXlsRowPositioner SectorXlsRowPositioner)
	{

		if (row != null && secSheet != null && sumSheet != null && SectorXlsRowPositioner != null)
		{
			int colnum = 0;
			row.setHeight((short) 4500);

			CTMarker ctMarker = CTMarker.Factory.newInstance();
			ctMarker.setSymbol(CTMarkerStyle.Factory.newInstance());
			CTBoolean ctBool = CTBoolean.Factory.newInstance();
			ctBool.setVal(false);

			// -----CAGR Inventory Turnover Ratio ----------------------------
			Cell dbcelllchartInvCAGR = row.createCell(colnum);
			dbcelllchartInvCAGR.setCellStyle(styleNoFill);

			// Create Drawing Object
			XSSFDrawing xlsx_drawing = secSheet.createDrawingPatriarch();

			/* Define anchor points in the worksheet to position the chart */
			XSSFClientAnchor anchor = xlsx_drawing.createAnchor(10 * Units.EMU_PER_PIXEL, 10 * Units.EMU_PER_PIXEL, 520 * Units.EMU_PER_PIXEL,
			          280 * Units.EMU_PER_PIXEL, colnum, row.getRowNum(), colnum, row.getRowNum());

			/* Create the chart object based on the anchor point */
			XSSFChart my_line_chartInvCAGR = xlsx_drawing.createChart(anchor);

			// my_line_chartInvCAGR.setTitle(constant_ChartTitles[0]);

			/* Define chart AXIS */
			ChartAxis bottomAxis_chartInvCAGR = my_line_chartInvCAGR.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);

			ValueAxis leftAxis_chartInvCAGR = my_line_chartInvCAGR.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
			leftAxis_chartInvCAGR.setCrosses(AxisCrosses.AUTO_ZERO);

			LineChartData data_chartInvCAGR = my_line_chartInvCAGR.getChartDataFactory().createLineChartData();

			ChartDataSource<String> xs_chartInvCAGR = DataSources.fromStringCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumScripCode, colnumScripCode));
			ChartDataSource<Number> ys1_chartInvCAGR = DataSources.fromNumericCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumCAGR_ITR, colnumCAGR_ITR));

			/* Add chart data sources as data to the chart */
			LineChartSeries InvCAGRSeries = data_chartInvCAGR.addSeries(xs_chartInvCAGR, ys1_chartInvCAGR);

			CTChart ctChart = my_line_chartInvCAGR.getCTChart();
			CTTitle title = ctChart.addNewTitle();
			title.addNewOverlay().setVal(false);
			CTTx tx = title.addNewTx();
			CTTextBody rich = tx.addNewRich();
			rich.addNewBodyPr(); // body properties must exist, but can be empty
			CTTextParagraph para = rich.addNewP();
			CTRegularTextRun r = para.addNewR();
			r.setT(constant_ChartTitles[9]);

			/* Plot the chart with the inputs from data and chart axis */
			my_line_chartInvCAGR.plot(data_chartInvCAGR, new ChartAxis[]
			{ bottomAxis_chartInvCAGR, leftAxis_chartInvCAGR
			});

			CTPlotArea plotAreaCAGRSales = my_line_chartInvCAGR.getCTChart().getPlotArea();

			for ( CTLineSer ser : plotAreaCAGRSales.getLineChartArray()[0].getSerArray() )
			{
				// Don't smoothen and don't put markers
				ser.setMarker(ctMarker);
				ser.setSmooth(ctBool);

			}
			colnum++;

			// -----Avg Inventory Turnover Ratio ----------------------------
			Cell dbcelllchartAvgITR = row.createCell(colnum);
			dbcelllchartAvgITR.setCellStyle(styleNoFill);

			// Create Drawing Object
			XSSFDrawing xlsx_drawingAvgITR = secSheet.createDrawingPatriarch();

			/* Define anchor points in the worksheet to position the chart */
			XSSFClientAnchor anchorAvgITR = xlsx_drawing.createAnchor(10 * Units.EMU_PER_PIXEL, 10 * Units.EMU_PER_PIXEL,
			          520 * Units.EMU_PER_PIXEL, 280 * Units.EMU_PER_PIXEL, colnum, row.getRowNum(), colnum, row.getRowNum());

			/* Create the chart object based on the anchor point */
			XSSFChart my_line_chartAvgITR = xlsx_drawingAvgITR.createChart(anchorAvgITR);

			/* Define chart AXIS */
			ChartAxis bottomAxis_chartAvgITR = my_line_chartAvgITR.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);

			ValueAxis leftAxis_chartAvgITR = my_line_chartAvgITR.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
			leftAxis_chartAvgITR.setCrosses(AxisCrosses.AUTO_ZERO);

			LineChartData data_chartAvgITR = my_line_chartAvgITR.getChartDataFactory().createLineChartData();

			ChartDataSource<String> xs_chartAvgITR = DataSources.fromStringCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumScripCode, colnumScripCode));
			ChartDataSource<Number> ys1_chartAvgITR = DataSources.fromNumericCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumAvg_ITR, colnumAvg_ITR));

			/* Add chart data sources as data to the chart */
			LineChartSeries AvgITRSeries = data_chartAvgITR.addSeries(xs_chartAvgITR, ys1_chartAvgITR);

			CTChart ctChartAvgITR = my_line_chartAvgITR.getCTChart();
			CTTitle titleAvgITR = ctChartAvgITR.addNewTitle();
			titleAvgITR.addNewOverlay().setVal(false);
			CTTx txAvgITR = titleAvgITR.addNewTx();
			CTTextBody richAvgITR = txAvgITR.addNewRich();
			richAvgITR.addNewBodyPr(); // body properties must exist, but can be empty
			CTTextParagraph paraAvgITR = richAvgITR.addNewP();
			CTRegularTextRun rCAGRPrfit = paraAvgITR.addNewR();
			rCAGRPrfit.setT(constant_ChartTitles[10]);

			/* Plot the chart with the inputs from data and chart axis */
			my_line_chartAvgITR.plot(data_chartAvgITR, new ChartAxis[]
			{ bottomAxis_chartAvgITR, leftAxis_chartAvgITR
			});

			CTPlotArea plotAreaAvgITR = my_line_chartAvgITR.getCTChart().getPlotArea();

			for ( CTLineSer ser : plotAreaAvgITR.getLineChartArray()[0].getSerArray() )
			{
				// Don't smoothen and don't put markers
				ser.setMarker(ctMarker);
				ser.setSmooth(ctBool);

			}

			colnum++;

			// -----Avg Reserves Provisions Ratio ----------------------------
			Cell dbcelllchartAvgRPR = row.createCell(colnum);
			dbcelllchartAvgRPR.setCellStyle(styleNoFill);

			// Create Drawing Object
			XSSFDrawing xlsx_drawingAvgRPR = secSheet.createDrawingPatriarch();

			/* Define anchor points in the worksheet to position the chart */
			XSSFClientAnchor anchorAvgRPR = xlsx_drawing.createAnchor(10 * Units.EMU_PER_PIXEL, 10 * Units.EMU_PER_PIXEL,
			          520 * Units.EMU_PER_PIXEL, 280 * Units.EMU_PER_PIXEL, colnum, row.getRowNum(), colnum, row.getRowNum());

			/* Create the chart object based on the anchor point */
			XSSFChart my_line_chartAvgRPR = xlsx_drawingAvgRPR.createChart(anchorAvgRPR);

			/* Define chart AXIS */
			ChartAxis bottomAxis_chartAvgRPR = my_line_chartAvgRPR.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);

			ValueAxis leftAxis_chartAvgRPR = my_line_chartAvgRPR.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
			leftAxis_chartAvgRPR.setCrosses(AxisCrosses.AUTO_ZERO);

			LineChartData data_chartAvgRPR = my_line_chartAvgRPR.getChartDataFactory().createLineChartData();

			ChartDataSource<String> xs_chartAvgRPR = DataSources.fromStringCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumScripCode, colnumScripCode));
			ChartDataSource<Number> ys1_chartAvgRPR = DataSources.fromNumericCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumAvg_RPR, colnumAvg_RPR));

			/* Add chart data sources as data to the chart */
			LineChartSeries AvgRPRSeries = data_chartAvgRPR.addSeries(xs_chartAvgRPR, ys1_chartAvgRPR);

			CTChart ctChartAvgRPR = my_line_chartAvgRPR.getCTChart();
			CTTitle titleAvgRPR = ctChartAvgRPR.addNewTitle();
			titleAvgRPR.addNewOverlay().setVal(false);
			CTTx txAvgRPR = titleAvgRPR.addNewTx();
			CTTextBody richAvgRPR = txAvgRPR.addNewRich();
			richAvgRPR.addNewBodyPr(); // body properties must exist, but can be empty
			CTTextParagraph paraAvgRPR = richAvgRPR.addNewP();
			CTRegularTextRun rAvgRPR = paraAvgRPR.addNewR();
			rAvgRPR.setT(constant_ChartTitles[11]);

			/* Plot the chart with the inputs from data and chart axis */
			my_line_chartAvgRPR.plot(data_chartAvgRPR, new ChartAxis[]
			{ bottomAxis_chartAvgRPR, leftAxis_chartAvgRPR
			});

			CTPlotArea plotAreaAvgRPR = my_line_chartAvgRPR.getCTChart().getPlotArea();

			for ( CTLineSer ser : plotAreaAvgRPR.getLineChartArray()[0].getSerArray() )
			{
				// Don't smoothen and don't put markers
				ser.setMarker(ctMarker);
				ser.setSmooth(ctBool);

			}

			colnum++;

		}
	}

	/**
	 * --------------------------------------- Plot CAGR Debt
	 * 
	 * @param row
	 *             - Row Instance
	 * @param secSheet
	 *             - Sector Sheet
	 * @param sumSheet
	 *             - Summary Sheet
	 * @param styleNoFill
	 *             - No Fill Plain Style
	 * @param SectorXlsRowPositioner
	 *             - Sector Xls Row Positioner Instance for Current Sector
	 *             ---------------------------------------------------------------------------------
	 */
	private void plotCAGRDebt(Row row, XSSFSheet secSheet, XSSFSheet sumSheet, XSSFCellStyle styleNoFill,
	          Ty_SectorXlsRowPositioner SectorXlsRowPositioner)
	{

		if (row != null && secSheet != null && sumSheet != null && SectorXlsRowPositioner != null)
		{
			int colnum = 0;
			row.setHeight((short) 4500);

			CTMarker ctMarker = CTMarker.Factory.newInstance();
			ctMarker.setSymbol(CTMarkerStyle.Factory.newInstance());
			CTBoolean ctBool = CTBoolean.Factory.newInstance();
			ctBool.setVal(false);

			// -----CAGR Debt Equity Ratio ----------------------------
			Cell dbcelllchartDERCAGR = row.createCell(colnum);
			dbcelllchartDERCAGR.setCellStyle(styleNoFill);

			// Create Drawing Object
			XSSFDrawing xlsx_drawing = secSheet.createDrawingPatriarch();

			/* Define anchor points in the worksheet to position the chart */
			XSSFClientAnchor anchor = xlsx_drawing.createAnchor(10 * Units.EMU_PER_PIXEL, 10 * Units.EMU_PER_PIXEL, 520 * Units.EMU_PER_PIXEL,
			          280 * Units.EMU_PER_PIXEL, colnum, row.getRowNum(), colnum, row.getRowNum());

			/* Create the chart object based on the anchor point */
			XSSFChart my_line_chartDERCAGR = xlsx_drawing.createChart(anchor);

			// my_line_chartDERCAGR.setTitle(constant_ChartTitles[0]);

			/* Define chart AXIS */
			ChartAxis bottomAxis_chartDERCAGR = my_line_chartDERCAGR.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);

			ValueAxis leftAxis_chartDERCAGR = my_line_chartDERCAGR.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
			leftAxis_chartDERCAGR.setCrosses(AxisCrosses.AUTO_ZERO);

			LineChartData data_chartDERCAGR = my_line_chartDERCAGR.getChartDataFactory().createLineChartData();

			ChartDataSource<String> xs_chartDERCAGR = DataSources.fromStringCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumScripCode, colnumScripCode));
			ChartDataSource<Number> ys1_chartDERCAGR = DataSources.fromNumericCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumCAGR_DER, colnumCAGR_DER));

			/* Add chart data sources as data to the chart */
			LineChartSeries DERCAGRSeries = data_chartDERCAGR.addSeries(xs_chartDERCAGR, ys1_chartDERCAGR);

			CTChart ctChart = my_line_chartDERCAGR.getCTChart();
			CTTitle title = ctChart.addNewTitle();
			title.addNewOverlay().setVal(false);
			CTTx tx = title.addNewTx();
			CTTextBody rich = tx.addNewRich();
			rich.addNewBodyPr(); // body properties must exist, but can be empty
			CTTextParagraph para = rich.addNewP();
			CTRegularTextRun r = para.addNewR();
			r.setT(constant_ChartTitles[12]);

			/* Plot the chart with the inputs from data and chart axis */
			my_line_chartDERCAGR.plot(data_chartDERCAGR, new ChartAxis[]
			{ bottomAxis_chartDERCAGR, leftAxis_chartDERCAGR
			});

			CTPlotArea plotAreaCAGRSales = my_line_chartDERCAGR.getCTChart().getPlotArea();

			for ( CTLineSer ser : plotAreaCAGRSales.getLineChartArray()[0].getSerArray() )
			{
				// Don't smoothen and don't put markers
				ser.setMarker(ctMarker);
				ser.setSmooth(ctBool);

			}
			colnum++;

			// -----Avg Debt Equity Ratio ----------------------------
			Cell dbcelllchartAvgDER = row.createCell(colnum);
			dbcelllchartAvgDER.setCellStyle(styleNoFill);

			// Create Drawing Object
			XSSFDrawing xlsx_drawingAvgDER = secSheet.createDrawingPatriarch();

			/* Define anchor points in the worksheet to position the chart */
			XSSFClientAnchor anchorAvgDER = xlsx_drawing.createAnchor(10 * Units.EMU_PER_PIXEL, 10 * Units.EMU_PER_PIXEL,
			          520 * Units.EMU_PER_PIXEL, 280 * Units.EMU_PER_PIXEL, colnum, row.getRowNum(), colnum, row.getRowNum());

			/* Create the chart object based on the anchor point */
			XSSFChart my_line_chartAvgDER = xlsx_drawingAvgDER.createChart(anchorAvgDER);

			/* Define chart AXIS */
			ChartAxis bottomAxis_chartAvgDER = my_line_chartAvgDER.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);

			ValueAxis leftAxis_chartAvgDER = my_line_chartAvgDER.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
			leftAxis_chartAvgDER.setCrosses(AxisCrosses.AUTO_ZERO);

			LineChartData data_chartAvgDER = my_line_chartAvgDER.getChartDataFactory().createLineChartData();

			ChartDataSource<String> xs_chartAvgDER = DataSources.fromStringCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumScripCode, colnumScripCode));
			ChartDataSource<Number> ys1_chartAvgDER = DataSources.fromNumericCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumAvg_DER, colnumAvg_DER));

			/* Add chart data sources as data to the chart */
			LineChartSeries AvgDERSeries = data_chartAvgDER.addSeries(xs_chartAvgDER, ys1_chartAvgDER);

			CTChart ctChartAvgDER = my_line_chartAvgDER.getCTChart();
			CTTitle titleAvgDER = ctChartAvgDER.addNewTitle();
			titleAvgDER.addNewOverlay().setVal(false);
			CTTx txAvgDER = titleAvgDER.addNewTx();
			CTTextBody richAvgDER = txAvgDER.addNewRich();
			richAvgDER.addNewBodyPr(); // body properties must exist, but can be empty
			CTTextParagraph paraAvgDER = richAvgDER.addNewP();
			CTRegularTextRun rCAGRPrfit = paraAvgDER.addNewR();
			rCAGRPrfit.setT(constant_ChartTitles[13]);

			/* Plot the chart with the inputs from data and chart axis */
			my_line_chartAvgDER.plot(data_chartAvgDER, new ChartAxis[]
			{ bottomAxis_chartAvgDER, leftAxis_chartAvgDER
			});

			CTPlotArea plotAreaAvgDER = my_line_chartAvgDER.getCTChart().getPlotArea();

			for ( CTLineSer ser : plotAreaAvgDER.getLineChartArray()[0].getSerArray() )
			{
				// Don't smoothen and don't put markers
				ser.setMarker(ctMarker);
				ser.setSmooth(ctBool);

			}

			colnum++;

			// -----CAGR DEbt ----------------------------
			Cell dbcelllchartCAGRDebt = row.createCell(colnum);
			dbcelllchartCAGRDebt.setCellStyle(styleNoFill);

			// Create Drawing Object
			XSSFDrawing xlsx_drawingCAGRDebt = secSheet.createDrawingPatriarch();

			/* Define anchor points in the worksheet to position the chart */
			XSSFClientAnchor anchorCAGRDebt = xlsx_drawing.createAnchor(10 * Units.EMU_PER_PIXEL, 10 * Units.EMU_PER_PIXEL,
			          520 * Units.EMU_PER_PIXEL, 280 * Units.EMU_PER_PIXEL, colnum, row.getRowNum(), colnum, row.getRowNum());

			/* Create the chart object based on the anchor point */
			XSSFChart my_line_chartCAGRDebt = xlsx_drawingCAGRDebt.createChart(anchorCAGRDebt);

			/* Define chart AXIS */
			ChartAxis bottomAxis_chartCAGRDebt = my_line_chartCAGRDebt.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);

			ValueAxis leftAxis_chartCAGRDebt = my_line_chartCAGRDebt.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
			leftAxis_chartCAGRDebt.setCrosses(AxisCrosses.AUTO_ZERO);

			LineChartData data_chartCAGRDebt = my_line_chartCAGRDebt.getChartDataFactory().createLineChartData();

			ChartDataSource<String> xs_chartCAGRDebt = DataSources.fromStringCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumScripCode, colnumScripCode));
			ChartDataSource<Number> ys1_chartCAGRDebt = DataSources.fromNumericCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumCAGR_Debt, colnumCAGR_Debt));

			/* Add chart data sources as data to the chart */
			LineChartSeries CAGRDebtSeries = data_chartCAGRDebt.addSeries(xs_chartCAGRDebt, ys1_chartCAGRDebt);

			CTChart ctChartCAGRDebt = my_line_chartCAGRDebt.getCTChart();
			CTTitle titleCAGRDebt = ctChartCAGRDebt.addNewTitle();
			titleCAGRDebt.addNewOverlay().setVal(false);
			CTTx txCAGRDebt = titleCAGRDebt.addNewTx();
			CTTextBody richCAGRDebt = txCAGRDebt.addNewRich();
			richCAGRDebt.addNewBodyPr(); // body properties must exist, but can be empty
			CTTextParagraph paraCAGRDebt = richCAGRDebt.addNewP();
			CTRegularTextRun rCAGRDebt = paraCAGRDebt.addNewR();
			rCAGRDebt.setT(constant_ChartTitles[14]);

			/* Plot the chart with the inputs from data and chart axis */
			my_line_chartCAGRDebt.plot(data_chartCAGRDebt, new ChartAxis[]
			{ bottomAxis_chartCAGRDebt, leftAxis_chartCAGRDebt
			});

			CTPlotArea plotAreaCAGRDebt = my_line_chartCAGRDebt.getCTChart().getPlotArea();

			for ( CTLineSer ser : plotAreaCAGRDebt.getLineChartArray()[0].getSerArray() )
			{
				// Don't smoothen and don't put markers
				ser.setMarker(ctMarker);
				ser.setSmooth(ctBool);

			}

			colnum++;

		}
	}

	/**
	 * --------------------------------------- Plot Price DEviation and Share Holding Patterns
	 * 
	 * @param row
	 *             - Row Instance
	 * @param secSheet
	 *             - Sector Sheet
	 * @param sumSheet
	 *             - Summary Sheet
	 * @param styleNoFill
	 *             - No Fill Plain Style
	 * @param SectorXlsRowPositioner
	 *             - Sector Xls Row Positioner Instance for Current Sector
	 *             ---------------------------------------------------------------------------------
	 */

	private void plotPDevSHP(Row row, XSSFSheet secSheet, XSSFSheet sumSheet, XSSFCellStyle styleNoFill,
	          Ty_SectorXlsRowPositioner SectorXlsRowPositioner)
	{

		if (row != null && secSheet != null && sumSheet != null && SectorXlsRowPositioner != null)
		{
			int colnum = 0;
			row.setHeight((short) 4500);

			CTMarker ctMarker = CTMarker.Factory.newInstance();
			ctMarker.setSymbol(CTMarkerStyle.Factory.newInstance());
			CTBoolean ctBool = CTBoolean.Factory.newInstance();
			ctBool.setVal(false);

			// -----Price Deviation from Avg. ----------------------------
			Cell dbcelllchartPDev = row.createCell(colnum);
			dbcelllchartPDev.setCellStyle(styleNoFill);

			// Create Drawing Object
			XSSFDrawing xlsx_drawing = secSheet.createDrawingPatriarch();

			/* Define anchor points in the worksheet to position the chart */
			XSSFClientAnchor anchor = xlsx_drawing.createAnchor(10 * Units.EMU_PER_PIXEL, 10 * Units.EMU_PER_PIXEL, 520 * Units.EMU_PER_PIXEL,
			          280 * Units.EMU_PER_PIXEL, colnum, row.getRowNum(), colnum, row.getRowNum());

			/* Create the chart object based on the anchor point */
			XSSFChart my_line_chartPDev = xlsx_drawing.createChart(anchor);

			// my_line_chartPDev.setTitle(constant_ChartTitles[0]);

			/* Define chart AXIS */
			ChartAxis bottomAxis_chartPDev = my_line_chartPDev.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);

			ValueAxis leftAxis_chartPDev = my_line_chartPDev.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
			leftAxis_chartPDev.setCrosses(AxisCrosses.AUTO_ZERO);

			LineChartData data_chartPDev = my_line_chartPDev.getChartDataFactory().createLineChartData();

			ChartDataSource<String> xs_chartPDev = DataSources.fromStringCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumScripCode, colnumScripCode));
			ChartDataSource<Number> ys1_chartPDev = DataSources.fromNumericCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumCAGR_PDev, colnumCAGR_PDev));

			/* Add chart data sources as data to the chart */
			LineChartSeries PDevSeries = data_chartPDev.addSeries(xs_chartPDev, ys1_chartPDev);

			CTChart ctChart = my_line_chartPDev.getCTChart();
			CTTitle title = ctChart.addNewTitle();
			title.addNewOverlay().setVal(false);
			CTTx tx = title.addNewTx();
			CTTextBody rich = tx.addNewRich();
			rich.addNewBodyPr(); // body properties must exist, but can be empty
			CTTextParagraph para = rich.addNewP();
			CTRegularTextRun r = para.addNewR();
			r.setT(constant_ChartTitles[15]);

			/* Plot the chart with the inputs from data and chart axis */
			my_line_chartPDev.plot(data_chartPDev, new ChartAxis[]
			{ bottomAxis_chartPDev, leftAxis_chartPDev
			});

			CTPlotArea plotAreaCAGRSales = my_line_chartPDev.getCTChart().getPlotArea();

			for ( CTLineSer ser : plotAreaCAGRSales.getLineChartArray()[0].getSerArray() )
			{
				// Don't smoothen and don't put markers
				ser.setMarker(ctMarker);
				ser.setSmooth(ctBool);

			}
			colnum++;

			// -----FII Shareholding Pattern ----------------------------
			Cell dbcelllchartFIISHP = row.createCell(colnum);
			dbcelllchartFIISHP.setCellStyle(styleNoFill);

			// Create Drawing Object
			XSSFDrawing xlsx_drawingFIISHP = secSheet.createDrawingPatriarch();

			/* Define anchor points in the worksheet to position the chart */
			XSSFClientAnchor anchorFIISHP = xlsx_drawing.createAnchor(10 * Units.EMU_PER_PIXEL, 10 * Units.EMU_PER_PIXEL,
			          520 * Units.EMU_PER_PIXEL, 280 * Units.EMU_PER_PIXEL, colnum, row.getRowNum(), colnum, row.getRowNum());

			/* Create the chart object based on the anchor point */
			XSSFChart my_line_chartFIISHP = xlsx_drawingFIISHP.createChart(anchorFIISHP);

			/* Define chart AXIS */
			ChartAxis bottomAxis_chartFIISHP = my_line_chartFIISHP.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);

			ValueAxis leftAxis_chartFIISHP = my_line_chartFIISHP.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
			leftAxis_chartFIISHP.setCrosses(AxisCrosses.AUTO_ZERO);

			LineChartData data_chartFIISHP = my_line_chartFIISHP.getChartDataFactory().createLineChartData();

			ChartDataSource<String> xs_chartFIISHP = DataSources.fromStringCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumScripCode, colnumScripCode));
			ChartDataSource<Number> ys1_chartFIISHP = DataSources.fromNumericCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumFIIHold, colnumFIIHold));

			/* Add chart data sources as data to the chart */
			LineChartSeries FIISHPSeries = data_chartFIISHP.addSeries(xs_chartFIISHP, ys1_chartFIISHP);

			CTChart ctChartFIISHP = my_line_chartFIISHP.getCTChart();
			CTTitle titleFIISHP = ctChartFIISHP.addNewTitle();
			titleFIISHP.addNewOverlay().setVal(false);
			CTTx txFIISHP = titleFIISHP.addNewTx();
			CTTextBody richFIISHP = txFIISHP.addNewRich();
			richFIISHP.addNewBodyPr(); // body properties must exist, but can be empty
			CTTextParagraph paraFIISHP = richFIISHP.addNewP();
			CTRegularTextRun rCAGRPrfit = paraFIISHP.addNewR();
			rCAGRPrfit.setT(constant_ChartTitles[16]);

			/* Plot the chart with the inputs from data and chart axis */
			my_line_chartFIISHP.plot(data_chartFIISHP, new ChartAxis[]
			{ bottomAxis_chartFIISHP, leftAxis_chartFIISHP
			});

			CTPlotArea plotAreaFIISHP = my_line_chartFIISHP.getCTChart().getPlotArea();

			for ( CTLineSer ser : plotAreaFIISHP.getLineChartArray()[0].getSerArray() )
			{
				// Don't smoothen and don't put markers
				ser.setMarker(ctMarker);
				ser.setSmooth(ctBool);

			}

			colnum++;

			// -----Promoter SHP ----------------------------
			Cell dbcelllchartPromSHP = row.createCell(colnum);
			dbcelllchartPromSHP.setCellStyle(styleNoFill);

			// Create Drawing Object
			XSSFDrawing xlsx_drawingPromSHP = secSheet.createDrawingPatriarch();

			/* Define anchor points in the worksheet to position the chart */
			XSSFClientAnchor anchorPromSHP = xlsx_drawing.createAnchor(10 * Units.EMU_PER_PIXEL, 10 * Units.EMU_PER_PIXEL,
			          520 * Units.EMU_PER_PIXEL, 280 * Units.EMU_PER_PIXEL, colnum, row.getRowNum(), colnum, row.getRowNum());

			/* Create the chart object based on the anchor point */
			XSSFChart my_line_chartPromSHP = xlsx_drawingPromSHP.createChart(anchorPromSHP);

			/* Define chart AXIS */
			ChartAxis bottomAxis_chartPromSHP = my_line_chartPromSHP.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);

			ValueAxis leftAxis_chartPromSHP = my_line_chartPromSHP.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
			leftAxis_chartPromSHP.setCrosses(AxisCrosses.AUTO_ZERO);

			LineChartData data_chartPromSHP = my_line_chartPromSHP.getChartDataFactory().createLineChartData();

			ChartDataSource<String> xs_chartPromSHP = DataSources.fromStringCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumScripCode, colnumScripCode));
			ChartDataSource<Number> ys1_chartPromSHP = DataSources.fromNumericCellRange(sumSheet, new CellRangeAddress(
			          SectorXlsRowPositioner.getFromRow() - 1, SectorXlsRowPositioner.getToRow() - 1, colnumPromHold, colnumPromHold));

			/* Add chart data sources as data to the chart */
			LineChartSeries PromSHPSeries = data_chartPromSHP.addSeries(xs_chartPromSHP, ys1_chartPromSHP);

			CTChart ctChartPromSHP = my_line_chartPromSHP.getCTChart();
			CTTitle titlePromSHP = ctChartPromSHP.addNewTitle();
			titlePromSHP.addNewOverlay().setVal(false);
			CTTx txPromSHP = titlePromSHP.addNewTx();
			CTTextBody richPromSHP = txPromSHP.addNewRich();
			richPromSHP.addNewBodyPr(); // body properties must exist, but can be empty
			CTTextParagraph paraPromSHP = richPromSHP.addNewP();
			CTRegularTextRun rPromSHP = paraPromSHP.addNewR();
			rPromSHP.setT(constant_ChartTitles[17]);

			/* Plot the chart with the inputs from data and chart axis */
			my_line_chartPromSHP.plot(data_chartPromSHP, new ChartAxis[]
			{ bottomAxis_chartPromSHP, leftAxis_chartPromSHP
			});

			CTPlotArea plotAreaPromSHP = my_line_chartPromSHP.getCTChart().getPlotArea();

			for ( CTLineSer ser : plotAreaPromSHP.getLineChartArray()[0].getSerArray() )
			{
				// Don't smoothen and don't put markers
				ser.setMarker(ctMarker);
				ser.setSmooth(ctBool);

			}

			colnum++;

		}
	}

	/**
	 * ------------------------- Create Sector Overview MShare Comparison Sheet --------------------------------
	 * 
	 * @param secSheet
	 * @param wbCtxt
	 * @param scASrv
	 * @param secName
	 * @throws EX_General
	 *              -------------------------------------------------------------------------------------------------
	 */
	private void createSectoroVWSheet(XSSFSheet secoVWSheet, XSSFWorkbook wbCtxt, IScripAnalysisSrv scASrv, String secName, int numScrips)
	          throws EX_General
	{
		int rowNum = 0;
		TY_ColStats colStats = null;

		/**
		 * Get the Sector Specific ScRoot Attribute Containers and trigger mShareAnalytics Service
		 */
		if (mShareSrv != null && secoVWSheet != null)
		{
			ArrayList<TY_ScRoot_AttrContainers> secSpRootAttrContainers = new ArrayList<TY_ScRoot_AttrContainers>();

			secSpRootAttrContainers = scASrv.getScRootAttContainersList().stream().filter(x -> x.getScRoot().getscSector().equals(secName))
			          .collect(Collectors.toCollection(ArrayList::new));
			if (secSpRootAttrContainers != null)
			{
				TY_Sector_AttrContainers sectorAttrContainers = mShareSrv.generateMarketShareAnalytics(secSpRootAttrContainers);
				if (sectorAttrContainers != null)
				{
					colStats = getColStats(mShareSrv.getMinYear(), mShareSrv.getMaxYear(), true);
					/*
					 * Scan over Each Attribute that is relevant for Market share Comparion
					 */
					for ( TY_Attr_ScKeyFigsContainer attrSCKeyFigsContainer : sectorAttrContainers.getAttrSp_scKeyFigs() )
					{

						populateAttributeSpData(secoVWSheet, wbCtxt, scASrv, attrSCKeyFigsContainer, colStats, rowNum, mShareSrv);
						rowNum += 1 + 1 + numScrips + 1 + 1; // Starts with zero + Header + Years + Number of
						                                     // Scrips + Chart
					}
				}
			}

		}

		// Set Column Size
		for ( int c = 0; c < colStats.getNumCols(); c++ )
		{
			secoVWSheet.setColumnWidth(c, colStats.getWidthCol());
		}
	}

	/**
	 * ----------------------Get Column Stats in terms of Number of Columns needed and the width of Each Column
	 * 
	 * @param Min
	 *             - Min Number range
	 * @param Max
	 *             - Max Number Range
	 * @param colHead
	 *             - Column has header
	 * @return
	 *         ----------------------------------------------------------------------------------------------------
	 */
	private TY_ColStats getColStats(int Min, int Max, boolean colHead)
	{
		TY_ColStats colStats = new TY_ColStats();

		if (colHead)
		{
			colStats.setNumCols((Max - Min) + 1 + 1);
		}
		else
		{
			colStats.setNumCols((Max - Min) + 1);
		}

		colStats.setWidthCol(totalColSpread / colStats.getNumCols());

		return colStats;
	}

	/**
	 * ----------------------------- Populate Each Attribute Specific Data------------------------
	 * 
	 * @param secoVWSheet
	 * @param wbCtxt
	 * @param scASrv
	 * @param colStats
	 * @param RowNum
	 *             ------------------------------------------------------------------------------------------
	 */
	private void populateAttributeSpData(XSSFSheet secoVWSheet, XSSFWorkbook wbCtxt, IScripAnalysisSrv scASrv,
	          TY_Attr_ScKeyFigsContainer attrSCKeyFigsContainer, TY_ColStats colStats, int RowNum, IMShareScripAnalytics mShSrv)
	{
		/**
		 * Some Styles
		 */
		XSSFCellStyle styleHeader = XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleHeaderBlueUline, wbCtxt, CellType.STRING);
		styleHeader.setWrapText(true);
		styleHeader.setVerticalAlignment(VerticalAlignment.CENTER);

		XSSFCellStyle styleNoFill = XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleNoFill, wbCtxt, CellType.STRING);
		styleNoFill.setWrapText(true);
		styleNoFill.setVerticalAlignment(VerticalAlignment.CENTER);

		// Some Styles
		XSSFCellStyle styleYears = XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleTotalBlueGrayItalic, wbCtxt, CellType.STRING);
		styleYears.setWrapText(true);
		styleYears.setVerticalAlignment(VerticalAlignment.CENTER);

		XSSFCellStyle styleScCode = XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleKeyGold, wbCtxt, CellType.STRING);
		styleScCode.setWrapText(true);
		styleScCode.setVerticalAlignment(VerticalAlignment.CENTER);

		int MinYear = mShSrv.getMinYear();

		/**
		 * Create Header Row for Each Attribute First spanning colSpread
		 */
		Row row = secoVWSheet.createRow(RowNum);
		PrepareMergedTitlesforRow(row, attrSCKeyFigsContainer.getAttrName() + constant_RDS, colStats.getNumCols(), styleHeader, secoVWSheet);
		RowNum++;

		/**
		 * Create Title Row for Years
		 */
		row = secoVWSheet.createRow(RowNum);
		for ( int c = 0; c < colStats.getNumCols(); c++ )
		{
			Cell cell = row.createCell(c);
			if (c == 0)
			{
				cell.setCellType(CellType.STRING);
				cell.setCellValue(constant_Years);
				cell.setCellStyle(styleYears);

			}
			else
			{
				cell.setCellType(CellType.STRING);

				DataFormat fmt = wbCtxt.createDataFormat();
				styleYears.setDataFormat(fmt.getFormat("@"));
				cell.setCellStyle(styleYears);
				cell.setCellValue(MinYear);
				MinYear++;

			}
		}
		RowNum++;

		/**
		 * Create Content - GO ScripWise for years
		 */

		// For Each Scrip
		for ( String scCode : mShSrv.getScCodes() )
		{
			MinYear = mShSrv.getMinYear();
			row = secoVWSheet.createRow(RowNum); // Create a new Row
			for ( int c = 0; c < colStats.getNumCols(); c++ )
			{
				Cell cell = row.createCell(c);
				if (c == 0)
				{
					cell.setCellType(CellType.STRING);
					cell.setCellValue(scCode);
					cell.setCellStyle(styleScCode);

				}
				else
				{

					// Get figure for Key mapping current Min Year
					int cYear = MinYear;
					TY_scCodeKeyFigure scCodeKeyFig = attrSCKeyFigsContainer.getScCodeKeyFigures().stream().filter(x -> x.getKey() == cYear)
					          .filter(x -> x.getScCode().equals(scCode)).findFirst().get();
					if (scCodeKeyFig != null)
					{
						cell.setCellType(CellType.NUMERIC);
						cell.setCellValue(scCodeKeyFig.getFigure());
						cell.setCellStyle(styleNoFill);

					}
					MinYear++;
				}
			}
			RowNum++;

		}

		/*
		 * Create Chart for The Attribute Data
		 */
		// Prepare a new Row Merging al the Columns to enclose chart in a Single Bigger Cell
		row = secoVWSheet.createRow(RowNum);
		if (row != null && colStats.getNumCols() > 2)
		{

			for ( int c = 0; c < colStats.getNumCols(); c++ )
			{
				Cell cell = row.createCell(c);
				cell.setCellStyle(styleNoFill);

			}

			// Merge Rows for Columns
			CellRangeAddress cellRangeAddress = new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, colStats.getNumCols());
			secoVWSheet.addMergedRegion(cellRangeAddress);

			// Creates the cell
			Cell cell = CellUtil.createCell(row, 0, "");

			// Sets the allignment to the created cell
			CellUtil.setVerticalAlignment(cell, VerticalAlignment.CENTER);

			/**
			 * ----------------------- Plotting Starts for Attribute here ------------------------------
			 */
			// We will Plot within this cell Now
			row.setHeight((short) 4500);

			CTMarker ctMarker = CTMarker.Factory.newInstance();
			ctMarker.setSymbol(CTMarkerStyle.Factory.newInstance());
			CTBoolean ctBool = CTBoolean.Factory.newInstance();
			ctBool.setVal(false);

			// Create Drawing Object
			XSSFDrawing xlsx_drawing = secoVWSheet.createDrawingPatriarch();
			/* Define anchor points in the worksheet to position the chart */
			XSSFClientAnchor anchor = xlsx_drawing.createAnchor(10 * Units.EMU_PER_PIXEL, 10 * Units.EMU_PER_PIXEL, 1500 * Units.EMU_PER_PIXEL,
			          280 * Units.EMU_PER_PIXEL, 0, row.getRowNum(), colStats.getNumCols() - 1, row.getRowNum());

			/* Create the chart object based on the anchor point */
			XSSFChart my_line_chart = xlsx_drawing.createChart(anchor);

			/* Define legends for the line chart and set the position of the legend */

			XSSFChartLegend legend = my_line_chart.getOrCreateLegend();
			legend.setPosition(LegendPosition.BOTTOM);
			/* Create data for the chart */
			/* Define chart AXIS */
			ChartAxis bottomAxis = my_line_chart.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);
			// bottomAxis.setVisible(false);

			ValueAxis leftAxis = my_line_chart.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
			leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

			LineChartData data = my_line_chart.getChartDataFactory().createLineChartData();

			int yrRowNum = row.getRowNum() - mShSrv.getScCodes().size() - 1; // Chart Row - Num Scrips - Zero
			                                                                 // Start
			// X- Axis Chart Data Source
			/*
			 * Cell Range Address is defined as First row, last row, first
			 * column, last column
			 */
			ChartDataSource<String> xs = DataSources.fromStringCellRange(secoVWSheet,
			          new CellRangeAddress(yrRowNum, yrRowNum, 1, colStats.getNumCols() - 1));

			/**
			 * A Series will be added to chart for Each Scrip
			 */
			int x = 0;
			for ( int dataSeriesRow = yrRowNum + 1; dataSeriesRow <= yrRowNum + mShSrv.getScCodes().size(); dataSeriesRow++ )
			{

				ChartDataSource<Number> ys = DataSources.fromNumericCellRange(secoVWSheet,
				          new CellRangeAddress(dataSeriesRow, dataSeriesRow, 1, colStats.getNumCols() - 1));
				/* Add chart data sources as data to the chart */
				LineChartSeries compSeries = data.addSeries(xs, ys);
				compSeries.setTitle(mShSrv.getScCodes().get(x));
				x++;
			}

			/* Plot the chart with the inputs from data and chart axis */
			my_line_chart.plot(data, new ChartAxis[]
			{ bottomAxis, leftAxis
			});

			CTPlotArea plotArea = my_line_chart.getCTChart().getPlotArea();

			for ( CTLineSer ser : plotArea.getLineChartArray()[0].getSerArray() )
			{
				// Don't smoothen and don't put markers
				ser.setMarker(ctMarker);
				ser.setSmooth(ctBool);

			}

		}
		RowNum++;

	}

}
