package scriptsengine.pricingengine.reports.interfaces;

import java.util.ArrayList;

import scriptsengine.pricingengine.reports.definitions.TY_PriceWatchItem;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * Price Watch Service Interface
 *
 */
public interface IPriceWatchService
{

	public ArrayList<TY_PriceWatchItem> computeProjectedPrices() throws EX_General;

	public void computeandShowProjectedPrices_Console() throws EX_General;

	public void computeandDLProjectedPrices_XLS(String filepath) throws EX_General;

	public void generateDetailedReport_XLS(String filepath) throws EX_General;

	public TY_PriceWatchItem computeProjectedPriceforScripdesc(String scripDescStartsWith) throws EX_General;

	public TY_PriceWatchItem computeProjectedPriceforScCode(String scCode) throws EX_General;

}
