package scriptsengine.pricingengine.services.interfaces;

import java.util.ArrayList;

import scriptsengine.pricingengine.definitions.TY_AvgPE;
import scriptsengine.pricingengine.definitions.TY_Last_NP_FVR;
import scriptsengine.pricingengine.definitions.TY_NPSD;
import scriptsengine.pricingengine.definitions.TY_PFactor;
import scriptsengine.pricingengine.definitions.TY_PFactorDetail;
import scriptsengine.pricingengine.definitions.TY_PricesReturn;
import scriptsengine.pricingengine.definitions.TY_RawMPP_Stats;
import scriptsengine.pricingengine.reports.definitions.TY_ReportPPS_Default;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Interface for Scrip Pricing Service
 */
public interface ISA_ScripPriceProjectionService
{

	public String getScCode();

	public TY_Last_NP_FVR getLastNp_FVR();

	public TY_AvgPE getAvgPE();

	public double getAvgENPR();

	public TY_NPSD getNpd();

	public double getDMA_200();

	public double getPromoterHolding();

	public ArrayList<TY_PFactor> getProjectionFactors();

	public ArrayList<TY_PricesReturn> getProjectedPrices();

	public ArrayList<TY_PricesReturn> getPriceProjectionsforScrip(String scCode) throws EX_General;

	public ArrayList<TY_PricesReturn> getPriceProjectionsforScrip_byDescription(String scripDescPattern) throws EX_General;

	public void addFactorforPriceCalculation(TY_PFactor pFactor) throws EX_General;

	public void addFactorItemforPriceFactor(String factorType, TY_PFactorDetail factorItem) throws EX_General;

	/**
	 * Price Forecast Stats Methods -----------------------------------------------------------------------
	 */
	public ArrayList<TY_RawMPP_Stats> getRawMstats();

	public TY_ReportPPS_Default getDefPstats() throws EX_General;
	/**
	 * Price Forecast Stats Methods -----------------------------------------------------------------------
	 */
}
