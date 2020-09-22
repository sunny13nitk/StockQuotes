package scriptsengine.statistics.definitions;

public class TY_scCodeKeyFigure
{
	private String	scCode;
	private int	key;
	private double	figure;

	/**
	 * @return the scCode
	 */
	public String getScCode()
	{
		return scCode;
	}

	/**
	 * @param scCode
	 *             the scCode to set
	 */
	public void setScCode(String scCode)
	{
		this.scCode = scCode;
	}

	/**
	 * @return the key
	 */
	public int getKey()
	{
		return key;
	}

	/**
	 * @param key
	 *             the key to set
	 */
	public void setKey(int key)
	{
		this.key = key;
	}

	/**
	 * @return the figure
	 */
	public double getFigure()
	{
		return figure;
	}

	/**
	 * @param figure
	 *             the figure to set
	 */
	public void setFigure(double figure)
	{
		this.figure = figure;
	}

	/**
	 * 
	 */
	public TY_scCodeKeyFigure()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param scCode
	 * @param key
	 * @param figure
	 */
	public TY_scCodeKeyFigure(String scCode, int key, double figure)
	{
		super();
		this.scCode = scCode;
		this.key = key;
		this.figure = figure;
	}

}
