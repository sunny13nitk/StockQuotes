package scriptsengine.uploadengine.validations.implementations;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
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
import modelframework.implementations.MessagesFormatter;
import modelframework.interfaces.IQueryService;
import modelframework.managers.QueryManager;
import modelframework.types.TY_NameValue;
import scriptsengine.pojos.OB_Scrip_General;
import scriptsengine.uploadengine.exceptions.EX_ScripCodeNotFoundException;
import scriptsengine.uploadengine.exceptions.EX_ScripCodeSheetNotFoundException;
import scriptsengine.uploadengine.exceptions.EX_ScripMetadata;
import scriptsengine.uploadengine.services.implementations.ScripSheetMetadataService;
import scriptsengine.uploadengine.validations.interfaces.IScripExists;

@Service("ScripExistsService")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ScripExistsService implements IScripExists
{

	@Autowired
	private ScripSheetMetadataService	scMdtSrv;

	@Autowired
	private MessagesFormatter		msgFormatter;

	private final String			descField	= "scName";

	@Override
	public boolean ISScripExisting(XSSFWorkbook wbContext, String filepath) throws EX_ScripCodeSheetNotFoundException, EX_ScripCodeNotFoundException,
	          EX_ScripMetadata, EX_InvalidObjectException, EX_NotRootException, SQLException, IllegalAccessException, IllegalArgumentException,
	          InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException, EX_NullParamsException,
	          EX_ParamCountMismatchException, EX_ParamInitializationException, EX_InvalidRelationException
	{
		boolean scrip_exists = false;

		String scCode = this.getScripCodefromWB(wbContext, filepath);
		if (scCode != null)
		{
			IQueryService sq = (IQueryService) QueryManager
			          .getQuerybyRootObjname(scMdtSrv.getSheetMdtbySheetName(scMdtSrv.getBaseSheetName()).getBobjName());
			if (sq != null)
			{

				ArrayList<TY_NameValue> params = new ArrayList<TY_NameValue>();

				params.add(new TY_NameValue(scMdtSrv.getBaseSheetKeyField(), scCode));
				String condn = scMdtSrv.getBaseSheetKeyField() + "  = ? ";

				ArrayList<OB_Scrip_General> scrips = sq.executeQuery(condn, params);
				if (scrips.size() > 0)
				{
					scrip_exists = true;
				}
			}
		}

		return scrip_exists;
	}

	@Override
	public boolean ISScripExisting(String scripCode) throws EX_InvalidObjectException, EX_NotRootException, SQLException, IllegalAccessException,
	          IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException,
	          EX_NullParamsException, EX_ParamCountMismatchException, EX_ParamInitializationException, EX_InvalidRelationException
	{
		boolean scrip_exists = false;

		String scCode = scripCode;
		if (scCode != null)
		{
			IQueryService sq = (IQueryService) QueryManager
			          .getQuerybyRootObjname(scMdtSrv.getSheetMdtbySheetName(scMdtSrv.getBaseSheetName()).getBobjName());
			if (sq != null)
			{

				ArrayList<TY_NameValue> params = new ArrayList<TY_NameValue>();

				params.add(new TY_NameValue(scMdtSrv.getBaseSheetKeyObjField(), scCode));
				String condn = " " + scMdtSrv.getBaseSheetKeyObjField() + "  = ? ";

				ArrayList<OB_Scrip_General> scrips = sq.executeQuery(condn, params);
				if (scrips.size() > 0)
				{
					scrip_exists = true;
				}
			}
		}

		return scrip_exists;
	}

	/**
	 * Get Scrip code from the Work Book Sheet "General"
	 * 
	 * @throws EX_ScripMetadata
	 */
	@SuppressWarnings("deprecation")
	@Override
	public String getScripCodefromWB(XSSFWorkbook wbContext, String filepath)
	          throws EX_ScripCodeSheetNotFoundException, EX_ScripCodeNotFoundException, EX_ScripMetadata
	{
		String cellStrValue = null;
		String ScripCode = null;
		boolean keyFound = false;
		if (wbContext != null && scMdtSrv != null)
		{
			// Get Base(General) Sheet from Workbook

			XSSFSheet generalSheet = wbContext.getSheet(scMdtSrv.getBaseSheetName());
			if (generalSheet != null)
			{
				/**
				 * Get Key field in base sheet - Scrip Code
				 */
				String keyField = scMdtSrv.getBaseSheetKeyField();
				if (keyField != null)
				{
					// Get iterator to all the rows in current sheet
					Iterator<Row> rowIterator = generalSheet.iterator();
					while (rowIterator.hasNext() && keyFound != true)
					{
						Row row = rowIterator.next();
						// For each row, iterate through each columns
						Iterator<Cell> cellIterator = row.cellIterator();
						while (cellIterator.hasNext() && keyFound != true)
						{
							Cell cell = cellIterator.next();
							switch (cell.getCellType())
							{
							case Cell.CELL_TYPE_STRING:
								cellStrValue = cell.getStringCellValue();
								if (cellStrValue.equals(keyField))
								{
									// Scrip Code Text description Found in cell

									// Read subsequent cell for Scrip Code
									Cell cell_code = cellIterator.next();
									ScripCode = cell_code.getStringCellValue();
									keyFound = true; // No Further Sheet Traversing

								}

								break;
							}
						}
					}
				}

				else
				{
					// throw Exception - Sheet Metadata missing Base Sheet Key field Name
					EX_ScripMetadata exMdt = new EX_ScripMetadata(new Object[]
					{ filepath
					});
					msgFormatter.generate_message_snippet(exMdt);
					throw exMdt;

				}

			}
			else
			{
				// throw Exception
				EX_ScripCodeSheetNotFoundException exnoSheet = new EX_ScripCodeSheetNotFoundException(new Object[]
				{ filepath
				});
				msgFormatter.generate_message_snippet(exnoSheet);
				throw exnoSheet;

			}

		}

		// TODO Auto-generated method stub
		return ScripCode;
	}

	@Override
	public OB_Scrip_General getScripRootfromWB(XSSFWorkbook wbContext, String filepath) throws EX_ScripCodeSheetNotFoundException,
	          EX_ScripCodeNotFoundException, EX_ScripMetadata, EX_InvalidObjectException, EX_NotRootException, SQLException, IllegalAccessException,
	          IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException,
	          EX_NullParamsException, EX_ParamCountMismatchException, EX_ParamInitializationException, EX_InvalidRelationException
	{
		OB_Scrip_General scripRoot = null;

		String scCode = this.getScripCodefromWB(wbContext, filepath);
		if (scCode != null)
		{
			IQueryService sq = (IQueryService) QueryManager
			          .getQuerybyRootObjname(scMdtSrv.getSheetMdtbySheetName(scMdtSrv.getBaseSheetName()).getBobjName());
			if (sq != null)
			{

				ArrayList<TY_NameValue> params = new ArrayList<TY_NameValue>();

				params.add(new TY_NameValue(scMdtSrv.getBaseSheetKeyObjField(), scCode));
				String condn = " " + scMdtSrv.getBaseSheetKeyObjField() + "  = ? ";

				ArrayList<OB_Scrip_General> scrips = sq.executeQuery(condn, params);
				if (scrips.size() > 0)
				{
					scripRoot = scrips.get(0);
				}
			}
		}

		return scripRoot;
	}

	@Override
	public OB_Scrip_General getScripRootbyCode(String scripCode) throws EX_InvalidObjectException, EX_NotRootException, SQLException,
	          IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException,
	          SecurityException, EX_NullParamsException, EX_ParamCountMismatchException, EX_ParamInitializationException, EX_InvalidRelationException
	{
		OB_Scrip_General scripRoot = null;

		String scCode = scripCode;
		if (scCode != null)
		{
			IQueryService sq = (IQueryService) QueryManager
			          .getQuerybyRootObjname(scMdtSrv.getSheetMdtbySheetName(scMdtSrv.getBaseSheetName()).getBobjName());
			if (sq != null)
			{

				ArrayList<TY_NameValue> params = new ArrayList<TY_NameValue>();

				params.add(new TY_NameValue(scMdtSrv.getBaseSheetKeyObjField(), scCode));
				String condn = " " + scMdtSrv.getBaseSheetKeyObjField() + "  = ? ";

				ArrayList<OB_Scrip_General> scrips = sq.executeQuery(condn, params);
				if (scrips.size() > 0)
				{
					scripRoot = scrips.get(0);
				}
			}
		}

		return scripRoot;
	}

	@Override
	public OB_Scrip_General getScripRootbyDescStartingwith(String scDescPattern) throws EX_InvalidObjectException, EX_NotRootException, SQLException,
	          IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException,
	          SecurityException, EX_NullParamsException, EX_ParamCountMismatchException, EX_ParamInitializationException, EX_InvalidRelationException
	{
		OB_Scrip_General scripRoot = null;
		if (scDescPattern != null)
		{
			IQueryService sq = (IQueryService) QueryManager
			          .getQuerybyRootObjname(scMdtSrv.getSheetMdtbySheetName(scMdtSrv.getBaseSheetName()).getBobjName());
			if (sq != null)
			{

				ArrayList<TY_NameValue> params = new ArrayList<TY_NameValue>();
				String pattern = scDescPattern + "%";

				params.add(new TY_NameValue(descField, pattern));
				String condn = " " + descField + " LIKE  ? ";

				ArrayList<OB_Scrip_General> scrips = sq.executeQuery(condn, params);
				if (scrips.size() > 0)
				{
					scripRoot = scrips.get(0);
				}
			}

		}

		return scripRoot;
	}

}
