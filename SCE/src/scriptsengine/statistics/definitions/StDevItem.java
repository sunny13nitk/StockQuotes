package scriptsengine.statistics.definitions;

/**
 * 
 * Standard deviation Item defintion to hold itemized values
 *
 */

public class StDevItem
{
	private String	item_title;
	private double	val1;
	private double	val2;
	private double	average;
	private double	deviation;
	private double	deviation_sq;

	/**
	 * @return the item_title
	 */
	public String getItem_title()
	{
		return item_title;
	}

	/**
	 * @param item_title
	 *             the item_title to set
	 */
	public void setItem_title(String item_title)
	{
		this.item_title = item_title;
	}

	/**
	 * @return the val1
	 */
	public double getVal1()
	{
		return val1;
	}

	/**
	 * @param val1
	 *             the val1 to set
	 */
	public void setVal1(double val1)
	{
		this.val1 = val1;
	}

	/**
	 * @return the val2
	 */
	public double getVal2()
	{
		return val2;
	}

	/**
	 * @param val2
	 *             the val2 to set
	 */
	public void setVal2(double val2)
	{
		this.val2 = val2;
	}

	/**
	 * @return the average
	 */
	public double getAverage()
	{
		return average;
	}

	/**
	 * @param average
	 *             the average to set
	 */
	public void setAverage(double average)
	{
		this.average = average;
	}

	/**
	 * @return the deviation
	 */
	public double getDeviation()
	{
		return deviation;
	}

	/**
	 * @param deviation
	 *             the deviation to set
	 */
	public void setDeviation(double deviation)
	{
		this.deviation = deviation;
	}

	/**
	 * @return the deviation_sq
	 */
	public double getDeviation_sq()
	{
		return deviation_sq;
	}

	/**
	 * @param deviation_sq
	 *             the deviation_sq to set
	 */
	public void setDeviation_sq(double deviation_sq)
	{
		this.deviation_sq = deviation_sq;
	}

	/**
	 * @param item_title
	 * @param val1
	 * @param val2
	 * @param average
	 * @param deviation
	 * @param deviation_sq
	 */
	public StDevItem(String item_title, double val1, double val2, double average, double deviation, double deviation_sq)
	{
		super();
		this.item_title = item_title;
		this.val1 = val1;
		this.val2 = val2;
		this.average = average;
		this.deviation = deviation;
		this.deviation_sq = deviation_sq;
	}

	/**
	 * @param item_title
	 * @param val1
	 * @param val2
	 */
	public StDevItem(String item_title, double val1, double val2)
	{
		super();
		this.item_title = item_title;
		this.val1 = val1;
		this.val2 = val2;
	}

	/**
	 * @param item_title
	 * @param average
	 */
	public StDevItem(String item_title, double average)
	{
		super();
		this.item_title = item_title;
		this.average = average;
	}

	/**
	 * 
	 */
	public StDevItem()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
