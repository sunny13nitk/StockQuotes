package scriptsengine.pricingengine.services.interfaces;

import scriptsengine.pricingengine.definitions.TY_AvgPE;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * Scrip Analytics - Average PE for a Scrip Service Interface
 *
 */
public interface ISA_AvgPEService
{
	public TY_AvgPE getPERatiosforScripCode(String scCode) throws EX_General;

}
