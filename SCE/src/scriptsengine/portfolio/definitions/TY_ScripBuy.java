package scriptsengine.portfolio.definitions;

import java.util.ArrayList;

/**
 * 
 * Placeholder Container for Scrip Buy
 */
public class TY_ScripBuy
{
	private String					dematAC;
	private String					scDesc;
	private String					scCode;
	private ArrayList<TY_QtyPriceDate>	buyItems;

	/**
	 * @return the dematAC
	 */
	public String getDematAC()
	{
		return dematAC;
	}

	/**
	 * @param dematAC
	 *             the dematAC to set
	 */
	public void setDematAC(String dematAC)
	{
		this.dematAC = dematAC;
	}

	/**
	 * @return the scDesc
	 */
	public String getScDesc()
	{
		return scDesc;
	}

	/**
	 * @param scDesc
	 *             the scDesc to set
	 */
	public void setScDesc(String scDesc)
	{
		this.scDesc = scDesc;
	}

	/**
	 * @return the scCode
	 */
	public String getScCode()
	{
		return scCode;
	}

	/**
	 * @param scCode
	 *             the scCode to set
	 */
	public void setScCode(String scCode)
	{
		this.scCode = scCode;
	}

	/**
	 * @return the buyItems
	 */
	public ArrayList<TY_QtyPriceDate> getBuyItems()
	{
		return buyItems;
	}

	/**
	 * @param buyItems
	 *             the buyItems to set
	 */
	public void setBuyItems(ArrayList<TY_QtyPriceDate> buyItems)
	{
		this.buyItems = buyItems;
	}

	/**
	 * 
	 */
	public TY_ScripBuy()
	{
		super();
		this.buyItems = new ArrayList<TY_QtyPriceDate>();
	}

	/**
	 * @param dematAC
	 * @param scDesc
	 * @param scCode
	 * @param buyItems
	 */
	public TY_ScripBuy(String dematAC, String scDesc, String scCode, ArrayList<TY_QtyPriceDate> buyItems)
	{
		super();
		this.dematAC = dematAC;
		this.scDesc = scDesc;
		this.scCode = scCode;
		this.buyItems = buyItems;
	}

}
