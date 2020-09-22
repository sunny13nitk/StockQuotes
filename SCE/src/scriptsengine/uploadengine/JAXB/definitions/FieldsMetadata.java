package scriptsengine.uploadengine.JAXB.definitions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Fields Metadata inside a sheet
 * which sheet to contain which fields and their mapping to corresponding entity fields
 * Characterstics of each of the fields
 */
@XmlRootElement(name = "FieldsMetadata")
@XmlAccessorType(XmlAccessType.FIELD)
public class FieldsMetadata
{
	// Field Description on Sheet
	private String		sheetField;

	// Field is a key - uniquely identities a entity
	private Boolean	key;

	// Field is Mandatory - field is mandatory and must be filled in especially in create operation
	private Boolean	mandatory;

	// If Field is a DEcmial and not a normal Int
	private Boolean	precision_ON;

	// If the field is numeric and the value is non zero - value should be greater than zero
	private Boolean	nonZero;

	private Boolean	splitAware;

	// Field mapping to Object- Root/Dependant Field
	private String		objField;

	/**
	 * @return the splitAware
	 */
	public Boolean getSplitAware()
	{
		return splitAware;
	}

	/**
	 * @param splitAware
	 *             the splitAware to set
	 */
	public void setSplitAware(Boolean splitAware)
	{
		this.splitAware = splitAware;
	}

	/**
	 * @return the nonZero
	 */
	public Boolean getNonZero()
	{
		return nonZero;
	}

	/**
	 * @param nonZero
	 *             the nonZero to set
	 */
	public void setNonZero(Boolean nonZero)
	{
		this.nonZero = nonZero;
	}

	/**
	 * @return the precision_ON
	 */
	public Boolean getPrecision_ON()
	{
		return precision_ON;
	}

	/**
	 * @param precision_ON
	 *             the precision_ON to set
	 */
	public void setPrecision_ON(Boolean precision_ON)
	{
		this.precision_ON = precision_ON;
	}

	/**
	 * @return the sheetField
	 */
	public String getSheetField()
	{
		return sheetField;
	}

	/**
	 * @param sheetField
	 *             the sheetField to set
	 */
	public void setSheetField(String sheetField)
	{
		this.sheetField = sheetField;
	}

	/**
	 * @return the key
	 */
	public Boolean getKey()
	{
		return key;
	}

	/**
	 * @param key
	 *             the key to set
	 */
	public void setKey(Boolean key)
	{
		this.key = key;
	}

	/**
	 * @return the mandatory
	 */
	public Boolean getMandatory()
	{
		return mandatory;
	}

	/**
	 * @param mandatory
	 *             the mandatory to set
	 */
	public void setMandatory(Boolean mandatory)
	{
		this.mandatory = mandatory;
	}

	/**
	 * @return the objField
	 */
	public String getObjField()
	{
		return objField;
	}

	/**
	 * @param objField
	 *             the objField to set
	 */
	public void setObjField(String objField)
	{
		this.objField = objField;
	}

	/**
	 * @param sheetField
	 * @param key
	 * @param mandatory
	 * @param precision_ON
	 * @param nonZero
	 * @param splitAware
	 * @param objField
	 */
	public FieldsMetadata(String sheetField, Boolean key, Boolean mandatory, Boolean precision_ON, Boolean nonZero, Boolean splitAware,
	          String objField)
	{
		super();
		this.sheetField = sheetField;
		this.key = key;
		this.mandatory = mandatory;
		this.precision_ON = precision_ON;
		this.nonZero = nonZero;
		this.splitAware = splitAware;
		this.objField = objField;
	}

	/**
	 * 
	 */
	public FieldsMetadata()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
