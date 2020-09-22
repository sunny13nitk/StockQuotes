package scriptsengine.pojos;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import modelframework.implementations.DependantObject;

@Component("OB_Scrip_Shareholding") // Initialize as bean in Context with name as Class Name
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE) // Always create a new bean Instance
public class OB_Scrip_Shareholding extends DependantObject
{
	private String	scCode;
	private double	promoter;
	private double	fii;
	private double	dii;
	private double	general;
	private double	pledged;

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
	 * @return the promoter
	 */
	public double getpromoter()
	{
		return promoter;
	}

	/**
	 * @param promoter
	 *             the promoter to set
	 */
	public void setpromoter(double promoter)
	{
		this.promoter = promoter;
	}

	/**
	 * @return the fii
	 */
	public double getfii()
	{
		return fii;
	}

	/**
	 * @param fii
	 *             the fii to set
	 */
	public void setfii(double fii)
	{
		this.fii = fii;
	}

	/**
	 * @return the dii
	 */
	public double getdii()
	{
		return dii;
	}

	/**
	 * @param dii
	 *             the dii to set
	 */
	public void setdii(double dii)
	{
		this.dii = dii;
	}

	/**
	 * @return the general
	 */
	public double getgeneral()
	{
		return general;
	}

	/**
	 * @param general
	 *             the general to set
	 */
	public void setgeneral(double general)
	{
		this.general = general;
	}

	/**
	 * @return the pledged
	 */
	public double getpledged()
	{
		return pledged;
	}

	/**
	 * @param pledged
	 *             the pledged to set
	 */
	public void setpledged(double pledged)
	{
		this.pledged = pledged;
	}

	/**
	 * @param scCode
	 * @param promoter
	 * @param fii
	 * @param dii
	 * @param general
	 * @param pledged
	 */
	public OB_Scrip_Shareholding(String scCode, double promoter, double fii, double dii, double general, double pledged)
	{
		super();
		this.scCode = scCode;
		this.promoter = promoter;
		this.fii = fii;
		this.dii = dii;
		this.general = general;
		this.pledged = pledged;
	}

	/**
	 * 
	 */
	public OB_Scrip_Shareholding()
	{

	}

}
