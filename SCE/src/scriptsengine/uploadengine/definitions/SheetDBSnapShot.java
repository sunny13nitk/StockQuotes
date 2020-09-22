package scriptsengine.uploadengine.definitions;

import java.util.ArrayList;

import modelframework.types.TY_NameValue;

/**
 * Base Class for Sheet Specific Scrip DB Snapshot
 *
 */
public class SheetDBSnapShot
{
	private String					sheetName;
	private boolean				dataMaintained;
	private ArrayList<TY_NameValue>	keyVals;

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
	 * @return the dataMaintained
	 */
	public boolean isDataMaintained()
	{
		return dataMaintained;
	}

	/**
	 * @param dataMaintained
	 *             the dataMaintained to set
	 */
	public void setDataMaintained(boolean dataMaintained)
	{
		this.dataMaintained = dataMaintained;
	}

	/**
	 * @return the keyVals
	 */
	public ArrayList<TY_NameValue> getKeyVals()
	{
		return keyVals;
	}

	/**
	 * @param keyVals
	 *             the keyVals to set
	 */
	public void setKeyVals(ArrayList<TY_NameValue> keyVals)
	{
		this.keyVals = keyVals;
	}

	/**
	 * @param sheetName
	 * @param dataMaintained
	 * @param keyVals
	 */
	public SheetDBSnapShot(String sheetName, boolean dataMaintained, ArrayList<TY_NameValue> keyVals)
	{
		super();
		this.sheetName = sheetName;
		this.dataMaintained = dataMaintained;
		this.keyVals = keyVals;
	}

	/**
	 * 
	 */
	public SheetDBSnapShot()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
