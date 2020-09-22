package scriptsengine.reportsengine.repDS.definitions;

import java.util.ArrayList;

import scriptsengine.statistics.JAXB.definitions.StatsAttrDetails;

/**
 * 
 * Report Data Source Field Information runtime placeholder
 */
public class TY_ReportDS_fldInfo
{

	private String					label;
	private String					refAttrName;
	private ArrayList<TY_AlertInfo>	alertsList;
	private StatsAttrDetails			attribMdt;

	/**
	 * @return the attribMdt
	 */
	public StatsAttrDetails getAttribMdt()
	{
		return attribMdt;
	}

	/**
	 * @param attribMdt
	 *             the attribMdt to set
	 */
	public void setAttribMdt(StatsAttrDetails attribMdt)
	{
		this.attribMdt = attribMdt;
	}

	/**
	 * @return the label
	 */
	public String getLabel()
	{
		return label;
	}

	/**
	 * @param label
	 *             the label to set
	 */
	public void setLabel(String label)
	{
		this.label = label;
	}

	/**
	 * @return the refAttrName
	 */
	public String getRefAttrName()
	{
		return refAttrName;
	}

	/**
	 * @param refAttrName
	 *             the refAttrName to set
	 */
	public void setRefAttrName(String refAttrName)
	{
		this.refAttrName = refAttrName;
	}

	/**
	 * @return the alertsList
	 */
	public ArrayList<TY_AlertInfo> getAlertsList()
	{
		return alertsList;
	}

	/**
	 * @param alertsList
	 *             the alertsList to set
	 */
	public void setAlertsList(ArrayList<TY_AlertInfo> alertsList)
	{
		this.alertsList = alertsList;
	}

	/**
	 * @param label
	 * @param refAttrName
	 * @param alertsList
	 */
	public TY_ReportDS_fldInfo(String label, String refAttrName, ArrayList<TY_AlertInfo> alertsList, StatsAttrDetails attrMdt)
	{
		super();
		this.label = label;
		this.refAttrName = refAttrName;
		this.alertsList = new ArrayList<TY_AlertInfo>();
		this.attribMdt = attrMdt;
	}

	/**
	 * 
	 */
	public TY_ReportDS_fldInfo()
	{
		super();
		this.alertsList = new ArrayList<TY_AlertInfo>();

	}

}
