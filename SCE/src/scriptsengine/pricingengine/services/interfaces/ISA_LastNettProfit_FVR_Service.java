package scriptsengine.pricingengine.services.interfaces;

import scriptsengine.pricingengine.definitions.TY_Last_NP_FVR;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * Scrip Analytics - Last Nett Profit and Face Value Ratio Service
 *
 */

public interface ISA_LastNettProfit_FVR_Service
{

	public TY_Last_NP_FVR getLastNettProfit_FVRforScripCode(String scCode) throws EX_General;

}
