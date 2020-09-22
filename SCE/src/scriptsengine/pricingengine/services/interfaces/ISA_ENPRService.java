package scriptsengine.pricingengine.services.interfaces;

import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * Scrip Analytics - EPNR Service Interface
 *
 */
public interface ISA_ENPRService
{

	public double getAvgENPRforScripCode(String scCode) throws EX_General;
}
