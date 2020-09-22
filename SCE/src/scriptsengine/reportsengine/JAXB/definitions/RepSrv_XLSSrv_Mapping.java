package scriptsengine.reportsengine.JAXB.definitions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * Mapping between Reporting Service and XLS Service Beans
 */

@XmlRootElement(name = "RepSrv_XLSSrv_Mapping")
@XmlAccessorType(XmlAccessType.FIELD)
public class RepSrv_XLSSrv_Mapping
{
	private String	repSrvName;
	private String	XLSSrvName;

	/**
	 * @return the repSrvName
	 */
	public String getRepSrvName()
	{
		return repSrvName;
	}

	/**
	 * @param repSrvName
	 *             the repSrvName to set
	 */
	public void setRepSrvName(String repSrvName)
	{
		this.repSrvName = repSrvName;
	}

	/**
	 * @return the xLSSrvName
	 */
	public String getXLSSrvName()
	{
		return XLSSrvName;
	}

	/**
	 * @param xLSSrvName
	 *             the xLSSrvName to set
	 */
	public void setXLSSrvName(String xLSSrvName)
	{
		XLSSrvName = xLSSrvName;
	}

	/**
	 * 
	 */
	public RepSrv_XLSSrv_Mapping()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param repSrvName
	 * @param xLSSrvName
	 */
	public RepSrv_XLSSrv_Mapping(String repSrvName, String xLSSrvName)
	{
		super();
		this.repSrvName = repSrvName;
		XLSSrvName = xLSSrvName;
	}

}
