package scriptsengine.statistics.definitions;

public class TY_KeyFigure
{

	private int	key;
	private double	figure;

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
	public TY_KeyFigure()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param key
	 * @param figure
	 */
	public TY_KeyFigure(int key, double figure)
	{
		super();
		this.key = key;
		this.figure = figure;
	}

}
