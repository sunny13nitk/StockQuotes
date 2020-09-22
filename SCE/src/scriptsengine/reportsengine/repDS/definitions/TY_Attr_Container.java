package scriptsengine.reportsengine.repDS.definitions;

import java.util.ArrayList;

import scriptsengine.statistics.alerts.definitions.TY_Alert;
import scriptsengine.statistics.definitions.TY_DeltaFigure;
import scriptsengine.statistics.definitions.TY_KeyFigure;

/**
 * 
 * Attribute Container Class - Structure Definition
 */
public class TY_Attr_Container
{
	private String					attrName;
	private String					attrLabel;
	private ArrayList<TY_KeyFigure>	keyFigures;
	private double					Avg;
	private ArrayList<TY_DeltaFigure>	deltaFigures;
	private double					deltaAvg;
	private ArrayList<TY_Alert>		alerts;

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
	 * @return the alerts
	 */
	public ArrayList<TY_Alert> getAlerts()
	{
		return alerts;
	}

	/**
	 * @param alerts
	 *             the alerts to set
	 */
	public void setAlerts(ArrayList<TY_Alert> alerts)
	{
		this.alerts = alerts;
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
	 * @return the keyFigures
	 */
	public ArrayList<TY_KeyFigure> getKeyFigures()
	{
		return keyFigures;
	}

	/**
	 * @param keyFigures
	 *             the keyFigures to set
	 */
	public void setKeyFigures(ArrayList<TY_KeyFigure> keyFigures)
	{
		this.keyFigures = keyFigures;
	}

	/**
	 * @return the avg
	 */
	public double getAvg()
	{
		return Avg;
	}

	/**
	 * @param avg
	 *             the avg to set
	 */
	public void setAvg(double avg)
	{
		Avg = avg;
	}

	/**
	 * @return the deltaFigures
	 */
	public ArrayList<TY_DeltaFigure> getDeltaFigures()
	{
		return deltaFigures;
	}

	/**
	 * @param deltaFigures
	 *             the deltaFigures to set
	 */
	public void setDeltaFigures(ArrayList<TY_DeltaFigure> deltaFigures)
	{
		this.deltaFigures = deltaFigures;
	}

	/**
	 * @return the deltaAvg
	 */
	public double getDeltaAvg()
	{
		return deltaAvg;
	}

	/**
	 * @param deltaAvg
	 *             the deltaAvg to set
	 */
	public void setDeltaAvg(double deltaAvg)
	{
		this.deltaAvg = deltaAvg;
	}

	/**
	 * 
	 */
	public TY_Attr_Container()
	{
		super();
		this.keyFigures = new ArrayList<TY_KeyFigure>();
		this.deltaFigures = new ArrayList<TY_DeltaFigure>();
		this.alerts = new ArrayList<TY_Alert>();
	}

	/**
	 * @param attrName
	 * @param keyFigures
	 * @param avg
	 * @param deltaFigures
	 * @param deltaAvg
	 */
	public TY_Attr_Container(String attrName, String label, ArrayList<TY_KeyFigure> keyFigures, double avg, ArrayList<TY_DeltaFigure> deltaFigures,
	          double deltaAvg)
	{
		super();
		this.attrName = attrName;
		this.attrLabel = label;
		this.keyFigures = new ArrayList<TY_KeyFigure>();
		this.deltaFigures = new ArrayList<TY_DeltaFigure>();
		Avg = avg;
		this.deltaAvg = deltaAvg;
		this.alerts = new ArrayList<TY_Alert>();
	}

}
