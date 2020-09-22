package scriptsengine.valuehelpers.definitions;

/**
 * 
 * Placeholder POJO for String Key and String Description
 */
public class TY_StringKeyDesc
{
	private String	key;
	private String	desc;

	/**
	 * @return the key
	 */
	public String getKey()
	{
		return key;
	}

	/**
	 * @param key
	 *             the key to set
	 */
	public void setKey(String key)
	{
		this.key = key;
	}

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
	 * @param key
	 * @param desc
	 */
	public TY_StringKeyDesc(String key, String desc)
	{
		super();
		this.key = key;
		this.desc = desc;
	}

	/**
	 * 
	 */
	public TY_StringKeyDesc()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
