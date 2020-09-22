package scriptsengine.statistics.interfaces;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import scriptsengine.statistics.definitions.StDev;
import scriptsengine.statistics.definitions.StDevResult;
import scriptsengine.statistics.exceptions.EX_StdDeviation;

public interface IStDev
{
	// Initialize from Excel file path
	public void initializefromXlsx(String filepath) throws EX_StdDeviation, FileNotFoundException, IOException;

	// Initialize from Work book Instance and Sheet Name - To be used when a single sheet within a workbook is to be
	// used - closing the work book will be handled by calling program

	public void initializefromWB(XSSFWorkbook wb, String sheetName) throws IOException, EX_StdDeviation;

	public void initializefromSheet(XSSFSheet sheet) throws EX_StdDeviation;

	// Initialize from code
	public void initialize();

	// Get Deviation Handle
	public StDev getDeviation();

	// Validate the deviation
	public void validate() throws EX_StdDeviation;

	// Process the calculations
	public void process();

	// Return the Summary
	public ArrayList<StDevResult> getResults();

	// Return Detailed Results
	public StDev getDetailedResults();

}
