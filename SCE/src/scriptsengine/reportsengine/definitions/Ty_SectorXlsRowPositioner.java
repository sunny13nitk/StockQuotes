package scriptsengine.reportsengine.definitions;

public class Ty_SectorXlsRowPositioner
{
	private String	sectorName;
	private int	fromRow;
	private int	toRow;

	/**
	 * @return the sectorName
	 */
	public String getSectorName()
	{
		return sectorName;
	}

	/**
	 * @param sectorName
	 *             the sectorName to set
	 */
	public void setSectorName(String sectorName)
	{
		this.sectorName = sectorName;
	}

	/**
	 * @return the fromRow
	 */
	public int getFromRow()
	{
		return fromRow;
	}

	/**
	 * @param fromRow
	 *             the fromRow to set
	 */
	public void setFromRow(int fromRow)
	{
		this.fromRow = fromRow;
	}

	/**
	 * @return the toRow
	 */
	public int getToRow()
	{
		return toRow;
	}

	/**
	 * @param toRow
	 *             the toRow to set
	 */
	public void setToRow(int toRow)
	{
		this.toRow = toRow;
	}

	/**
	 * @param sectorName
	 * @param fromRow
	 * @param toRow
	 */
	public Ty_SectorXlsRowPositioner(String sectorName, int fromRow, int toRow)
	{
		super();
		this.sectorName = sectorName;
		this.fromRow = fromRow;
		this.toRow = toRow;
	}

	/**
	 * 
	 */
	public Ty_SectorXlsRowPositioner()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
