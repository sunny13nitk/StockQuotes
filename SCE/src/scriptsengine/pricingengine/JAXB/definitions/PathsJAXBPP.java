package scriptsengine.pricingengine.JAXB.definitions;

/**
 * WorkBook JAXB Path class - a Session Bean wil be instantiated while loading Scrips Engine
 *
 */
public class PathsJAXBPP
{
	private String	JaxPath_PP_xsd;
	private String	JaxPath_PP_xml_GEN;
	private String	JaxPath_PP_xml;

	/**
	 * @return the jaxPath_PP_xsd
	 */
	public String getJaxPath_PP_xsd()
	{
		return JaxPath_PP_xsd;
	}

	/**
	 * @param jaxPath_PP_xsd
	 *             the jaxPath_PP_xsd to set
	 */
	public void setJaxPath_PP_xsd(String jaxPath_PP_xsd)
	{
		JaxPath_PP_xsd = jaxPath_PP_xsd;
	}

	/**
	 * @return the jaxPath_PP_xml_GEN
	 */
	public String getJaxPath_PP_xml_GEN()
	{
		return JaxPath_PP_xml_GEN;
	}

	/**
	 * @param jaxPath_PP_xml_GEN
	 *             the jaxPath_PP_xml_GEN to set
	 */
	public void setJaxPath_PP_xml_GEN(String jaxPath_PP_xml_GEN)
	{
		JaxPath_PP_xml_GEN = jaxPath_PP_xml_GEN;
	}

	/**
	 * @return the jaxPath_PP_xml
	 */
	public String getJaxPath_PP_xml()
	{
		return JaxPath_PP_xml;
	}

	/**
	 * @param jaxPath_PP_xml
	 *             the jaxPath_PP_xml to set
	 */
	public void setJaxPath_PP_xml(String jaxPath_PP_xml)
	{
		JaxPath_PP_xml = jaxPath_PP_xml;
	}

	/**
	 * 
	 */
	public PathsJAXBPP()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param jaxPath_PP_xsd
	 * @param jaxPath_PP_xml_GEN
	 * @param jaxPath_PP_xml
	 */
	public PathsJAXBPP(String jaxPath_PP_xsd, String jaxPath_PP_xml_GEN, String jaxPath_PP_xml)
	{
		super();
		JaxPath_PP_xsd = jaxPath_PP_xsd;
		JaxPath_PP_xml_GEN = jaxPath_PP_xml_GEN;
		JaxPath_PP_xml = jaxPath_PP_xml;
	}

}
