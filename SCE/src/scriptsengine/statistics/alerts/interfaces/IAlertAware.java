package scriptsengine.statistics.alerts.interfaces;

import java.util.ArrayList;

import scriptsengine.reportsengine.repDS.definitions.TY_Attr_Container;
import scriptsengine.statistics.alerts.definitions.TY_Alert;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Alert Aware Interface -To Be Implemented by Alert Processing Service Implementation for Individual Stastistics
 * Attributes
 */
public interface IAlertAware
{
	public ArrayList<TY_Alert> processAlertsforAttribContainer(TY_Attr_Container attrContainer) throws EX_General;
}
