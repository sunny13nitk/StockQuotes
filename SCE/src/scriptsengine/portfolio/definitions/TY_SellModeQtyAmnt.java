package scriptsengine.portfolio.definitions;

import scriptsengine.enums.SCEenums;
import scriptsengine.enums.SCEenums.scripSellMode;

/**
 * 
 * POJO for Scrip Sell Helper that contains system determined Sell Mode applicable, Total Current Sell Amount and Total
 * Sell Qty
 * alongwith Current Realization
 */
public class TY_SellModeQtyAmnt
{
	private String					scCode;
	private boolean				freeSharesEligible;

	private double					totalCurrSellAmnt;

	private int					totalSellQty;

	private double					currRealization;

	private double					sppu;

	private int					numFreeShares;

	private SCEenums.scripSellMode	autoSellMode;

	/**
	 * @return the numFreeShares
	 */
	public int getNumFreeShares()
	{
		return numFreeShares;
	}

	/**
	 * @param numFreeShares
	 *             the numFreeShares to set
	 */
	public void setNumFreeShares(int numFreeShares)
	{
		this.numFreeShares = numFreeShares;
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
	 * @return the sppu
	 */
	public double getSppu()
	{
		return sppu;
	}

	/**
	 * @param sppu
	 *             the sppu to set
	 */
	public void setSppu(double sppu)
	{
		this.sppu = sppu;
	}

	/**
	 * @return the autoSellMode
	 */
	public SCEenums.scripSellMode getAutoSellMode()
	{
		return autoSellMode;
	}

	/**
	 * @param autoSellMode
	 *             the autoSellMode to set
	 */
	public void setAutoSellMode(SCEenums.scripSellMode autoSellMode)
	{
		this.autoSellMode = autoSellMode;
	}

	/**
	 * @return the freeSharesEligible
	 */
	public boolean isFreeSharesEligible()
	{
		return freeSharesEligible;
	}

	/**
	 * @param freeSharesEligible
	 *             the freeSharesEligible to set
	 */
	public void setFreeSharesEligible(boolean freeSharesEligible)
	{
		this.freeSharesEligible = freeSharesEligible;
	}

	/**
	 * @return the totalCurrSellAmnt
	 */
	public double getTotalCurrSellAmnt()
	{
		return totalCurrSellAmnt;
	}

	/**
	 * @param totalCurrSellAmnt
	 *             the totalCurrSellAmnt to set
	 */
	public void setTotalCurrSellAmnt(double totalCurrSellAmnt)
	{
		this.totalCurrSellAmnt = totalCurrSellAmnt;
	}

	/**
	 * @return the totalSellQty
	 */
	public int getTotalSellQty()
	{
		return totalSellQty;
	}

	/**
	 * @param totalSellQty
	 *             the totalSellQty to set
	 */
	public void setTotalSellQty(int totalSellQty)
	{
		this.totalSellQty = totalSellQty;
	}

	/**
	 * @return the currRealization
	 */
	public double getCurrRealization()
	{
		return currRealization;
	}

	/**
	 * @param currRealization
	 *             the currRealization to set
	 */
	public void setCurrRealization(double currRealization)
	{
		this.currRealization = currRealization;
	}

	/**
	 * @param scCode
	 * @param freeSharesEligible
	 * @param totalCurrSellAmnt
	 * @param totalSellQty
	 * @param currRealization
	 * @param sppu
	 * @param numFreeShares
	 * @param autoSellMode
	 */
	public TY_SellModeQtyAmnt(String scCode, boolean freeSharesEligible, double totalCurrSellAmnt, int totalSellQty, double currRealization,
	          double sppu, int numFreeShares, scripSellMode autoSellMode)
	{
		super();
		this.scCode = scCode;
		this.freeSharesEligible = freeSharesEligible;
		this.totalCurrSellAmnt = totalCurrSellAmnt;
		this.totalSellQty = totalSellQty;
		this.currRealization = currRealization;
		this.sppu = sppu;
		this.numFreeShares = numFreeShares;
		this.autoSellMode = autoSellMode;
	}

	/**
	 * Default Mode is Profit and Loss
	 */
	public TY_SellModeQtyAmnt()
	{
		super();
		this.autoSellMode = scripSellMode.PandL;
		// TODO Auto-generated constructor stub
	}

}
