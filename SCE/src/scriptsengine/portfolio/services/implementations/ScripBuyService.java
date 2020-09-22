package scriptsengine.portfolio.services.implementations;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.managers.ModelObjectFactory;
import scriptsengine.enums.SCEenums;
import scriptsengine.portfolio.definitions.TY_DematSrcAmnt;
import scriptsengine.portfolio.definitions.TY_PosSS;
import scriptsengine.portfolio.definitions.TY_QtyPriceDate;
import scriptsengine.portfolio.definitions.TY_ScripBuy;
import scriptsengine.portfolio.definitions.TY_ScripBuySummary;
import scriptsengine.portfolio.definitions.TY_Scrip_PositionModel;
import scriptsengine.portfolio.pojos.OB_Positions_Header;
import scriptsengine.portfolio.pojos.OB_Positions_Item;
import scriptsengine.portfolio.services.interfaces.IPortfolioManager;
import scriptsengine.portfolio.services.interfaces.IScripBuyService;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.validations.interfaces.IScripExists;

/**
 * 
 * Scrip Buy Service - Prototype Bean autowired with Session scoped Portfolio Manager
 */
@Service("ScripBuyService")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ScripBuyService implements IScripBuyService
{

	/********* HOOK US UP ********************/
	@Autowired
	private IPortfolioManager	portFMgr;

	@Autowired
	private IScripExists		scExSrv;

	/****************************************
	 * PRIVATE MEMBERS
	 ***************************************/

	private TY_DematSrcAmnt		scBuyHelper;

	/*******************************************
	 * INTERFACE IMPLEMENTATIONS
	 *****************************************/

	@Override
	public TY_DematSrcAmnt getScripBuyHelper()
	{
		// TODO Auto-generated method stub
		return scBuyHelper;
	}

	@Override
	public void setScripBuyHelper(TY_DematSrcAmnt scBuyHelper)
	{
		if (this.scBuyHelper == null)
		{
			this.scBuyHelper = scBuyHelper;
		}

	}

	/**
	 * Trigger Pre Purchase Process for FI Validation and Amounts holding
	 */
	@Override
	public void prePurchaseProcessTrigger(TY_ScripBuy scripPurchaseDetails)
	{
		/**
		 * DO NOT WRITE ANYTHING - WILL BE handled via ScripBuyFI Aspect
		 */
	}

	/**
	 * Trigger Post Purchase Process for FI Validation and Amounts holding
	 */
	@Override
	public void postPurchaseProcessTrigger()
	{
		/**
		 * DO NOT WRITE ANYTHING - WILL BE handled via ScripBuyFI Aspect
		 */
	}

	/**
	 * -------------------------------------------------------------------------------
	 * Buy a Scrip passing in Scrip buy container Instance
	 * 
	 * @param scripPurchaseDetails
	 *             - Scrip buy container Instance
	 * @throws EX_General
	 *              - General Exception
	 *              --------------------------------------------------------------------
	 */
	@Override
	public TY_ScripBuySummary buyScrip(TY_ScripBuy scripPurchaseDetails, Boolean simulate) throws EX_General
	{
		TY_ScripBuySummary buySummary = null;
		OB_Positions_Header posH = null;
		if (scripPurchaseDetails != null && portFMgr != null)
		{

			if (portFMgr.validateDematAC(scripPurchaseDetails.getDematAC()))
			{

				// Determine if Scrip Code is Provided
				if (scripPurchaseDetails.getScCode() == null && scripPurchaseDetails.getScDesc() != null)
				{
					// In case Not populate through SCrip Exists Service
					if (scExSrv != null)
					{
						try
						{
							scripPurchaseDetails
							          .setScCode(scExSrv.getScripRootbyDescStartingwith(scripPurchaseDetails.getScDesc()).getscCode());
						}
						catch (Exception e)
						{
							EX_General egen = new EX_General("INVALID_SCRIP", new Object[]
							{ scripPurchaseDetails.getScDesc(), e.getMessage()
							});
							throw egen;
						}
					}

					// If Still Scrip code can't be determined - throw Exception
					if (scripPurchaseDetails.getScCode() == null)
					{
						EX_General egen = new EX_General("ERR_NOSCRIP", null);
						throw egen;

					}

					/**
					 * Determine If Scrip already exists in Portfolio - Positions Header get
					 */
					posH = portFMgr.getPositionsHeaderforScrip(scripPurchaseDetails.getScCode());
					if (simulate != true)
					{
						if (posH != null)
						{
							try
							{
								buySummary = processPurchaseUpdate(scripPurchaseDetails, posH);
							}

							catch (Exception e)
							{
								EX_General egen = new EX_General("ERR_SCRIP_BUY_UPDATE", new Object[]
								{ scripPurchaseDetails.getScCode(), e.getMessage()
								});
								throw egen;
							}
						}
						else
						{
							if (scBuyHelper != null)
							{
								try
								{
									buySummary = processPurchaseCreate(scripPurchaseDetails);
								}
								catch (Exception e)
								{
									EX_General egen = new EX_General("ERR_SCRIP_BUY_SIM", new Object[]
									{ scripPurchaseDetails.getScCode(), e.getMessage()
									});
									throw egen;
								}
							}
						}
					}
					else // Only Simulate
					{
						try
						{
							buySummary = processSellSimulation(posH, scripPurchaseDetails);
						}
						catch (Exception e)
						{
							EX_General egen = new EX_General("RR_SCRIP_BUY_NEW", new Object[]
							{ scripPurchaseDetails.getScCode(), e.getMessage()
							});
							throw egen;
						}

					}

				}

			}
		}
		return buySummary;
	}

	/**********************************
	 * PRIVATE SECTION
	 * 
	 * @throws Exception
	 *********************************/

	/**
	 * ----------------------------------------------------------------------------------------
	 * Process Scrip Purchase for the first time for a Given Scrip ----------------------------
	 * 
	 * @param scripPurchaseDetails
	 * @throws Exception
	 *              ------------------------------------------------------------------------------
	 */
	private TY_ScripBuySummary processPurchaseCreate(TY_ScripBuy scripPurchaseDetails) throws Exception
	{
		TY_ScripBuySummary buySummary = null;
		double totalInvestment = 0;
		int totalQty = 0;
		double PPU = 0;

		/**
		 * compute total Investement , total Quantities and PPU
		 */
		totalInvestment = scBuyHelper.getTxnAmount();
		totalQty = scripPurchaseDetails.getBuyItems().stream().mapToInt(TY_QtyPriceDate::getQty).sum();

		PPU = totalInvestment / totalQty;

		/**
		 * Now Start Entity Creation Process
		 */

		OB_Positions_Header posHeader = ModelObjectFactory.createObjectbyClass(new OB_Positions_Header().getClass());
		if (posHeader != null)
		{
			/**
			 * Position model for Update in Portfolio Manager
			 */
			TY_Scrip_PositionModel scPosModel = new TY_Scrip_PositionModel();

			posHeader.setScCode(scripPurchaseDetails.getScCode());
			posHeader.setCurrHolding(totalQty);
			posHeader.setCurrInvestment(totalInvestment);
			posHeader.setPPU(PPU);
			posHeader.setAmntRealized(0);
			posHeader.setDividendEarnings(0);
			posHeader.setFreeHolding(0);

			scPosModel.setScPosHeader(posHeader);

			/*
			 * Create Relation - Position Items
			 */
			for ( TY_QtyPriceDate qtypriceDate : scripPurchaseDetails.getBuyItems() )
			{
				OB_Positions_Item posItem = (OB_Positions_Item) posHeader.Create_RelatedEntity("OB_Positions_Items_Rel");
				if (posItem != null)
				{
					posItem.setDematAcNum(scripPurchaseDetails.getDematAC());
					posItem.setPPUTxn(qtypriceDate.getPrice());
					posItem.setQtyTxn(qtypriceDate.getQty());
					posItem.setTxnDate(qtypriceDate.getTxnDate());
					posItem.setTxnType(SCEenums.txnType.BUY);
					posItem.setETQ(qtypriceDate.getQty());
					scPosModel.getScPosItems().add(posItem);
				}

			}

			/*
			 * Commit the Txn
			 */
			if (posHeader.Save())
			{
				/**
				 * Update Positions in the Portfolio Manager
				 * since this is first time Purchase for the Scrip - Apppend the postiions in Portfolio Manager
				 */
				this.portFMgr.getScripPositions().add(scPosModel);
				buySummary = new TY_ScripBuySummary();
				buySummary.setBeforeBuy(null);
				buySummary.setAfterBuy(new TY_PosSS(scripPurchaseDetails.getScCode(), totalQty, 0, PPU, totalInvestment,
				          portFMgr.getContributiontoPortfoliobyAmount(totalInvestment),
				          portFMgr.getContributiontoPortfoliobyAmount(totalInvestment)));

			}

		}
		return buySummary;

	}

	/**
	 * ----------------------------------------------------------------------------------------
	 * Process Scrip Purchase Update for an Scrip already in My Portfolio - Positions ----------------------------
	 * 
	 * @param scripPurchaseDetails
	 * @throws Exception
	 *              ------------------------------------------------------------------------------
	 */
	private TY_ScripBuySummary processPurchaseUpdate(TY_ScripBuy scripPurchaseDetails, OB_Positions_Header posH) throws Exception
	{
		TY_ScripBuySummary buySummary = null;
		double totalInvestment = 0;
		int totalQty = 0;
		double PPU = 0;

		/**
		 * compute total Investement , total Quantities and PPU
		 */
		// Total Investment = Sum of Amounts in Current Purchase REquest prepopulate dby prePurchase Process FI Aspect
		// + Positions Header Total before up to current Investment
		totalInvestment = scBuyHelper.getTxnAmount() + posH.getCurrInvestment();

		totalQty = scripPurchaseDetails.getBuyItems().stream().mapToInt(TY_QtyPriceDate::getQty).sum() + posH.getCurrHolding();

		PPU = totalInvestment / totalQty;

		/**
		 * Now Start Positions Header Entity Update Process and Items Entity Creation Process
		 */

		if (posH.lock())
		{
			if (posH.switchtoChangeMode())
			{
				// Prepare REturn Structure - Positions SnapShot
				buySummary = new TY_ScripBuySummary();
				// Before Buy
				TY_PosSS beforeBuy = new TY_PosSS(scripPurchaseDetails.getScCode(), posH.getCurrHolding(), posH.getFreeHolding(), posH.getPPU(),
				          posH.getCurrInvestment(), portFMgr.getScripContributiontoPortfolio(scripPurchaseDetails.getScCode()),
				          portFMgr.getSectorContributiontoPortfoliobyScCode(scripPurchaseDetails.getScCode()));
				buySummary.setBeforeBuy(beforeBuy);

				// After Buy
				TY_PosSS afterBuy = new TY_PosSS(scripPurchaseDetails.getScCode(), totalQty, posH.getFreeHolding(), PPU, totalInvestment,
				          portFMgr.getContributiontoPortfoliobyAmount(totalInvestment),
				          portFMgr.getContributiontoPortfoliobyAmount(totalInvestment));
				buySummary.setAfterBuy(afterBuy);

				ArrayList<OB_Positions_Item> posItemsTmp = new ArrayList<OB_Positions_Item>();
				posH.setCurrInvestment(totalInvestment);
				posH.setCurrHolding(totalQty);
				posH.setPPU(PPU);

				/*
				 * Create Relation - Position Items
				 */
				for ( TY_QtyPriceDate qtypriceDate : scripPurchaseDetails.getBuyItems() )
				{
					OB_Positions_Item posItem = (OB_Positions_Item) posH.Create_RelatedEntity("OB_Positions_Items_Rel");
					if (posItem != null)
					{
						posItem.setDematAcNum(scripPurchaseDetails.getDematAC());
						posItem.setPPUTxn(qtypriceDate.getPrice());
						posItem.setQtyTxn(qtypriceDate.getQty());
						posItem.setTxnDate(qtypriceDate.getTxnDate());
						posItem.setTxnType(SCEenums.txnType.BUY);
						posItem.setETQ(qtypriceDate.getQty());
						posItemsTmp.add(posItem);

					}

				}

				/*
				 * Commit the Txn
				 */
				if (posH.Save())
				{
					/**
					 * Update Positions in the Portfolio Manager
					 * Update the Header and Append the Items
					 */

					TY_Scrip_PositionModel posModelScrip = portFMgr.getPositionsModelforScrip(scripPurchaseDetails.getScCode());
					if (posModelScrip != null)
					{
						// Header Update
						posModelScrip.getScPosHeader().setCurrInvestment(totalInvestment);
						posModelScrip.getScPosHeader().setCurrHolding(totalQty);
						posModelScrip.getScPosHeader().setPPU(PPU);

						// Items Append
						posModelScrip.getScPosItems().addAll(posItemsTmp);

					}

				}
			}

		}
		return buySummary;

	}

	private TY_ScripBuySummary processSellSimulation(OB_Positions_Header posH, TY_ScripBuy scripPurchaseDetails) throws Exception
	{
		TY_ScripBuySummary buySummary = null;
		double totalInvestment = 0;
		int totalQty = 0;
		double PPU = 0;
		if (posH == null) // New Purchase
		{

			/**
			 * compute total Investement , total Quantities and PPU
			 */
			totalInvestment = scBuyHelper.getTxnAmount();
			totalQty = scripPurchaseDetails.getBuyItems().stream().mapToInt(TY_QtyPriceDate::getQty).sum();

			PPU = totalInvestment / totalQty;

			buySummary = new TY_ScripBuySummary();
			buySummary.setBeforeBuy(null);
			buySummary.setAfterBuy(new TY_PosSS(scripPurchaseDetails.getScCode(), totalQty, 0, PPU, totalInvestment,
			          portFMgr.getContributiontoPortfoliobyAmount(totalInvestment), portFMgr.getContributiontoPortfoliobyAmount(totalInvestment)));

		}
		else
		{

			/**
			 * compute total Investement , total Quantities and PPU
			 */
			// Total Investment = Sum of Amounts in Current Purchase REquest prepopulated by prePurchase Process FI
			// Aspect
			// + Positions Header Total before up to current Investment
			totalInvestment = scBuyHelper.getTxnAmount() + posH.getCurrInvestment();

			totalQty = scripPurchaseDetails.getBuyItems().stream().mapToInt(TY_QtyPriceDate::getQty).sum() + posH.getCurrHolding();

			PPU = totalInvestment / totalQty;

			// Prepare REturn Structure - Positions SnapShot
			buySummary = new TY_ScripBuySummary();
			// Before Buy
			TY_PosSS beforeBuy = new TY_PosSS(scripPurchaseDetails.getScCode(), posH.getCurrHolding(), posH.getFreeHolding(), posH.getPPU(),
			          posH.getCurrInvestment(), portFMgr.getScripContributiontoPortfolio(scripPurchaseDetails.getScCode()),
			          portFMgr.getSectorContributiontoPortfoliobyScCode(scripPurchaseDetails.getScCode()));
			buySummary.setBeforeBuy(beforeBuy);

			// After Buy
			TY_PosSS afterBuy = new TY_PosSS(scripPurchaseDetails.getScCode(), totalQty, posH.getFreeHolding(), PPU, totalInvestment,
			          portFMgr.getContributiontoPortfoliobyAmount(totalInvestment), portFMgr.getContributiontoPortfoliobyAmount(totalInvestment));
			buySummary.setAfterBuy(afterBuy);

		}
		return buySummary;
	}

}
