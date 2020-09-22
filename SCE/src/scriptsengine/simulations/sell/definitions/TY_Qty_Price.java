package scriptsengine.simulations.sell.definitions;

/**
 * 
 * Quantity Price POJO
 */
public class TY_Qty_Price
{
	private int	Qty;
	private double	price;

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
		return price;
	}

	/**
	 * @param price
	 *             the price to set
	 */
	public void setPrice(double price)
	{
		this.price = price;
	}

	/**
	 * @param qty
	 * @param price
	 */
	public TY_Qty_Price(int qty, double price)
	{
		super();
		Qty = qty;
		this.price = price;
	}

	/**
	 * 
	 */
	public TY_Qty_Price()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
