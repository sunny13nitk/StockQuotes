package root.busslogic.logicalServices.interfaces;

public interface IBuySellTradeSrv
{
	public void ProcessTrade(
	        Object[] jpArgs
	) throws Exception;
	
	public void updateDB(
	) throws Exception;
}
