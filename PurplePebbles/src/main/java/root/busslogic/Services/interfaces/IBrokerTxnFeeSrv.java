package root.busslogic.Services.interfaces;

public interface IBrokerTxnFeeSrv
{
	public double getTxnCost(
	        String brokerName, double txnAmount
	) throws Exception;
}
