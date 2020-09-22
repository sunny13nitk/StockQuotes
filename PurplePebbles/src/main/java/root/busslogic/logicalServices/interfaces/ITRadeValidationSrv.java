package root.busslogic.logicalServices.interfaces;

import root.busslogic.entity.Trade;

public interface ITRadeValidationSrv
{
	public void validateTradeforPF(
	        int pid, Trade tradeItem
	) throws Exception;
}
