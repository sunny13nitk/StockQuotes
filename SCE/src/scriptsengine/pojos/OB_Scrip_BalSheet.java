package scriptsengine.pojos;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import modelframework.implementations.DependantObject;

@Component("OB_Scrip_BalSheet") // Initialize as bean in Context with name as Class Name
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE) // Always Create a New Instance
public class OB_Scrip_BalSheet extends DependantObject
{
	private String	scCode;
	private int	year;
	private double	avgPrice;
	private double	priceDeviation;
	private double	netWorth;
	private double	securedLoans;
	private double	unsecuredLoans;
	private double	NettBlock;
	private double	CWIP;
	private double	Investments;
	private double	Reserves;
	private double	Provisions;
	private double	NettSales;
	private double	NettProfit;
	private double	NettProfitMargin;
	private double	EPS;
	private double	Inventories;
	private double	BookValue;
	private double	FaceValue;
	private double	DPS;

	/**
	 * @return the nettBlock
	 */
	public double getNettBlock()
	{
		return NettBlock;
	}

	/**
	 * @param nettBlock
	 *             the nettBlock to set
	 */
	public void setNettBlock(double nettBlock)
	{
		NettBlock = nettBlock;
	}

	/**
	 * @return the cWIP
	 */
	public double getCWIP()
	{
		return CWIP;
	}

	/**
	 * @param cWIP
	 *             the cWIP to set
	 */
	public void setCWIP(double cWIP)
	{
		CWIP = cWIP;
	}

	/**
	 * @return the investments
	 */
	public double getInvestments()
	{
		return Investments;
	}

	/**
	 * @param investments
	 *             the investments to set
	 */
	public void setInvestments(double investments)
	{
		Investments = investments;
	}

	/**
	 * @return the reserves
	 */
	public double getReserves()
	{
		return Reserves;
	}

	/**
	 * @param reserves
	 *             the reserves to set
	 */
	public void setReserves(double reserves)
	{
		Reserves = reserves;
	}

	/**
	 * @return the provisions
	 */
	public double getProvisions()
	{
		return Provisions;
	}

	/**
	 * @param provisions
	 *             the provisions to set
	 */
	public void setProvisions(double provisions)
	{
		Provisions = provisions;
	}

	/**
	 * @return the nettSales
	 */
	public double getNettSales()
	{
		return NettSales;
	}

	/**
	 * @param nettSales
	 *             the nettSales to set
	 */
	public void setNettSales(double nettSales)
	{
		NettSales = nettSales;
	}

	/**
	 * @return the nettProfit
	 */
	public double getNettProfit()
	{
		return NettProfit;
	}

	/**
	 * @param nettProfit
	 *             the nettProfit to set
	 */
	public void setNettProfit(double nettProfit)
	{
		NettProfit = nettProfit;
	}

	/**
	 * @return the nettProfitMargin
	 */
	public double getNettProfitMargin()
	{
		return NettProfitMargin;
	}

	/**
	 * @param nettProfitMargin
	 *             the nettProfitMargin to set
	 */
	public void setNettProfitMargin(double nettProfitMargin)
	{
		NettProfitMargin = nettProfitMargin;
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
	 * @return the inventories
	 */
	public double getInventories()
	{
		return Inventories;
	}

	/**
	 * @param inventories
	 *             the inventories to set
	 */
	public void setInventories(double inventories)
	{
		Inventories = inventories;
	}

	/**
	 * @return the bookValue
	 */
	public double getBookValue()
	{
		return BookValue;
	}

	/**
	 * @param bookValue
	 *             the bookValue to set
	 */
	public void setBookValue(double bookValue)
	{
		BookValue = bookValue;
	}

	/**
	 * @return the faceValue
	 */
	public double getFaceValue()
	{
		return FaceValue;
	}

	/**
	 * @param faceValue
	 *             the faceValue to set
	 */
	public void setFaceValue(double faceValue)
	{
		FaceValue = faceValue;
	}

	/**
	 * @return the dPS
	 */
	public double getDPS()
	{
		return DPS;
	}

	/**
	 * @param dPS
	 *             the dPS to set
	 */
	public void setDPS(double dPS)
	{
		DPS = dPS;
	}

	/**
	 * @return the scCode
	 */
	public String getscCode()
	{
		return scCode;
	}

	/**
	 * @param scCode
	 *             the scCode to set
	 */
	public void setscCode(String scCode)
	{
		this.scCode = scCode;
	}

	/**
	 * @return the year
	 */
	public int getyear()
	{
		return year;
	}

	/**
	 * @param year
	 *             the year to set
	 */
	public void setyear(int year)
	{
		this.year = year;
	}

	/**
	 * @return the avgPrice
	 */
	public double getavgPrice()
	{
		return avgPrice;
	}

	/**
	 * @param avgPrice
	 *             the avgPrice to set
	 */
	public void setavgPrice(double avgPrice)
	{
		this.avgPrice = avgPrice;
	}

	/**
	 * @return the priceDeviation
	 */
	public double getpriceDeviation()
	{
		return priceDeviation;
	}

	/**
	 * @param priceDeviation
	 *             the priceDeviation to set
	 */
	public void setpriceDeviation(double priceDeviation)
	{
		this.priceDeviation = priceDeviation;
	}

	/**
	 * @return the netWorth
	 */
	public double getnetWorth()
	{
		return netWorth;
	}

	/**
	 * @param netWorth
	 *             the netWorth to set
	 */
	public void setnetWorth(double netWorth)
	{
		this.netWorth = netWorth;
	}

	/**
	 * @return the securedLoans
	 */
	public double getsecuredLoans()
	{
		return securedLoans;
	}

	/**
	 * @param securedLoans
	 *             the securedLoans to set
	 */
	public void setsecuredLoans(double securedLoans)
	{
		this.securedLoans = securedLoans;
	}

	/**
	 * @return the unsecuredLoans
	 */
	public double getunsecuredLoans()
	{
		return unsecuredLoans;
	}

	/**
	 * @param unsecuredLoans
	 *             the unsecuredLoans to set
	 */
	public void setunsecuredLoans(double unsecuredLoans)
	{
		this.unsecuredLoans = unsecuredLoans;
	}

	public OB_Scrip_BalSheet()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param scCode
	 * @param year
	 * @param avgPrice
	 * @param priceDeviation
	 * @param netWorth
	 * @param securedLoans
	 * @param unsecuredLoans
	 * @param nettBlock
	 * @param cWIP
	 * @param investments
	 * @param reserves
	 * @param provisions
	 * @param nettSales
	 * @param nettProfit
	 * @param nettProfitMargin
	 * @param ePS
	 * @param inventories
	 * @param bookValue
	 * @param faceValue
	 * @param dPS
	 */
	public OB_Scrip_BalSheet(String scCode, int year, double avgPrice, double priceDeviation, double netWorth, double securedLoans,
	          double unsecuredLoans, double nettBlock, double cWIP, double investments, double reserves, double provisions, double nettSales,
	          double nettProfit, double nettProfitMargin, double ePS, double inventories, double bookValue, double faceValue, double dPS)
	{
		super();
		this.scCode = scCode;
		this.year = year;
		this.avgPrice = avgPrice;
		this.priceDeviation = priceDeviation;
		this.netWorth = netWorth;
		this.securedLoans = securedLoans;
		this.unsecuredLoans = unsecuredLoans;
		NettBlock = nettBlock;
		CWIP = cWIP;
		Investments = investments;
		Reserves = reserves;
		Provisions = provisions;
		NettSales = nettSales;
		NettProfit = nettProfit;
		NettProfitMargin = nettProfitMargin;
		EPS = ePS;
		Inventories = inventories;
		BookValue = bookValue;
		FaceValue = faceValue;
		DPS = dPS;
	}

}
