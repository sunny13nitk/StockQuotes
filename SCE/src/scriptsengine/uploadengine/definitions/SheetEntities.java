package scriptsengine.uploadengine.definitions;

import java.util.ArrayList;

/**
 * Sheet Entities Class
 * Holds the sheet Name and the Object pojo Entities contained in the sheet
 * 
 * @param <T>
 */
public class SheetEntities<T>
{
	private String			sheetName;
	private ArrayList<T>	sheetEntityList;

	/**
	 * @return the sheetName
	 */
	public String getSheetName()
	{
		return sheetName;
	}

	/**
	 * @param sheetName
	 *             the sheetName to set
	 */
	public void setSheetName(String sheetName)
	{
		this.sheetName = sheetName;
	}

	/**
	 * @return the sheetEntityList
	 */
	public ArrayList<T> getSheetEntityList()
	{
		return sheetEntityList;
	}

	/**
	 * @param sheetEntityList
	 *             the sheetEntityList to set
	 */
	public void setSheetEntityList(ArrayList<T> sheetEntityList)
	{
		this.sheetEntityList = sheetEntityList;
	}

	/**
	 * @param sheetName
	 * @param sheetEntityList
	 */
	public SheetEntities(String sheetName, ArrayList<T> sheetEntityList)
	{
		super();
		this.sheetName = sheetName;
		this.sheetEntityList = sheetEntityList;
	}

	/**
	 * 
	 */
	public SheetEntities()
	{
		this.sheetEntityList = new ArrayList<T>();
	}

}
