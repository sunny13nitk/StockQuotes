package scriptsengine.pojos;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import modelframework.implementations.DependantObject;

@Component("OB_Scrip_FinishedProduct") // Initialize as bean in Context with name as Class Name
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE) // Always Create a New Instance
public class OB_Scrip_FinishedProduct extends DependantObject
{
	private String	scCode;
	private int	Year;
	private String	FPCatg;	// Finished Product category
	private double	PerSales;	// Percentage Contribution to Sales

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
	 * @return the fPCatg
	 */
	public String getFPCatg()
	{
		return FPCatg;
	}

	/**
	 * @param fPCatg
	 *             the fPCatg to set
	 */
	public void setFPCatg(String fPCatg)
	{
		FPCatg = fPCatg;
	}

	/**
	 * @return the perSales
	 */
	public double getPerSales()
	{
		return PerSales;
	}

	/**
	 * @param perSales
	 *             the perSales to set
	 */
	public void setPerSales(double perSales)
	{
		PerSales = perSales;
	}

	/**
	 * @param scCode
	 * @param year
	 * @param fPCatg
	 * @param perSales
	 */
	public OB_Scrip_FinishedProduct(String scCode, int year, String fPCatg, double perSales)
	{
		super();
		this.scCode = scCode;
		Year = year;
		FPCatg = fPCatg;
		PerSales = perSales;
	}

	/**
	 * 
	 */
	public OB_Scrip_FinishedProduct()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
