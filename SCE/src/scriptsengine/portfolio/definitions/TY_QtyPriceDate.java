package scriptsengine.portfolio.definitions;

import java.util.Date;

/**
 * 
 * Placeholder Container for Scrip Quantity /Price and Date transacted
 */
public class TY_QtyPriceDate
{
	private int	Qty;
	private double	Price;
	private Date	TxnDate;

	/**
	 * @return the qty
	 */
	public int getQty()
	{
		return Qty;
	}

	/**
	 * @param qty
	 *             the qty to set
	 */
	public void setQty(int qty)
	{
		Qty = qty;
	}

	/**
	 * @return the price
	 */
	public double getPrice()
	{
		return Price;
	}

	/**
	 * @param price
	 *             the price to set
	 */
	public void setPrice(double price)
	{
		Price = price;
	}

	/**
	 * @return the txnDate
	 */
	public Date getTxnDate()
	{
		return TxnDate;
	}

	/**
	 * @param txnDate
	 *             the txnDate to set
	 */
	public void setTxnDate(Date txnDate)
	{
		TxnDate = txnDate;
	}

	/**
	 * @param qty
	 * @param price
	 * @param txnDate
	 */
	public TY_QtyPriceDate(int qty, double price, Date txnDate)
	{
		super();
		Qty = qty;
		Price = price;
		TxnDate = txnDate;
	}

	/**
	 * 
	 */
	public TY_QtyPriceDate()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
