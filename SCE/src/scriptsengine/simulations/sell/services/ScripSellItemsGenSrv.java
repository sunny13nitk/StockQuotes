package scriptsengine.simulations.sell.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import scriptsengine.customizing.interfaces.IBrokerageFeeSrv;
import scriptsengine.customizing.interfaces.ICapitalGainsSrv;
import scriptsengine.customizing.interfaces.IOppCostSrv;
import scriptsengine.portfolio.definitions.TY_Scrip_PositionModel;
import scriptsengine.portfolio.pojos.OB_Positions_Item;
import scriptsengine.portfolio.services.interfaces.IPortfolioManager;
import scriptsengine.simulations.sell.definitions.TY_SCTxn_Summary;
import scriptsengine.simulations.sell.definitions.TY_SaleEligibleItems;
import scriptsengine.simulations.sell.definitions.TY_SellProposalI;
import scriptsengine.simulations.sell.interfaces.IScripSellEligibleItemsFinder;
import scriptsengine.simulations.sell.interfaces.IScripSellItemsGenerator;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Scrips Sell Proposal Items Generation Service - Generates the Sell Proposal Line Items
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ScripSellItemsGenSrv implements IScripSellItemsGenerator
{

	/**
	 * ------------------- AUTOWIRED BEANS-------------------------------------
	 */

	@Autowired
	private IScripSellEligibleItemsFinder	eligibleSellItemsSrv;	// Eligible Sell Items Finder Service

	@Autowired
	private IBrokerageFeeSrv				brokerageSrv;			// Brokerage Computation Service
	@Autowired
	private ICapitalGainsSrv				capGSrv;				// Capital Gains Computation Service
	@Autowired
	private IOppCostSrv					oppCostSrv;			// Opportunity Cost Computation Service
	@Autowired
	private IPortfolioManager			portFMgr;				// Portfolio Manager

	/**
	 * ---------------------- PRIVATE PROPERTIES----------------------------
	 */

	private ArrayList<TY_SaleEligibleItems>	eligibleItems;
	private TY_SCTxn_Summary				txnSummary;
	private String						scCode;

	/**
	 * @return the eligibleItems
	 */
	@Override
	public ArrayList<TY_SaleEligibleItems> getEligibleItems()
	{
		return eligibleItems;
	}

	/**
	 * ---------------------- GENERATE SELL PROPOSAL ITEMS for a Scrip and it's Txn. Summary-----------
	 * 
	 * @param scCode
	 *             - Srip Code (TO basically get hold of Positions Details from Portfolio Manager)
	 * @param tnxSummary
	 *             - Txn. Totals Summary
	 * @return - Sell Proposal Items
	 * @throws EX_General
	 *              ---------------------------------------------------------------------------------------------
	 */
	@Override
	public ArrayList<TY_SellProposalI> generateSellPorposalItems(TY_SCTxn_Summary txnSummary) throws EX_General
	{
		ArrayList<TY_SellProposalI> sellProposalItems = new ArrayList<TY_SellProposalI>();

		// 1. First Generate the eligible Items to Sell from Position Items for the Scrip using eligible Items Service

		if (eligibleSellItemsSrv != null && txnSummary.getScCode() != null && txnSummary != null && txnSummary.getTxnDate() != null)
		{
			this.txnSummary = txnSummary;
			this.scCode = txnSummary.getScCode();
			this.eligibleItems = eligibleSellItemsSrv.getEligibleSaleItems(txnSummary);

			if (eligibleItems != null)
			{
				// Prepare Sales Proposal Items from eligible Items
				sellProposalItems = prepareSalesProposalItems();

			}
		}

		return sellProposalItems;

	}

	@Override
	public String getScCode()
	{
		// TODO Auto-generated method stub
		return this.scCode;
	}

	/**
	 * ----------------- PREPARE SALES PROPOSAL ITEMS-------------
	 * 
	 * @return - Sales Proposal Items
	 *         -----------------------------------------------------------
	 */
	private ArrayList<TY_SellProposalI> prepareSalesProposalItems() throws EX_General
	{
		ArrayList<TY_SellProposalI> sellProposalItems = new ArrayList<TY_SellProposalI>();

		if (this.eligibleItems != null && this.txnSummary != null && this.scCode != null && portFMgr != null)
		{

			// Get the Positions for the Scrip
			TY_Scrip_PositionModel posModel = portFMgr.getPositionsModelforScrip(scCode);
			if (posModel != null)
			{
				if (posModel.getScPosItems() != null)
				{
					if (posModel.getScPosItems().size() > 0)
					{
						// From the positions compare for eligible items by posItemNo and start preparing the
						// Structure
						for ( TY_SaleEligibleItems eligibleItem : this.eligibleItems )
						{
							OB_Positions_Item posItem = posModel.getScPosItems().stream()
							          .filter(x -> x.getPrimaryKey_Int() == eligibleItem.getPosItemNo()).findFirst().get();

							if (posItem != null)
							{
								TY_SellProposalI sellItem = new TY_SellProposalI();
								sellItem.setPosItemNo(eligibleItem.getPosItemNo());
								sellItem.setDematAC(posItem.getDematAcNum());
								sellItem.setSellQty(eligibleItem.getQtytoSell());
								sellItem.setTxnAmount(eligibleItem.getQtytoSell() * txnSummary.getAvgPPU());

								// Accomodate FRee Shares contribution to Realization Also if Any
								double preTaxRealization = (eligibleItem.getNumFreeShares() * txnSummary.getAvgPPU())
								          + (eligibleItem.getQtytoSell() - eligibleItem.getNumFreeShares())
								                    * (txnSummary.getAvgPPU() - posModel.getScPosHeader().getPPU());

								if (this.capGSrv != null)
								{
									sellItem.setTaxIncurred(capGSrv.calculateCapitalGainsTax(eligibleItem.getDaysDiff(), preTaxRealization));
								}

								if (this.brokerageSrv != null)
								{
									sellItem.setFeeIncurred(
									          brokerageSrv.calculateBrokerageinclTaxes(posItem.getDematAcNum(), sellItem.getTxnAmount()));
								}

								if (this.oppCostSrv != null)
								{
									sellItem.setOppCost(oppCostSrv.calculateOpportunityCost(eligibleItem.getDaysDiff(),
									          eligibleItem.getQtytoSell() * posModel.getScPosHeader().getPPU()));
								}

								sellItem.setRealization(preTaxRealization
								          - (sellItem.getTaxIncurred() + sellItem.getFeeIncurred() + sellItem.getOppCost()));

								sellProposalItems.add(sellItem);

							}
						}
					}
				}
			}

		}

		return sellProposalItems;
	}

}
