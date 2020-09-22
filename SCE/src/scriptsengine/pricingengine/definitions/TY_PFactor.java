package scriptsengine.pricingengine.definitions;

import java.util.ArrayList;

public class TY_PFactor
{
	private String						factorType;
	private ArrayList<TY_PFactorDetail>	factoritems;

	/**
	 * @return the factorType
	 */
	public String getFactorType()
	{
		return factorType;
	}

	/**
	 * @param factorType
	 *             the factorType to set
	 */
	public void setFactorType(String factorType)
	{
		this.factorType = factorType;
	}

	/**
	 * @return the factoritems
	 */
	public ArrayList<TY_PFactorDetail> getFactoritems()
	{
		return factoritems;
	}

	/**
	 * @param factoritems
	 *             the factoritems to set
	 */
	public void setFactoritems(ArrayList<TY_PFactorDetail> factoritems)
	{
		this.factoritems = factoritems;
	}

	/**
	 * @param factorType
	 * @param factoritems
	 */
	public TY_PFactor(String factorType, ArrayList<TY_PFactorDetail> factoritems)
	{
		super();
		this.factorType = factorType;
		this.factoritems = factoritems;
	}

	/**
	 * 
	 */
	public TY_PFactor()
	{
		super();
		this.factoritems = new ArrayList<TY_PFactorDetail>();
	}

}
