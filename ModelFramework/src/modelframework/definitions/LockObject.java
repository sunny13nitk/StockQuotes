package modelframework.definitions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LockObject
{

	private String	objName;

	private Object	pKey;

	private String	userName;

	private String	timestamp;

	/**
	 * @return the objName
	 */
	public String getObjName()
	{
		return objName;
	}

	/**
	 * @param objName
	 *             the objName to set
	 */
	public void setObjName(String objName)
	{
		this.objName = objName;
	}

	/**
	 * @return the pKey
	 */
	public Object getpKey()
	{
		return pKey;
	}

	/**
	 * @param pKey
	 *             the pKey to set
	 */
	public void setpKey(Object pKey)
	{
		this.pKey = pKey;
	}

	/**
	 * @return the userName
	 */
	public String getUserName()
	{
		return userName;
	}

	/**
	 * @param userName
	 *             the userName to set
	 */
	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	/**
	 * @return the timestamp
	 */
	public String getTimestamp()
	{
		return timestamp;
	}

	/**
	 * @param timestamp
	 *             the timestamp to set
	 */
	public void setTimestamp(String timestamp)
	{
		this.timestamp = timestamp;
	}

	/**
	 * Obtain Lock Object Blank Instance with Current time Stamp
	 */
	public LockObject()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Calendar cal = Calendar.getInstance();
		String date_time = (dateFormat.format(cal.getTime()));

		this.setTimestamp(date_time);
	}

	/**
	 * @param objName
	 * @param pKey
	 * @param userName
	 */
	public LockObject(String objName, Object pKey, String userName)
	{

		this.objName = objName;
		this.pKey = pKey;
		this.userName = userName;

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Calendar cal = Calendar.getInstance();
		String date_time = (dateFormat.format(cal.getTime()));

		this.setTimestamp(date_time);
	}

}
