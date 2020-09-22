package scriptsengine.pricingengine.priceStratergies.services.implementations;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import scriptsengine.constants.PriceProjectionFactorsConstants;
import scriptsengine.constants.SheetNamesConstants;
import scriptsengine.enums.SCEenums.direction;
import scriptsengine.pojos.OB_Scrip_BalSheet;
import scriptsengine.pricingengine.definitions.TY_PFactor;
import scriptsengine.pricingengine.definitions.TY_PFactorDetail;
import scriptsengine.pricingengine.definitions.TY_PricesReturn;
import scriptsengine.pricingengine.priceStratergies.services.interfaces.IPPS_DefaultStratergy;
import scriptsengine.pricingengine.services.interfaces.ISA_ScripPriceProjectionService;
import scriptsengine.uploadengine.definitions.ScripDataContainer;
import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.services.interfaces.IScripDataContainerSrv;
import scriptsengine.utilities.interfaces.IPercentageAdjustService;
import scriptsengine.utilities.types.PenultimateQYear;

/**
 * 
 * Sales Forecats Service - Utlizes Nett Profit Margin
 */
@Service("PPS_SalesForecastService")
public class PPS_SalesForecastService implements IPPS_DefaultStratergy
{
	@Autowired
	private IPercentageAdjustService	perSrv;

	@Autowired
	private IScripDataContainerSrv	scDataConSrv;
	private ScripDataContainer		scDataContainer;

	@SuppressWarnings(
	{ "unchecked", "rawtypes"
	})
	@Override
	public TY_PricesReturn getPriceProjections(ISA_ScripPriceProjectionService ppSrv) throws EX_General
	{
		TY_PricesReturn pRet = new TY_PricesReturn();
		pRet.setPriceType(PriceProjectionFactorsConstants.SalesForecast);
		TY_PFactor pFactor = null;

		ArrayList<OB_Scrip_BalSheet> balSheetEntities = new ArrayList<OB_Scrip_BalSheet>();

		double nettFactor = 0;
		double tmpCYPP = 0;
		double effNSD = 0;
		if (ppSrv != null)
		{

			/**
			 * First retrieve the correct Price Projection Factor
			 */
			pFactor = ppSrv.getProjectionFactors().stream().filter(x -> x.getFactorType().equals(PriceProjectionFactorsConstants.SalesForecast))
			          .findFirst().get();
			if (pFactor != null)
			{
				for ( TY_PFactorDetail factordetail : pFactor.getFactoritems() )
				{
					if (factordetail.getDirection() == direction.DECREASE)
					{
						if (nettFactor != 0)
						{
							nettFactor = nettFactor - factordetail.getPercentage();
						}
						else
						{
							nettFactor = factordetail.getPercentage();
						}
					}
					else if (factordetail.getDirection() == direction.INCREASE)
					{
						nettFactor = nettFactor + factordetail.getPercentage();
					}
				}
				if (nettFactor > 0)
				{
					// Get Effective Nett Sales Delta
					effNSD = ppSrv.getNpd().getNSD() + nettFactor;
				}
				else
				{
					// Get Effective Nett Sales Delta
					effNSD = ppSrv.getNpd().getNSD() - nettFactor;
				}

				// Get Current Year Projected Sales

				scDataContainer = scDataConSrv.getScripDetailsfromDB(ppSrv.getScCode());
				if (scDataContainer != null)
				{
					SheetEntities SheetEntities = scDataContainer.getEntitiesforSheet(SheetNamesConstants.BalanceSheet);
					if (SheetEntities != null)
					{
						balSheetEntities = SheetEntities.getSheetEntityList();
						if (balSheetEntities.size() > 0)
						{
							PenultimateQYear pqYear = new PenultimateQYear();
							double prevYearSales = get_PenultimateYearSales(pqYear, balSheetEntities);

							double currYearprojSales = perSrv.adjustPercentagetoFigure(prevYearSales, effNSD);

							double avgNPM = balSheetEntities.stream().mapToDouble(OB_Scrip_BalSheet::getNettProfitMargin).average()
							          .getAsDouble();

							// CYPP
							pRet.setCYPP(currYearprojSales * avgNPM / 100);

							// CYPEPUS
							pRet.setCYPEPUS((pRet.getCYPP() * ppSrv.getAvgENPR()) / 100);

							// ------------------------- Projected Prices-----------------------

							// Average PE Projected Price
							pRet.getProjectedPrices().setAvgPE_PP(pRet.getCYPEPUS() * ppSrv.getLastNp_FVR().getCurrFV()
							          * ppSrv.getAvgPE().getAvgPE() * ppSrv.getLastNp_FVR().getFVR());

							// Max PE Projected Price
							pRet.getProjectedPrices().setMaxPE_PP(pRet.getCYPEPUS() * ppSrv.getLastNp_FVR().getCurrFV()
							          * ppSrv.getAvgPE().getMaxPE() * ppSrv.getLastNp_FVR().getFVR());

							// Min PE Projected Price
							pRet.getProjectedPrices().setMinPE_PP(pRet.getCYPEPUS() * ppSrv.getLastNp_FVR().getCurrFV()
							          * ppSrv.getAvgPE().getMinPE() * ppSrv.getLastNp_FVR().getFVR());

							// If PE is adjusted
							pRet.getProjectedPrices().setPeAdjusted(ppSrv.getAvgPE().isPeAdjusted());
						}
					}
				}

			}
		}

		return pRet;
	}

	private double get_PenultimateYearSales(PenultimateQYear pQyear, ArrayList<OB_Scrip_BalSheet> balSheetEntList)
	{

		double prevYearSales = 0;
		OB_Scrip_BalSheet balSheetEnt = null;

		if (pQyear.getYear() != 0 && balSheetEntList != null)
		{
			// First check if Balance Sheet Data exists for the To Year (e.g Last Year balanceSheet data is not yet
			// maintained and current month is April as annual results for last year are not yet consolidated and out

			try
			{
				balSheetEnt = balSheetEntList.stream().filter(x -> x.getyear() == pQyear.getYear()).findFirst().get();

			}

			catch (NoSuchElementException e)
			{
				// Data Not found for the Target Year can be case of April this year where last year BalanceSheet
				// Data is not yet consolidated
				// Reduce the year by 1 in this case

				// No Try in this case let it dump if even the penultimate year data is not maintianed for previous
				// year
				balSheetEnt = balSheetEntList.stream().filter(x -> x.getyear() == (pQyear.getYear() - 1)).findFirst().get();

			}

			if (balSheetEnt != null)
			{
				prevYearSales = balSheetEnt.getNettSales();
			}

		}
		return prevYearSales;

	}

}
