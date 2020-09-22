package scriptsengine.reportsengine.definitions;

/**
 * 
 * Statistics for Sectors Averages for different Attributes
 */
public class Ty_SectorAvgStats
{
	public String	sector;
	private String	attrName;
	private String	attrLabel;
	private double	avgAvg;
	private double	avgDeltaAvg;

	/**
	 * @return the sector
	 */
	public String getSector()
	{
		return sector;
	}

	/**
	 * @param sector
	 *             the sector to set
	 */
	public void setSector(String sector)
	{
		this.sector = sector;
	}

	/**
	 * @return the attrName
	 */
	public String getAttrName()
	{
		return attrName;
	}

	/**
	 * @param attrName
	 *             the attrName to set
	 */
	public void setAttrName(String attrName)
	{
		this.attrName = attrName;
	}

	/**
	 * @return the attrLabel
	 */
	public String getAttrLabel()
	{
		return attrLabel;
	}

	/**
	 * @param attrLabel
	 *             the attrLabel to set
	 */
	public void setAttrLabel(String attrLabel)
	{
		this.attrLabel = attrLabel;
	}

	/**
	 * @return the avgAvg
	 */
	public double getAvgAvg()
	{
		return avgAvg;
	}

	/**
	 * @param avgAvg
	 *             the avgAvg to set
	 */
	public void setAvgAvg(double avgAvg)
	{
		this.avgAvg = avgAvg;
	}

	/**
	 * @return the avgDeltaAvg
	 */
	public double getAvgDeltaAvg()
	{
		return avgDeltaAvg;
	}

	/**
	 * @param avgDeltaAvg
	 *             the avgDeltaAvg to set
	 */
	public void setAvgDeltaAvg(double avgDeltaAvg)
	{
		this.avgDeltaAvg = avgDeltaAvg;
	}

	/**
	 * @param sector
	 * @param attrName
	 * @param attrLabel
	 * @param avgAvg
	 * @param avgDeltaAvg
	 */
	public Ty_SectorAvgStats(String sector, String attrName, String attrLabel, double avgAvg, double avgDeltaAvg)
	{
		super();
		this.sector = sector;
		this.attrName = attrName;
		this.attrLabel = attrLabel;
		this.avgAvg = avgAvg;
		this.avgDeltaAvg = avgDeltaAvg;
	}

	/**
	 * 
	 */
	public Ty_SectorAvgStats()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
