package scriptsengine.pojos;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import modelframework.implementations.RootObject;

/**
 * Scrip Sectors Root Object - Holds the Sectors Information
 *
 */
@Component("OB_Scrip_Sector") // Initialize as bean in Context with name as Class Name
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE) // Always Create a New Instance
public class OB_Scrip_Sector extends RootObject
{
	private String	Sector;
	private double	AvgPE;

	/**
	 * @return the sector
	 */
	public String getSector()
	{
		return Sector;
	}

	/**
	 * @param sector
	 *             the sector to set
	 */
	public void setSector(String sector)
	{
		Sector = sector;
	}

	/**
	 * @return the avgPE
	 */
	public double getAvgPE()
	{
		return AvgPE;
	}

	/**
	 * @param avgPE
	 *             the avgPE to set
	 */
	public void setAvgPE(double avgPE)
	{
		AvgPE = avgPE;
	}

	/**
	 * @param sector
	 * @param avgPE
	 */
	public OB_Scrip_Sector(String sector, double avgPE)
	{
		super();
		Sector = sector;
		AvgPE = avgPE;
	}

	/**
	 * 
	 */
	public OB_Scrip_Sector()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
