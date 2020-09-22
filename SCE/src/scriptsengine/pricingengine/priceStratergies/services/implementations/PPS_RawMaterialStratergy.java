package scriptsengine.pricingengine.priceStratergies.services.implementations;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import modelframework.implementations.MessagesFormatter;
import scriptsengine.constants.PriceProjectionFactorsConstants;
import scriptsengine.constants.SheetNamesConstants;
import scriptsengine.pojos.OB_Scrip_BalSheet;
import scriptsengine.pojos.OB_Scrip_Expenses;
import scriptsengine.pojos.OB_Scrip_RawMaterial;
import scriptsengine.pricingengine.definitions.TY_PFactor;
import scriptsengine.pricingengine.definitions.TY_PFactorDetail;
import scriptsengine.pricingengine.definitions.TY_PricesReturn;
import scriptsengine.pricingengine.definitions.TY_RMCNP;
import scriptsengine.pricingengine.definitions.TY_RawMPP_Stats;
import scriptsengine.pricingengine.priceStratergies.services.interfaces.IPPS_DefaultStratergy;
import scriptsengine.pricingengine.priceStratergies.services.interfaces.IRawMaterial_PPStatistics;
import scriptsengine.pricingengine.priceStratergies.services.interfaces.helperSrvices.IRawMContributionService;
import scriptsengine.pricingengine.services.interfaces.ISA_ScripPriceProjectionService;
import scriptsengine.uploadengine.definitions.ScripDataContainer;
import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.services.interfaces.IScripDataContainerSrv;
import scriptsengine.utilities.implementations.DeltaCalcService;
import scriptsengine.utilities.types.PenultimateQYear;

@Service("PPS_RawMaterialStratergy")
public class PPS_RawMaterialStratergy implements IPPS_DefaultStratergy, IRawMaterial_PPStatistics
{

	@Autowired
	private IScripDataContainerSrv	scDataConSrv;
	@Autowired
	private IRawMContributionService	rawMContSrv;
	@Autowired
	private MessagesFormatter		msgFormatter;
	@Autowired
	private DeltaCalcService			delSrv;

	private ScripDataContainer		scDataContainer;
	private ArrayList<TY_RawMPP_Stats>	statistics;

	/**
	 * @return the statistics
	 */
	public ArrayList<TY_RawMPP_Stats> getStatistics()
	{
		return statistics;
	}

	/**
	 * Constructor
	 */
	public PPS_RawMaterialStratergy()
	{
		super();
		this.statistics = new ArrayList<TY_RawMPP_Stats>();
	}

	/**
	 * 
	 */
	@SuppressWarnings(
	{ "rawtypes", "unchecked"
	})
	@Override
	public TY_PricesReturn getPriceProjections(ISA_ScripPriceProjectionService ppSrv) throws EX_General
	{
		TY_PricesReturn pRet = new TY_PricesReturn();
		if (scDataConSrv != null && ppSrv != null)
		{
			scDataContainer = scDataConSrv.getScripDetailsfromDB(ppSrv.getScCode());
			if (scDataContainer != null)
			{

				// Clear the statistics everytime this service is called since it is a context scope Bean
				this.statistics.clear();
				pRet.setPriceType(PriceProjectionFactorsConstants.RawMaterials);
				ArrayList<TY_PFactorDetail> effRMCList = new ArrayList<TY_PFactorDetail>();

				TY_PFactor pFactor = null;

				/**
				 * Get the factor for Raw Materials
				 */
				pFactor = ppSrv.getProjectionFactors().stream()
				          .filter(x -> x.getFactorType().equals(PriceProjectionFactorsConstants.RawMaterials)).findFirst().get();
				if (pFactor != null)
				{
					SheetEntities SheetEntities = scDataContainer.getEntitiesforSheet(SheetNamesConstants.RawMaterialsSheet);
					if (SheetEntities != null)
					{
						ArrayList<OB_Scrip_RawMaterial> RawMaterialsList = SheetEntities.getSheetEntityList();
						if (RawMaterialsList != null)
						{
							if (RawMaterialsList.size() > 0)
							{
								if (rawMContSrv != null)
								{
									/**
									 * Get Effective Raw Materials Contribution per Category for Individual Raw
									 * Materials - Only Compare for Recent Penultimate Year RawMaterials as
									 * product line and raw Material reqts of company might have canged
									 * recently
									 */
									PenultimateQYear pqYear = new PenultimateQYear();

									ArrayList<OB_Scrip_RawMaterial> recentRawMaterialsList = RawMaterialsList.stream()
									          .filter(x -> x.getYear() == evaluate_PenultimateYear(pqYear, RawMaterialsList))
									          .collect(Collectors.toCollection(ArrayList::new));

									effRMCList = rawMContSrv.getEffectiveRMC(pFactor, recentRawMaterialsList);
									if (effRMCList != null)
									{
										if (effRMCList.size() > 0)
										{
											generateStatistics(effRMCList);
											populateStatistics(RawMaterialsList);
											if (this.statistics.size() > 0)
											{
												/**
												 * Get Nett REduction in Profit
												 */
												double rawMNPD = this.statistics.stream()
												          .mapToDouble(TY_RawMPP_Stats::getReductioninProfit).sum();
												double npdfromppSrv = ppSrv.getNpd().getNPD();

												double revisedNPD = npdfromppSrv - rawMNPD;

												/**
												 * Now Calculate price projecton from this NPD Revised
												 */

												// CYPP
												pRet.setCYPP((ppSrv.getLastNp_FVR().getLastNettProfit()) * (1 + revisedNPD / 100));

												// CYPEPUS
												pRet.setCYPEPUS((pRet.getCYPP() * ppSrv.getAvgENPR()) / 100);

												// ------------------------- Projected
												// Prices-----------------------

												// Average PE Projected Price
												pRet.getProjectedPrices()
												          .setAvgPE_PP(pRet.getCYPEPUS() * ppSrv.getLastNp_FVR().getCurrFV()
												                    * ppSrv.getAvgPE().getAvgPE() * ppSrv.getLastNp_FVR().getFVR());

												// Max PE Projected Price
												pRet.getProjectedPrices()
												          .setMaxPE_PP(pRet.getCYPEPUS() * ppSrv.getLastNp_FVR().getCurrFV()
												                    * ppSrv.getAvgPE().getMaxPE() * ppSrv.getLastNp_FVR().getFVR());

												// Min PE Projected Price
												pRet.getProjectedPrices()
												          .setMinPE_PP(pRet.getCYPEPUS() * ppSrv.getLastNp_FVR().getCurrFV()
												                    * ppSrv.getAvgPE().getMinPE() * ppSrv.getLastNp_FVR().getFVR());

												// If PE is adjusted
												pRet.getProjectedPrices().setPeAdjusted(ppSrv.getAvgPE().isPeAdjusted());
											}

										}
									}
								}
							}
						}

					}
				}
			}
		}
		return pRet;
	}

	private int evaluate_PenultimateYear(PenultimateQYear pQyear, ArrayList<OB_Scrip_RawMaterial> rawMEntList)
	{

		int year = 0;
		OB_Scrip_RawMaterial rawMEnt = null;

		if (pQyear.getYear() != 0 && rawMEntList != null)
		{
			// First check if Balance Sheet Data exists for the To Year (e.g Last Year balanceSheet data is not yet
			// maintained and current month is April as annual results for last year are not yet consolidated and out

			try
			{
				rawMEnt = rawMEntList.stream().filter(x -> x.getYear() == pQyear.getYear()).findFirst().get();

			}

			catch (NoSuchElementException e)
			{
				// Data Not found for the Target Year can be case of April this year where last year BalanceSheet
				// Data is not yet consolidated
				// Reduce the year by 1 in this case

				// No Try in this case let it dump if even the penultimate year data is not maintianed for previous
				// year
				rawMEnt = rawMEntList.stream().filter(x -> x.getYear() == (pQyear.getYear() - 1)).findFirst().get();

			}

			if (rawMEnt != null)
			{
				year = rawMEnt.getYear();
			}

		}
		return year;

	}

	@Override
	public ArrayList<TY_RawMPP_Stats> getStatisticsforRawMaterialSrv() throws EX_General
	{
		return this.statistics;
	}

	private void generateStatistics(ArrayList<TY_PFactorDetail> effRMCList)
	{

		for ( TY_PFactorDetail effRMC : effRMCList )
		{
			TY_RawMPP_Stats rawMStats = new TY_RawMPP_Stats();

			rawMStats.setCategory(effRMC.getDesc());
			rawMStats.setDirection(effRMC.getDirection());
			rawMStats.setEffPercentage(effRMC.getPercentage());

			statistics.add(rawMStats);
		}
	}

	@SuppressWarnings(
	{ "unchecked", "rawtypes"
	})
	private void populateStatistics(ArrayList<OB_Scrip_RawMaterial> RawMaterialsList) throws EX_General
	{
		if (RawMaterialsList != null && this.statistics != null)
		{
			if (RawMaterialsList.size() > 0)
			{
				ArrayList<OB_Scrip_BalSheet> balSheetEntities = new ArrayList<OB_Scrip_BalSheet>();
				ArrayList<OB_Scrip_Expenses> expEntities = new ArrayList<OB_Scrip_Expenses>();
				for ( TY_RawMPP_Stats rawMStat : statistics )
				{

					// Compute Average RMC for Each Category
					ArrayList<OB_Scrip_RawMaterial> rawMSelList = new ArrayList<OB_Scrip_RawMaterial>();
					rawMSelList = RawMaterialsList.stream().filter(x -> x.getRawMCatg().equals(rawMStat.getCategory()))
					          .collect(Collectors.toCollection(ArrayList::new));
					if (rawMSelList.size() > 0)
					{
						ArrayList<TY_RMCNP> rmcnpList = new ArrayList<TY_RMCNP>();
						rawMStat.setAvgRMC(rawMSelList.stream().mapToDouble(OB_Scrip_RawMaterial::getPerRMC).average().getAsDouble());
						if (rawMStat.getAvgRMC() != 0)
						{
							// Create new Type for Year, RMC weight and RMCNP

							/**
							 * Get the Expenses and Balance Sheet Entities from scDataContainer
							 */
							if (scDataContainer != null)
							{
								SheetEntities SheetEntities = scDataContainer.getEntitiesforSheet(SheetNamesConstants.BalanceSheet);
								if (SheetEntities != null)
								{
									balSheetEntities = SheetEntities.getSheetEntityList();
								}

								SheetEntities = scDataContainer.getEntitiesforSheet(SheetNamesConstants.ExpensesSheet);
								if (SheetEntities != null)
								{
									expEntities = SheetEntities.getSheetEntityList();
								}

								/**
								 * Iterate over the selected Raw Materials for the List
								 */
								for ( OB_Scrip_RawMaterial rawMSel : rawMSelList )
								{
									/**
									 * Get Nett Profit and RawMaterial Expenses from respective Relations for
									 * Year in Raw Material Entity
									 */
									TY_RMCNP rmcnp = new TY_RMCNP();
									try
									{

										rmcnp.setYear(rawMSel.getYear());

										rmcnp.setRMCWeight(((expEntities.stream().filter(w -> w.getYear() == rmcnp.getYear()).findFirst()
										          .get().getCostRawMt() * rawMStat.getAvgRMC()) / 100));

										rmcnp.setRMCNP(rmcnp.getRMCWeight() / ((balSheetEntities.stream()
										          .filter(x -> x.getyear() == rmcnp.getYear()).findFirst().get()).getNettProfit()) * 100);
										rmcnpList.add(rmcnp);

									}

									catch (NoSuchElementException e)
									{
										EX_General msgChgErr = new EX_General("ERRRMSTRAT", new Object[]
										{ rmcnp.getYear()
										});
										msgFormatter.generate_message_snippet(msgChgErr);
										throw msgChgErr;
									}

								}

								if (rmcnpList.size() > 0)
								{
									rawMStat.setAvgRMCNP(rmcnpList.stream().mapToDouble(TY_RMCNP::getRMCNP).average().getAsDouble());
									if (delSrv != null)
									{
										double effPer = this.statistics.stream().filter(x -> x.getCategory().equals(rawMStat.getCategory()))
										          .findFirst().get().getEffPercentage();

										if (effPer < 0)
										{
											rawMStat.setEffRMCNP(delSrv.adjustNumberbyPercentage(rawMStat.getAvgRMCNP(), effPer * -1,
											          scriptsengine.enums.SCEenums.direction.DECREASE));
										}
										else
										{
											rawMStat.setEffRMCNP(delSrv.adjustNumberbyPercentage(rawMStat.getAvgRMCNP(), effPer,
											          scriptsengine.enums.SCEenums.direction.INCREASE));
										}

										rawMStat.setReductioninProfit(rawMStat.getEffRMCNP() - rawMStat.getAvgRMCNP());

									}

								}

							}
						}
					}

				}
			}
		}
	}

}
