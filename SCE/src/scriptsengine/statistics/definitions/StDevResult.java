package scriptsengine.statistics.definitions;

public class StDevResult
{
	private String	title;
	private double	rsd;
	private double	spread_high;

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
	 * 
	 */
	public StDevResult()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param title
	 * @param rsd
	 * @param spread_high
	 */
	public StDevResult(String title, double rsd, double spread_high)
	{
		super();
		this.title = title;
		this.rsd = rsd;
		this.spread_high = spread_high;
	}

}
