package scriptsengine.simulations.sell.definitions;

import java.util.ArrayList;

public class TY_TaxPerspective
{
	private TY_TaxProjections			taxSsavH;

	private ArrayList<TY_TaxProjections>	taxSavI;

	/**
	 * @return the taxSsavH
	 */
	public TY_TaxProjections getTaxSsavH()
	{
		return taxSsavH;
	}

	/**
	 * @param taxSsavH
	 *             the taxSsavH to set
	 */
	public void setTaxSsavH(TY_TaxProjections taxSsavH)
	{
		this.taxSsavH = taxSsavH;
	}

	/**
	 * @return the taxSavI
	 */
	public ArrayList<TY_TaxProjections> getTaxSavI()
	{
		return taxSavI;
	}

	/**
	 * @param taxSavI
	 *             the taxSavI to set
	 */
	public void setTaxSavI(ArrayList<TY_TaxProjections> taxSavI)
	{
		this.taxSavI = taxSavI;
	}

	/**
	 * 
	 */
	public TY_TaxPerspective()
	{
		super();
		this.taxSavI = new ArrayList<TY_TaxProjections>();
	}

	/**
	 * @param taxSsavH
	 * @param taxSavI
	 */
	public TY_TaxPerspective(TY_TaxProjections taxSsavH, ArrayList<TY_TaxProjections> taxSavI)
	{
		super();
		this.taxSsavH = taxSsavH;
		this.taxSavI = taxSavI;
	}

}
