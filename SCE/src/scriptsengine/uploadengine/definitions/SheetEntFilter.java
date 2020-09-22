package scriptsengine.uploadengine.definitions;

import java.util.ArrayList;

import modelframework.types.TY_Filter;

/**
 * Sheet Entity Filter
 * Holds the Sheet name and the Filter object generated from Sheet Entities for the Sheet
 */
public class SheetEntFilter
{
	private String				sheetName;

	private String				relName;

	private TY_Filter			filter;

	private ArrayList<String>	keys;

	/**
	 * @return the keys
	 */
	public ArrayList<String> getKeys()
	{
		return keys;
	}

	/**
	 * @param keys
	 *             the keys to set
	 */
	public void setKeys(ArrayList<String> keys)
	{
		this.keys = keys;
	}

	/**
	 * @return the relName
	 */
	public String getRelName()
	{
		return relName;
	}

	/**
	 * @param relName
	 *             the relName to set
	 */
	public void setRelName(String relName)
	{
		this.relName = relName;
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
	 * @return the filter
	 */
	public TY_Filter getFilter()
	{
		return filter;
	}

	/**
	 * @param filter
	 *             the filter to set
	 */
	public void setFilter(TY_Filter filter)
	{
		this.filter = filter;
	}

	/**
	 * @param sheetName
	 * @param relName
	 * @param filter
	 * @param keys
	 */
	public SheetEntFilter(String sheetName, String relName, TY_Filter filter, ArrayList<String> keys)
	{
		super();
		this.sheetName = sheetName;
		this.relName = relName;
		this.filter = filter;
		this.keys = keys;
	}

	/**
	 * 
	 */
	public SheetEntFilter()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
