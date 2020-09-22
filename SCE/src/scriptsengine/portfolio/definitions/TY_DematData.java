package scriptsengine.portfolio.definitions;

/**
 * 
 * POJO to hold Demat AC Specific information from Account of Type demat
 */
public class TY_DematData
{
	private String	AcNum;
	private String	sourceACNum;
	private double	brokerage;

	/**
	 * @return the acNum
	 */
	public String getAcNum()
	{
		return AcNum;
	}

	/**
	 * @param acNum
	 *             the acNum to set
	 */
	public void setAcNum(String acNum)
	{
		AcNum = acNum;
	}

	/**
	 * @return the sourceACNum
	 */
	public String getSourceACNum()
	{
		return sourceACNum;
	}

	/**
	 * @param sourceACNum
	 *             the sourceACNum to set
	 */
	public void setSourceACNum(String sourceACNum)
	{
		this.sourceACNum = sourceACNum;
	}

	/**
	 * @return the brokerage
	 */
	public double getBrokerage()
	{
		return brokerage;
	}

	/**
	 * @param brokerage
	 *             the brokerage to set
	 */
	public void setBrokerage(double brokerage)
	{
		this.brokerage = brokerage;
	}

	/**
	 * 
	 */
	public TY_DematData()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param acNum
	 * @param sourceACNum
	 * @param brokerage
	 */
	public TY_DematData(String acNum, String sourceACNum, double brokerage)
	{
		super();
		AcNum = acNum;
		this.sourceACNum = sourceACNum;
		this.brokerage = brokerage;
	}

}
