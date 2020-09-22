package scriptsengine.simulations.sell.definitions;

public class TY_TaxProjections
{
	private int	numDays;
	private double	probTaxSavings;
	private int	probNumFreeShares;

	/**
	 * @return the numDays
	 */
	public int getNumDays()
	{
		return numDays;
	}

	/**
	 * @param numDays
	 *             the numDays to set
	 */
	public void setNumDays(int numDays)
	{
		this.numDays = numDays;
	}

	/**
	 * @return the probTaxSavings
	 */
	public double getProbTaxSavings()
	{
		return probTaxSavings;
	}

	/**
	 * @param probTaxSavings
	 *             the probTaxSavings to set
	 */
	public void setProbTaxSavings(double probTaxSavings)
	{
		this.probTaxSavings = probTaxSavings;
	}

	/**
	 * @return the probNumFreeShares
	 */
	public int getProbNumFreeShares()
	{
		return probNumFreeShares;
	}

	/**
	 * @param probNumFreeShares
	 *             the probNumFreeShares to set
	 */
	public void setProbNumFreeShares(int probNumFreeShares)
	{
		this.probNumFreeShares = probNumFreeShares;
	}

	/**
	 * 
	 */
	public TY_TaxProjections()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param numDays
	 * @param probTaxSavings
	 * @param probNumFreeShares
	 */
	public TY_TaxProjections(int numDays, double probTaxSavings, int probNumFreeShares)
	{
		super();
		this.numDays = numDays;
		this.probTaxSavings = probTaxSavings;
		this.probNumFreeShares = probNumFreeShares;
	}

}
