package scriptsengine.pricingengine.definitions;

import scriptsengine.enums.SCEenums;

/**
 * 
 * Pricing Projection Factor Detail
 *
 */
public class TY_PFactorDetail
{
	private String				desc;
	private SCEenums.direction	direction;
	private double				percentage;

	/**
	 * @return the desc
	 */
	public String getDesc()
	{
		return desc;
	}

	/**
	 * @param desc
	 *             the desc to set
	 */
	public void setDesc(String desc)
	{
		this.desc = desc;
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
	 * @param desc
	 * @param direction
	 * @param percentage
	 */
	public TY_PFactorDetail(String desc, scriptsengine.enums.SCEenums.direction direction, double percentage)
	{
		super();
		this.desc = desc;
		this.direction = direction;
		this.percentage = percentage;
	}

	/**
	 * 
	 */
	public TY_PFactorDetail()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
