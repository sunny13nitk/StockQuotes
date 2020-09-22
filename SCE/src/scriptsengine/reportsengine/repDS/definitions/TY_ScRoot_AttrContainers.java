package scriptsengine.reportsengine.repDS.definitions;

import java.util.ArrayList;

import scriptsengine.pojos.OB_Scrip_General;
import scriptsengine.statistics.definitions.TY_AttribSingleVal;

public class TY_ScRoot_AttrContainers
{

	private OB_Scrip_General				scRoot;

	private ArrayList<TY_Attr_Container>	attrContainers;

	private ArrayList<TY_AttribSingleVal>	singleValContainers;

	/**
	 * @return the singleValContainers
	 */
	public ArrayList<TY_AttribSingleVal> getSingleValContainers()
	{
		return singleValContainers;
	}

	/**
	 * @param singleValContainers
	 *             the singleValContainers to set
	 */
	public void setSingleValContainers(ArrayList<TY_AttribSingleVal> singleValContainers)
	{
		this.singleValContainers = singleValContainers;
	}

	/**
	 * @return the scRoot
	 */
	public OB_Scrip_General getScRoot()
	{
		return scRoot;
	}

	/**
	 * @param scRoot
	 *             the scRoot to set
	 */
	public void setScRoot(OB_Scrip_General scRoot)
	{
		this.scRoot = scRoot;
	}

	/**
	 * @return the attrContainers
	 */
	public ArrayList<TY_Attr_Container> getAttrContainers()
	{
		return attrContainers;
	}

	/**
	 * @param attrContainers
	 *             the attrContainers to set
	 */
	public void setAttrContainers(ArrayList<TY_Attr_Container> attrContainers)
	{
		this.attrContainers = attrContainers;
	}

	/**
	 * @param scRoot
	 * @param attrContainers
	 */
	public TY_ScRoot_AttrContainers(OB_Scrip_General scRoot, ArrayList<TY_Attr_Container> attrContainers)
	{
		super();
		this.scRoot = scRoot;
		this.attrContainers = new ArrayList<TY_Attr_Container>();
		this.singleValContainers = new ArrayList<TY_AttribSingleVal>();
	}

	/**
	 * 
	 */
	public TY_ScRoot_AttrContainers()
	{
		super();
		this.attrContainers = new ArrayList<TY_Attr_Container>();
		this.singleValContainers = new ArrayList<TY_AttribSingleVal>();
	}

}
