package scriptsengine.reportsengine.definitions;

public class TY_ColStats
{
	private int	numCols;
	private int	widthCol;

	/**
	 * @return the numCols
	 */
	public int getNumCols()
	{
		return numCols;
	}

	/**
	 * @param numCols
	 *             the numCols to set
	 */
	public void setNumCols(int numCols)
	{
		this.numCols = numCols;
	}

	/**
	 * @return the widthCol
	 */
	public int getWidthCol()
	{
		return widthCol;
	}

	/**
	 * @param widthCol
	 *             the widthCol to set
	 */
	public void setWidthCol(int widthCol)
	{
		this.widthCol = widthCol;
	}

	/**
	 * 
	 */
	public TY_ColStats()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param numCols
	 * @param widthCol
	 */
	public TY_ColStats(int numCols, int widthCol)
	{
		super();
		this.numCols = numCols;
		this.widthCol = widthCol;
	}

}
