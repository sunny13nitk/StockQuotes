package scriptsengine.simulations.sell.definitions;

public class TY_SS_RelRealizations
{
	private TY_RelRealization	avgPERelativeRealizations;
	private TY_RelRealization	adjPERelativeRealizations;

	/**
	 * @return the avgPERelativeRealizations
	 */
	public TY_RelRealization getAvgPERelativeRealizations()
	{
		return avgPERelativeRealizations;
	}

	/**
	 * @param avgPERelativeRealizations
	 *             the avgPERelativeRealizations to set
	 */
	public void setAvgPERelativeRealizations(TY_RelRealization avgPERelativeRealizations)
	{
		this.avgPERelativeRealizations = avgPERelativeRealizations;
	}

	/**
	 * @return the adjPERelativeRealizations
	 */
	public TY_RelRealization getAdjPERelativeRealizations()
	{
		return adjPERelativeRealizations;
	}

	/**
	 * @param adjPERelativeRealizations
	 *             the adjPERelativeRealizations to set
	 */
	public void setAdjPERelativeRealizations(TY_RelRealization adjPERelativeRealizations)
	{
		this.adjPERelativeRealizations = adjPERelativeRealizations;
	}

	/**
	 * 
	 */
	public TY_SS_RelRealizations()
	{
		super();
		this.adjPERelativeRealizations = new TY_RelRealization();
		this.avgPERelativeRealizations = new TY_RelRealization();
	}

	/**
	 * @param avgPERelativeRealizations
	 * @param adjPERelativeRealizations
	 */
	public TY_SS_RelRealizations(TY_RelRealization avgPERelativeRealizations, TY_RelRealization adjPERelativeRealizations)
	{
		super();
		this.avgPERelativeRealizations = avgPERelativeRealizations;
		this.adjPERelativeRealizations = adjPERelativeRealizations;
	}

}
