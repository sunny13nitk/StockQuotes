package scriptsengine.statistics.definitions;

public class TY_DeltaFigure
{
	private String	deltaKey;
	private double	figure;

	/**
	 * @return the deltaKey
	 */
	public String getDeltaKey()
	{
		return deltaKey;
	}

	/**
	 * @param deltaKey
	 *             the deltaKey to set
	 */
	public void setDeltaKey(String deltaKey)
	{
		this.deltaKey = deltaKey;
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
	 * @param deltaKey
	 * @param figure
	 */
	public TY_DeltaFigure(String deltaKey, double figure)
	{
		super();
		this.deltaKey = deltaKey;
		this.figure = figure;
	}

	/**
	 * 
	 */
	public TY_DeltaFigure()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
