package scriptsengine.uploadengine.JAXB.definitions;

/**
 * WorkBook JAXB Path class - a Session Bean wil be instantiated while loading Scrips Engine
 *
 */
public class PathsJAXB
{
	private String	JaxPath_WB_xsd;
	private String	JaxPath_WB_xml_GEN;
	private String	JaxPath_WB_xml;

	/**
	 * @return the jaxPath_WB_xsd
	 */
	public String getJaxPath_WB_xsd()
	{
		return JaxPath_WB_xsd;
	}

	/**
	 * @param jaxPath_WB_xsd
	 *             the jaxPath_WB_xsd to set
	 */
	public void setJaxPath_WB_xsd(String jaxPath_WB_xsd)
	{
		JaxPath_WB_xsd = jaxPath_WB_xsd;
	}

	/**
	 * @return the jaxPath_WB_xml_GEN
	 */
	public String getJaxPath_WB_xml_GEN()
	{
		return JaxPath_WB_xml_GEN;
	}

	/**
	 * @param jaxPath_WB_xml_GEN
	 *             the jaxPath_WB_xml_GEN to set
	 */
	public void setJaxPath_WB_xml_GEN(String jaxPath_WB_xml_GEN)
	{
		JaxPath_WB_xml_GEN = jaxPath_WB_xml_GEN;
	}

	/**
	 * @return the jaxPath_WB_xml
	 */
	public String getJaxPath_WB_xml()
	{
		return JaxPath_WB_xml;
	}

	/**
	 * @param jaxPath_WB_xml
	 *             the jaxPath_WB_xml to set
	 */
	public void setJaxPath_WB_xml(String jaxPath_WB_xml)
	{
		JaxPath_WB_xml = jaxPath_WB_xml;
	}

	/**
	 * @param jaxPath_WB_xsd
	 * @param jaxPath_WB_xml_GEN
	 * @param jaxPath_WB_xml
	 */
	public PathsJAXB(String jaxPath_WB_xsd, String jaxPath_WB_xml_GEN, String jaxPath_WB_xml)
	{
		super();
		JaxPath_WB_xsd = jaxPath_WB_xsd;
		JaxPath_WB_xml_GEN = jaxPath_WB_xml_GEN;
		JaxPath_WB_xml = jaxPath_WB_xml;
	}

	/**
	 * 
	 */
	public PathsJAXB()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
