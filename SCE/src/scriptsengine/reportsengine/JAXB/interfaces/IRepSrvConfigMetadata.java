package scriptsengine.reportsengine.JAXB.interfaces;

import scriptsengine.reportsengine.JAXB.definitions.RepSrv_XLSSrv_MapList;
import scriptsengine.uploadengine.exceptions.EX_General;

public interface IRepSrvConfigMetadata
{

	public RepSrv_XLSSrv_MapList getRepSrvMetadata() throws EX_General;

	public String getRepSrvforSrvBeanName(String srvBeanName) throws EX_General;

}
