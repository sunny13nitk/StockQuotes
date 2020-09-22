package scriptsengine.pojos;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import modelframework.implementations.DependantObject;

@Component("OB_Scrip_Ratios") // Initialize as bean in Context with name as Class Name
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE) // Always Create a New Instance
public class OB_Scrip_Ratios extends DependantObject
{
	private String	scCode;
	private int	Year;
	private double	CurrRatio;
	private double	QuickRatio;
	private double	ITR;
	private double	DER;
	private double	WorkCapDays;
	private double	ERR;

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
	 * @return the currRatio
	 */
	public double getCurrRatio()
	{
		return CurrRatio;
	}

	/**
	 * @param currRatio
	 *             the currRatio to set
	 */
	public void setCurrRatio(double currRatio)
	{
		CurrRatio = currRatio;
	}

	/**
	 * @return the quickRatio
	 */
	public double getQuickRatio()
	{
		return QuickRatio;
	}

	/**
	 * @param quickRatio
	 *             the quickRatio to set
	 */
	public void setQuickRatio(double quickRatio)
	{
		QuickRatio = quickRatio;
	}

	/**
	 * @return the iTR
	 */
	public double getITR()
	{
		return ITR;
	}

	/**
	 * @param iTR
	 *             the iTR to set
	 */
	public void setITR(double iTR)
	{
		ITR = iTR;
	}

	/**
	 * @return the dER
	 */
	public double getDER()
	{
		return DER;
	}

	/**
	 * @param dER
	 *             the dER to set
	 */
	public void setDER(double dER)
	{
		DER = dER;
	}

	/**
	 * @return the workCapDays
	 */
	public double getWorkCapDays()
	{
		return WorkCapDays;
	}

	/**
	 * @param workCapDays
	 *             the workCapDays to set
	 */
	public void setWorkCapDays(double workCapDays)
	{
		WorkCapDays = workCapDays;
	}

	/**
	 * @return the eRR
	 */
	public double getERR()
	{
		return ERR;
	}

	/**
	 * @param eRR
	 *             the eRR to set
	 */
	public void setERR(double eRR)
	{
		ERR = eRR;
	}

	/**
	 * @param scCode
	 * @param year
	 * @param currRatio
	 * @param quickRatio
	 * @param iTR
	 * @param dER
	 * @param workCapDays
	 * @param eRR
	 */
	public OB_Scrip_Ratios(String scCode, int year, double currRatio, double quickRatio, double iTR, double dER, double workCapDays, double eRR)
	{
		super();
		this.scCode = scCode;
		Year = year;
		CurrRatio = currRatio;
		QuickRatio = quickRatio;
		ITR = iTR;
		DER = dER;
		WorkCapDays = workCapDays;
		ERR = eRR;
	}

	/**
	 * 
	 */
	public OB_Scrip_Ratios()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
