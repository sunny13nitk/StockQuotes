package scriptsengine.customizing.types;

/**
 * 
 * POJO for Scrip Sell Base Configuration Bean - The Context specific Bean would be initialized in scebeans.xml
 */
public class TY_ScripSellBaseConfig
{
	private int		numDays_LTCG;			// No. of Days to hold purchase for Long Term Capital Gain Exemption

	private double		capitalGainsTaxRate;	// Capital Gains Tax Rate

	private double		oppCostperAnnum;		// Opportunity Cost Per Annum Percentage - Rate Pecentage to
	                                             // Accomodate per
	                                             // Annum Opportunity Cost

	private int		numDays_oppCost;		// Number of Days for considering a unit quantum of Opprtunity Cost

	private double		oppCostTDS;			// TDS for Opprotunity Cost that is to be payed upfront - e.g. FD TDS

	private boolean	reInvestTaxOppCost;		// REinvest Tax and Opportunity Costs in case of computing FRee Shares

	/**
	 * @return the reInvestTaxOppCost
	 */
	public boolean isReInvestTaxOppCost()
	{
		return reInvestTaxOppCost;
	}

	/**
	 * @param reInvestTaxOppCost
	 *             the reInvestTaxOppCost to set
	 */
	public void setReInvestTaxOppCost(boolean reInvestTaxOppCost)
	{
		this.reInvestTaxOppCost = reInvestTaxOppCost;
	}

	/**
	 * @return the oppCostTDS
	 */
	public double getOppCostTDS()
	{
		return oppCostTDS;
	}

	/**
	 * @param oppCostTDS
	 *             the oppCostTDS to set
	 */
	public void setOppCostTDS(double oppCostTDS)
	{
		this.oppCostTDS = oppCostTDS;
	}

	/**
	 * @return the numDays_oppCost
	 */
	public int getNumDays_oppCost()
	{
		return numDays_oppCost;
	}

	/**
	 * @param numDays_oppCost
	 *             the numDays_oppCost to set
	 */
	public void setNumDays_oppCost(int numDays_oppCost)
	{
		this.numDays_oppCost = numDays_oppCost;
	}

	/**
	 * @return the numDays_LTCG
	 */
	public int getNumDays_LTCG()
	{
		return numDays_LTCG;
	}

	/**
	 * @param numDays_LTCG
	 *             the numDays_LTCG to set
	 */
	public void setNumDays_LTCG(int numDays_LTCG)
	{
		this.numDays_LTCG = numDays_LTCG;
	}

	/**
	 * @return the oppCostperAnnum
	 */
	public double getOppCostperAnnum()
	{
		return oppCostperAnnum;
	}

	/**
	 * @param oppCostperAnnum
	 *             the oppCostperAnnum to set
	 */
	public void setOppCostperAnnum(double oppCostperAnnum)
	{
		this.oppCostperAnnum = oppCostperAnnum;
	}

	public double getCapitalGainsTaxRate()
	{
		return capitalGainsTaxRate;
	}

	public void setCapitalGainsTaxRate(double capitalGainsTaxRate)
	{
		this.capitalGainsTaxRate = capitalGainsTaxRate;
	}

	/**
	 * 
	 */
	public TY_ScripSellBaseConfig()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param numDays_LTCG
	 * @param oppCostperAnnum
	 * @param numDays_oppCost
	 * @param capitalGainsTaxRate
	 */
	public TY_ScripSellBaseConfig(int numDays_LTCG, double oppCostperAnnum, int numDays_oppCost, double capitalGainsTaxRate, double tds)
	{
		super();
		this.numDays_LTCG = numDays_LTCG;
		this.oppCostperAnnum = oppCostperAnnum;
		this.numDays_oppCost = numDays_oppCost;
		this.capitalGainsTaxRate = capitalGainsTaxRate;
		this.oppCostTDS = tds;
		this.reInvestTaxOppCost = false;
	}

}
