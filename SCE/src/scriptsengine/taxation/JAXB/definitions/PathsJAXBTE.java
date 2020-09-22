package scriptsengine.taxation.JAXB.definitions;

/**
 * Taxation Service Mapper JAXB Path class - a Session Bean wil be instantiated while loading Scrips Engine
 *
 */
public class PathsJAXBTE
{
	private String	JaxPath_TE_xsd;
	private String	JaxPath_TE_xml_GEN;
	private String	JaxPath_TE_xml;

	/**
	 * @TEturn the JaxPath_TE_xsd
	 */
	public String getJaxPath_TE_xsd()
	{
		return JaxPath_TE_xsd;
	}

	/**
	 * @param JaxPath_TE_xsd
	 *             the JaxPath_TE_xsd to set
	 */
	public void setJaxPath_TE_xsd(String jaxPath_TE_xsd)
	{
		JaxPath_TE_xsd = jaxPath_TE_xsd;
	}

	/**
	 * @TEturn the jaxPath_TE_xml_GEN
	 */
	public String getJaxPath_TE_xml_GEN()
	{
		return JaxPath_TE_xml_GEN;
	}

	/**
	 * @param jaxPath_TE_xml_GEN
	 *             the jaxPath_TE_xml_GEN to set
	 */
	public void setJaxPath_TE_xml_GEN(String jaxPath_TE_xml_GEN)
	{
		JaxPath_TE_xml_GEN = jaxPath_TE_xml_GEN;
	}

	/**
	 * @TEturn the jaxPath_TE_xml
	 */
	public String getJaxPath_TE_xml()
	{
		return JaxPath_TE_xml;
	}

	/**
	 * @param jaxPath_TE_xml
	 *             the jaxPath_TE_xml to set
	 */
	public void setJaxPath_TE_xml(String jaxPath_TE_xml)
	{
		JaxPath_TE_xml = jaxPath_TE_xml;
	}

	/**
	 * 
	 */
	public PathsJAXBTE()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param JaxPath_TE_xsd
	 * @param jaxPath_TE_xml_GEN
	 * @param jaxPath_TE_xml
	 */
	public PathsJAXBTE(String jaxPath_TE_xsd, String jaxPath_TE_xml_GEN, String jaxPath_TE_xml)
	{
		super();
		JaxPath_TE_xsd = jaxPath_TE_xsd;
		JaxPath_TE_xml_GEN = jaxPath_TE_xml_GEN;
		JaxPath_TE_xml = jaxPath_TE_xml;
	}

}
