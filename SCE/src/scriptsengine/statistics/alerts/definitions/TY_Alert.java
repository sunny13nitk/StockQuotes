package scriptsengine.statistics.alerts.definitions;

import scriptsengine.enums.SCEenums;

public class TY_Alert
{
	private SCEenums.alertMode	alertMode;
	private String				alertMsgText;

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
	 * @return the alertMsgText
	 */
	public String getAlertMsgText()
	{
		return alertMsgText;
	}

	/**
	 * @param alertMsgText
	 *             the alertMsgText to set
	 */
	public void setAlertMsgText(String alertMsgText)
	{
		this.alertMsgText = alertMsgText;
	}

	/**
	 * @param alertMode
	 * @param alertMsgText
	 */
	public TY_Alert(scriptsengine.enums.SCEenums.alertMode alertMode, String alertMsgText)
	{
		super();
		this.alertMode = alertMode;
		this.alertMsgText = alertMsgText;
	}

	/**
	 * 
	 */
	public TY_Alert()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
