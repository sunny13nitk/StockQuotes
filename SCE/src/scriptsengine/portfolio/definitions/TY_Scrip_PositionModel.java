package scriptsengine.portfolio.definitions;

import java.util.ArrayList;

import scriptsengine.portfolio.pojos.OB_Positions_Header;
import scriptsengine.portfolio.pojos.OB_Positions_Item;

/**
 * 
 * Scrip Position Model which will hold Scrip Position Header and corresponding Items in one Container
 *
 */
public class TY_Scrip_PositionModel
{
	private OB_Positions_Header			scPosHeader;
	private ArrayList<OB_Positions_Item>	scPosItems;

	/**
	 * @return the scPosHeader
	 */
	public OB_Positions_Header getScPosHeader()
	{
		return scPosHeader;
	}

	/**
	 * @param scPosHeader
	 *             the scPosHeader to set
	 */
	public void setScPosHeader(OB_Positions_Header scPosHeader)
	{
		this.scPosHeader = scPosHeader;
	}

	/**
	 * @return the scPosItems
	 */
	public ArrayList<OB_Positions_Item> getScPosItems()
	{
		return scPosItems;
	}

	/**
	 * @param scPosItems
	 *             the scPosItems to set
	 */
	public void setScPosItems(ArrayList<OB_Positions_Item> scPosItems)
	{
		this.scPosItems = scPosItems;
	}

	/**
	 * 
	 */
	public TY_Scrip_PositionModel()
	{
		super();
		this.scPosItems = new ArrayList<OB_Positions_Item>();
	}

	/**
	 * @param scPosHeader
	 * @param scPosItems
	 */
	public TY_Scrip_PositionModel(OB_Positions_Header scPosHeader, ArrayList<OB_Positions_Item> scPosItems)
	{
		super();
		this.scPosHeader = scPosHeader;
		this.scPosItems = new ArrayList<OB_Positions_Item>();
		if (scPosItems != null)
		{
			this.scPosItems = scPosItems;
		}
	}

}
