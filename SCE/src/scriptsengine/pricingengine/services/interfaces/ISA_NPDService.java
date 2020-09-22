package scriptsengine.pricingengine.services.interfaces;

import scriptsengine.pricingengine.definitions.TY_NPSD;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * Nett Profit Delta Service Interface
 *
 */
public interface ISA_NPDService
{
	public TY_NPSD getNettProfitDeltaforScripCode(String scCode) throws EX_General;

}
