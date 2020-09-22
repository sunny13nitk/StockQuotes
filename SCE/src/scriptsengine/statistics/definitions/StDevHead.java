package scriptsengine.statistics.definitions;

import java.util.ArrayList;

/**
 * 
 * Standard Deviation Header to hold one set of data to compute Standard deviation on
 *
 */
public class StDevHead
{
	private String				title;
	private double				mean;
	private double				deviation_average;
	private double				standard_deviation;
	private double				rsd;
	private double				spread_high;
	private ArrayList<StDevItem>	items;

	/**
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title
	 *             the title to set
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * @return the mean
	 */
	public double getMean()
	{
		return mean;
	}

	/**
	 * @param mean
	 *             the mean to set
	 */
	public void setMean(double mean)
	{
		this.mean = mean;
	}

	/**
	 * @return the deviation_average
	 */
	public double getDeviation_average()
	{
		return deviation_average;
	}

	/**
	 * @param deviation_average
	 *             the deviation_average to set
	 */
	public void setDeviation_average(double deviation_average)
	{
		this.deviation_average = deviation_average;
	}

	/**
	 * @return the standard_deviation
	 */
	public double getStandard_deviation()
	{
		return standard_deviation;
	}

	/**
	 * @param standard_deviation
	 *             the standard_deviation to set
	 */
	public void setStandard_deviation(double standard_deviation)
	{
		this.standard_deviation = standard_deviation;
	}

	/**
	 * @return the rsd
	 */
	public double getRsd()
	{
		return rsd;
	}

	/**
	 * @param rsd
	 *             the rsd to set
	 */
	public void setRsd(double rsd)
	{
		this.rsd = rsd;
	}

	/**
	 * @return the spread_high
	 */
	public double getSpread_high()
	{
		return spread_high;
	}

	/**
	 * @param spread_high
	 *             the spread_high to set
	 */
	public void setSpread_high(double spread_high)
	{
		this.spread_high = spread_high;
	}

	/**
	 * @return the items
	 */
	public ArrayList<StDevItem> getItems()
	{
		return items;
	}

	/**
	 * @param items
	 *             the items to set
	 */
	public void setItems(ArrayList<StDevItem> items)
	{
		this.items = items;
	}

	public void addStdevItem(StDevItem devitem)
	{
		this.items.add(devitem);
	}

	/**
	 * @param title
	 * @param mean
	 * @param deviation_average
	 * @param standard_deviation
	 * @param rsd
	 * @param spread_high
	 * @param items
	 */
	public StDevHead(String title, double mean, double deviation_average, double standard_deviation, double rsd, double spread_high,
	          ArrayList<StDevItem> items)
	{
		super();
		this.title = title;
		this.mean = mean;
		this.deviation_average = deviation_average;
		this.standard_deviation = standard_deviation;
		this.rsd = rsd;
		this.spread_high = spread_high;
		this.items = items;
	}

	/**
	 * @param title
	 */
	public StDevHead(String title)
	{
		super();
		this.title = title;
		this.items = new ArrayList<StDevItem>();
	}

}
