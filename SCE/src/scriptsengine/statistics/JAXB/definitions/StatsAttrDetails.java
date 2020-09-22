package scriptsengine.statistics.JAXB.definitions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * Statistics Attribute Details
 */
@XmlRootElement(name = "StatsAttrDetails")
@XmlAccessorType(XmlAccessType.FIELD)
public class StatsAttrDetails
{
	private String		attrName;
	private String		figuresSrvBean;
	private String		sheetName;
	private String		fieldName;
	private String		keyfldName;
	private boolean	wrtFaceValue;
	private boolean	singleVal;
	private boolean	trendON;
	private boolean	deltaON;
	private boolean	avgON;
	private boolean	avgdeltaON;
	private boolean	highBetterON;
	private boolean	selfsecSpON;
	private boolean	avgDeltasecSpON;
	private boolean	avgDeltaglobalON;
	private boolean	longtermIncON;
	private boolean	alertON;
	private String		alertDetSrvBean;
	private boolean	mShareAnalysisON;

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
	 * @return the figuresSrvBean
	 */
	public String getFiguresSrvBean()
	{
		return figuresSrvBean;
	}

	/**
	 * @param figuresSrvBean
	 *             the figuresSrvBean to set
	 */
	public void setFiguresSrvBean(String figuresSrvBean)
	{
		this.figuresSrvBean = figuresSrvBean;
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
	 * @return the fieldName
	 */
	public String getFieldName()
	{
		return fieldName;
	}

	/**
	 * @param fieldName
	 *             the fieldName to set
	 */
	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}

	/**
	 * @return the keyfldName
	 */
	public String getKeyfldName()
	{
		return keyfldName;
	}

	/**
	 * @param keyfldName
	 *             the keyfldName to set
	 */
	public void setKeyfldName(String keyfldName)
	{
		this.keyfldName = keyfldName;
	}

	/**
	 * @return the wrtFaceValue
	 */
	public boolean isWrtFaceValue()
	{
		return wrtFaceValue;
	}

	/**
	 * @param wrtFaceValue
	 *             the wrtFaceValue to set
	 */
	public void setWrtFaceValue(boolean wrtFaceValue)
	{
		this.wrtFaceValue = wrtFaceValue;
	}

	/**
	 * @return the singleVal
	 */
	public boolean isSingleVal()
	{
		return singleVal;
	}

	/**
	 * @param singleVal
	 *             the singleVal to set
	 */
	public void setSingleVal(boolean singleVal)
	{
		this.singleVal = singleVal;
	}

	/**
	 * @return the trendON
	 */
	public boolean isTrendON()
	{
		return trendON;
	}

	/**
	 * @param trendON
	 *             the trendON to set
	 */
	public void setTrendON(boolean trendON)
	{
		this.trendON = trendON;
	}

	/**
	 * @return the deltaON
	 */
	public boolean isDeltaON()
	{
		return deltaON;
	}

	/**
	 * @param deltaON
	 *             the deltaON to set
	 */
	public void setDeltaON(boolean deltaON)
	{
		this.deltaON = deltaON;
	}

	/**
	 * @return the avgON
	 */
	public boolean isAvgON()
	{
		return avgON;
	}

	/**
	 * @param avgON
	 *             the avgON to set
	 */
	public void setAvgON(boolean avgON)
	{
		this.avgON = avgON;
	}

	/**
	 * @return the avgdeltaON
	 */
	public boolean isAvgdeltaON()
	{
		return avgdeltaON;
	}

	/**
	 * @param avgdeltaON
	 *             the avgdeltaON to set
	 */
	public void setAvgdeltaON(boolean avgdeltaON)
	{
		this.avgdeltaON = avgdeltaON;
	}

	/**
	 * @return the highBetterON
	 */
	public boolean isHighBetterON()
	{
		return highBetterON;
	}

	/**
	 * @param highBetterON
	 *             the highBetterON to set
	 */
	public void setHighBetterON(boolean highBetterON)
	{
		this.highBetterON = highBetterON;
	}

	/**
	 * @return the selfsecSpON
	 */
	public boolean isSelfsecSpON()
	{
		return selfsecSpON;
	}

	/**
	 * @param selfsecSpON
	 *             the selfsecSpON to set
	 */
	public void setSelfsecSpON(boolean selfsecSpON)
	{
		this.selfsecSpON = selfsecSpON;
	}

	/**
	 * @return the avgDeltasecSpON
	 */
	public boolean isAvgDeltasecSpON()
	{
		return avgDeltasecSpON;
	}

	/**
	 * @param avgDeltasecSpON
	 *             the avgDeltasecSpON to set
	 */
	public void setAvgDeltasecSpON(boolean avgDeltasecSpON)
	{
		this.avgDeltasecSpON = avgDeltasecSpON;
	}

	/**
	 * @return the avgDeltaglobalON
	 */
	public boolean isAvgDeltaglobalON()
	{
		return avgDeltaglobalON;
	}

	/**
	 * @param avgDeltaglobalON
	 *             the avgDeltaglobalON to set
	 */
	public void setAvgDeltaglobalON(boolean avgDeltaglobalON)
	{
		this.avgDeltaglobalON = avgDeltaglobalON;
	}

	/**
	 * @return the longtermIncON
	 */
	public boolean isLongtermIncON()
	{
		return longtermIncON;
	}

	/**
	 * @param longtermIncON
	 *             the longtermIncON to set
	 */
	public void setLongtermIncON(boolean longtermIncON)
	{
		this.longtermIncON = longtermIncON;
	}

	/**
	 * @return the alertON
	 */
	public boolean isAlertON()
	{
		return alertON;
	}

	/**
	 * @param alertON
	 *             the alertON to set
	 */
	public void setAlertON(boolean alertON)
	{
		this.alertON = alertON;
	}

	/**
	 * @return the alertDetSrvBean
	 */
	public String getAlertDetSrvBean()
	{
		return alertDetSrvBean;
	}

	/**
	 * @param alertDetSrvBean
	 *             the alertDetSrvBean to set
	 */
	public void setAlertDetSrvBean(String alertDetSrvBean)
	{
		this.alertDetSrvBean = alertDetSrvBean;
	}

	/**
	 * @return the mShareAnalysisON
	 */
	public boolean ismShareAnalysisON()
	{
		return mShareAnalysisON;
	}

	/**
	 * @param mShareAnalysisON
	 *             the mShareAnalysisON to set
	 */
	public void setmShareAnalysisON(boolean mShareAnalysisON)
	{
		this.mShareAnalysisON = mShareAnalysisON;
	}

	/**
	 * 
	 */
	public StatsAttrDetails()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param attrName
	 * @param figuresSrvBean
	 * @param sheetName
	 * @param fieldName
	 * @param keyfldName
	 * @param wrtFaceValue
	 * @param singleVal
	 * @param trendON
	 * @param deltaON
	 * @param avgON
	 * @param avgdeltaON
	 * @param highBetterON
	 * @param selfsecSpON
	 * @param avgDeltasecSpON
	 * @param avgDeltaglobalON
	 * @param longtermIncON
	 * @param alertON
	 * @param alertDetSrvBean
	 * @param mShareAnalysisON
	 */
	public StatsAttrDetails(String attrName, String figuresSrvBean, String sheetName, String fieldName, String keyfldName, boolean wrtFaceValue,
	          boolean singleVal, boolean trendON, boolean deltaON, boolean avgON, boolean avgdeltaON, boolean highBetterON, boolean selfsecSpON,
	          boolean avgDeltasecSpON, boolean avgDeltaglobalON, boolean longtermIncON, boolean alertON, String alertDetSrvBean,
	          boolean mShareAnalysisON)
	{
		super();
		this.attrName = attrName;
		this.figuresSrvBean = figuresSrvBean;
		this.sheetName = sheetName;
		this.fieldName = fieldName;
		this.keyfldName = keyfldName;
		this.wrtFaceValue = wrtFaceValue;
		this.singleVal = singleVal;
		this.trendON = trendON;
		this.deltaON = deltaON;
		this.avgON = avgON;
		this.avgdeltaON = avgdeltaON;
		this.highBetterON = highBetterON;
		this.selfsecSpON = selfsecSpON;
		this.avgDeltasecSpON = avgDeltasecSpON;
		this.avgDeltaglobalON = avgDeltaglobalON;
		this.longtermIncON = longtermIncON;
		this.alertON = alertON;
		this.alertDetSrvBean = alertDetSrvBean;
		this.mShareAnalysisON = mShareAnalysisON;
	}

}
