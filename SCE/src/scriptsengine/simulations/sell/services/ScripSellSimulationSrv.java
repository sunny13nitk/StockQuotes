package scriptsengine.simulations.sell.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import scriptsengine.customizing.types.TY_ScripSellBaseConfig;
import scriptsengine.enums.SCEenums;
import scriptsengine.enums.SCEenums.scripSellMode;
import scriptsengine.portfolio.pojos.OB_Positions_Header;
import scriptsengine.portfolio.services.interfaces.IPortfolioManager;
import scriptsengine.pricingengine.reports.definitions.TY_PriceWatchItem;
import scriptsengine.pricingengine.reports.interfaces.IPriceWatchService;
import scriptsengine.simulations.sell.definitions.TY_FreeSharesInfo;
import scriptsengine.simulations.sell.definitions.TY_PandLInfo;
import scriptsengine.simulations.sell.definitions.TY_SCTxn_Summary;
import scriptsengine.simulations.sell.definitions.TY_SS_PricesRealizations;
import scriptsengine.simulations.sell.definitions.TY_SaleEligibleItems;
import scriptsengine.simulations.sell.definitions.TY_SellPoposalH;
import scriptsengine.simulations.sell.definitions.TY_SellProposalI;
import scriptsengine.simulations.sell.definitions.TY_SellProposalI_Summary;
import scriptsengine.simulations.sell.definitions.TY_Sell_Proposal;
import scriptsengine.simulations.sell.definitions.TY_Sell_Quote;
import scriptsengine.simulations.sell.definitions.TY_TaxProjections;
import scriptsengine.simulations.sell.interfaces.IScTxnSummary;
import scriptsengine.simulations.sell.interfaces.IScripSellItemsGenerator;
import scriptsengine.simulations.sell.interfaces.IScripSellSimulation;
import scriptsengine.simulations.sell.interfaces.IScripSellValidator;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.validations.interfaces.IScripExists;
import scriptsengine.utilities.interfaces.IDeltaCalcService;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ScripSellSimulationSrv implements IScripSellSimulation
{

	/**
	 * ------------------- AUTOWIRED BEANS-------------------------------------
	 */

	@Autowired
	private IScripExists			scEXSrv;			// Scrip Exists Service

	@Autowired
	private IScTxnSummary			scTxnSummarySrv;	// Scrip Transaction Summary

	@Autowired
	private IPortfolioManager		portFMgrSrv;		// Portfolio Manager

	@Autowired
	private IScripSellValidator		scSellValdSrv;		// Scrip Sell Validation Service

	@Autowired
	private IScripSellItemsGenerator	scItemsGenSrv;		// Scrip Sell Items Generation Service

	@Autowired
	private TY_ScripSellBaseConfig	baseConfig;		// Scrip Selling Base Config

	@Autowired
	private IPriceWatchService		prWatchSrv;		// Price Watch Service

	@Autowired
	private IDeltaCalcService		deltaSrv;			// Delta Calculation Service

	/**
	 * ---------------------- PRIVATE PROPERTIES----------------------------
	 */

	private TY_SCTxn_Summary			scTxnSummary;

	/**
	 * ------------------- GETTERS AND SETTERS---------------------------------
	 */

	/**
	 * @return the scTxnSummary
	 */
	public TY_SCTxn_Summary getScTxnSummary()
	{
		return scTxnSummary;
	}

	/**
	 * @param scTxnSummary
	 *             the scTxnSummary to set
	 */
	public void setScTxnSummary(TY_SCTxn_Summary scTxnSummary)
	{
		this.scTxnSummary = scTxnSummary;
	}

	/**
	 * ----------------------------------------------------------------------------------------------
	 * Generate Selling Proposal with Perspectives Free Shares and P&L for a specific Scrip Sell Quote
	 * 
	 * @param scSellQuote
	 *             - Scrip Sel Quote Container
	 * @return - Complete Selling Proposal
	 * @throws EX_General
	 *              - Exception
	 *              ----------------------------------------------------------------------------------
	 */
	@Override
	public TY_Sell_Proposal generateProposalforSellQuote(TY_Sell_Quote scSellQuote) throws EX_General
	{

		TY_Sell_Proposal sellProposal = null;

		if (scSellQuote != null)
		{

			if ((scSellQuote.getScCode() != null || scSellQuote.getScDesc() != null) && (scSellQuote.getSellItems() != null)
			          && portFMgrSrv != null)
			{
				if (scSellQuote.getSellItems().size() > 0)
				{

					// 0. Default the Date to Today if not populated
					if (scSellQuote.getSellDate() == null)
					{
						// DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
						Date date = new Date();

						scSellQuote.setSellDate(date);
					}

					// 1. Generate the Summary totals First
					if (scTxnSummarySrv != null)
					{
						this.scTxnSummary = scTxnSummarySrv.prepareTxnSummary(scSellQuote);

						// 2. Determine Scrip code if not Specified
						if (scSellQuote.getScCode() == null && scEXSrv != null)
						{
							try
							{
								scSellQuote.setScCode(scEXSrv.getScripRootbyDescStartingwith(scSellQuote.getScDesc()).getscCode());
								this.scTxnSummary.setScCode(scSellQuote.getScCode());
							}
							catch (Exception e)
							{
								EX_General egen = new EX_General("INVALID_SCRIP", new Object[]
								{ scSellQuote.getScDesc(), e.getMessage()
								});
								throw egen;
							}
						}

						// 3. Validate the Sell Quote and determine if sale involves the FRee shares
						if (this.scSellValdSrv != null)
						{
							SCEenums.scripSellMode mode = scSellValdSrv.validaeScripSaleforScrip(scSellQuote.getScCode(),
							          scTxnSummary.getTotalQty());

							if (mode != null)
							{
								/**
								 * Create a new Sell Proposal Instance
								 */

								sellProposal = new TY_Sell_Proposal();

								// 4. In case of Free Shares sale - Free Shares Perspective to bet set null
								if (mode == scripSellMode.PandL)
								{
									TY_SellPoposalH selPropH = new TY_SellPoposalH(null, new TY_PandLInfo(), new TY_SS_PricesRealizations());
									sellProposal.setSellProposalHeader(selPropH);
								}
								else
								{
									TY_SellPoposalH selPropH = new TY_SellPoposalH(new TY_FreeSharesInfo(), new TY_PandLInfo(),
									          new TY_SS_PricesRealizations());
									sellProposal.setSellProposalHeader(selPropH);
								}
							}

							/**
							 * 5. Generate the Proposal Items- through Proposal Items Generation Service which
							 * will generate according to Taxation and Brokerage Expenses
							 * Import - Sc Code and Txn Summary; Return - proposal Items
							 */

							if (this.scItemsGenSrv != null)
							{
								sellProposal.setSellProposalItems(scItemsGenSrv.generateSellPorposalItems(scTxnSummary));
								/**
								 * Set Eligible Items in SEll Proposal which would be used to generate Tax
								 * Perspective
								 */
								if (this.scItemsGenSrv.getEligibleItems() != null)
								{
									sellProposal.setEligibleItems(this.scItemsGenSrv.getEligibleItems());
								}
							}

							/**
							 * Modulate and Adjust the Items to generate Items Summary
							 */
							generateItemsSummary(sellProposal);

							/**
							 * Generate Proposal Header
							 */
							if (sellProposal.getSellProposalSummary() != null)
							{
								if (sellProposal.getSellProposalSummary().size() > 0)
								{
									generateProposalHeader(sellProposal);

									/**
									 * Generate Tax Perspective - In case of Any applicable Taxes
									 */
									if (sellProposal.getSellProposalHeader().getPandLPerspective().getTotalTaxliability() > 0)
									{
										generateTaxPerspective(sellProposal);
									}
								}
							}

							/**
							 * Populate Summary in Proposal needed for Executing Sell to avoid recalculations
							 */
							if (sellProposal.getScTxnSummary() != null)
							{
								sellProposal.setScTxnSummary(scTxnSummary);
							}

						}

					}
				}
			}
		}

		return sellProposal;
	}

	/***** PRIVATE METHODS ***********************************/

	/***
	 * Generate Items Summary
	 */

	private void generateItemsSummary(TY_Sell_Proposal sellProposal)
	{
		if (sellProposal != null)
		{
			if (sellProposal.getSellProposalItems() != null)
			{
				if (sellProposal.getSellProposalItems().size() > 0)
				{
					ArrayList<TY_SellProposalI> sortedSell = sellProposal.getSellProposalItems().stream()
					          .collect(Collectors.groupingBy(x -> x.getDematAC())).entrySet().stream()
					          .map(e -> e.getValue().stream()
					                    .reduce((f1, f2) -> new TY_SellProposalI(f1.getPosItemNo(), f1.getDematAC(),
					                              f1.getSellQty() + f2.getSellQty(), f1.getTxnAmount() + f2.getTxnAmount(),
					                              f1.getTaxIncurred() + f2.getTaxIncurred(), f1.getFeeIncurred() + f2.getFeeIncurred(),
					                              f1.getOppCost() + f2.getOppCost(), f1.getRealization() + f2.getRealization())))
					          .map(f -> f.get()).collect(Collectors.toCollection(ArrayList::new));

					if (sortedSell != null)
					{
						sortedSell.stream()
						          .forEach(x -> sellProposal.getSellProposalSummary()
						                    .add(new TY_SellProposalI_Summary(x.getDematAC(), x.getSellQty(), x.getTxnAmount(),
						                              x.getTaxIncurred(), x.getFeeIncurred(), x.getOppCost(), x.getRealization())));
					}
				}
			}
		}
	}

	/**
	 * Generate Sell Proposal Header - P&l and FRee Shares Perspective
	 * 
	 * @param sell_Proposal
	 */
	private void generateProposalHeader(TY_Sell_Proposal sellProposal) throws EX_General
	{
		if (sellProposal != null)
		{
			if (sellProposal.getSellProposalSummary() != null)
			{
				if (sellProposal.getSellProposalSummary().size() > 0)
				{

					// Get and generate P&L perspective
					if (sellProposal.getSellProposalHeader().getPandLPerspective() != null)
					{

						sellProposal.getSellProposalHeader().getPandLPerspective()
						          .setTotalTxnAmount(sellProposal.getSellProposalSummary().stream().mapToDouble(x -> x.getTxnAmount()).sum());
						sellProposal.getSellProposalHeader().getPandLPerspective().setTotalTaxliability(
						          sellProposal.getSellProposalSummary().stream().mapToDouble(x -> x.getTaxIncurred()).sum());
						sellProposal.getSellProposalHeader().getPandLPerspective()
						          .setTotalFees(sellProposal.getSellProposalSummary().stream().mapToDouble(x -> x.getFeeIncurred()).sum());
						sellProposal.getSellProposalHeader().getPandLPerspective()
						          .setTotalOppCost(sellProposal.getSellProposalSummary().stream().mapToDouble(x -> x.getOppCost()).sum());
						sellProposal.getSellProposalHeader().getPandLPerspective().setTotalRealization(
						          sellProposal.getSellProposalSummary().stream().mapToDouble(x -> x.getRealization()).sum());

					}

					// Get and generate Free shares perspective
					// FRee Share Perspective can be null depending upon the mode returned by Scrip sell Validation
					// Service
					if (sellProposal.getSellProposalHeader().getFreeSharesPespective() != null)
					{
						double numFreeShares = 0;
						if (baseConfig != null)
						{
							if (baseConfig.isReInvestTaxOppCost())
							{
								numFreeShares = (sellProposal.getSellProposalHeader().getPandLPerspective().getTotalRealization()
								          + sellProposal.getSellProposalHeader().getPandLPerspective().getTotalTaxliability()
								          + sellProposal.getSellProposalHeader().getPandLPerspective().getTotalOppCost())
								          / scTxnSummary.getAvgPPU();

								sellProposal.getSellProposalHeader().getFreeSharesPespective().setReinvestTaxOpp(true);

							}
							else
							{
								numFreeShares = sellProposal.getSellProposalHeader().getPandLPerspective().getTotalRealization()
								          / scTxnSummary.getAvgPPU();

								sellProposal.getSellProposalHeader().getFreeSharesPespective().setReinvestTaxOpp(false);
							}
						}

						int numFReeint = (int) numFreeShares;

						sellProposal.getSellProposalHeader().getFreeSharesPespective().setNumFreeShares(numFReeint);
						double decimalShares = numFreeShares - numFReeint;
						sellProposal.getSellProposalHeader().getFreeSharesPespective()
						          .setRealizationSpill(decimalShares * scTxnSummary.getAvgPPU());

						sellProposal.getSellProposalHeader().getFreeSharesPespective()
						          .setNumActualSale(scTxnSummary.getTotalQty() - numFReeint);

					}

					// Generate Prices and Realization Perspective in Header
					if (deltaSrv != null && prWatchSrv != null && scItemsGenSrv != null)
					{
						TY_PriceWatchItem pwItem = prWatchSrv.computeProjectedPriceforScCode(scItemsGenSrv.getScCode());
						if (pwItem != null)
						{
							// GEt Prices Realization Perspective
							if (sellProposal.getSellProposalHeader().getPricesRealizationPerspective() != null)
							{
								TY_SS_PricesRealizations sellprRealPersp = sellProposal.getSellProposalHeader()
								          .getPricesRealizationPerspective();

								OB_Positions_Header posH = portFMgrSrv.getPositionsHeaderforScrip(scItemsGenSrv.getScCode());
								if (posH != null)
								{
									sellprRealPersp.getPrices().setActualBuyPrice(posH.getPPU());
									sellprRealPersp.getPrices().setActualSellPrice(scTxnSummary.getAvgPPU());

									// Populate Avg PE Prices
									sellprRealPersp.getPrices().getAvgPEPrices().setMinPE_PP(pwItem.getProjPrices().getMinPE_PP());
									sellprRealPersp.getPrices().getAvgPEPrices().setAvgPE_PP(pwItem.getProjPrices().getAvgPE_PP());
									sellprRealPersp.getPrices().getAvgPEPrices().setMaxPE_PP(pwItem.getProjPrices().getMaxPE_PP());

									// Also populate Adjusted PE prices
									if (pwItem.getAvgPE() != pwItem.getUnadj_avgPE())
									{
										sellprRealPersp.getPrices().getAdjPEPrices()
										          .setMinPE_PP(pwItem.getUnadj_projPrices().getMinPE_PP());
										sellprRealPersp.getPrices().getAdjPEPrices()
										          .setAvgPE_PP(pwItem.getUnadj_projPrices().getAvgPE_PP());
										sellprRealPersp.getPrices().getAdjPEPrices()
										          .setMaxPE_PP(pwItem.getUnadj_projPrices().getMaxPE_PP());
										sellprRealPersp.getPrices().setPEDifferent(true);
									}

									// Populate Ratios
									sellprRealPersp.getRatios().setActualBuytoSellRealization(
									          deltaSrv.getDeltaPercentage(sellprRealPersp.getPrices().getActualBuyPrice(),
									                    sellprRealPersp.getPrices().getActualSellPrice()));

									sellprRealPersp.getRatios().getAvgPERealizationRatios().setActualBuytoAvgPrice(
									          deltaSrv.getDeltaPercentage(sellprRealPersp.getPrices().getActualBuyPrice(),
									                    sellprRealPersp.getPrices().getAvgPEPrices().getAvgPE_PP()));

									sellprRealPersp.getRatios().getAvgPERealizationRatios().setActualBuytoMaxPrice(
									          deltaSrv.getDeltaPercentage(sellprRealPersp.getPrices().getActualBuyPrice(),
									                    sellprRealPersp.getPrices().getAvgPEPrices().getMaxPE_PP()));

									if (sellprRealPersp.getPrices().isPEDifferent())
									{

										sellprRealPersp.getRatios().getAdjPERealizationRatios().setActualBuytoAvgPrice(
										          deltaSrv.getDeltaPercentage(sellprRealPersp.getPrices().getActualBuyPrice(),
										                    sellprRealPersp.getPrices().getAdjPEPrices().getAvgPE_PP()));

										sellprRealPersp.getRatios().getAdjPERealizationRatios().setActualBuytoMaxPrice(
										          deltaSrv.getDeltaPercentage(sellprRealPersp.getPrices().getActualBuyPrice(),
										                    sellprRealPersp.getPrices().getAdjPEPrices().getMaxPE_PP()));

									}

									// Populate Realizations

									sellprRealPersp.getRealizations().getAvgPERelativeRealizations()
									          .setRelRealizationAvgPrice((sellprRealPersp.getRatios().getActualBuytoSellRealization()
									                    / sellprRealPersp.getRatios().getAvgPERealizationRatios().getActualBuytoAvgPrice())
									                    * 100);

									sellprRealPersp.getRealizations().getAvgPERelativeRealizations()
									          .setRelRealizationMaxPrice((sellprRealPersp.getRatios().getActualBuytoSellRealization()
									                    / sellprRealPersp.getRatios().getAvgPERealizationRatios().getActualBuytoMaxPrice())
									                    * 100);

									if (sellprRealPersp.getPrices().isPEDifferent())
									{
										sellprRealPersp.getRealizations().getAdjPERelativeRealizations().setRelRealizationAvgPrice(
										          (sellprRealPersp.getRatios().getActualBuytoSellRealization() / sellprRealPersp.getRatios()
										                    .getAdjPERealizationRatios().getActualBuytoAvgPrice()) * 100);

										sellprRealPersp.getRealizations().getAdjPERelativeRealizations().setRelRealizationMaxPrice(
										          (sellprRealPersp.getRatios().getActualBuytoSellRealization() / sellprRealPersp.getRatios()
										                    .getAdjPERealizationRatios().getActualBuytoMaxPrice()) * 100);
									}

								}

							}
						}
					}

				}
			}
		}

	}

	/**
	 * Generate Tax Perspective in Sell Proposal
	 * 
	 * @param sellProposal
	 */
	private void generateTaxPerspective(TY_Sell_Proposal sellProposal)
	{
		if (sellProposal.getSellProposalItems().size() > 0 && sellProposal.getEligibleItems() != null)
		{

			/*
			 * Get Taxable Items from Eligible Items
			 */
			ArrayList<TY_SaleEligibleItems> taxableItems = sellProposal.getEligibleItems().stream()
			          .filter(x -> x.getDaysDiff() < baseConfig.getNumDays_LTCG()).collect(Collectors.toCollection(ArrayList::new));
			if (taxableItems != null)
			{
				TY_TaxProjections taxH = new TY_TaxProjections();
				taxH.setProbTaxSavings(sellProposal.getSellProposalHeader().getPandLPerspective().getTotalTaxliability());

				taxH.setNumDays(baseConfig.getNumDays_LTCG() - taxableItems.stream().mapToInt(x -> x.getDaysDiff()).min().getAsInt());

				int numFreeShares = (int) (sellProposal.getSellProposalHeader().getPandLPerspective().getTotalTaxliability()
				          / scTxnSummary.getAvgPPU());

				taxH.setProbNumFreeShares(numFreeShares);

				sellProposal.getTaxPerspective().setTaxSsavH(taxH);

				if (taxableItems.size() > 1)
				{
					// Generate ind. line Items
					for ( TY_SaleEligibleItems taxItem : taxableItems )
					{
						TY_TaxProjections taxI = new TY_TaxProjections();
						taxI.setProbTaxSavings(sellProposal.getSellProposalItems().stream()
						          .filter(x -> x.getPosItemNo() == taxItem.getPosItemNo()).findFirst().get().getTaxIncurred());
						taxI.setNumDays(baseConfig.getNumDays_LTCG() - taxItem.getDaysDiff());
						numFreeShares = (int) (taxI.getProbTaxSavings() / scTxnSummary.getAvgPPU());
						taxI.setProbNumFreeShares(numFreeShares);
						sellProposal.getTaxPerspective().getTaxSavI().add(taxI);
					}

				}
				else
				{
					// Single Line Item as Header
					TY_TaxProjections taxI = new TY_TaxProjections();
					taxI.setProbTaxSavings(taxH.getProbTaxSavings());
					taxI.setNumDays(taxH.getNumDays());
					taxI.setProbNumFreeShares(taxH.getProbNumFreeShares());
					sellProposal.getTaxPerspective().getTaxSavI().add(taxI);
				}

			}

		}
	}
}
