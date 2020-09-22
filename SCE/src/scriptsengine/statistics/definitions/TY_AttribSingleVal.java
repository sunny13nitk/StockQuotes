package scriptsengine.statistics.definitions;

public class TY_AttribSingleVal
{
	private String	attrName;
	private String	attrLabel;
	private Object	value;
	private String	attrType;

	/**
	 * @return the attrLabel
	 */
	public String getAttrLabel()
	{
		return attrLabel;
	}

	/**
	 * @param attrLabel
	 *             the attrLabel to set
	 */
	public void setAttrLabel(String attrLabel)
	{
		this.attrLabel = attrLabel;
	}

	/**
	 * @return the attrName
	 */
	public String getAttrName()
	{
		return attrName;
	}

	/**
	 * @param attrName
	 *             the attrName to set
	 */
	public void setAttrName(String attrName)
	{
		this.attrName = attrName;
	}

	/**
	 * @return the value
	 */
	public Object getValue()
	{
		return value;
	}

	/**
	 * @param value
	 *             the value to set
	 */
	public void setValue(Object value)
	{
		this.value = value;
	}

	/**
	 * @return the attrType
	 */
	public String getAttrType()
	{
		return attrType;
	}

	/**
	 * @param attrType
	 *             the attrType to set
	 */
	public void setAttrType(String attrType)
	{
		this.attrType = attrType;
	}

	/**
	 * @param attrName
	 * @param value
	 * @param attrType
	 */
	public TY_AttribSingleVal(String attrName, String label, Object value, String attrType)
	{
		super();
		this.attrName = attrName;
		this.attrLabel = label;
		this.value = value;
		this.attrType = attrType;
	}

	/**
	 * 
	 */
	public TY_AttribSingleVal()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
