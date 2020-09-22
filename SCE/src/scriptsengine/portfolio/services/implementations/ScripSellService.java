package scriptsengine.portfolio.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import scriptsengine.portfolio.definitions.TY_ScripSell;
import scriptsengine.portfolio.definitions.TY_SellModeQtyAmnt;
import scriptsengine.portfolio.pojos.OB_Positions_Header;
import scriptsengine.portfolio.services.interfaces.IPortfolioManager;
import scriptsengine.portfolio.services.interfaces.IScripSellService;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.validations.interfaces.IScripExists;

@Service("ScripSellService")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ScripSellService implements IScripSellService
{

	/********* HOOK US UP ********************/
	@Autowired
	private IPortfolioManager	portFMgr;

	@Autowired
	private IScripExists		scExSrv;

	/****************************************
	 * PRIVATE MEMBERS
	 ***************************************/

	private TY_SellModeQtyAmnt	scSellHelper;

	/*******************************************
	 * INTERFACE IMPLEMENTATIONS
	 *****************************************/

	@Override
	public void sellScrip(TY_ScripSell scripSellDetails) throws EX_General
	{
		if (scripSellDetails != null && portFMgr != null)
		{
			OB_Positions_Header posH = null;

			if (scSellHelper != null)
			{
				/**
				 * Determine If Scrip already exists in Portfolio - Positions Header get as the Scrip can be sold
				 * only if it is held in Portfolio
				 */
				posH = portFMgr.getPositionsHeaderforScrip(scripSellDetails.getScCode());
			}

			/**
			 * Total Sell Qty < = Current Holdings
			 */
			if (scSellHelper.getTotalSellQty() <= posH.getCurrHolding())
			{
				processSalefromCurrHoldings(scripSellDetails, posH);
			}

			/**
			 * Total Sell Qty < = (Current Holdings + FRee Shares)
			 */
			else if (scSellHelper.getTotalSellQty() <= (posH.getCurrHolding() + posH.getFreeHolding()))
			{

				processSalefromFreeShares(scripSellDetails, posH);
			}

		}

	}

	@Override
	public TY_SellModeQtyAmnt getScripSellHelper()
	{
		return scSellHelper;
	}

	@Override
	public void setScripSellHelper(TY_SellModeQtyAmnt scSellHelper)
	{
		if (this.scSellHelper == null)
		{
			this.scSellHelper = scSellHelper;
		}

	}

	@Override
	public void preSellProcessTrigger(TY_ScripSell scripSellDetails)
	{
		/**
		 * DO NOT WRITE ANYTHING - WILL BE handled via ScripSellFI Aspect
		 */

	}

	@Override
	public void postSellProcessTrigger()
	{
		/**
		 * DO NOT WRITE ANYTHING - WILL BE handled via ScripSellFI Aspect
		 */

	}

	/*****************************************************************
	 * PRIVATE METHODS
	 *******************************************************************/

	private void processSalefromCurrHoldings(TY_ScripSell scSellDetails, OB_Positions_Header posH)
	{

	}

	private void processSalefromFreeShares(TY_ScripSell scSellDetails, OB_Positions_Header posH)
	{

	}
}
