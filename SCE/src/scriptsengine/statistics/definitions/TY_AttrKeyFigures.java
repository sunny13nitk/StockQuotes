package scriptsengine.statistics.definitions;

import java.util.ArrayList;

public class TY_AttrKeyFigures
{
	private String					attrName;
	private ArrayList<TY_KeyFigure>	keyFigs;

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
	 * @return the keyFigs
	 */
	public ArrayList<TY_KeyFigure> getKeyFigs()
	{
		return keyFigs;
	}

	/**
	 * @param keyFigs
	 *             the keyFigs to set
	 */
	public void setKeyFigs(ArrayList<TY_KeyFigure> keyFigs)
	{
		this.keyFigs = keyFigs;
	}

	/**
	 * @param attrName
	 * @param keyFigs
	 */
	public TY_AttrKeyFigures(String attrName, ArrayList<TY_KeyFigure> keyFigs)
	{
		super();
		this.attrName = attrName;
		this.keyFigs = new ArrayList<TY_KeyFigure>();
	}

	/**
	 * 
	 */
	public TY_AttrKeyFigures()
	{
		super();
		this.keyFigs = new ArrayList<TY_KeyFigure>();
	}

}
