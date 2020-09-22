package scriptsengine.pojos;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import modelframework.implementations.DependantObject;

@Component("OB_Scrip_RawMaterial") // Initialize as bean in Context with name as Class Name
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE) // Always Create a New Instance
public class OB_Scrip_RawMaterial extends DependantObject
{
	private String	scCode;
	private int	Year;
	private String	RawMCatg;
	private String	RawM;	// Raw Materials Separated by Comma
	private double	PerRMC;	// Percentage Contribution to RMC

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
	 * @return the rawMCatg
	 */
	public String getRawMCatg()
	{
		return RawMCatg;
	}

	/**
	 * @param rawMCatg
	 *             the rawMCatg to set
	 */
	public void setRawMCatg(String rawMCatg)
	{
		RawMCatg = rawMCatg;
	}

	/**
	 * @return the rawM
	 */
	public String getRawM()
	{
		return RawM;
	}

	/**
	 * @param rawM
	 *             the rawM to set
	 */
	public void setRawM(String rawM)
	{
		RawM = rawM;
	}

	/**
	 * @return the perRMC
	 */
	public double getPerRMC()
	{
		return PerRMC;
	}

	/**
	 * @param perRMC
	 *             the perRMC to set
	 */
	public void setPerRMC(double perRMC)
	{
		PerRMC = perRMC;
	}

	/**
	 * @param scCode
	 * @param year
	 * @param rawMCatg
	 * @param rawM
	 * @param perRMC
	 */
	public OB_Scrip_RawMaterial(String scCode, int year, String rawMCatg, String rawM, double perRMC)
	{
		super();
		this.scCode = scCode;
		Year = year;
		RawMCatg = rawMCatg;
		RawM = rawM;
		PerRMC = perRMC;
	}

	/**
	 * 
	 */
	public OB_Scrip_RawMaterial()
	{

	}

}
