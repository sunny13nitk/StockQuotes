package scriptsengine.portfolio.definitions;

import java.util.ArrayList;

import scriptsengine.enums.SCEenums;
import scriptsengine.enums.SCEenums.scripSellMode;

/**
 * 
 * POJO for scrip selling
 *
 */
public class TY_ScripSell
{

	private String					scDesc;
	private String					scCode;
	private SCEenums.scripSellMode	sellMode;
	private ArrayList<TY_QtyPriceDate>	sellItems;

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
	 * @return the sellMode
	 */
	public SCEenums.scripSellMode getSellMode()
	{
		return sellMode;
	}

	/**
	 * @param sellMode
	 *             the sellMode to set
	 */
	public void setSellMode(SCEenums.scripSellMode sellMode)
	{
		this.sellMode = sellMode;
	}

	/**
	 * @return the sellItems
	 */
	public ArrayList<TY_QtyPriceDate> getSellItems()
	{
		return sellItems;
	}

	/**
	 * @param sellItems
	 *             the sellItems to set
	 */
	public void setSellItems(ArrayList<TY_QtyPriceDate> buyItems)
	{
		this.sellItems = buyItems;
	}

	/**
	 * Default Constructor - Initialize Sell mode to Normal P&L
	 */
	public TY_ScripSell()
	{
		super();
		this.sellMode = scripSellMode.PandL;
		this.sellItems = new ArrayList<TY_QtyPriceDate>();
	}

	/**
	 * @param scDesc
	 * @param scCode
	 * @param sellMode
	 * @param sellItems
	 */
	public TY_ScripSell(String scDesc, String scCode, scripSellMode sellMode, ArrayList<TY_QtyPriceDate> sellItems)
	{
		super();
		this.scDesc = scDesc;
		this.scCode = scCode;
		this.sellMode = sellMode;
		this.sellItems = sellItems;
	}

}
