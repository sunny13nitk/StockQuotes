package scriptsengine.statistics.interfaces;

import scriptsengine.reportsengine.repDS.definitions.TY_Attr_Container;
import scriptsengine.statistics.JAXB.definitions.StatsAttrDetails;
import scriptsengine.uploadengine.definitions.ScripDataContainer;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Attribute Processor Service Interface
 */
public interface IAttribProcessor
{
	public TY_Attr_Container getAttribDataforAttrib(ScripDataContainer scDataCon, StatsAttrDetails attribMdt) throws EX_General;
}
