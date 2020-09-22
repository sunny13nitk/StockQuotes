package scriptsengine.statistics.definitions;

public class TY_AttribKeyFigure
{
	private String	Attrib;
	private int	Key;
	private double	Figure;

	/**
	 * @return the attrib
	 */
	public String getAttrib()
	{
		return Attrib;
	}

	/**
	 * @param attrib
	 *             the attrib to set
	 */
	public void setAttrib(String attrib)
	{
		Attrib = attrib;
	}

	/**
	 * @return the key
	 */
	public int getKey()
	{
		return Key;
	}

	/**
	 * @param key
	 *             the key to set
	 */
	public void setKey(int key)
	{
		Key = key;
	}

	/**
	 * @return the figure
	 */
	public double getFigure()
	{
		return Figure;
	}

	/**
	 * @param figure
	 *             the figure to set
	 */
	public void setFigure(double figure)
	{
		Figure = figure;
	}

	/**
	 * @param attrib
	 * @param key
	 * @param figure
	 */
	public TY_AttribKeyFigure(String attrib, int key, double figure)
	{
		super();
		Attrib = attrib;
		Key = key;
		Figure = figure;
	}

	/**
	 * 
	 */
	public TY_AttribKeyFigure()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
