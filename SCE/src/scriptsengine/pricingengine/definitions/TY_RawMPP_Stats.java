package scriptsengine.pricingengine.definitions;

import scriptsengine.enums.SCEenums;

/**
 * Class to hold Raw Material Price Projection Statistics
 *
 */
public class TY_RawMPP_Stats
{
	private String				category;
	private SCEenums.direction	direction;
	private double				effPercentage;
	private double				avgRMC;
	private double				avgRMCNP;
	private double				effRMCNP;
	private double				reductioninProfit;

	/**
	 * @return the category
	 */
	public String getCategory()
	{
		return category;
	}

	/**
	 * @param category
	 *             the category to set
	 */
	public void setCategory(String category)
	{
		this.category = category;
	}

	/**
	 * @return the direction
	 */
	public SCEenums.direction getDirection()
	{
		return direction;
	}

	/**
	 * @param direction
	 *             the direction to set
	 */
	public void setDirection(SCEenums.direction direction)
	{
		this.direction = direction;
	}

	/**
	 * @return the effPercentage
	 */
	public double getEffPercentage()
	{
		return effPercentage;
	}

	/**
	 * @param effPercentage
	 *             the effPercentage to set
	 */
	public void setEffPercentage(double effPercentage)
	{
		this.effPercentage = effPercentage;
	}

	/**
	 * @return the avgRMC
	 */
	public double getAvgRMC()
	{
		return avgRMC;
	}

	/**
	 * @param avgRMC
	 *             the avgRMC to set
	 */
	public void setAvgRMC(double avgRMC)
	{
		this.avgRMC = avgRMC;
	}

	/**
	 * @return the avgRMCNP
	 */
	public double getAvgRMCNP()
	{
		return avgRMCNP;
	}

	/**
	 * @param avgRMCNP
	 *             the avgRMCNP to set
	 */
	public void setAvgRMCNP(double avgRMCNP)
	{
		this.avgRMCNP = avgRMCNP;
	}

	/**
	 * @return the effRMCNP
	 */
	public double getEffRMCNP()
	{
		return effRMCNP;
	}

	/**
	 * @param effRMCNP
	 *             the effRMCNP to set
	 */
	public void setEffRMCNP(double effRMCNP)
	{
		this.effRMCNP = effRMCNP;
	}

	/**
	 * @return the reductioninProfit
	 */
	public double getReductioninProfit()
	{
		return reductioninProfit;
	}

	/**
	 * @param reductioninProfit
	 *             the reductioninProfit to set
	 */
	public void setReductioninProfit(double deltaProfit)
	{
		this.reductioninProfit = deltaProfit;
	}

	/**
	 * @param category
	 * @param direction
	 * @param effPercentage
	 * @param avgRMC
	 * @param avgRMCNP
	 * @param effRMCNP
	 * @param reductioninProfit
	 */
	public TY_RawMPP_Stats(String category, scriptsengine.enums.SCEenums.direction direction, double effPercentage, double avgRMC, double avgRMCNP,
	          double effRMCNP, double deltaProfit)
	{
		super();
		this.category = category;
		this.direction = direction;
		this.effPercentage = effPercentage;
		this.avgRMC = avgRMC;
		this.avgRMCNP = avgRMCNP;
		this.effRMCNP = effRMCNP;
		this.reductioninProfit = deltaProfit;
	}

	/**
	 * 
	 */
	public TY_RawMPP_Stats()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
