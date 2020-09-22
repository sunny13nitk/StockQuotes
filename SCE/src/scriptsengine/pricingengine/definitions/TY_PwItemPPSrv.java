package scriptsengine.pricingengine.definitions;

import scriptsengine.pricingengine.reports.definitions.TY_PriceWatchItem;
import scriptsengine.pricingengine.services.interfaces.ISA_ScripPriceProjectionService;

/**
 * Price Watch Item and Scrip Price Projection Service Instance POJO
 *
 */
public class TY_PwItemPPSrv
{
	private TY_PriceWatchItem			pwItem;
	private ISA_ScripPriceProjectionService	scppSrv;

	/**
	 * @return the pwItem
	 */
	public TY_PriceWatchItem getPwItem()
	{
		return pwItem;
	}

	/**
	 * @param pwItem
	 *             the pwItem to set
	 */
	public void setPwItem(TY_PriceWatchItem pwItem)
	{
		this.pwItem = pwItem;
	}

	/**
	 * @return the scppSrv
	 */
	public ISA_ScripPriceProjectionService getScppSrv()
	{
		return scppSrv;
	}

	/**
	 * @param scppSrv
	 *             the scppSrv to set
	 */
	public void setScppSrv(ISA_ScripPriceProjectionService scppSrv)
	{
		this.scppSrv = scppSrv;
	}

	/**
	 * 
	 */
	public TY_PwItemPPSrv()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param pwItem
	 * @param scppSrv
	 */
	public TY_PwItemPPSrv(TY_PriceWatchItem pwItem, ISA_ScripPriceProjectionService scppSrv)
	{
		super();
		this.pwItem = pwItem;
		this.scppSrv = scppSrv;
	}

}
