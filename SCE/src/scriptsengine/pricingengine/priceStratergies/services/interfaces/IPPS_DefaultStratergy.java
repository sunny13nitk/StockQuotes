package scriptsengine.pricingengine.priceStratergies.services.interfaces;

import scriptsengine.pricingengine.definitions.TY_PricesReturn;
import scriptsengine.pricingengine.services.interfaces.ISA_ScripPriceProjectionService;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Default Price Projection Stratergy
 */
public interface IPPS_DefaultStratergy
{
	/**
	 * Get Projected Price for an Instance of Price Projection Service
	 * 
	 * @param ppSrv
	 *             - Price Projection Service Instance
	 * @return - Prices Return Structure
	 * @throws EX_General
	 *              - Exception
	 */
	public TY_PricesReturn getPriceProjections(ISA_ScripPriceProjectionService ppSrv) throws EX_General;
}
