package scriptsengine.pricingengine.reports.definitions;

import java.util.ArrayList;

import scriptsengine.pojos.OB_Scrip_QuartersData;
import scriptsengine.pricingengine.definitions.TY_PricesReturn;

/**
 * Composite data type for Price Projection Default stratergy Reporting
 *
 */
public class TY_ReportPPS_Default
{
	private ArrayList<TY_DefaultPriceForecast>	pfItems;

	private ArrayList<OB_Scrip_QuartersData>	penultimateYdata;

	private ArrayList<OB_Scrip_QuartersData>	currentYdata;

	private TY_PricesReturn					pricesReturn;

	/**
	 * @return the pfItems
	 */
	public ArrayList<TY_DefaultPriceForecast> getPfItems()
	{
		return pfItems;
	}

	/**
	 * @param pfItems
	 *             the pfItems to set
	 */
	public void setPfItems(ArrayList<TY_DefaultPriceForecast> pfItems)
	{
		this.pfItems = pfItems;
	}

	/**
	 * @return the penultimateYdata
	 */
	public ArrayList<OB_Scrip_QuartersData> getPenultimateYdata()
	{
		return penultimateYdata;
	}

	/**
	 * @param penultimateYdata
	 *             the penultimateYdata to set
	 */
	public void setPenultimateYdata(ArrayList<OB_Scrip_QuartersData> penultimateYdata)
	{
		this.penultimateYdata = penultimateYdata;
	}

	/**
	 * @return the currentYdata
	 */
	public ArrayList<OB_Scrip_QuartersData> getCurrentYdata()
	{
		return currentYdata;
	}

	/**
	 * @param currentYdata
	 *             the currentYdata to set
	 */
	public void setCurrentYdata(ArrayList<OB_Scrip_QuartersData> currentYdata)
	{
		this.currentYdata = currentYdata;
	}

	/**
	 * @return the pricesReturn
	 */
	public TY_PricesReturn getPricesReturn()
	{
		return pricesReturn;
	}

	/**
	 * @param pricesReturn
	 *             the pricesReturn to set
	 */
	public void setPricesReturn(TY_PricesReturn pricesReturn)
	{
		this.pricesReturn = pricesReturn;
	}

	/**
	 * @param pfItems
	 * @param penultimateYdata
	 * @param currentYdata
	 * @param pricesReturn
	 */
	public TY_ReportPPS_Default(ArrayList<TY_DefaultPriceForecast> pfItems, ArrayList<OB_Scrip_QuartersData> penultimateYdata,
	          ArrayList<OB_Scrip_QuartersData> currentYdata, TY_PricesReturn pricesReturn)
	{
		super();
		this.pfItems = pfItems;
		this.penultimateYdata = penultimateYdata;
		this.currentYdata = currentYdata;
		this.pricesReturn = pricesReturn;
	}

	/**
	 * Blank Constructor
	 */
	public TY_ReportPPS_Default()
	{
		this.pfItems = new ArrayList<TY_DefaultPriceForecast>();
		this.penultimateYdata = new ArrayList<OB_Scrip_QuartersData>();
		this.currentYdata = new ArrayList<OB_Scrip_QuartersData>();
	}

}
