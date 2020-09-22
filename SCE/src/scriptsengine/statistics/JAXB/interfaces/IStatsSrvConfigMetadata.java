package scriptsengine.statistics.JAXB.interfaces;

import java.util.ArrayList;

import scriptsengine.statistics.JAXB.definitions.StatsAttrDetails;
import scriptsengine.statistics.JAXB.definitions.StatsAttrList;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Statistics Service Config Metadata Interface
 */
public interface IStatsSrvConfigMetadata
{

	public StatsAttrList getStatAtrributesList() throws EX_General;

	public String getFiguresSrvBeanName(String attrName) throws EX_General;

	public String getAlertSrvBeanName(String attrName) throws EX_General;

	public StatsAttrDetails getattrDetailsbyAttrName(String attrName) throws EX_General;

	public ArrayList<String> getmShareRelevant_AttrNames() throws EX_General;

}
