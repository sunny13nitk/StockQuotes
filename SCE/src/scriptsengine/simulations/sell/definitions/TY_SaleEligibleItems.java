package scriptsengine.simulations.sell.definitions;

/**
 * 
 * OPTIMAL SALE Eligible Items determined From scrips Positions considering Tax Benefits for LTCG
 */
public class TY_SaleEligibleItems
{

	private int	posItemNo;
	private int	daysDiff;
	private int	qtytoSell;
	private int	numFreeShares;

	/**
	 * @return the numFreeShares
	 */
	public int getNumFreeShares()
	{
		return numFreeShares;
	}

	/**
	 * @param numFreeShares
	 *             the numFreeShares to set
	 */
	public void setNumFreeShares(int numFreeShares)
	{
		this.numFreeShares = numFreeShares;
	}

	/**
	 * @return the posItemNo
	 */
	public int getPosItemNo()
	{
		return posItemNo;
	}

	/**
	 * @param posItemNo
	 *             the posItemNo to set
	 */
	public void setPosItemNo(int posItemNo)
	{
		this.posItemNo = posItemNo;
	}

	/**
	 * @return the daysDiff
	 */
	public int getDaysDiff()
	{
		return daysDiff;
	}

	/**
	 * @param daysDiff
	 *             the daysDiff to set
	 */
	public void setDaysDiff(int daysDiff)
	{
		this.daysDiff = daysDiff;
	}

	/**
	 * @return the qtytoSell
	 */
	public int getQtytoSell()
	{
		return qtytoSell;
	}

	/**
	 * @param qtytoSell
	 *             the qtytoSell to set
	 */
	public void setQtytoSell(int qtytoSell)
	{
		this.qtytoSell = qtytoSell;
	}

	/**
	 * 
	 */
	public TY_SaleEligibleItems()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param posItemNo
	 * @param daysDiff
	 * @param qtytoSell
	 * @param numFreeShares
	 */
	public TY_SaleEligibleItems(int posItemNo, int daysDiff, int qtytoSell, int numFreeShares)
	{
		super();
		this.posItemNo = posItemNo;
		this.daysDiff = daysDiff;
		this.qtytoSell = qtytoSell;
		this.numFreeShares = numFreeShares;
	}

}
