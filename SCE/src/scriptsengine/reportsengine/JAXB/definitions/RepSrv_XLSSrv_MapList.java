package scriptsengine.reportsengine.JAXB.definitions;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * Map List of Reporting Service and XLS Service
 */
@XmlRootElement(name = "RepSrv_XLSSrv_MapList")
@XmlAccessorType(XmlAccessType.FIELD)
public class RepSrv_XLSSrv_MapList
{

	private ArrayList<RepSrv_XLSSrv_Mapping> srvMappings;

	/**
	 * @return the srvMappings
	 */
	public ArrayList<RepSrv_XLSSrv_Mapping> getSrvMappings()
	{
		return srvMappings;
	}

	/**
	 * @param srvMappings
	 *             the srvMappings to set
	 */
	public void setSrvMappings(ArrayList<RepSrv_XLSSrv_Mapping> srvMappings)
	{
		this.srvMappings = srvMappings;
	}

	/**
	 * @param srvMappings
	 */
	public RepSrv_XLSSrv_MapList(ArrayList<RepSrv_XLSSrv_Mapping> srvMappings)
	{
		super();
		this.srvMappings = srvMappings;
	}

	/**
	 * 
	 */
	public RepSrv_XLSSrv_MapList()
	{
		super();
		this.srvMappings = new ArrayList<RepSrv_XLSSrv_Mapping>();
	}

}
