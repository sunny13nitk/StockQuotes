package scriptsengine.portfolio.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import scriptsengine.enums.SCEenums.scripSellMode;
import scriptsengine.portfolio.definitions.TY_ScripSell;
import scriptsengine.portfolio.definitions.TY_SellModeQtyAmnt;
import scriptsengine.portfolio.pojos.OB_Positions_Header;
import scriptsengine.portfolio.services.interfaces.IPortfolioManager;
import scriptsengine.portfolio.services.interfaces.ISellScripModeDetSrv;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.validations.interfaces.IScripExists;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SellScripModeDetSrv implements ISellScripModeDetSrv
{

	/********* HOOK US UP ********************/
	@Autowired
	private IPortfolioManager	portFMgr;
	@Autowired
	private IScripExists		scExSrv;

	/********* HOOK US UP ********************/

	/********* PRIVATE PROPERTIES ********************/
	private double				freeSharesRealization;

	/********* PRIVATE PROPERTIES ********************/

	/********* INTERFACE IMPLEMENTATIONS ********************/

	/**
	 * ------------------------------------------------------------------------------------------------
	 * Determine and Return Scrip Sell Helper according to System Rules : Only with Scrip Sell Container
	 * Uses Portfolio Manager bean as autowired to deduce results
	 * 
	 * @param selScripDetails
	 *             - Scrip Sell Container
	 * @return - Scrip Sell Helper Instance
	 * 
	 * @throws EX_General
	 *              -------------------------------------------------------------------------------------------
	 * 
	 */
	@Override
	public TY_SellModeQtyAmnt getScripSellMode(TY_ScripSell scripSellDetails) throws EX_General
	{
		TY_SellModeQtyAmnt scSellHelper = null;

		if (portFMgr != null && scripSellDetails != null)
		{
			if (portFMgr.getMyDematACs() != null)
			{
				scSellHelper = new TY_SellModeQtyAmnt();
				int totalSellQty = scripSellDetails.getSellItems().stream().mapToInt(x -> x.getQty()).sum();
				double totalCurrAmnt = scripSellDetails.getSellItems().stream().mapToDouble(x -> x.getPrice() * x.getQty()).sum();
				scSellHelper.setTotalCurrSellAmnt(totalCurrAmnt);
				scSellHelper.setTotalSellQty(totalSellQty);
				scSellHelper.setSppu(totalCurrAmnt / totalSellQty);

				// Further DEtails
				// Determine if Scrip Code is Provided
				if (scripSellDetails.getScCode() == null && scripSellDetails.getScDesc() != null)
				{
					// In case Not populate through SCrip Exists Service
					if (scExSrv != null)
					{
						try
						{
							scripSellDetails.setScCode(scExSrv.getScripRootbyDescStartingwith(scripSellDetails.getScDesc()).getscCode());
						}
						catch (Exception e)
						{
							EX_General egen = new EX_General("INVALID_SCRIP", new Object[]
							{ scripSellDetails.getScDesc(), e.getMessage()
							});
							throw egen;
						}
					}

					// If Still Scrip code can't be determined - throw Exception
					if (scripSellDetails.getScCode() == null)
					{
						EX_General egen = new EX_General("ERR_NOSCRIP", null);
						throw egen;

					}
					else
					{
						scSellHelper.setScCode(scripSellDetails.getScCode());
					}

				}

				/**
				 * Determine If Scrip already exists in Portfolio - Positions Header get as the Scrip can be sold
				 * only if it is held in Portfolio
				 */
				OB_Positions_Header posH = portFMgr.getPositionsHeaderforScrip(scripSellDetails.getScCode());
				if (posH == null)
				{
					EX_General egen = new EX_General("ERR_SCRIP_SELL_NOHOLD", new Object[]
					{ scripSellDetails.getScCode()
					});
					throw egen;
				}
				else
				{
					// *************** MODE DETERMIATION**************************************
					// Also Validate that the Current Holding + Free shares in Positons Header for the Scrip should
					// not be less than total Sell Qty using Scrip sell Helper
					if ((posH.getCurrHolding() + posH.getFreeHolding()) < scSellHelper.getTotalSellQty())
					{
						EX_General egen = new EX_General("ERR_SCRIP_SELL_QTY", new Object[]
						{ (posH.getCurrHolding() + posH.getFreeHolding()), scripSellDetails.getScCode(), scSellHelper.getTotalSellQty()
						});
						throw egen;

					}

					// First Figure Out If the Investement Has/could be realized from current Sale of Shares

					if (!isInvestmentFree(posH, scSellHelper))
					{
						/**
						 * LOSS SCENARIO - Cannot be free shares Mode even if requested by User
						 */
						scSellHelper.setFreeSharesEligible(false);
						scSellHelper.setAutoSellMode(scripSellMode.PandL);

					}
					else
					{
						/**
						 * PROFIT SCENARIO - Can be free shares Mode if requested by User
						 */
						scSellHelper.setFreeSharesEligible(true);

						if (scripSellDetails.getSellMode() == scripSellMode.FreeShares)
						{
							// Set System mode to Free Shares only if user has requested so
							scSellHelper.setAutoSellMode(scripSellMode.FreeShares);
						}
						else
						{
							scSellHelper.setAutoSellMode(scripSellMode.PandL);
						}

					}

					/**
					 * Total Sell Qty < = Current Holdings
					 */
					if (scSellHelper.getTotalSellQty() <= posH.getCurrHolding())
					{
						// No changes to Mode determined till this stage - DO NOTHING
					}

					/**
					 * Total Sell Qty < = (Current Holdings + FRee Shares)
					 */
					else if (scSellHelper.getTotalSellQty() <= (posH.getCurrHolding() + posH.getFreeHolding()))
					{
						// MODE in this case can only be P&L since free shares are ought to be sold so no new
						// free shares can be added
						scSellHelper.setAutoSellMode(scripSellMode.PandL);

					}

					// *************** MODE DETERMIATION**************************************

					// If System determines Free share mode till this stage - calculate # Free shares and
					// realization spill

					if (scSellHelper.getAutoSellMode() == scripSellMode.FreeShares)
					{
						freeSharesCalibration(scSellHelper);
					}
					else
					{
						// Determine Realization for a Normal P & L Scenario
						setRealization(posH, scSellHelper);
						PandLCalibration(scSellHelper);
					}
				}
			}

		}

		return scSellHelper;
	}

	/********* ---------------------PRIVATE METHODS ---------------- ********************/

	/**
	 * -----------------------------------------------------------------------------------------------------
	 * Return true if Investment in shares have been freed till now or ought to be freed from Current Share Sale
	 * 
	 * @param posH
	 * @param scSellHelper
	 * @return
	 *         -----------------------------------------------------------------------------------------------------
	 */
	private boolean isInvestmentFree(OB_Positions_Header posH, TY_SellModeQtyAmnt scSellHelper)
	{
		boolean invFree = false;
		if (posH != null)
		{
			freeSharesRealization = scSellHelper.getTotalCurrSellAmnt() + posH.getAmntRealized() + posH.getDividendEarnings()
			          - posH.getCurrInvestment();
			if (freeSharesRealization > 0)
			{
				invFree = true;
			}
			else
			{
				invFree = false;
			}
		}

		return invFree;
	}

	/**
	 * -----------------------------------------------------------------------------------------------------
	 * Determine Realization that is accrued as Sale of Shares
	 * Taxes and Fee will be included as autowired service here later and affect this value
	 * 
	 * @param posH
	 * @param scSellHelper
	 *             ----------------------------------------------------------------------------------------------------
	 *             -
	 */
	private void setRealization(OB_Positions_Header posH, TY_SellModeQtyAmnt scSellHelper)
	{
		scSellHelper.setCurrRealization(scSellHelper.getTotalSellQty() * (scSellHelper.getSppu() - posH.getPPU()));

	}

	/**
	 * -------------------------------------------------------------------------------------------------------
	 * Trigger Free Shares Calibration in Scrip Sell Helper object for Number fo Free shares and any spill over
	 * Realization
	 * 
	 * @param scSellHelper
	 *             - Scrip Sell Helper
	 *             ----------------------------------------------------------------------------------------------------
	 *             -
	 */
	private void freeSharesCalibration(TY_SellModeQtyAmnt scSellHelper)
	{
		// Calculate Number of FRee Shares eligibility
		double numFreeShares = (freeSharesRealization / scSellHelper.getSppu());

		int numFReeint = (int) numFreeShares;

		double decimalShares = numFreeShares - numFReeint;

		double newRealization = decimalShares * scSellHelper.getSppu();

		// Update in scSellHelper
		scSellHelper.setNumFreeShares(numFReeint);
		scSellHelper.setCurrRealization(newRealization);

	}

	private void PandLCalibration(TY_SellModeQtyAmnt scSellHelper)
	{

	}

}
