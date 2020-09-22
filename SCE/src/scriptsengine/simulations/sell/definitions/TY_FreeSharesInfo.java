package scriptsengine.simulations.sell.definitions;

public class TY_FreeSharesInfo
{
	private int		numFreeShares;
	private double		realizationSpill;
	private int		numActualSale;
	private boolean	reinvestTaxOpp;

	/**
	 * @return the reinvestTaxOpp
	 */
	public boolean isReinvestTaxOpp()
	{
		return reinvestTaxOpp;
	}

	/**
	 * @param reinvestTaxOpp
	 *             the reinvestTaxOpp to set
	 */
	public void setReinvestTaxOpp(boolean reinvestTaxOpp)
	{
		this.reinvestTaxOpp = reinvestTaxOpp;
	}

	/**
	 * @return the numActualSale
	 */
	public int getNumActualSale()
	{
		return numActualSale;
	}

	/**
	 * @param numActualSale
	 *             the numActualSale to set
	 */
	public void setNumActualSale(int numActualSale)
	{
		this.numActualSale = numActualSale;
	}

	/**
	 * @return the numFreeShares
	 */
	public int getNumFreeShares()
	{
		return numFreeShares;
	}

	/**
	 * @param numFreeShares
	 *             the numFreeShares to set
	 */
	public void setNumFreeShares(int numFreeShares)
	{
		this.numFreeShares = numFreeShares;
	}

	/**
	 * @return the realizationSpill
	 */
	public double getRealizationSpill()
	{
		return realizationSpill;
	}

	/**
	 * @param realizationSpill
	 *             the realizationSpill to set
	 */
	public void setRealizationSpill(double realizationSpill)
	{
		this.realizationSpill = realizationSpill;
	}

	/**
	 * 
	 */
	public TY_FreeSharesInfo()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param numFreeShares
	 * @param realizationSpill
	 */
	public TY_FreeSharesInfo(int numFreeShares, double realizationSpill, int actual, boolean reinv)
	{
		super();
		this.numFreeShares = numFreeShares;
		this.numActualSale = actual;
		this.realizationSpill = realizationSpill;
		this.reinvestTaxOpp = reinv;
	}

}
