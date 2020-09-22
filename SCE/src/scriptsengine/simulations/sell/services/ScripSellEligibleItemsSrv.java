package scriptsengine.simulations.sell.services;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import scriptsengine.customizing.types.TY_ScripSellBaseConfig;
import scriptsengine.enums.SCEenums;
import scriptsengine.portfolio.definitions.TY_Scrip_PositionModel;
import scriptsengine.portfolio.pojos.OB_Positions_Header;
import scriptsengine.portfolio.pojos.OB_Positions_Item;
import scriptsengine.portfolio.services.interfaces.IPortfolioManager;
import scriptsengine.simulations.sell.definitions.TY_SCTxn_Summary;
import scriptsengine.simulations.sell.definitions.TY_SaleEligibleItems;
import scriptsengine.simulations.sell.interfaces.IScripSellEligibleItemsFinder;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.utilities.interfaces.IDateService;

/**
 * Service to determine and find the Eligible Items optimally from Positions Items of a Scrip considering Taxation and
 * LTCG
 */

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ScripSellEligibleItemsSrv implements IScripSellEligibleItemsFinder
{

	/**
	 * ------------------- AUTOWIRED BEANS-------------------------------------
	 */
	@Autowired
	private IPortfolioManager		portFMgrSrv;	// Portfolio Manager

	@Autowired
	private IDateService			dateSrv;		// Dates Utility Service

	@Autowired
	private TY_ScripSellBaseConfig	baseConfig;	// Scrip Sell Base Config Bean

	/**
	 * ---------------------- PRIVATE PROPERTIES----------------------------
	 */

	/**
	 * -------------------------------------------------------------------------------------------------------
	 * Determine Eligible Sell Items from Scrip's Current Positions in Portfolio Manager considering Tax Implicatioons
	 * for Present and Future
	 * 
	 * @param scCode
	 *             - Scrip Code
	 * @param tnxSummary
	 *             - Txn. Totals summary
	 * @return - Eligible Sale Items
	 * @throws EX_General
	 *              ---------------------------------------------------------------------------------------------------
	 */
	@Override
	public ArrayList<TY_SaleEligibleItems> getEligibleSaleItems(TY_SCTxn_Summary txnSummary) throws EX_General
	{
		ArrayList<TY_SaleEligibleItems> eligibleItems = new ArrayList<TY_SaleEligibleItems>();

		TY_Scrip_PositionModel posModel = null;

		// First get the Positions for Current scrip from Portfolio Manager

		if (portFMgrSrv != null)
		{

			posModel = portFMgrSrv.getPositionsModelforScrip(txnSummary.getScCode());

			if (posModel != null)
			{
				// Get TENTATIVE SELL ITEMS
				ArrayList<TY_SaleEligibleItems> allItems = this.getTentativeSellItems(posModel, txnSummary.getTxnDate());

				if (allItems.size() > 0 && baseConfig != null)
				{
					// Process tentative items for Taxation consideration to get final list of Sell Items with
					// corresponding quantities to sell
					eligibleItems = processTentativeSellItems(allItems, txnSummary);

					// Process for Free Shares
					if (eligibleItems != null)
					{
						eligibleItems = processForFreeShares(eligibleItems, txnSummary, posModel);
					}

				}

			}

		}

		return eligibleItems;
	}

	/**
	 * PRIVATE METHODS-------------------------------------------
	 * 
	 */

	/**
	 * ------------------- Get Probable Sell Items that might Qualify for Selling---
	 * 
	 * @param posModel
	 *             - Positions Model for Scrip
	 * @param sellDate
	 *             - Seliing Date
	 * @return - List of Sale Eligible Items
	 * @throws EX_General
	 *              - Exception
	 *              --------------------------------------------------------------------------
	 */
	private ArrayList<TY_SaleEligibleItems> getTentativeSellItems(TY_Scrip_PositionModel posModel, Date sellDate) throws EX_General
	{
		ArrayList<TY_SaleEligibleItems> allItems = null;
		// 2. Get Buy Positions Items with ETQ > 0
		ArrayList<OB_Positions_Item> buyItemsposETQ = posModel.getScPosItems().stream().filter(x -> x.getTxnType().equals(SCEenums.txnType.BUY))
		          .filter(w -> w.getETQ() > 0).collect(Collectors.toCollection(ArrayList::new));
		if (buyItemsposETQ != null && dateSrv != null)
		{
			// 3.Calculate Days difference for Each of the Items thus obtained
			allItems = new ArrayList<TY_SaleEligibleItems>();

			for ( OB_Positions_Item posItem : buyItemsposETQ )
			{
				int daysDiff = 0;
				try
				{
					daysDiff = dateSrv.getNumDaysbwSqlSysDates(posItem.getTxnDate(), sellDate);
				}
				catch (ParseException e)
				{
					EX_General egen = new EX_General("ERR_DATE_PARSE", new Object[]
					{ e.getMessage()
					});
					throw egen;
				}

				TY_SaleEligibleItems eligItem = new TY_SaleEligibleItems(posItem.getPrimaryKey_Int(), daysDiff, posItem.getETQ(), 0);
				allItems.add(eligItem);
			}
		}
		return allItems;
	}

	/**
	 * Further Process the Sell Items
	 * 
	 * @param probableItems
	 * @return - final Sell Items
	 */
	private ArrayList<TY_SaleEligibleItems> processTentativeSellItems(ArrayList<TY_SaleEligibleItems> probableItems, TY_SCTxn_Summary txnSummary)
	{

		int currQty = 0;
		int qtytaxable = 0;

		ArrayList<TY_SaleEligibleItems> sellItems = new ArrayList<TY_SaleEligibleItems>();

		ArrayList<TY_SaleEligibleItems> taxFreeItems = null;

		ArrayList<TY_SaleEligibleItems> taxNotFreeItems = null;

		ArrayList<TY_SaleEligibleItems> taxNotFreeItemsSorted = null;

		taxFreeItems = probableItems.stream().filter(x -> x.getDaysDiff() >= baseConfig.getNumDays_LTCG())
		          .collect(Collectors.toCollection(ArrayList::new));

		taxNotFreeItems = probableItems.stream().filter(x -> x.getDaysDiff() < baseConfig.getNumDays_LTCG())
		          .collect(Collectors.toCollection(ArrayList::new));

		/*
		 * Get the sum of tax free quantities that can be sold off w/o any tax obligation
		 */
		if (taxFreeItems != null)
		{
			if (taxFreeItems.size() > 0)
			{

				currQty += taxFreeItems.stream().mapToInt(x -> x.getQtytoSell()).sum();
			}
		}

		/*
		 * Check if we need any more quantities to pull in from Taxable quantities to complete Sell Order
		 */
		if (currQty > 0)
		{
			qtytaxable = txnSummary.getTotalQty() - currQty;
		}
		else
		{
			qtytaxable = txnSummary.getTotalQty();
		}

		if (qtytaxable > 0)
		{
			// Add tax free quantities to Final Sell Quantities
			sellItems.addAll(taxFreeItems);

			Comparator<TY_SaleEligibleItems> byDiffDays = (e1, e2) -> Integer.compare(e1.getDaysDiff(), e2.getDaysDiff());
			// Sort Non Tax Free items in ascending order by DaysDiff to LTCG
			taxNotFreeItemsSorted = taxNotFreeItems.stream().sorted(byDiffDays).collect(Collectors.toCollection(ArrayList::new));

			int idx = 0;
			while (qtytaxable != 0)
			{

				if (qtytaxable > taxNotFreeItemsSorted.get(idx).getQtytoSell())
				{
					qtytaxable -= taxNotFreeItemsSorted.get(idx).getQtytoSell();
					sellItems.add(taxNotFreeItemsSorted.get(idx));

				}

				else
				{

					sellItems.add(new TY_SaleEligibleItems(taxNotFreeItemsSorted.get(idx).getPosItemNo(),
					          taxNotFreeItemsSorted.get(idx).getDaysDiff(), qtytaxable, 0));
					qtytaxable = 0;
				}

				idx++;
			}

		}
		else // Sufficient Quantities available as Non Taxable only - Utilize them
		{
			int curr_Qty = 0, sumQty = 0;

			boolean furtherLoop = true;

			for ( int idx = 0; idx <= taxFreeItems.size(); idx++ )
			{
				if (furtherLoop)
				{
					curr_Qty += taxFreeItems.get(idx).getQtytoSell();

					if (curr_Qty <= txnSummary.getTotalQty())
					{
						sellItems.add(taxFreeItems.get(idx));
						sumQty += taxFreeItems.get(idx).getQtytoSell();
					}
					else
					{
						int numSell = txnSummary.getTotalQty() - sumQty;
						sellItems.add(new TY_SaleEligibleItems(taxFreeItems.get(idx).getPosItemNo(), taxFreeItems.get(idx).getDaysDiff(),
						          numSell, 0));

						furtherLoop = false;
					}
				}
			}

		}

		return sellItems;

	}

	/**
	 * ---------------------------- Process for Free Shares if Any------------------------
	 * 
	 * @param probableItems
	 *             - SEl lItems
	 * @param txnSummary
	 *             - Txn. Summary
	 * @param posModel
	 *             - Scrip Position Model from Portfolio Manager
	 * @return - Items to Sell with Free Shares contribution in each sell Item
	 *         ---------------------------------------------------------------------------------------
	 */
	private ArrayList<TY_SaleEligibleItems> processForFreeShares(ArrayList<TY_SaleEligibleItems> probableItems, TY_SCTxn_Summary txnSummary,
	          TY_Scrip_PositionModel posModel)
	{
		ArrayList<TY_SaleEligibleItems> sellItems = new ArrayList<TY_SaleEligibleItems>();

		if (probableItems.size() > 0)
		{
			// Check if any free shares are involved in Current Sale
			OB_Positions_Header posH = posModel.getScPosHeader();
			if (posH != null)
			{
				if (txnSummary.getTotalQty() > posH.getCurrHolding())
				{
					// Free Shares involved
					int freeSharesToSell = txnSummary.getTotalQty() - posH.getCurrHolding();

					int adjQty = 0;
					boolean finalLoop = false;

					for ( int i = 0; i < probableItems.size(); i++ )
					{
						if (!finalLoop)
						{
							adjQty += probableItems.get(i).getQtytoSell();

							if (adjQty < freeSharesToSell)
							{
								probableItems.get(i).setNumFreeShares(probableItems.get(i).getQtytoSell());
							}
							else
							{
								int finalQty = freeSharesToSell - (adjQty - probableItems.get(i).getQtytoSell());
								probableItems.get(i).setNumFreeShares(finalQty);
								finalLoop = true;
							}
						}
					}

					sellItems = probableItems;

				}

				else
				{
					// No recalibration needed - All Sales from Current Holding
					sellItems = probableItems;
				}
			}
		}
		return sellItems;

	}

}
