package scriptsengine.simulations.sell.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import scriptsengine.enums.SCEenums.scripSellMode;
import scriptsengine.portfolio.pojos.OB_Positions_Header;
import scriptsengine.portfolio.services.implementations.PortfolioManager;
import scriptsengine.simulations.sell.interfaces.IScripSellValidator;
import scriptsengine.uploadengine.exceptions.EX_General;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ScripSellValidatorSrv implements IScripSellValidator
{

	@Autowired
	private PortfolioManager portFMgrSrv;

	/**
	 * Validate Scrip Sale for a Given Scrip Code and Sell quantity
	 */
	@Override
	public scripSellMode validaeScripSaleforScrip(String scCode, int sellQty) throws EX_General
	{
		scripSellMode mode = null;

		if (scCode != null && sellQty > 0)
		{
			// 1. Load Portfolio Manager - Validate to have at least one Demat Account
			if (portFMgrSrv.getMyDematACs().size() > 0)
			{
				/**
				 * Determine If Scrip already exists in Portfolio - Positions Header get as the Scrip can be sold
				 * only if it is held in Portfolio
				 */
				OB_Positions_Header posH = portFMgrSrv.getPositionsHeaderforScrip(scCode);
				if (posH == null)
				{
					EX_General egen = new EX_General("ERR_SCRIP_SELL_NOHOLD", new Object[]
					{ scCode
					});
					throw egen;
				}
				// Also Validate that the Current Holding + Free shares in Positons Header for the Scrip should
				// not be more than total Sell Qty
				if ((posH.getCurrHolding() + posH.getFreeHolding()) < sellQty)
				{
					EX_General egen = new EX_General("ERR_SCRIP_SELL_QTY", new Object[]
					{ (posH.getCurrHolding() + posH.getFreeHolding()), scCode, sellQty
					});
					throw egen;

				}

				// Sell Qty > Current Holding - Need to sell FRee Shares - P&L scenario
				if (sellQty > posH.getCurrHolding())
				{
					mode = scripSellMode.PandL;
				}
				else
				{
					// WE have FRee Shares Possibility depending upon later Computations for Tax and Fee which can
					// be confirmed after proposal Items generation
					mode = scripSellMode.Hybrid;
				}

			}
		}
		return mode;
	}

}
