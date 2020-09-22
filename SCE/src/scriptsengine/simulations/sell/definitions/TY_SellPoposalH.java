package scriptsengine.simulations.sell.definitions;

/**
 * SELL Proposal Header
 *
 */
public class TY_SellPoposalH
{
	private TY_FreeSharesInfo		freeSharesPespective;

	private TY_PandLInfo			PandLPerspective;

	private TY_SS_PricesRealizations	pricesRealizationPerspective;

	/**
	 * @return the pricesRealizationPerspective
	 */
	public TY_SS_PricesRealizations getPricesRealizationPerspective()
	{
		return pricesRealizationPerspective;
	}

	/**
	 * @param pricesRealizationPerspective
	 *             the pricesRealizationPerspective to set
	 */
	public void setPricesRealizationPerspective(TY_SS_PricesRealizations pricesRealizationPerspective)
	{
		this.pricesRealizationPerspective = pricesRealizationPerspective;
	}

	/**
	 * @return the freeSharesPespective
	 */
	public TY_FreeSharesInfo getFreeSharesPespective()
	{
		return freeSharesPespective;
	}

	/**
	 * @param freeSharesPespective
	 *             the freeSharesPespective to set
	 */
	public void setFreeSharesPespective(TY_FreeSharesInfo freeSharesPespective)
	{
		this.freeSharesPespective = freeSharesPespective;
	}

	/**
	 * @return the pandLPerspective
	 */
	public TY_PandLInfo getPandLPerspective()
	{
		return PandLPerspective;
	}

	/**
	 * @param pandLPerspective
	 *             the pandLPerspective to set
	 */
	public void setPandLPerspective(TY_PandLInfo pandLPerspective)
	{
		PandLPerspective = pandLPerspective;
	}

	/**
	 * 
	 */
	public TY_SellPoposalH()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param freeSharesPespective
	 * @param pandLPerspective
	 * @param pricesRealizationPerspective
	 */
	public TY_SellPoposalH(TY_FreeSharesInfo freeSharesPespective, TY_PandLInfo pandLPerspective,
	          TY_SS_PricesRealizations pricesRealizationPerspective)
	{
		super();
		this.freeSharesPespective = freeSharesPespective;
		PandLPerspective = pandLPerspective;
		this.pricesRealizationPerspective = pricesRealizationPerspective;
	}

}
