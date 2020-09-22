package scriptsengine.portfolio.definitions;

/**
 * Class Definiton to Hold Demat Account / Demat Source Funds Account and the total Amount involved for the Purchase
 * Scrips Txn
 *
 */
public class TY_DematSrcAmnt
{
	private String	dematAC;
	private String	sourceAC;
	private double	txnAmount;

	/**
	 * @return the dematAC
	 */
	public String getDematAC()
	{
		return dematAC;
	}

	/**
	 * @param dematAC
	 *             the dematAC to set
	 */
	public void setDematAC(String dematAC)
	{
		this.dematAC = dematAC;
	}

	/**
	 * @return the sourceAC
	 */
	public String getSourceAC()
	{
		return sourceAC;
	}

	/**
	 * @param sourceAC
	 *             the sourceAC to set
	 */
	public void setSourceAC(String sourceAC)
	{
		this.sourceAC = sourceAC;
	}

	/**
	 * @return the txnAmount
	 */
	public double getTxnAmount()
	{
		return txnAmount;
	}

	/**
	 * @param txnAmount
	 *             the txnAmount to set
	 */
	public void setTxnAmount(double txnAmount)
	{
		this.txnAmount = txnAmount;
	}

	/**
	 * 
	 */
	public TY_DematSrcAmnt()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param dematAC
	 * @param sourceAC
	 * @param txnAmount
	 */
	public TY_DematSrcAmnt(String dematAC, String sourceAC, double txnAmount)
	{
		super();
		this.dematAC = dematAC;
		this.sourceAC = sourceAC;
		this.txnAmount = txnAmount;
	}

}
