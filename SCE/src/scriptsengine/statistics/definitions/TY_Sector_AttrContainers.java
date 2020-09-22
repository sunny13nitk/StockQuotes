package scriptsengine.statistics.definitions;

import java.util.ArrayList;

/**
 * 
 * Final Output Of Market share Attribute Analytics Service for a Given Sector
 *
 */
public class TY_Sector_AttrContainers
{
	private String								sector;
	private ArrayList<TY_Attr_ScKeyFigsContainer>	attrSp_scKeyFigs;

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
	 * @return the attrSp_scKeyFigs
	 */
	public ArrayList<TY_Attr_ScKeyFigsContainer> getAttrSp_scKeyFigs()
	{
		return attrSp_scKeyFigs;
	}

	/**
	 * @param attrSp_scKeyFigs
	 *             the attrSp_scKeyFigs to set
	 */
	public void setAttrSp_scKeyFigs(ArrayList<TY_Attr_ScKeyFigsContainer> attrSp_scKeyFigs)
	{
		this.attrSp_scKeyFigs = attrSp_scKeyFigs;
	}

	/**
	 * 
	 */
	public TY_Sector_AttrContainers()
	{
		super();
		this.attrSp_scKeyFigs = new ArrayList<TY_Attr_ScKeyFigsContainer>();
	}

	/**
	 * @param sector
	 * @param attrSp_scKeyFigs
	 */
	public TY_Sector_AttrContainers(String sector, ArrayList<TY_Attr_ScKeyFigsContainer> attrSp_scKeyFigs)
	{
		super();
		this.sector = sector;
		this.attrSp_scKeyFigs = new ArrayList<TY_Attr_ScKeyFigsContainer>();
		if (attrSp_scKeyFigs != null)
		{
			this.attrSp_scKeyFigs = attrSp_scKeyFigs;
		}
	}

}
