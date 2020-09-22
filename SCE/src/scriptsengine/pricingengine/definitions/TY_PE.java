package scriptsengine.pricingengine.definitions;

/**
 * 
 * Class to hold PE objects with Price and Earnings for an Year
 */
public class TY_PE
{
	private int	Year;
	private double	PE;

	/**
	 * @return the year
	 */
	public int getYear()
	{
		return Year;
	}

	/**
	 * @param year
	 *             the year to set
	 */
	public void setYear(int year)
	{
		Year = year;
	}

	/**
	 * @return the pE
	 */
	public double getPE()
	{
		return PE;
	}

	/**
	 * @param pE
	 *             the pE to set
	 */
	public void setPE(double pE)
	{
		PE = pE;
	}

	/**
	 * @param year
	 * @param pE
	 */
	public TY_PE(int year, double pE)
	{
		super();
		Year = year;
		PE = pE;
	}

	/**
	 * 
	 */
	public TY_PE()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
