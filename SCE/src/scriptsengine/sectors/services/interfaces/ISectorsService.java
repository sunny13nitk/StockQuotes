package scriptsengine.sectors.services.interfaces;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;

import modelframework.exceptions.EX_InvalidObjectException;
import modelframework.exceptions.EX_InvalidRelationException;
import modelframework.exceptions.EX_NotRootException;
import modelframework.exceptions.EX_NullParamsException;
import modelframework.exceptions.EX_ParamCountMismatchException;
import modelframework.exceptions.EX_ParamInitializationException;
import scriptsengine.pojos.OB_Scrip_Sector;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * Interface for Sectors Service
 *
 */
public interface ISectorsService
{
	/**
	 * Identify if a Sector Exists in DB
	 * 
	 * @param secCode
	 *             - Sector Code
	 * @return - true if Sector Exists in DB
	 * @throws SQLException
	 * @throws EX_NotRootException
	 * @throws EX_InvalidObjectException
	 * @throws EX_InvalidRelationException
	 * @throws EX_ParamInitializationException
	 * @throws EX_ParamCountMismatchException
	 * @throws EX_NullParamsException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public boolean sectorExists(String secCode) throws EX_InvalidObjectException, EX_NotRootException, SQLException, IllegalAccessException,
	          IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException,
	          EX_NullParamsException, EX_ParamCountMismatchException, EX_ParamInitializationException, EX_InvalidRelationException;

	/**
	 * Get Sector Entity by Sector Code
	 * 
	 * @param secCode
	 *             - Sector Name
	 * @return - Sector Entity
	 * @throws EX_InvalidRelationException
	 * @throws EX_ParamInitializationException
	 * @throws EX_ParamCountMismatchException
	 * @throws EX_NullParamsException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 * @throws EX_NotRootException
	 * @throws EX_InvalidObjectException
	 */
	public OB_Scrip_Sector getSectorbyCode(String secCode)
	          throws EX_InvalidObjectException, EX_NotRootException, SQLException, IllegalAccessException, IllegalArgumentException,
	          InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException, EX_NullParamsException,
	          EX_ParamCountMismatchException, EX_ParamInitializationException, EX_InvalidRelationException;

	/**
	 * Create Sector in the DB only by Sector Code
	 * 
	 * @param secCode
	 *             - Sector Code for Sector to be created
	 * @return - true if sector created succesfully
	 * @throws EX_General
	 */
	public boolean createSectorwithexistenceCheck(String secCode) throws EX_General;

	/**
	 * Create Sector by Sector Code and Avg PE for the Sector
	 * 
	 * @param secCode
	 *             - Sector Code
	 * @param avgPE
	 *             - Average PE
	 * @return
	 * @throws EX_General
	 */
	public boolean createSector(String secCode, double avgPE) throws EX_General;

	/**
	 * Create/Update Sector(s) from Wb at Specified Path
	 * 
	 * @param filepath
	 *             - Excel file Path
	 * @throws EX_General
	 * @throws SQLException
	 * @throws EX_NotRootException
	 * @throws EX_InvalidObjectException
	 */
	public void createUpdateSectors(String filepath) throws EX_General, EX_InvalidObjectException, EX_NotRootException, SQLException;

	/**
	 * Download Sectors information at specified filepath
	 * 
	 * @param dlfilepath
	 *             - D/L excel file path
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void downloadSectors(String dlfilepath) throws FileNotFoundException, IOException;

	/**
	 * Create Sector by Scrip Code without checking for Existence of the Scrip
	 * 
	 * @param secCode
	 *             - Scrip to be Created - Not chekced for Existence
	 * @return - true if created
	 * @throws EX_General
	 */
	public boolean createSector(String secCode) throws EX_General;

	public ArrayList<OB_Scrip_Sector> getAllSectors() throws EX_InvalidObjectException, EX_NotRootException, SQLException, EX_General;

}
