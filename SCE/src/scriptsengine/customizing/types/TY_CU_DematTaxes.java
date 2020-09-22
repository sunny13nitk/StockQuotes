package scriptsengine.customizing.types;

/**
 * POJO for Demat Charges and Taxes as Levied by Govt
 * Note: Only taxes upto 3 decimal places significant are considered e.g. Stamp duty etc which are very lo are not
 * considered
 * Bean Intiailized in scebeans.xml
 */
public class TY_CU_DematTaxes
{

	private double	ETT;
	private double	SrvTax;
	private double	STT;

	/**
	 * @return the eTT
	 */
	public double getETT()
	{
		return ETT;
	}

	/**
	 * @param eTT
	 *             the eTT to set
	 */
	public void setETT(double eTT)
	{
		ETT = eTT;
	}

	/**
	 * @return the srvTax
	 */
	public double getSrvTax()
	{
		return SrvTax;
	}

	/**
	 * @param srvTax
	 *             the srvTax to set
	 */
	public void setSrvTax(double srvTax)
	{
		SrvTax = srvTax;
	}

	/**
	 * @return the sTT
	 */
	public double getSTT()
	{
		return STT;
	}

	/**
	 * @param sTT
	 *             the sTT to set
	 */
	public void setSTT(double sTT)
	{
		STT = sTT;
	}

	/**
	 * 
	 */
	public TY_CU_DematTaxes()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param eTT
	 * @param srvTax
	 * @param sTT
	 */
	public TY_CU_DematTaxes(double eTT, double srvTax, double sTT)
	{
		super();
		ETT = eTT;
		SrvTax = srvTax;
		STT = sTT;
	}

}
