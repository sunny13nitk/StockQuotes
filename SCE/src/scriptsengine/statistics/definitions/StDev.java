package scriptsengine.statistics.definitions;

import java.util.ArrayList;

public class StDev
{
	private ArrayList<StDevHead> deviationHeads;

	/**
	 * @return the deviationHeads
	 */
	public ArrayList<StDevHead> getDeviationHeads()
	{
		return deviationHeads;
	}

	/**
	 * @param deviationHeads
	 *             the deviationHeads to set
	 */
	public void setDeviationHeads(ArrayList<StDevHead> deviationHeads)
	{
		this.deviationHeads = deviationHeads;
	}

	/**
	 * 
	 */
	public StDev()
	{
		this.deviationHeads = new ArrayList<StDevHead>();
	}

	public void addStdevHeader(StDevHead devHead)
	{
		this.deviationHeads.add(devHead);
	}
}
