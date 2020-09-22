package scriptsengine.reportsengine.implementations;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import modelframework.implementations.GeneralMessage;
import modelframework.implementations.MessagesFormatter;
import scriptsengine.enums.SCEenums.direction;
import scriptsengine.pojos.OB_Scrip_QuartersData;
import scriptsengine.pricingengine.reports.definitions.TY_DefaultPriceForecast;
import scriptsengine.pricingengine.services.interfaces.ISA_ScripPriceProjectionService;
import scriptsengine.reportsengine.interfaces.IXLSReportGenerator;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.utilities.implementations.DeltaCalcService;
import scriptsengine.utilities.implementations.XLSCellStyleGetter;

/**
 * 
 * Prices Forecaster XLS Generation Service
 */
@Service("PriceForecastXlSSrv")
public class PriceForecastXlSSrv implements IXLSReportGenerator
{

	@Autowired
	private MessagesFormatter	msgFormatter;

	@Autowired
	private DeltaCalcService		deltaSrv;

	private final String[]		pfItemHeaders	=
	{ "Year", "Price (Rs.)", "Nett. Profit (Rs. Cr.)", "EPS", "Face Value", "EUV - EPS Unit Value = EPS/Face Value",
	          "ENPR = (EUV/Nett Profit) * 100", "FVR", "Eff. EPS = EPS/FVR", "Price/Eff. EPS", "Adj. PE"
	};

	private final String[]		peHeaders		=
	{ "Min'm PE", "Average PE", "Max'm PE"
	};

	private final String[]		qHeaders		=
	{ "Year", "Quarter", "Profit"
	};

	private final String[]		calcHeaders	=
	{ "NPD - Nett. Profit Delta(%)", "CYPP (Rs. Cr.) = Last Year's Nett profit (1+NPD/100)", "CYPEUV = (Avg ENPR  * CYPP)/100"
	};

	private final String[]		priceHeaders	=
	{ "Price Min. PE (CYPEUV*CFV*Min.PE*FVR)", "Price Avg. PE (CYPEUV*CFV*Avg.PE*FVR)", "Price Max. PE (CYPEUV*CFV*Max.PE*FVR)",
	          "PE Adjusted Prices (Rs.)"
	};

	/**
	 * -------------------------- XLS Generation Methods---------------------------
	 */
	/**
	 * Implementation for XLS Report Generation
	 */
	@Override
	public void triggerXLSReportGeneration(String filePath, Object srvObj) throws EX_General
	{
		ISA_ScripPriceProjectionService ppSrv = null;
		XSSFWorkbook wbCtxt = null;
		if (srvObj instanceof ISA_ScripPriceProjectionService)
		{
			ppSrv = (ISA_ScripPriceProjectionService) srvObj;
			if (ppSrv != null)
			{
				String fileName = ppSrv.getScCode();
				performBasicChecks(ppSrv);
				// Write the workbook in file system
				FileOutputStream outStream;
				try
				{
					outStream = new FileOutputStream(new File(filePath + fileName + ".xlsx"));
					if (outStream != null)
					{
						wbCtxt = new XSSFWorkbook();
						createSheet(wbCtxt, ppSrv);
					}

					// finally write the workbook context to physical file
					wbCtxt.write(outStream);
					outStream.close();
					// Write Success Message in Log
					GeneralMessage msgGen = new GeneralMessage("PP_XLS_SUCC", new Object[]
					{ fileName, filePath
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
		ISA_ScripPriceProjectionService ppSrv = null;
		if (wbctxt != null && srvObj != null)
		{
			if (srvObj instanceof ISA_ScripPriceProjectionService)
			{
				ppSrv = (ISA_ScripPriceProjectionService) srvObj;
				if (ppSrv != null)
				{
					performBasicChecks(ppSrv);

					createSheet(wbctxt, ppSrv);

				}
			}
		}

	}

	@Override
	public void performBasicChecks(Object srvObj) throws EX_General
	{
		ISA_ScripPriceProjectionService ppSrv = null;

		if (srvObj instanceof ISA_ScripPriceProjectionService)
		{
			ppSrv = (ISA_ScripPriceProjectionService) srvObj;
		}

		if (ppSrv.getDefPstats() == null)
		{
			EX_General egen = new EX_General("ERRPPSTATS_DEF", new Object[]
			{ ppSrv.getScCode()
			});
			msgFormatter.generate_message_snippet(egen);
			throw egen;
		}
		else
		{
			if (ppSrv.getDefPstats().getPfItems() == null)
			{
				EX_General egen = new EX_General("ERRPPSTATS_NODEF", new Object[]
				{ ppSrv.getScCode()
				});
				msgFormatter.generate_message_snippet(egen);
				throw egen;
			}
			else
			{
				if (ppSrv.getDefPstats().getPfItems().size() == 0)
				{
					EX_General egen = new EX_General("ERRPPSTATS_NODEF", new Object[]
					{ ppSrv.getScCode()
					});
					msgFormatter.generate_message_snippet(egen);
					throw egen;
				}
			}
		}

	}

	/**
	 * -------------------------- XLS Generation Methods---------------------------
	 */

	/**
	 * -------------------------- Private Helper Methods---------------------------
	 * 
	 * @throws EX_General
	 */

	private void createSheet(XSSFWorkbook wbCtxt, ISA_ScripPriceProjectionService ppSrv) throws EX_General
	{
		XSSFSheet sheet = wbCtxt.createSheet(ppSrv.getScCode());
		if (sheet != null)
		{

			// Create Header Row
			Row row = sheet.createRow(0);
			if (row != null)
			{
				int c = 0, i = 1;
				XSSFCellStyle styleRet = XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleHeaderBlueUline, wbCtxt, CellType.STRING);
				for ( String header : this.pfItemHeaders )
				{
					Cell cell = row.createCell(c);
					sheet.setColumnWidth(c, 5200);
					cell.setCellValue(header);

					styleRet.setWrapText(true);
					cell.setCellStyle(styleRet);
					c++;

				}
			}

			// PF Items Data Rendering
			int rowItemidx = 1;
			for ( TY_DefaultPriceForecast pfItem : ppSrv.getDefPstats().getPfItems() )
			{

				Row rowItem = sheet.createRow(rowItemidx);
				if (rowItem != null)
				{
					int c = 0;
					while (c <= 10)
					{
						Cell cell = rowItem.createCell(c);
						switch (c)
						{

						case 0:
							cell.setCellType(CellType.STRING);
							cell.setCellValue(pfItem.getYear());
							cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleNoFill, wbCtxt, CellType._NONE));
							c++;
							break;
						case 1:
							cell.setCellType(CellType.NUMERIC);
							cell.setCellValue(pfItem.getPrice());
							cell.setCellStyle(
							          XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleNoFill, wbCtxt, CellType.NUMERIC));
							c++;
							break;
						case 2:
							cell.setCellType(CellType.NUMERIC);
							cell.setCellValue(pfItem.getNettProfit());
							cell.setCellStyle(
							          XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleNoFill, wbCtxt, CellType.NUMERIC));
							c++;
							break;
						case 3:
							cell.setCellType(CellType.NUMERIC);
							cell.setCellValue(pfItem.getEPS());
							cell.setCellStyle(
							          XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleNoFill, wbCtxt, CellType.NUMERIC));
							c++;
							break;
						case 4:
							cell.setCellType(CellType.NUMERIC);
							cell.setCellValue(pfItem.getFaceValue());
							cell.setCellStyle(
							          XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleNoFill, wbCtxt, CellType.NUMERIC));
							c++;
							break;
						case 5:
							cell.setCellType(CellType.NUMERIC);
							cell.setCellValue(pfItem.getEUV());
							cell.setCellStyle(
							          XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleNoFill, wbCtxt, CellType.NUMERIC));
							c++;
							break;
						case 6:
							cell.setCellType(CellType.NUMERIC);
							cell.setCellValue(pfItem.getENPR());
							cell.setCellStyle(
							          XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleNoFill, wbCtxt, CellType.NUMERIC));
							c++;
							break;
						case 7:
							cell.setCellType(CellType.NUMERIC);
							cell.setCellValue(pfItem.getLineFVR());
							cell.setCellStyle(
							          XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleNoFill, wbCtxt, CellType.NUMERIC));
							c++;
							break;
						case 8:
							cell.setCellType(CellType.NUMERIC);
							cell.setCellValue(pfItem.getEffEPS());
							cell.setCellStyle(
							          XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleNoFill, wbCtxt, CellType.NUMERIC));
							c++;
							break;
						case 9:
							cell.setCellType(CellType.NUMERIC);
							cell.setCellValue(pfItem.getEffPE());
							cell.setCellStyle(
							          XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleNoFill, wbCtxt, CellType.NUMERIC));
							c++;
							break;

						case 10:
							cell.setCellType(CellType.NUMERIC);
							cell.setCellValue(pfItem.getAdjPE());
							cell.setCellStyle(
							          XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleNoFill, wbCtxt, CellType.NUMERIC));
							c++;
							rowItemidx++;
							break;

						default:
							break;
						}

					}
				}
			}

			// Averages
			row = sheet.createRow(rowItemidx);

			int c = 0;

			while (c <= 10)
			{
				Cell cell = row.createCell(c);
				switch (c)
				{

				case 0:
					cell.setCellType(CellType.STRING);
					cell.setCellValue("Averages");
					cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleGraySolid, wbCtxt, CellType.STRING));
					c++;
					break;
				case 1:
					// cell.setCellType(CellType._NONE);
					cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleBlankBorderBlack, wbCtxt, CellType._NONE));
					c++;
					break;
				case 2:
					// cell.setCellType(CellType._NONE);
					cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleBlankBorderBlack, wbCtxt, CellType._NONE));
					c++;
					break;
				case 3:
					// cell.setCellType(CellType._NONE);
					cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleBlankBorderBlack, wbCtxt, CellType._NONE));
					c++;
					break;
				case 4:
					// cell.setCellType(CellType._NONE);
					cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleBlankBorderBlack, wbCtxt, CellType._NONE));
					c++;
					break;
				case 5:
					// cell.setCellType(CellType._NONE);
					cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleBlankBorderBlack, wbCtxt, CellType._NONE));
					c++;
					break;
				case 6:
					cell.setCellType(CellType.NUMERIC);
					cell.setCellValue(ppSrv.getAvgENPR());
					cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleTotalBlue, wbCtxt, CellType.NUMERIC));
					c++;
					break;
				case 7:
					// cell.setCellType(CellType._NONE);
					cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleBlankBorderBlack, wbCtxt, CellType._NONE));
					c++;
					break;
				case 8:
					// cell.setCellType(CellType._NONE);
					cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleBlankBorderBlack, wbCtxt, CellType._NONE));
					c++;
					break;
				case 9:
					cell.setCellType(CellType.NUMERIC);
					cell.setCellValue(ppSrv.getAvgPE().getAvgPE_unadjusted());
					cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleTotalBlue, wbCtxt, CellType.NUMERIC));
					c++;
					break;

				case 10:
					cell.setCellType(CellType.NUMERIC);
					cell.setCellValue(ppSrv.getAvgPE().getAvgPE());
					cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleTotalBlue, wbCtxt, CellType.NUMERIC));
					c++;
					rowItemidx++;
					break;

				default:
					break;
				}
			}

			// ---------------------PE Row ------------------------------

			rowItemidx += 2;
			// Headers
			row = sheet.createRow(rowItemidx);
			c = 0;
			if (row != null)
			{

				for ( String header : this.peHeaders )
				{
					Cell cell = row.createCell(c);
					sheet.setColumnWidth(c, 5200);
					cell.setCellValue(header);
					XSSFCellStyle styleRet = XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleHeaderBlueUline, wbCtxt,
					          CellType.STRING);
					styleRet.setWrapText(true);
					cell.setCellStyle(styleRet);
					c++;

				}
			}

			// Current Face Value after 2 columns
			Cell cell = row.createCell(c + 2);
			sheet.setColumnWidth(c, 5200);
			cell.setCellValue("Current Face Value (CFV)");
			XSSFCellStyle styleRet = XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleHeaderBlueUline, wbCtxt, CellType.STRING);
			styleRet.setWrapText(true);
			cell.setCellStyle(styleRet);

			// PE adjusted Percentage
			cell = row.createCell(c + 5);
			sheet.setColumnWidth(c, 5200);
			cell.setCellValue("Adjusted PE - Percentage");
			styleRet = XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleHeaderBlueUline, wbCtxt, CellType.STRING);
			styleRet.setWrapText(true);
			cell.setCellStyle(styleRet);

			++rowItemidx;

			// PE Data
			if (ppSrv.getAvgPE() != null)
			{
				row = sheet.createRow(rowItemidx);

				c = 0;

				while (c < 3)
				{
					cell = row.createCell(c);
					switch (c)
					{
					case 0:
						cell.setCellType(CellType.NUMERIC);
						cell.setCellValue(ppSrv.getAvgPE().getMinPE());
						cell.setCellStyle(
						          XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleBlankBorderBlack, wbCtxt, CellType.NUMERIC));
						c++;
						break;
					case 1:
						cell.setCellType(CellType.NUMERIC);
						cell.setCellValue(ppSrv.getAvgPE().getAvgPE());
						cell.setCellStyle(
						          XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleBlankBorderBlack, wbCtxt, CellType.NUMERIC));
						c++;
						break;
					case 2:
						cell.setCellType(CellType.NUMERIC);
						cell.setCellValue(ppSrv.getAvgPE().getMaxPE());
						cell.setCellStyle(
						          XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleBlankBorderBlack, wbCtxt, CellType.NUMERIC));
						c++;
						break;

					default:
						break;
					}
				}
			}

			// Current Face Value after 2 columns
			cell = row.createCell(c + 2);
			cell.setCellType(CellType.NUMERIC);
			cell.setCellValue(ppSrv.getLastNp_FVR().getCurrFV());
			styleRet = XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleBlankBorderBlack, wbCtxt, CellType.NUMERIC);
			styleRet.setWrapText(true);
			cell.setCellStyle(styleRet);

			// PE Adjusted Percentage
			cell = row.createCell(c + 5);
			cell.setCellType(CellType.NUMERIC);
			cell.setCellValue(ppSrv.getAvgPE().getPeadj_percentage());
			styleRet = XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleBlankBorderBlack, wbCtxt, CellType.NUMERIC);
			styleRet.setWrapText(true);
			cell.setCellStyle(styleRet);

			// ---------------------- Quarterly comparisons ------------------------------------------------
			rowItemidx += 4;
			row = sheet.createRow(rowItemidx);
			c = 0;

			styleRet = XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleGraySolid, wbCtxt, CellType.STRING);
			styleRet.setWrapText(true);

			cell = row.createCell(c);
			cell.setCellType(CellType.STRING);
			cell.setCellValue("Current Year Performace");
			cell.setCellStyle(styleRet);

			cell = row.createCell(c + 5);
			cell.setCellType(CellType.STRING);
			cell.setCellValue("Penultimate Year Performace");
			cell.setCellStyle(styleRet);

			// QHeaders

			// Current and Penultimate Year QHeaders
			row = sheet.createRow(++rowItemidx);
			if (row != null)
			{
				c = 0;
				for ( String header : this.qHeaders )
				{
					cell = row.createCell(c);
					sheet.setColumnWidth(c, 5200);
					cell.setCellValue(header);
					styleRet = XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleHeaderBlueUline, wbCtxt, CellType.STRING);
					styleRet.setWrapText(true);
					cell.setCellStyle(styleRet);
					c++;

				}

				c += 2;
				for ( String header : this.qHeaders )
				{
					cell = row.createCell(c);
					sheet.setColumnWidth(c, 5200);
					cell.setCellValue(header);
					styleRet = XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleHeaderBlueUline, wbCtxt, CellType.STRING);
					styleRet.setWrapText(true);
					cell.setCellStyle(styleRet);
					c++;

				}
			}

			// Qdata

			++rowItemidx;
			// current Q Data

			for ( OB_Scrip_QuartersData qtrData : ppSrv.getDefPstats().getCurrentYdata() )
			{

				Row rowItem = sheet.createRow(rowItemidx);
				if (rowItem != null)
				{
					c = 0;
					while (c <= 2)
					{
						cell = rowItem.createCell(c);
						switch (c)
						{

						case 0:
							cell.setCellType(CellType.STRING);
							cell.setCellValue(qtrData.getYear());
							cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleNoFill, wbCtxt, CellType._NONE));
							c++;
							break;
						case 1:
							cell.setCellType(CellType.STRING);
							cell.setCellValue(qtrData.getQuarter());
							cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleNoFill, wbCtxt, CellType._NONE));
							c++;
							break;
						case 2:
							cell.setCellType(CellType.NUMERIC);
							cell.setCellValue(qtrData.getNettProfit());
							cell.setCellStyle(
							          XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleNoFill, wbCtxt, CellType.NUMERIC));
							c++;
							rowItemidx++;
							break;

						default:
							break;
						}

					}
				}
			}

			// penultimate Q Data
			rowItemidx = rowItemidx - (ppSrv.getDefPstats().getCurrentYdata().size());

			for ( OB_Scrip_QuartersData qtrData : ppSrv.getDefPstats().getPenultimateYdata() )
			{

				Row rowItem = sheet.getRow(rowItemidx);
				if (rowItem != null)
				{
					c = 5;
					while (c <= 7)
					{
						cell = rowItem.createCell(c);
						switch (c)
						{

						case 5:
							cell.setCellType(CellType.STRING);
							cell.setCellValue(qtrData.getYear());
							cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleNoFill, wbCtxt, CellType._NONE));
							c++;
							break;
						case 6:
							cell.setCellType(CellType.STRING);
							cell.setCellValue(qtrData.getQuarter());
							cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleNoFill, wbCtxt, CellType._NONE));
							c++;
							break;
						case 7:
							cell.setCellType(CellType.NUMERIC);
							cell.setCellValue(qtrData.getNettProfit());
							cell.setCellStyle(
							          XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleNoFill, wbCtxt, CellType.NUMERIC));
							c++;
							rowItemidx++;
							break;

						default:
							break;
						}

					}
				}
			}

			// Sum of Profits
			row = sheet.createRow(rowItemidx);
			c = 2;

			cell = row.createCell(c);
			cell.setCellType(CellType.NUMERIC);
			cell.setCellValue(ppSrv.getNpd().getNettProfit_Current());
			cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleTotalBlue, wbCtxt, CellType.NUMERIC));

			c += 5;
			cell = row.createCell(c);
			cell.setCellType(CellType.NUMERIC);
			cell.setCellValue(ppSrv.getNpd().getNettProfit_Penultimate());
			cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleTotalBlue, wbCtxt, CellType.NUMERIC));

			rowItemidx += 3;

			// Computations
			row = sheet.createRow(rowItemidx);
			if (row != null)
			{
				c = 0;
				for ( String header : this.calcHeaders )
				{
					cell = row.createCell(c);
					sheet.setColumnWidth(c, 5200);
					cell.setCellValue(header);
					styleRet = XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleHeaderBlueUline, wbCtxt, CellType.STRING);
					styleRet.setWrapText(true);
					cell.setCellStyle(styleRet);
					c++;

				}
			}
			++rowItemidx;
			row = sheet.createRow(rowItemidx);
			if (row != null)
			{
				c = 0;
				cell = row.createCell(c);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(ppSrv.getNpd().getNPD());
				cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleTotalBlue, wbCtxt, CellType.NUMERIC));

				c++;
				cell = row.createCell(c);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(ppSrv.getDefPstats().getPricesReturn().getCYPP());
				cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleTotalBlue, wbCtxt, CellType.NUMERIC));

				c++;
				cell = row.createCell(c);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(ppSrv.getDefPstats().getPricesReturn().getCYPEPUS());
				cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleTotalBlue, wbCtxt, CellType.NUMERIC));
			}

			// Final Prices
			--rowItemidx;
			row = sheet.getRow(rowItemidx);

			// Create Header Row
			if (row != null)
			{
				c = 5;
				for ( String header : this.priceHeaders )
				{
					cell = row.createCell(c);
					sheet.setColumnWidth(c, 5200);
					cell.setCellValue(header);
					styleRet = XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleHeaderBlueUline, wbCtxt, CellType.STRING);
					styleRet.setWrapText(true);
					cell.setCellStyle(styleRet);
					c++;

				}
			}
			++rowItemidx;
			row = sheet.getRow(rowItemidx);
			if (row != null)
			{
				c = 5;
				cell = row.createCell(c);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(ppSrv.getDefPstats().getPricesReturn().getProjectedPrices().getMinPE_PP());
				cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleGood, wbCtxt, CellType.NUMERIC));

				c++;
				cell = row.createCell(c);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(ppSrv.getDefPstats().getPricesReturn().getProjectedPrices().getAvgPE_PP());
				cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleNormal, wbCtxt, CellType.NUMERIC));

				c++;
				cell = row.createCell(c);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(ppSrv.getDefPstats().getPricesReturn().getProjectedPrices().getMaxPE_PP());
				cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleBad, wbCtxt, CellType.NUMERIC));

				c++;
				cell = row.createCell(c);
				cell.setCellType(CellType.STRING);
				cell.setCellValue("TRUE");
				cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleBlankBorderBlack, wbCtxt, CellType.NUMERIC));

			}

			/**
			 * Only if PE is adjusted for Exorbitant Increase in consecutive years
			 */
			if (ppSrv.getAvgPE().getPeadj_percentage() > 0.1) // 0.1 to avoid mathematical error due to rounding off
			                                                  // if any
			{
				++rowItemidx;
				row = sheet.createRow(rowItemidx);
				if (row != null)
				{
					c = 5;
					cell = row.createCell(c);
					cell.setCellType(CellType.NUMERIC);
					cell.setCellValue(
					          deltaSrv.adjustNumberbyPercentage(ppSrv.getDefPstats().getPricesReturn().getProjectedPrices().getMinPE_PP(),
					                    ppSrv.getAvgPE().getPeadj_percentage(), direction.INCREASE));
					cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleGood, wbCtxt, CellType.NUMERIC));

					c++;
					cell = row.createCell(c);
					cell.setCellType(CellType.NUMERIC);
					cell.setCellValue(
					          deltaSrv.adjustNumberbyPercentage(ppSrv.getDefPstats().getPricesReturn().getProjectedPrices().getAvgPE_PP(),
					                    ppSrv.getAvgPE().getPeadj_percentage(), direction.INCREASE));
					cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleNormal, wbCtxt, CellType.NUMERIC));

					c++;
					cell = row.createCell(c);
					cell.setCellType(CellType.NUMERIC);
					cell.setCellValue(
					          deltaSrv.adjustNumberbyPercentage(ppSrv.getDefPstats().getPricesReturn().getProjectedPrices().getMaxPE_PP(),
					                    ppSrv.getAvgPE().getPeadj_percentage(), direction.INCREASE));
					cell.setCellStyle(XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleBad, wbCtxt, CellType.NUMERIC));

					c++;
					cell = row.createCell(c);
					cell.setCellType(CellType.STRING);
					cell.setCellValue("FALSE");
					cell.setCellStyle(
					          XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleBlankBorderBlack, wbCtxt, CellType.NUMERIC));

				}

			}

			// ------------------- Raw Materials ------------------------------------------
			if (ppSrv.getRawMstats() != null)
			{
				if (ppSrv.getRawMstats().size() > 0)
				{
					++rowItemidx;
					// Raw Materials Stats
					row = sheet.createRow(rowItemidx);
					c = 0;

					styleRet = XLSCellStyleGetter.getCellStyleforCode(XLSCellStyleGetter.styleGraySolid, wbCtxt, CellType.STRING);
					styleRet.setWrapText(true);

					cell = row.createCell(c);
					cell.setCellType(CellType.STRING);
					cell.setCellValue("Raw Material Statistics");
					cell.setCellStyle(styleRet);
				}
			}

		}

	}

	/**
	 * -------------------------- Private Helper Methods---------------------------
	 */

}
