package scriptsengine.uploadengine.validations.interfaces;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import modelframework.exceptions.EX_InvalidObjectException;
import modelframework.exceptions.EX_InvalidRelationException;
import modelframework.exceptions.EX_NotRootException;
import modelframework.exceptions.EX_NullParamsException;
import modelframework.exceptions.EX_ParamCountMismatchException;
import modelframework.exceptions.EX_ParamInitializationException;
import scriptsengine.pojos.OB_Scrip_General;
import scriptsengine.uploadengine.exceptions.EX_ScripCodeNotFoundException;
import scriptsengine.uploadengine.exceptions.EX_ScripCodeSheetNotFoundException;
import scriptsengine.uploadengine.exceptions.EX_ScripMetadata;

/**
 * 
 * Interfce to check existence of Scrip
 */
public interface IScripExists
{
	// Determine Scrip existence by Workbook Context Instance
	public boolean ISScripExisting(XSSFWorkbook wbContext, String filepath) throws EX_ScripCodeSheetNotFoundException, EX_ScripCodeNotFoundException,
	          EX_ScripMetadata, EX_InvalidObjectException, EX_NotRootException, SQLException, IllegalAccessException, IllegalArgumentException,
	          InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException, EX_NullParamsException,
	          EX_ParamCountMismatchException, EX_ParamInitializationException, EX_InvalidRelationException;

	// Determine Scrip existence by Scrip Code
	public boolean ISScripExisting(String scripCode) throws EX_InvalidObjectException, EX_NotRootException, SQLException, IllegalAccessException,
	          IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException,
	          EX_NullParamsException, EX_ParamCountMismatchException, EX_ParamInitializationException, EX_InvalidRelationException;

	// Get Scrip Root Entity by Workbook Context Instance
	public OB_Scrip_General getScripRootfromWB(XSSFWorkbook wbContext, String filepath) throws EX_ScripCodeSheetNotFoundException,
	          EX_ScripCodeNotFoundException, EX_ScripMetadata, EX_InvalidObjectException, EX_NotRootException, SQLException, IllegalAccessException,
	          IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException,
	          EX_NullParamsException, EX_ParamCountMismatchException, EX_ParamInitializationException, EX_InvalidRelationException;

	// Get Scrip Root Entity by Scrip Code
	public OB_Scrip_General getScripRootbyCode(String scripCode)
	          throws EX_InvalidObjectException, EX_NotRootException, SQLException, IllegalAccessException, IllegalArgumentException,
	          InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException, EX_NullParamsException,
	          EX_ParamCountMismatchException, EX_ParamInitializationException, EX_InvalidRelationException;

	// Get Scrip Root Entity by Scrip Code
	public OB_Scrip_General getScripRootbyDescStartingwith(String scDescPattern)
	          throws EX_InvalidObjectException, EX_NotRootException, SQLException, IllegalAccessException, IllegalArgumentException,
	          InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException, EX_NullParamsException,
	          EX_ParamCountMismatchException, EX_ParamInitializationException, EX_InvalidRelationException;

	// Get Scrip Code from Work book context
	public String getScripCodefromWB(XSSFWorkbook wbContext, String filepath)
	          throws EX_ScripCodeSheetNotFoundException, EX_ScripCodeNotFoundException, EX_ScripMetadata;

}
