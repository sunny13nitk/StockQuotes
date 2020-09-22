package scriptsengine.uploadengine.JAXB.definitions;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class to represent Sheet Metadata
 *
 */
@XmlRootElement(name = "SheetMetadata")
@XmlAccessorType(XmlAccessType.FIELD)
public class SheetMetadata
{
	// Name of Sheet
	private String					sheetName;

	// Associated Business Object
	private String					bobjName;

	// Sheet is a mandatory sheet for Scrip Creation
	private Boolean				requiredCreate;

	// Sheet is the base sheet without which the Scrip can neither be created nor updated
	private Boolean				basesheet;

	// Sheet will hold collection of entities and not a single entity
	// To be read in tabular way - i.e a single entity for each row of data to be mapped
	// While for non tabular way - i.e. each row mapped to a column of single entity
	private Boolean				collection;

	// Sheet Property descriptor linked to
	// /SCE/src/scriptsengine/uploadengine/properties/scripreldescriptors.properties
	private String					propDesc;

	// Entity Parser Bean Name - If any specific for a particular Sheet - e.g.Prices sheet processed by
	// Std. Deviation Service
	private String					entParserBeanName;

	// Update Needed processor Bean Name - Service that determines whether a particular sheet - indeed scrip is
	// eligible for being updated so it can be picked up in Mass Update Service
	private String					updateReqdProcessorBean;

	// List of Fields Metadata
	private ArrayList<FieldsMetadata>	fieldsDetails;

	/**
	 * @return the updateReqdProcessorBean
	 */
	public String getUpdateReqdProcessorBean()
	{
		return updateReqdProcessorBean;
	}

	/**
	 * @param updateReqdProcessorBean
	 *             the updateReqdProcessorBean to set
	 */
	public void setUpdateReqdProcessorBean(String updateReqdProcessorBean)
	{
		this.updateReqdProcessorBean = updateReqdProcessorBean;
	}

	/**
	 * @return the entParserBeanName
	 */
	public String getEntParserBeanName()
	{
		return entParserBeanName;
	}

	/**
	 * @param entParserBeanName
	 *             the entParserBeanName to set
	 */
	public void setEntParserBeanName(String entParserBeanName)
	{
		this.entParserBeanName = entParserBeanName;
	}

	/**
	 * @return the basesheet
	 */
	public Boolean getBasesheet()
	{
		return basesheet;
	}

	/**
	 * @param basesheet
	 *             the basesheet to set
	 */
	public void setBasesheet(Boolean basesheet)
	{
		this.basesheet = basesheet;
	}

	/**
	 * @return the sheetName
	 */
	public String getSheetName()
	{
		return sheetName;
	}

	/**
	 * @param sheetName
	 *             the sheetName to set
	 */
	public void setSheetName(String sheetName)
	{
		this.sheetName = sheetName;
	}

	/**
	 * @return the bobjName
	 */
	public String getBobjName()
	{
		return bobjName;
	}

	/**
	 * @param bobjName
	 *             the bobjName to set
	 */
	public void setBobjName(String bobjName)
	{
		this.bobjName = bobjName;
	}

	/**
	 * @return the requiredCreate
	 */
	public Boolean getRequiredCreate()
	{
		return requiredCreate;
	}

	/**
	 * @param requiredCreate
	 *             the requiredCreate to set
	 */
	public void setRequiredCreate(Boolean required)
	{
		this.requiredCreate = required;
	}

	/**
	 * @return the propDesc
	 */
	public String getPropDesc()
	{
		return propDesc;
	}

	/**
	 * @param propDesc
	 *             the propDesc to set
	 */
	public void setPropDesc(String propDesc)
	{
		this.propDesc = propDesc;
	}

	/**
	 * @return the collection
	 */
	public Boolean getCollection()
	{
		return collection;
	}

	/**
	 * @param collection
	 *             the collection to set
	 */
	public void setCollection(Boolean collection)
	{
		this.collection = collection;
	}

	/**
	 * @return the fieldsDetails
	 */
	public ArrayList<FieldsMetadata> getFieldsDetails()
	{
		return fieldsDetails;
	}

	/**
	 * @param fieldsDetails
	 *             the fieldsDetails to set
	 */
	public void setFieldsDetails(ArrayList<FieldsMetadata> fieldsDetails)
	{
		this.fieldsDetails = fieldsDetails;
	}

	/**
	 * @param sheetName
	 * @param bobjName
	 * @param requiredCreate
	 * @param basesheet
	 * @param collection
	 * @param propDesc
	 * @param entParserBeanName
	 * @param updateReqdProcessorBean
	 * @param fieldsDetails
	 */
	public SheetMetadata(String sheetName, String bobjName, Boolean requiredCreate, Boolean basesheet, Boolean collection, String propDesc,
	          String entParserBeanName, String updateReqdProcessorBean, ArrayList<FieldsMetadata> fieldsDetails)
	{
		super();
		this.sheetName = sheetName;
		this.bobjName = bobjName;
		this.requiredCreate = requiredCreate;
		this.basesheet = basesheet;
		this.collection = collection;
		this.propDesc = propDesc;
		this.entParserBeanName = entParserBeanName;
		this.updateReqdProcessorBean = updateReqdProcessorBean;
		this.fieldsDetails = fieldsDetails;
	}

	/**
	 * 
	 */
	public SheetMetadata()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
