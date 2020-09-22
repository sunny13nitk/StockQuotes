package scriptsengine.simulations.sell.definitions;

import java.util.ArrayList;
import java.util.Date;

/**
 * 
 * Scrip Sell Quotation
 */
public class TY_Sell_Quote
{
	private String					scCode;
	private String					scDesc;
	private Date					sellDate;

	private ArrayList<TY_Qty_Price>	sellItems;

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
	 * @return the sellDate
	 */
	public Date getSellDate()
	{
		return sellDate;
	}

	/**
	 * @param sellDate
	 *             the sellDate to set
	 */
	public void setSellDate(Date sellDate)
	{
		this.sellDate = sellDate;
	}

	/**
	 * @return the sellItems
	 */
	public ArrayList<TY_Qty_Price> getSellItems()
	{
		return sellItems;
	}

	/**
	 * @param sellItems
	 *             the sellItems to set
	 */
	public void setSellItems(ArrayList<TY_Qty_Price> sellItems)
	{
		this.sellItems = sellItems;
	}

	/**
	 * 
	 */
	public TY_Sell_Quote()
	{
		super();
		this.sellItems = new ArrayList<TY_Qty_Price>();
	}

	/**
	 * @param scCode
	 * @param scDesc
	 * @param sellDate
	 * @param sellItems
	 */
	public TY_Sell_Quote(String scCode, String scDesc, Date sellDate, ArrayList<TY_Qty_Price> sellItems)
	{
		super();
		this.scCode = scCode;
		this.scDesc = scDesc;
		this.sellDate = sellDate;
		this.sellItems = new ArrayList<TY_Qty_Price>();
	}

}
