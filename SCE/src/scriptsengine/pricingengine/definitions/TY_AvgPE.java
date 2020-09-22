package scriptsengine.pricingengine.definitions;

import java.util.ArrayList;

/**
 * 
 * Class to Hold average PE object information
 */
public class TY_AvgPE
{
	private double			avgPE;
	private double			minPE;
	private double			maxPE;
	private double			avgPE_unadjusted;
	private double			peadj_percentage;
	private boolean		peAdjusted;

	private ArrayList<TY_PE>	adjustedPEList;

	/**
	 * @return the peadj_percentage
	 */
	public double getPeadj_percentage()
	{
		return peadj_percentage;
	}

	/**
	 * @param peadj_percentage
	 *             the peadj_percentage to set
	 */
	public void setPeadj_percentage(double peadj_percentage)
	{
		this.peadj_percentage = peadj_percentage;
	}

	/**
	 * @return the avgPE_unadjusted
	 */
	public double getAvgPE_unadjusted()
	{
		return avgPE_unadjusted;
	}

	/**
	 * @param avgPE_unadjusted
	 *             the avgPE_unadjusted to set
	 */
	public void setAvgPE_unadjusted(double avgPE_unadjusted)
	{
		this.avgPE_unadjusted = avgPE_unadjusted;
	}

	/**
	 * @return the adjustedPEList
	 */
	public ArrayList<TY_PE> getAdjustedPEList()
	{
		return adjustedPEList;
	}

	/**
	 * @param adjustedPEList
	 *             the adjustedPEList to set
	 */
	public void setAdjustedPEList(ArrayList<TY_PE> adjustedPEList)
	{
		this.adjustedPEList = adjustedPEList;
	}

	/**
	 * @return the peAdjusted
	 */
	public boolean isPeAdjusted()
	{
		return peAdjusted;
	}

	/**
	 * @param peAdjusted
	 *             the peAdjusted to set
	 */
	public void setPeAdjusted(boolean peAdjusted)
	{
		this.peAdjusted = peAdjusted;
	}

	/**
	 * @return the avgPE
	 */
	public double getAvgPE()
	{
		return avgPE;
	}

	/**
	 * @param avgPE
	 *             the avgPE to set
	 */
	public void setAvgPE(double avgPE)
	{
		this.avgPE = avgPE;
	}

	/**
	 * @return the minPE
	 */
	public double getMinPE()
	{
		return minPE;
	}

	/**
	 * @param minPE
	 *             the minPE to set
	 */
	public void setMinPE(double minPE)
	{
		this.minPE = minPE;
	}

	/**
	 * @return the maxPE
	 */
	public double getMaxPE()
	{
		return maxPE;
	}

	/**
	 * @param maxPE
	 *             the maxPE to set
	 */
	public void setMaxPE(double maxPE)
	{
		this.maxPE = maxPE;
	}

	/**
	 * @param avgPE
	 * @param minPE
	 * @param maxPE
	 * @param avgPE_unadjusted
	 * @param peadj_percentage
	 * @param peAdjusted
	 * @param adjustedPEList
	 */
	public TY_AvgPE(double avgPE, double minPE, double maxPE, double avgPE_unadjusted, double peadj_percentage, boolean peAdjusted,
	          ArrayList<TY_PE> adjustedPEList)
	{
		super();
		this.avgPE = avgPE;
		this.minPE = minPE;
		this.maxPE = maxPE;
		this.avgPE_unadjusted = avgPE_unadjusted;
		this.peadj_percentage = peadj_percentage;
		this.peAdjusted = peAdjusted;
		this.adjustedPEList = new ArrayList<TY_PE>();
	}

	/**
	 * Set PE adjusted to false by default
	 */
	public TY_AvgPE()
	{
		super();
		this.adjustedPEList = new ArrayList<TY_PE>();
		// this.setPeAdjusted(false);
	}

}
