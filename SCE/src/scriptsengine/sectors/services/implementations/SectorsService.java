package scriptsengine.sectors.services.implementations;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
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
import modelframework.interfaces.IQueryService;
import modelframework.managers.ModelObjectFactory;
import modelframework.managers.QueryManager;
import modelframework.types.TY_NameValue;
import scriptsengine.enums.SCEenums;
import scriptsengine.enums.SCEenums.ModeOperation;
import scriptsengine.pojos.OB_Scrip_Sector;
import scriptsengine.sectors.services.interfaces.ISectorsService;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.services.implementations.WBFilepathService;
import scriptsengine.uploadengine.validations.implementations.FilepathValidationService;

/**
 * Sectors Service Bean
 *
 */
@Service("SectorsService")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SectorsService implements ISectorsService
{

	private final String			secObjName	= "OB_Scrip_Sector";

	private final String			wbName		= "SectorsList.xlsx";

	private final String			sheetName		= "Sectors";

	@Autowired
	private MessagesFormatter		msgFormatter;

	@Autowired
	private FilepathValidationService	fpvSrv;

	@Autowired
	private WBFilepathService		wbSrv;

	@Override
	public boolean sectorExists(String secCode) throws EX_InvalidObjectException, EX_NotRootException, SQLException, IllegalAccessException,
	          IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException,
	          EX_NullParamsException, EX_ParamCountMismatchException, EX_ParamInitializationException, EX_InvalidRelationException
	{
		// convert Sector Code to Upper Case
		if (secCode != null)
		{
			String scCodeUp = secCode.toUpperCase();
			// Fire the Query to check if sector Exists
			IQueryService qs_sec = (IQueryService) QueryManager.getQuerybyRootObjname(secObjName);
			if (qs_sec != null)
			{
				ArrayList<TY_NameValue> params = new ArrayList<TY_NameValue>();
				if (params != null)
				{
					params.add(new TY_NameValue("Sector", scCodeUp));
					String condn = "Sector = ?";
					ArrayList<OB_Scrip_Sector> Sectors = qs_sec.executeQuery(condn, params);
					if (Sectors != null)
					{
						if (Sectors.size() > 0)
							return true;
					}
				}
			}

		}

		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createSector(String secCode) throws EX_General
	{
		if (secCode != null)
		{
			// Check if Sector Already exists
			try
			{
				OB_Scrip_Sector sectorEnt = ModelObjectFactory.createObjectbyName(secObjName);
				if (sectorEnt != null)
				{
					sectorEnt.setSector(secCode);
					if (sectorEnt.Save())
						return true;
				}

			}
			catch (IllegalArgumentException | SecurityException e)
			{
				EX_General egen = new EX_General("ERRSECCREATE", new Object[]
				{ secCode, e.getMessage()
				});
				msgFormatter.generate_message_snippet(egen);
				throw egen;
			}
		}

		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createSector(String secCode, double avgPE) throws EX_General
	{
		if (secCode != null)
		{
			// Check if Sector Already exists
			try
			{
				OB_Scrip_Sector sectorEnt = ModelObjectFactory.createObjectbyName(secObjName);
				if (sectorEnt != null)
				{
					sectorEnt.setSector(secCode);
					sectorEnt.setAvgPE(avgPE);
					if (sectorEnt.Save())
						return true;
				}

			}
			catch (IllegalArgumentException | SecurityException e)
			{
				EX_General egen = new EX_General("ERRSECCREATE", new Object[]
				{ secCode, e.getMessage()
				});
				msgFormatter.generate_message_snippet(egen);
				throw egen;
			}
		}

		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createUpdateSectors(String filepath) throws EX_General, EX_InvalidObjectException, EX_NotRootException, SQLException
	{
		XSSFWorkbook wbctxt = null;
		double decimalVal = 0;
		String strVal = null;
		int rowidx = 0;
		int modeUpdCount = 0;
		int modeCrCount = 0;
		SCEenums.ModeOperation mode;

		ArrayList<OB_Scrip_Sector> sectors = new ArrayList<OB_Scrip_Sector>();
		if (filepath != null)
		{
			if (fpvSrv != null)
			{
				try
				{
					if (fpvSrv.validateFilePath(filepath))
					{
						if (wbSrv != null)
						{
							wbctxt = wbSrv.getWBcontextfromFilepath(filepath);
							if (wbctxt != null)
							{
								XSSFSheet sheetSec = wbctxt.getSheetAt(0);
								if (sheetSec != null)
								{
									/**
									 * Get all existing Sectors for Comparison
									 */
									sectors = this.getAllSectors();
									// Get iterator to all the rows in current sheet
									Iterator<Row> rowIterator = sheetSec.iterator();
									while (rowIterator.hasNext())
									{
										Row row = rowIterator.next();
										// From 2nd row Onwards
										if (rowidx >= 1)
										{

											OB_Scrip_Sector newsec = new OB_Scrip_Sector();
											// For each row, iterate through each columns
											Iterator<Cell> cellIterator = row.cellIterator();
											while (cellIterator.hasNext())
											{
												Cell cell = cellIterator.next();

												switch (cell.getCellType())
												{
												// Column Name - Description
												case Cell.CELL_TYPE_STRING:
													strVal = cell.getStringCellValue().toUpperCase();
													newsec.setSector(strVal);
													break;

												case Cell.CELL_TYPE_NUMERIC:
													decimalVal = cell.getNumericCellValue();
													newsec.setAvgPE(decimalVal);
													break;
												}

											}
											mode = createUpdateSector(sectors, newsec);
											if (mode == ModeOperation.CREATE)
											{
												modeCrCount++;
											}
											else if (mode == ModeOperation.UPDATE)
											{
												modeUpdCount++;
											}

										}
										rowidx++;
									}
								}
								// Publish Status to Log file
								GeneralMessage msg = new GeneralMessage("SECUPDSTAT", new Object[]
								{ filepath, modeCrCount, modeUpdCount
								});
								msgFormatter.generate_message_snippet(msg);

							}

						}
					}
				}
				catch (IOException e)
				{
					EX_General egen = new EX_General("FILENOTFOUND", new Object[]
					{ filepath
					});
					msgFormatter.generate_message_snippet(egen);
					throw egen;
				}
			}
		}

	}

	private SCEenums.ModeOperation createUpdateSector(ArrayList<OB_Scrip_Sector> sectors, OB_Scrip_Sector newSector)
	{
		SCEenums.ModeOperation mode = SCEenums.ModeOperation.NONE;
		// 1. Determine is Sector Exists
		try
		{
			if (this.sectorExists(newSector.getSector()))
			{
				// Sector Exists
				// Get Corresponding Sector Entity from DB
				OB_Scrip_Sector secSel = sectors.stream().filter(x -> x.getSector().equals(newSector.getSector().toUpperCase())).findFirst()
				          .get();
				if (secSel != null)
				{
					// Compare AvG PE of the Sector from DB with one maintained in Sheet
					if (secSel.getAvgPE() == newSector.getAvgPE())
					{
						// Do Nothing - DB Entity and Sheet Entry same
					}
					else
					{
						// Switch to Change Mode and edit the PE and Save the entity
						secSel.lock();
						secSel.switchtoChangeMode();
						secSel.setAvgPE(newSector.getAvgPE());
						secSel.Save();
						mode = ModeOperation.UPDATE;
					}
				}
			}
			else
			{
				// Sector Does Not Exists
				// Create a new Sector and Save
				try
				{
					this.createSector(newSector.getSector(), newSector.getAvgPE());
					mode = ModeOperation.CREATE;
				}
				catch (EX_General e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException | NoSuchMethodException
		          | SecurityException | EX_InvalidObjectException | EX_NotRootException | SQLException | EX_NullParamsException
		          | EX_ParamCountMismatchException | EX_ParamInitializationException | EX_InvalidRelationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mode;
	}

	@Override
	public void downloadSectors(String dlfilepath) throws IOException
	{
		if (dlfilepath != null)
		{
			ArrayList<OB_Scrip_Sector> sectors = new ArrayList<OB_Scrip_Sector>();
			// Get all Sectors Info
			IQueryService qs_sec;
			try
			{
				qs_sec = (IQueryService) QueryManager.getQuerybyRootObjname(secObjName);
				if (qs_sec != null)
				{
					try
					{
						sectors = qs_sec.executeQuery();
						if (sectors != null)
						{
							if (sectors.size() > 0)
							{
								// some Sectors Exist - Download in excel
								// Create workbook
								XSSFWorkbook workbook = new XSSFWorkbook();
								XSSFSheet sheet = workbook.createSheet(this.sheetName);

								XSSFCellStyle styleKey = workbook.createCellStyle();
								styleKey.setBorderBottom(CellStyle.BORDER_THICK);
								styleKey.setBottomBorderColor(IndexedColors.BLUE.getIndex());
								styleKey.setBorderLeft(CellStyle.BORDER_THICK);
								styleKey.setLeftBorderColor(IndexedColors.GREEN.getIndex());

								// Create Header Row
								Row rowHead = sheet.createRow(0);
								Cell cell1 = rowHead.createCell(0);
								cell1.setCellValue("Sector");
								cell1.setCellStyle(styleKey);
								Cell cell2 = rowHead.createCell(1);
								cell2.setCellValue("Avg. PE");
								cell2.setCellStyle(styleKey);

								int rowidx = 1;

								for ( OB_Scrip_Sector sector : sectors )
								{
									Row row = sheet.createRow(rowidx++);
									if (row != null)
									{
										int i = 0;
										Cell cell = row.createCell(i);
										if (sector.getSector() != null)
										{
											cell.setCellValue(sector.getSector());
										}
										i++;
										Cell cellpe = row.createCell(i);

										cellpe.setCellValue(sector.getAvgPE());

									}
								}

								sheet.autoSizeColumn(0);
								sheet.autoSizeColumn(1);

								String filename = dlfilepath + "/" + this.wbName;
								// Write the workbook in file system
								FileOutputStream out;

								out = new FileOutputStream(new File(filename));
								workbook.write(out);
								out.close();

							}
						}

					}
					catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException
					          | NoSuchMethodException | SecurityException | EX_NullParamsException | EX_ParamCountMismatchException
					          | EX_ParamInitializationException | EX_InvalidRelationException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			catch (EX_InvalidObjectException | EX_NotRootException | SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	@Override
	public OB_Scrip_Sector getSectorbyCode(String secCode) throws EX_InvalidObjectException, EX_NotRootException, SQLException,
	          IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException,
	          SecurityException, EX_NullParamsException, EX_ParamCountMismatchException, EX_ParamInitializationException, EX_InvalidRelationException
	{

		OB_Scrip_Sector sector = null;
		// convert Sector Code to Upper Case
		if (secCode != null)
		{
			String scCodeUp = secCode.toUpperCase();
			// Fire the Query to check if sector Exists
			IQueryService qs_sec = (IQueryService) QueryManager.getQuerybyRootObjname(secObjName);
			if (qs_sec != null)
			{
				ArrayList<TY_NameValue> params = new ArrayList<TY_NameValue>();
				if (params != null)
				{
					params.add(new TY_NameValue("Sector", scCodeUp));
					String condn = "Sector = ?";
					ArrayList<OB_Scrip_Sector> Sectors = qs_sec.executeQuery(condn, params);
					if (Sectors != null)
					{
						if (Sectors.size() > 0)
						{
							sector = Sectors.get(0);
						}
					}
				}
			}

		}

		// TODO Auto-generated method stub
		return sector;
	}

	@Override
	public boolean createSectorwithexistenceCheck(String secCode) throws EX_General
	{
		if (secCode != null)
		{
			// Check if Sector Already exists
			try
			{
				if (!this.sectorExists(secCode))
				{

					OB_Scrip_Sector sectorEnt = ModelObjectFactory.createObjectbyName(secObjName);
					if (sectorEnt != null)
					{
						// Always Create Sector in Upper Case
						sectorEnt.setSector(secCode.toUpperCase());
						if (sectorEnt.Save())
							return true;
					}
				}
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException | NoSuchMethodException
			          | SecurityException | EX_InvalidObjectException | EX_NotRootException | SQLException | EX_NullParamsException
			          | EX_ParamCountMismatchException | EX_ParamInitializationException | EX_InvalidRelationException e)
			{
				EX_General egen = new EX_General("ERRSECCREATE", new Object[]
				{ secCode, e.getMessage()
				});
				msgFormatter.generate_message_snippet(egen);
				throw egen;
			}
		}

		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<OB_Scrip_Sector> getAllSectors() throws EX_InvalidObjectException, EX_NotRootException, SQLException, EX_General
	{
		ArrayList<OB_Scrip_Sector> sectors = null;

		IQueryService qs_sec = (IQueryService) QueryManager.getQuerybyRootObjname(secObjName);
		if (qs_sec != null)
		{

			try
			{
				sectors = qs_sec.executeQuery();
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException | NoSuchMethodException
			          | SecurityException | EX_NullParamsException | EX_ParamCountMismatchException | EX_ParamInitializationException
			          | EX_InvalidRelationException e)
			{
				EX_General egen = new EX_General("ERRGETSECTORS", new Object[]
				{ e.getMessage()
				});
				msgFormatter.generate_message_snippet(egen);
				throw egen;
			}
		}

		return sectors;
	}

}
