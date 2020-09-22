package scriptsengine.reportsengine.definitions;

/**
 * 
 * Class to hold Sectors and corresponding Scrips count in the specific sector
 */
public class Ty_SectorCount
{
	private String	sector;
	private int	numScrips;

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
	 * @return the numScrips
	 */
	public int getNumScrips()
	{
		return numScrips;
	}

	/**
	 * @param numScrips
	 *             the numScrips to set
	 */
	public void setNumScrips(int numScrips)
	{
		this.numScrips = numScrips;
	}

	/**
	 * @param sector
	 * @param numScrips
	 */
	public Ty_SectorCount(String sector, int numScrips)
	{
		super();
		this.sector = sector;
		this.numScrips = numScrips;
	}

	/**
	 * 
	 */
	public Ty_SectorCount()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
