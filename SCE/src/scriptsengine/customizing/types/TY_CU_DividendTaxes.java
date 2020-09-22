package scriptsengine.customizing.types;

/**
 * 
 * Dividend Taxation - Dividend Distribution Tax Properties
 * As per section 115-0 (1B) of Income Tax Act, 1961 dividend is taxable in the hands of company as dividend
 * distribution tax (DDT) on gross up amount of dividend
 */
public class TY_CU_DividendTaxes
{
	private double	Tax;
	private double	Surcharge;
	private double	Ecess;

	public double getTax()
	{
		return Tax;
	}

	public void setTax(double tax)
	{
		Tax = tax;
	}

	public double getSurcharge()
	{
		return Surcharge;
	}

	public void setSurcharge(double surcharge)
	{
		Surcharge = surcharge;
	}

	public double getEcess()
	{
		return Ecess;
	}

	public void setEcess(double ecess)
	{
		Ecess = ecess;
	}

	public TY_CU_DividendTaxes(double tax, double surcharge, double ecess)
	{
		super();
		Tax = tax;
		Surcharge = surcharge;
		Ecess = ecess;
	}

	public TY_CU_DividendTaxes()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
