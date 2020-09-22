package scriptsengine.pricingengine.priceStratergies.services.implementations;

import org.springframework.stereotype.Service;

import scriptsengine.constants.PriceProjectionFactorsConstants;
import scriptsengine.pricingengine.definitions.TY_PricesReturn;
import scriptsengine.pricingengine.priceStratergies.services.interfaces.IPPS_DefaultStratergy;
import scriptsengine.pricingengine.services.interfaces.ISA_ScripPriceProjectionService;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Default Price Projection Stratergy
 *
 */
@Service("PPS_DefaultStratergy")
public class PPS_DefaultStratergy implements IPPS_DefaultStratergy
{

	@Override
	public TY_PricesReturn getPriceProjections(ISA_ScripPriceProjectionService ppSrv) throws EX_General
	{

		TY_PricesReturn pRet = new TY_PricesReturn();
		pRet.setPriceType(PriceProjectionFactorsConstants.DefaultPricing);

		if (ppSrv != null)
		{
			// CYPP
			pRet.setCYPP((ppSrv.getLastNp_FVR().getLastNettProfit()) * (1 + (ppSrv.getNpd().getNPD() / 100)));

			// CYPEPUS
			pRet.setCYPEPUS((pRet.getCYPP() * ppSrv.getAvgENPR()) / 100);

			// ------------------------- Projected Prices-----------------------

			// Average PE Projected Price
			pRet.getProjectedPrices().setAvgPE_PP(
			          pRet.getCYPEPUS() * ppSrv.getLastNp_FVR().getCurrFV() * ppSrv.getAvgPE().getAvgPE() * ppSrv.getLastNp_FVR().getFVR());

			// Max PE Projected Price
			pRet.getProjectedPrices().setMaxPE_PP(
			          pRet.getCYPEPUS() * ppSrv.getLastNp_FVR().getCurrFV() * ppSrv.getAvgPE().getMaxPE() * ppSrv.getLastNp_FVR().getFVR());

			// Min PE Projected Price
			pRet.getProjectedPrices().setMinPE_PP(
			          pRet.getCYPEPUS() * ppSrv.getLastNp_FVR().getCurrFV() * ppSrv.getAvgPE().getMinPE() * ppSrv.getLastNp_FVR().getFVR());

			// If PE is adjusted
			pRet.getProjectedPrices().setPeAdjusted(ppSrv.getAvgPE().isPeAdjusted());
		}

		return pRet;
	}

}
