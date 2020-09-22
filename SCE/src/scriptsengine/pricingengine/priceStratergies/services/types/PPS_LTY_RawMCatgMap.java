package scriptsengine.pricingengine.priceStratergies.services.types;

import scriptsengine.enums.SCEenums;

/**
 * Price Projection Stratergy Local Type Raw Material Category Mapping
 *
 */
public class PPS_LTY_RawMCatgMap
{
	private String				rawM;
	private SCEenums.direction	direction;
	private double				percentage;
	private String				rawMCatg;

	/**
	 * @return the rawM
	 */
	public String getRawM()
	{
		return rawM;
	}

	/**
	 * @param rawM
	 *             the rawM to set
	 */
	public void setRawM(String rawM)
	{
		this.rawM = rawM;
	}

	/**
	 * @return the direction
	 */
	public SCEenums.direction getDirection()
	{
		return direction;
	}

	/**
	 * @param direction
	 *             the direction to set
	 */
	public void setDirection(SCEenums.direction direction)
	{
		this.direction = direction;
	}

	/**
	 * @return the percentage
	 */
	public double getPercentage()
	{
		return percentage;
	}

	/**
	 * @param percentage
	 *             the percentage to set
	 */
	public void setPercentage(double percentage)
	{
		this.percentage = percentage;
	}

	/**
	 * @return the rawMCatg
	 */
	public String getRawMCatg()
	{
		return rawMCatg;
	}

	/**
	 * @param rawMCatg
	 *             the rawMCatg to set
	 */
	public void setRawMCatg(String rawMCatg)
	{
		this.rawMCatg = rawMCatg;
	}

	/**
	 * @param rawM
	 * @param direction
	 * @param percentage
	 * @param rawMCatg
	 */
	public PPS_LTY_RawMCatgMap(String rawM, scriptsengine.enums.SCEenums.direction direction, double percentage, String rawMCatg)
	{
		super();
		this.rawM = rawM;
		this.direction = direction;
		this.percentage = percentage;
		this.rawMCatg = rawMCatg;
	}

	/**
	 * 
	 */
	public PPS_LTY_RawMCatgMap()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
