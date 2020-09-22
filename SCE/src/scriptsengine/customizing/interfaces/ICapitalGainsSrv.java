package scriptsengine.customizing.interfaces;

/**
 * 
 * Interface for Capital Gains Computation for a particular Instance with Number of Days in holding and Total
 * Transaction Amount
 *
 */
public interface ICapitalGainsSrv
{
	public double calculateCapitalGainsTax(int NumDays, double RealizedAmount);

}
