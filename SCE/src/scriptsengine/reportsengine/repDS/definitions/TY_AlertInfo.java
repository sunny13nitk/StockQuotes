package scriptsengine.reportsengine.repDS.definitions;

import scriptsengine.enums.SCEenums;
import scriptsengine.enums.SCEenums.direction;

public class TY_AlertInfo
{
	private SCEenums.alertType	alertType;
	private int				occurances;
	private SCEenums.direction	alertCmpDirection;
	private double				valuetoCmp;
	private String				msgID;
	private boolean			genericmsg;
	private SCEenums.alertMode	alertMode;

	/**
	 * @return the alertMode
	 */
	public SCEenums.alertMode getAlertMode()
	{
		return alertMode;
	}

	/**
	 * @param alertMode
	 *             the alertMode to set
	 */
	public void setAlertMode(SCEenums.alertMode alertMode)
	{
		this.alertMode = alertMode;
	}

	/**
	 * @return the genericmsg
	 */
	public boolean isGenericmsg()
	{
		return genericmsg;
	}

	/**
	 * @param genericmsg
	 *             the genericmsg to set
	 */
	public void setGenericmsg(boolean genericmsg)
	{
		this.genericmsg = genericmsg;
	}

	/**
	 * @return the alertType
	 */
	public SCEenums.alertType getAlertType()
	{
		return alertType;
	}

	/**
	 * @param alertType
	 *             the alertType to set
	 */
	public void setAlertType(SCEenums.alertType alertType)
	{
		this.alertType = alertType;
	}

	/**
	 * @return the occurances
	 */
	public int getOccurances()
	{
		return occurances;
	}

	/**
	 * @param occurances
	 *             the occurances to set
	 */
	public void setOccurances(int occurances)
	{
		this.occurances = occurances;
	}

	/**
	 * @return the alertCmpDirection
	 */
	public SCEenums.direction getAlertCmpDirection()
	{
		return alertCmpDirection;
	}

	/**
	 * @param alertCmpDirection
	 *             the alertCmpDirection to set
	 */
	public void setAlertCmpDirection(SCEenums.direction alertCmpDirection)
	{
		this.alertCmpDirection = alertCmpDirection;
	}

	/**
	 * @return the valuetoCmp
	 */
	public double getValuetoCmp()
	{
		return valuetoCmp;
	}

	/**
	 * @param valuetoCmp
	 *             the valuetoCmp to set
	 */
	public void setValuetoCmp(double valuetoCmp)
	{
		this.valuetoCmp = valuetoCmp;
	}

	/**
	 * @return the msgID
	 */
	public String getMsgID()
	{
		return msgID;
	}

	/**
	 * @param msgID
	 *             the msgID to set
	 */
	public void setMsgID(String msgID)
	{
		this.msgID = msgID;
	}

	/**
	 * @param alertType
	 * @param occurances
	 * @param alertCmpDirection
	 * @param valuetoCmp
	 * @param msgID
	 */
	public TY_AlertInfo(scriptsengine.enums.SCEenums.alertType alertType, int occurances, direction alertCmpDirection, double valuetoCmp,
	          String msgID, boolean genericmsg, SCEenums.alertMode alertMode)
	{
		super();
		this.alertType = alertType;
		this.occurances = occurances;
		this.alertCmpDirection = alertCmpDirection;
		this.valuetoCmp = valuetoCmp;
		this.msgID = msgID;
		this.setGenericmsg(genericmsg);
		this.setAlertMode(alertMode);
	}

	/**
	 * 
	 */
	public TY_AlertInfo()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
