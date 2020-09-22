package scriptsengine.statistics.definitions;

import java.util.ArrayList;

public class TY_Attr_ScKeyFigsContainer
{
	private String						attrName;
	private ArrayList<TY_scCodeKeyFigure>	scCodeKeyFigures;

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
	 * @return the scCodeKeyFigures
	 */
	public ArrayList<TY_scCodeKeyFigure> getScCodeKeyFigures()
	{
		return scCodeKeyFigures;
	}

	/**
	 * @param scCodeKeyFigures
	 *             the scCodeKeyFigures to set
	 */
	public void setScCodeKeyFigures(ArrayList<TY_scCodeKeyFigure> scCodeKeyFigures)
	{
		this.scCodeKeyFigures = scCodeKeyFigures;
	}

	/**
	 * 
	 */
	public TY_Attr_ScKeyFigsContainer()
	{
		super();
		this.scCodeKeyFigures = new ArrayList<TY_scCodeKeyFigure>();
	}

	/**
	 * @param attrName
	 * @param scCodeKeyFigures
	 */
	public TY_Attr_ScKeyFigsContainer(String attrName, ArrayList<TY_scCodeKeyFigure> scCodeKeyFigures)
	{
		super();
		this.attrName = attrName;
		this.scCodeKeyFigures = new ArrayList<TY_scCodeKeyFigure>();
	}

}
