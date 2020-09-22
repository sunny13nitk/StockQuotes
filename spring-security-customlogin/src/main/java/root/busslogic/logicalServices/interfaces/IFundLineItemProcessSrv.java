package root.busslogic.logicalServices.interfaces;

public interface IFundLineItemProcessSrv
{
	public void ProcessFundLineItem(
	        Object[] jpArgs
	) throws Exception;
	
	public void Initialize(
	);
	
	public void getFundLine(
	);
	
	public void doPreChecks(
	) throws Exception;
	
	public void updateFundLine(
	);
}
