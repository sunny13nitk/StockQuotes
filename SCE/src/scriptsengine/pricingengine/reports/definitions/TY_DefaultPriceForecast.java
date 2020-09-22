package scriptsengine.pricingengine.reports.definitions;

/**
 * Default Price forecast Service Report Data Type
 *
 */
public class TY_DefaultPriceForecast
{
	private int	year;
	private double	price;
	private double	nettProfit;
	private double	EPS;
	private double	faceValue;
	private double	EUV;
	private double	ENPR;
	private double	lineFVR;
	private double	effEPS;
	private double	effPE;
	private double	adjPE;

	/**
	 * @return the adjPE
	 */
	public double getAdjPE()
	{
		return adjPE;
	}

	/**
	 * @param adjPE
	 *             the adjPE to set
	 */
	public void setAdjPE(double adjPE)
	{
		this.adjPE = adjPE;
	}

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
	 * @return the price
	 */
	public double getPrice()
	{
		return price;
	}

	/**
	 * @param price
	 *             the price to set
	 */
	public void setPrice(double price)
	{
		this.price = price;
	}

	/**
	 * @return the nettProfit
	 */
	public double getNettProfit()
	{
		return nettProfit;
	}

	/**
	 * @param nettProfit
	 *             the nettProfit to set
	 */
	public void setNettProfit(double nettProfit)
	{
		this.nettProfit = nettProfit;
	}

	/**
	 * @return the ePS
	 */
	public double getEPS()
	{
		return EPS;
	}

	/**
	 * @param ePS
	 *             the ePS to set
	 */
	public void setEPS(double ePS)
	{
		EPS = ePS;
	}

	/**
	 * @return the faceValue
	 */
	public double getFaceValue()
	{
		return faceValue;
	}

	/**
	 * @param faceValue
	 *             the faceValue to set
	 */
	public void setFaceValue(double faceValue)
	{
		this.faceValue = faceValue;
	}

	/**
	 * @return the eUV
	 */
	public double getEUV()
	{
		return EUV;
	}

	/**
	 * @param eUV
	 *             the eUV to set
	 */
	public void setEUV(double eUV)
	{
		EUV = eUV;
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
	 * @return the lineFVR
	 */
	public double getLineFVR()
	{
		return lineFVR;
	}

	/**
	 * @param lineFVR
	 *             the lineFVR to set
	 */
	public void setLineFVR(double lineFVR)
	{
		this.lineFVR = lineFVR;
	}

	/**
	 * @return the effEPS
	 */
	public double getEffEPS()
	{
		return effEPS;
	}

	/**
	 * @param effEPS
	 *             the effEPS to set
	 */
	public void setEffEPS(double effEPS)
	{
		this.effEPS = effEPS;
	}

	/**
	 * @return the effPE
	 */
	public double getEffPE()
	{
		return effPE;
	}

	/**
	 * @param effPE
	 *             the effPE to set
	 */
	public void setEffPE(double effPE)
	{
		this.effPE = effPE;
	}

	/**
	 * 
	 */
	public TY_DefaultPriceForecast()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param year
	 * @param price
	 * @param nettProfit
	 * @param ePS
	 * @param faceValue
	 * @param eUV
	 * @param eNPR
	 * @param lineFVR
	 * @param effEPS
	 * @param effPE
	 * @param adjPE
	 */
	public TY_DefaultPriceForecast(int year, double price, double nettProfit, double ePS, double faceValue, double eUV, double eNPR, double lineFVR,
	          double effEPS, double effPE, double adjPE)
	{
		super();
		this.year = year;
		this.price = price;
		this.nettProfit = nettProfit;
		EPS = ePS;
		this.faceValue = faceValue;
		EUV = eUV;
		ENPR = eNPR;
		this.lineFVR = lineFVR;
		this.effEPS = effEPS;
		this.effPE = effPE;
		this.adjPE = adjPE;
	}

}
