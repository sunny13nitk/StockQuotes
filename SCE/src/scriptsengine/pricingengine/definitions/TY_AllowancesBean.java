package scriptsengine.pricingengine.definitions;

/**
 * 
 * Allowances Bean configured direction in scebeans.xml
 * --- Adding a new attribute to the class should be backed by setting the values in scebeans.xml ------
 */
public class TY_AllowancesBean
{
	/**
	 * PE Variance Allowance - Percentage by which the Max or Min PE can vary w.r.t Average PE for the Scrip
	 * Max PE adjusted to - avg. PE + this allowance to avoid overpricing
	 */
	private double	varPE_allowance;

	/**
	 * Factor of Safety PE allowance to determine Scrip Buy Price in case the Min PE < Avg. PE by more than
	 * varPE_Allowance
	 */
	private double	fosPE_allowance;

	/**
	 * <!-- Consecutive Years PE allowable percentage increase factor -->
	 */
	private double	adjPE_allowance;

	/**
	 * BaseLine Number of Latest Years in Numbers to be considered to PE weightage Booster
	 */
	private int	PEWeight_NYearsBaseline;

	/**
	 * PE Bosst Factor by which avg. last N Yrs PE
	 * must be increased to accomodate for giving more precedence to last 3
	 * yrs performace in compariosn to overall average
	 */
	private double	lastNYrsPE_BoostFactor;

	/**
	 * Minimum contribution% by last NYrs PE to Avg Pe determination for Price Projections
	 */
	private double	lastNYrsPE_minContr;

	/**
	 * Minimum Market Cap permissible to consider a Scrip
	 */
	private double	minMcap;

	/**
	 * ----------------------------Getters and Setters----------------------
	 */

	/**
	 * @return the lastNYrsPE_minContr
	 */
	public double getLastNYrsPE_minContr()
	{
		return lastNYrsPE_minContr;
	}

	/**
	 * @return the pEWeight_NYearsBaseline
	 */
	public int getPEWeight_NYearsBaseline()
	{
		return PEWeight_NYearsBaseline;
	}

	/**
	 * @param pEWeight_NYearsBaseline
	 *             the pEWeight_NYearsBaseline to set
	 */
	public void setPEWeight_NYearsBaseline(int pEWeight_NYearsBaseline)
	{
		PEWeight_NYearsBaseline = pEWeight_NYearsBaseline;
	}

	/**
	 * @param lastNYrsPE_minContr
	 *             the lastNYrsPE_minContr to set
	 */
	public void setLastNYrsPE_minContr(double last3YrsPE_minContr)
	{
		this.lastNYrsPE_minContr = last3YrsPE_minContr;
	}

	/**
	 * @return the adjPE_allowance
	 */
	public double getAdjPE_allowance()
	{
		return adjPE_allowance;
	}

	/**
	 * @param adjPE_allowance
	 *             the adjPE_allowance to set
	 */
	public void setAdjPE_allowance(double adjPE_allowance)
	{
		this.adjPE_allowance = adjPE_allowance;
	}

	/**
	 * @return the minMcap
	 */
	public double getMinMcap()
	{
		return minMcap;
	}

	/**
	 * @param minMcap
	 *             the minMcap to set
	 */
	public void setMinMcap(double minMcap)
	{
		this.minMcap = minMcap;
	}

	/**
	 * @return the varPE_allowance
	 */
	public double getVarPE_allowance()
	{
		return varPE_allowance;
	}

	/**
	 * @return the fosPE_allowance
	 */
	public double getFosPE_allowance()
	{
		return fosPE_allowance;
	}

	/**
	 * @param varPE_allowance
	 *             the varPE_allowance to set
	 */
	public void setVarPE_allowance(double varPE_allowance)
	{
		this.varPE_allowance = varPE_allowance;
	}

	/**
	 * @param fosPE_allowance
	 *             the fosPE_allowance to set
	 */
	public void setFosPE_allowance(double fosPE_allowance)
	{
		this.fosPE_allowance = fosPE_allowance;
	}

	/**
	 * @return the lastNYrsPE_BoostFactor
	 */
	public double getLastNYrsPE_BoostFactor()
	{
		return lastNYrsPE_BoostFactor;
	}

	/**
	 * @param lastNYrsPE_BoostFactor
	 *             the lastNYrsPE_BoostFactor to set
	 */
	public void setLastNYrsPE_BoostFactor(double last3YrsPE_BoostFactor)
	{
		this.lastNYrsPE_BoostFactor = last3YrsPE_BoostFactor;
	}

	/**
	 * @param varPE_allowance
	 * @param fosPE_allowance
	 * @param adjPE_allowance
	 * @param pEWeight_NYearsBaseline
	 * @param lastNYrsPE_BoostFactor
	 * @param lastNYrsPE_minContr
	 * @param minMcap
	 */
	public TY_AllowancesBean(double varPE_allowance, double fosPE_allowance, double adjPE_allowance, int pEWeight_NYearsBaseline,
	          double lastNYrsPE_BoostFactor, double lastNYrsPE_minContr, double minMcap)
	{
		super();
		this.varPE_allowance = varPE_allowance;
		this.fosPE_allowance = fosPE_allowance;
		this.adjPE_allowance = adjPE_allowance;
		PEWeight_NYearsBaseline = pEWeight_NYearsBaseline;
		this.lastNYrsPE_BoostFactor = lastNYrsPE_BoostFactor;
		this.lastNYrsPE_minContr = lastNYrsPE_minContr;
		this.minMcap = minMcap;
	}

	/**
	 * 
	 */
	public TY_AllowancesBean()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
