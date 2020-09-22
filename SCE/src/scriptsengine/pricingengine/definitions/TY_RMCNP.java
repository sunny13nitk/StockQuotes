package scriptsengine.pricingengine.definitions;

/**
 * 
 * Raw Material Contribution Nett Profit Definition
 */
public class TY_RMCNP
{

	private int	year;
	private double	RMCWeight;
	private double	RMCNP;

	/**
	 * @return the year
	 */
	public int getYear()
	{
		return year;
	}

	/**
	 * @param year
	 *             the year to set
	 */
	public void setYear(int year)
	{
		this.year = year;
	}

	/**
	 * @return the rMCWeight
	 */
	public double getRMCWeight()
	{
		return RMCWeight;
	}

	/**
	 * @param rMCWeight
	 *             the rMCWeight to set
	 */
	public void setRMCWeight(double rMCWeight)
	{
		RMCWeight = rMCWeight;
	}

	/**
	 * @return the rMCNP
	 */
	public double getRMCNP()
	{
		return RMCNP;
	}

	/**
	 * @param rMCNP
	 *             the rMCNP to set
	 */
	public void setRMCNP(double rMCNP)
	{
		RMCNP = rMCNP;
	}

	/**
	 * @param year
	 * @param rMCWeight
	 * @param rMCNP
	 */
	public TY_RMCNP(int year, double rMCWeight, double rMCNP)
	{
		super();
		this.year = year;
		RMCWeight = rMCWeight;
		RMCNP = rMCNP;
	}

	/**
	 * 
	 */
	public TY_RMCNP()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
