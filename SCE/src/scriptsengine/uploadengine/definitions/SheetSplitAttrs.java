package scriptsengine.uploadengine.definitions;

import java.util.ArrayList;

/**
 * 
 * Base definition for Sheet & Split aware attributes
 */
public class SheetSplitAttrs
{

	private String				sheetName;

	private String				relationName;

	private ArrayList<String>	splitAttrs;

	/**
	 * @return the relationName
	 */
	public String getRelationName()
	{
		return relationName;
	}

	/**
	 * @param relationName
	 *             the relationName to set
	 */
	public void setRelationName(String relationName)
	{
		this.relationName = relationName;
	}

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
	 * @return the splitAttrs
	 */
	public ArrayList<String> getSplitAttrs()
	{
		return splitAttrs;
	}

	/**
	 * @param splitAttrs
	 *             the splitAttrs to set
	 */
	public void setSplitAttrs(ArrayList<String> splitAttrs)
	{
		this.splitAttrs = splitAttrs;
	}

	public SheetSplitAttrs()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param sheetName
	 * @param relationName
	 * @param splitAttrs
	 */
	public SheetSplitAttrs(String sheetName, String relationName, ArrayList<String> splitAttrs)
	{
		super();
		this.sheetName = sheetName;
		this.relationName = relationName;
		this.splitAttrs = splitAttrs;
	}

}
