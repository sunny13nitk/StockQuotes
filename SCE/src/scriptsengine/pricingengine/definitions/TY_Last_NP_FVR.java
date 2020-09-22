package scriptsengine.pricingengine.definitions;

/**
 * 
 * Class to hold Last Year Profit and Face Value Ratio Comparison between Current Year Face Value and Last Year Face
 * Value
 */
public class TY_Last_NP_FVR
{

	private double	lastNettProfit;

	private double	FVR;

	private double	currFV;

	/**
	 * @return the currFV
	 */
	public double getCurrFV()
	{
		return currFV;
	}

	/**
	 * @param currFV
	 *             the currFV to set
	 */
	public void setCurrFV(double currFV)
	{
		this.currFV = currFV;
	}

	/**
	 * @return the lastNettProfit
	 */
	public double getLastNettProfit()
	{
		return lastNettProfit;
	}

	/**
	 * @param lastNettProfit
	 *             the lastNettProfit to set
	 */
	public void setLastNettProfit(double lastNettProfit)
	{
		this.lastNettProfit = lastNettProfit;
	}

	/**
	 * @return the fVR
	 */
	public double getFVR()
	{
		return FVR;
	}

	/**
	 * @param fVR
	 *             the fVR to set
	 */
	public void setFVR(double fVR)
	{
		FVR = fVR;
	}

	/**
	 * @param lastNettProfit
	 * @param fVR
	 * @param currFV
	 */
	public TY_Last_NP_FVR(double lastNettProfit, double fVR, double currFV)
	{
		super();
		this.lastNettProfit = lastNettProfit;
		FVR = fVR;
		this.currFV = currFV;
	}

	/**
	 * 
	 */
	public TY_Last_NP_FVR()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
