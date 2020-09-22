package scriptsengine.statistics.alerts.interfaces;

import java.util.ArrayList;

import scriptsengine.reportsengine.repDS.definitions.TY_AlertInfo;
import scriptsengine.reportsengine.repDS.definitions.TY_Attr_Container;
import scriptsengine.statistics.alerts.definitions.TY_Alert;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Interface for Generic Delta alerts
 */
public interface IGenericDeltaAlerts
{

	public ArrayList<TY_Alert> getAlertsforAttrContainer(TY_Attr_Container attrContainer, ArrayList<TY_AlertInfo> deltaalertsInfo) throws EX_General;

}
