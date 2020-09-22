package scriptsengine.utilities.dateStringUtil.JAXB.definitions;

/**
 * WorkBook JAXB Path class - a Session Bean wil be instantiated while loading Scrips Engine
 *
 */
public class PathsJAXBMO
{
	private String	JaxPath_MO_xsd;
	private String	JaxPath_MO_xml_GEN;
	private String	JaxPath_MO_xml;

	public String getJaxPath_MO_xsd()
	{
		return JaxPath_MO_xsd;
	}

	public void setJaxPath_MO_xsd(String jaxPath_MO_xsd)
	{
		JaxPath_MO_xsd = jaxPath_MO_xsd;
	}

	public String getJaxPath_MO_xml_GEN()
	{
		return JaxPath_MO_xml_GEN;
	}

	public void setJaxPath_MO_xml_GEN(String jaxPath_MO_xml_GEN)
	{
		JaxPath_MO_xml_GEN = jaxPath_MO_xml_GEN;
	}

	public String getJaxPath_MO_xml()
	{
		return JaxPath_MO_xml;
	}

	public void setJaxPath_MO_xml(String jaxPath_MO_xml)
	{
		JaxPath_MO_xml = jaxPath_MO_xml;
	}

	public PathsJAXBMO(String jaxPath_MO_xsd, String jaxPath_MO_xml_GEN, String jaxPath_MO_xml)
	{
		super();
		JaxPath_MO_xsd = jaxPath_MO_xsd;
		JaxPath_MO_xml_GEN = jaxPath_MO_xml_GEN;
		JaxPath_MO_xml = jaxPath_MO_xml;
	}

	public PathsJAXBMO()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
