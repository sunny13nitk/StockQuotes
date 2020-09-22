package scriptsengine.pricingengine.priceStratergies.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import scriptsengine.constants.PriceProjectionFactorsConstants;
import scriptsengine.enums.SCEenums.direction;
import scriptsengine.pricingengine.definitions.TY_PFactor;
import scriptsengine.pricingengine.definitions.TY_PFactorDetail;
import scriptsengine.pricingengine.definitions.TY_PricesReturn;
import scriptsengine.pricingengine.priceStratergies.services.interfaces.IPPS_DefaultStratergy;
import scriptsengine.pricingengine.services.interfaces.ISA_ScripPriceProjectionService;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.utilities.interfaces.IPercentageAdjustService;

/**
 * Price Projection Service for External Factors
 *
 */

@Service("PPS_ExternalFactorsService")
public class PPS_ExternalFactorsService implements IPPS_DefaultStratergy
{
	@Autowired
	private IPercentageAdjustService perSrv;

	@Override
	public TY_PricesReturn getPriceProjections(ISA_ScripPriceProjectionService ppSrv) throws EX_General
	{
		TY_PricesReturn pRet = new TY_PricesReturn();
		pRet.setPriceType(PriceProjectionFactorsConstants.External);
		TY_PFactor pFactor = null;

		double nettFactor = 0;
		double tmpCYPP = 0;

		if (ppSrv != null)
		{

			/**
			 * First retrieve the correct Price Projection Factor
			 */
			pFactor = ppSrv.getProjectionFactors().stream().filter(x -> x.getFactorType().equals(PriceProjectionFactorsConstants.External))
			          .findFirst().get();
			if (pFactor != null)
			{

				for ( TY_PFactorDetail factordetail : pFactor.getFactoritems() )
				{
					if (factordetail.getDirection() == direction.DECREASE)
					{
						nettFactor = nettFactor - factordetail.getPercentage();
					}
					else if (factordetail.getDirection() == direction.INCREASE)
					{
						nettFactor = nettFactor + factordetail.getPercentage();
					}
				}

				// CYPP
				tmpCYPP = (ppSrv.getLastNp_FVR().getLastNettProfit()) * (1 + (ppSrv.getNpd().getNPD() / 100));

				if (nettFactor != 0)
				{
					pRet.setCYPP(perSrv.adjustPercentagetoFigure(tmpCYPP, nettFactor));
				}

				else
				{
					pRet.setCYPP(tmpCYPP);
				}

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
		}

		return pRet;

	}

}
