package scriptsengine.reportsengine.JAXB.definitions;

/**
 * Reports Service Mapper JAXB Path class - a Session Bean wil be instantiated while loading Scrips Engine
 *
 */
public class PathsJAXBRE
{
	private String	JaxPath_RE_xsd;
	private String	JaxPath_RE_xml_GEN;
	private String	JaxPath_RE_xml;

	/**
	 * @return the JaxPath_RE_xsd
	 */
	public String getJaxPath_RE_xsd()
	{
		return JaxPath_RE_xsd;
	}

	/**
	 * @param JaxPath_RE_xsd
	 *             the JaxPath_RE_xsd to set
	 */
	public void setJaxPath_RE_xsd(String jaxPath_RE_xsd)
	{
		JaxPath_RE_xsd = jaxPath_RE_xsd;
	}

	/**
	 * @return the jaxPath_RE_xml_GEN
	 */
	public String getJaxPath_RE_xml_GEN()
	{
		return JaxPath_RE_xml_GEN;
	}

	/**
	 * @param jaxPath_RE_xml_GEN
	 *             the jaxPath_RE_xml_GEN to set
	 */
	public void setJaxPath_RE_xml_GEN(String jaxPath_RE_xml_GEN)
	{
		JaxPath_RE_xml_GEN = jaxPath_RE_xml_GEN;
	}

	/**
	 * @return the jaxPath_RE_xml
	 */
	public String getJaxPath_RE_xml()
	{
		return JaxPath_RE_xml;
	}

	/**
	 * @param jaxPath_RE_xml
	 *             the jaxPath_RE_xml to set
	 */
	public void setJaxPath_RE_xml(String jaxPath_RE_xml)
	{
		JaxPath_RE_xml = jaxPath_RE_xml;
	}

	/**
	 * 
	 */
	public PathsJAXBRE()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param JaxPath_RE_xsd
	 * @param jaxPath_RE_xml_GEN
	 * @param jaxPath_RE_xml
	 */
	public PathsJAXBRE(String jaxPath_RE_xsd, String jaxPath_RE_xml_GEN, String jaxPath_RE_xml)
	{
		super();
		JaxPath_RE_xsd = jaxPath_RE_xsd;
		JaxPath_RE_xml_GEN = jaxPath_RE_xml_GEN;
		JaxPath_RE_xml = jaxPath_RE_xml;
	}

}
