package scriptsengine.pricingengine.definitions;

/**
 * 
 * Class for PE Weights
 */
public class TY_PEWeights
{
	private double	lastNYrs_Wght;
	private double	restYrs_Wght;

	/**
	 * @return the lastNYrs_Wght
	 */
	public double getLastNYrs_Wght()
	{
		return lastNYrs_Wght;
	}

	/**
	 * @param lastNYrs_Wght
	 *             the lastNYrs_Wght to set
	 */
	public void setLastNYrs_Wght(double last3Yrs_Wght)
	{
		this.lastNYrs_Wght = last3Yrs_Wght;
	}

	/**
	 * @return the restYrs_Wght
	 */
	public double getRestYrs_Wght()
	{
		return restYrs_Wght;
	}

	/**
	 * @param restYrs_Wght
	 *             the restYrs_Wght to set
	 */
	public void setRestYrs_Wght(double restYrs_Wght)
	{
		this.restYrs_Wght = restYrs_Wght;
	}

	/**
	 * @param lastNYrs_Wght
	 * @param restYrs_Wght
	 */
	public TY_PEWeights(double last3Yrs_Wght, double restYrs_Wght)
	{
		super();
		this.lastNYrs_Wght = last3Yrs_Wght;
		this.restYrs_Wght = restYrs_Wght;
	}

	/**
	 * 
	 */
	public TY_PEWeights()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
