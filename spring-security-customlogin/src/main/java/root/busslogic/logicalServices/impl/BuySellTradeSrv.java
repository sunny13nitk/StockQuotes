package root.busslogic.logicalServices.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.busslogic.Services.interfaces.IFundLineSrv;
import root.busslogic.Services.interfaces.IPortfolioSrv;
import root.busslogic.entity.FundLineItem;
import root.busslogic.entity.Trade;
import root.busslogic.logicalServices.interfaces.IBuySellTradeSrv;
import root.busslogic.logicalServices.interfaces.ITRadeValidationSrv;

@Service
public class BuySellTradeSrv implements IBuySellTradeSrv
{
	@Autowired
	private ITRadeValidationSrv tradeValSrv;
	
	@Autowired
	private TradeTxnContextSrv tradeCtxtSrv;
	
	@Autowired
	private IFundLineSrv flSrv;
	
	@Autowired
	private IPortfolioSrv pfSrv;
	
	@Override
	public void ProcessTrade(
	        Object[] jpArgs
	) throws Exception
	{
		try
		{
			// Initialize
			if (jpArgs != null)
			{
				int   pid    = (int) jpArgs[0];
				Trade tradeI = (Trade) jpArgs[1];
				if (pid > 0 && tradeI != null && tradeValSrv != null)
				{
					// Validate the Trade - Populate Txn Context Session Bean
					tradeValSrv.validateTradeforPF(pid, tradeI);
					
					// Update the DB
					updateDB();
				}
			}
			
		} catch (Exception e)
		{
			throw new Exception(e);
		}
		
	}
	
	/**
	 * ACTUAL DB Update - Backed by Service Methods resp.
	 */
	
	@Override
	public void updateDB(
	) throws Exception
	{
		if (
		    tradeCtxtSrv != null && tradeCtxtSrv.getFlItem() != null && tradeCtxtSrv.getHoldingItem() != null
		            && tradeCtxtSrv.getTradeItem() != null
		)
		{
			
			try
			{
				// Update Fund Line - Update balance and Add Fund line Item
				if (tradeCtxtSrv.getFlid() > 0)
				{
					int          flid = tradeCtxtSrv.getFlid();
					FundLineItem flI  = tradeCtxtSrv.getFlItem();
					flSrv.addFundLineItem(flid, flI);
				}
				
				// Update/Insert Holding
				if (pfSrv != null)
				{
					switch (tradeCtxtSrv.getHoldingMode())
					{
						case Create:
							pfSrv.createHolding(tradeCtxtSrv.getHoldingItem());
							break;
						case Update:
							pfSrv.updateHolding(tradeCtxtSrv.getHoldingItem());
							break;
						default:
							break;
					}
				} // Create Trade
				pfSrv.createTrade(tradeCtxtSrv.getTradeItem());
				
			} catch (
			
			Exception e)
			{
				throw new Exception(e);
			}
		}
		
	}
	
}
