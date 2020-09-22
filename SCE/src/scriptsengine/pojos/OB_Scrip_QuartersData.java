package scriptsengine.pojos;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import modelframework.implementations.DependantObject;

@Component("OB_Scrip_QuartersData") // Initialize as bean in Context with name as Class Name
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE) // Always Create a New Instance
public class OB_Scrip_QuartersData extends DependantObject
{
	private String	scCode;
	private int	Year;
	private int	Quarter;
	private double	NettProfit;
	private double	NettSales;

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
	 * @return the quarter
	 */
	public int getQuarter()
	{
		return Quarter;
	}

	/**
	 * @param quarter
	 *             the quarter to set
	 */
	public void setQuarter(int quarter)
	{
		Quarter = quarter;
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
	 * @param scCode
	 * @param year
	 * @param quarter
	 * @param nettProfit
	 * @param nettSales
	 */
	public OB_Scrip_QuartersData(String scCode, int year, int quarter, double nettProfit, double nettSales)
	{
		super();
		this.scCode = scCode;
		Year = year;
		Quarter = quarter;
		NettProfit = nettProfit;
		NettSales = nettSales;
	}

	/**
	 * 
	 */
	public OB_Scrip_QuartersData()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
