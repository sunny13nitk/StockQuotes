package scriptsengine.pricingengine.definitions;

/**
 * ENPR - Earnings Net Profit Ratio Class for a Scrip
 * Calculated Type
 */
public class TY_ENPR
{
	private int	Year;
	private double	EpsUnitValue;
	private double	ENPR;

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
	 * @return the epsUnitValue
	 */
	public double getEpsUnitValue()
	{
		return EpsUnitValue;
	}

	/**
	 * @param epsUnitValue
	 *             the epsUnitValue to set
	 */
	public void setEpsUnitValue(double epsUnitValue)
	{
		EpsUnitValue = epsUnitValue;
	}

	/**
	 * @return the eNPR
	 */
	public double getENPR()
	{
		return ENPR;
	}

	/**
	 * @param eNPR
	 *             the eNPR to set
	 */
	public void setENPR(double eNPR)
	{
		ENPR = eNPR;
	}

	/**
	 * @param year
	 * @param epsUnitValue
	 * @param eNPR
	 */
	public TY_ENPR(int year, double epsUnitValue, double eNPR)
	{
		super();
		Year = year;
		EpsUnitValue = epsUnitValue;
		ENPR = eNPR;
	}

	/**
	 * 
	 */
	public TY_ENPR()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
