package scriptsengine.pricingengine.reports.interfaces;

import scriptsengine.pricingengine.reports.definitions.TY_ReportPPS_Default;
import scriptsengine.pricingengine.services.interfaces.ISA_ScripPriceProjectionService;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Service Interface for Default Pricing Stratergy Statistics
 * Since this statistics involves multiple data types and computations it is spererated as a separate Service
 * Need to provide Pricing projection Stratergy Interface BEan Instance
 */
public interface IDefaultPriceStratergyStats
{
	public TY_ReportPPS_Default getDefaultPriceProjection_Stats(ISA_ScripPriceProjectionService ppsrv) throws EX_General;
}
