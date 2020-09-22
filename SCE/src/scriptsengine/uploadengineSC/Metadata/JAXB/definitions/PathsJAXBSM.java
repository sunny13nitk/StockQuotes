package scriptsengine.uploadengineSC.Metadata.JAXB.definitions;

/**
 * WorkBook JAXB Path class - a Session Bean wil be instantiated while loading Scrips Engine
 *
 */
public class PathsJAXBSM
{
	private String	JaxPath_SM_xsd;
	private String	JaxPath_SM_xml_GEN;
	private String	JaxPath_SM_xml;

	public String getJaxPath_SM_xsd()
	{
		return JaxPath_SM_xsd;
	}

	public void setJaxPath_SM_xsd(String jaxPath_SM_xsd)
	{
		JaxPath_SM_xsd = jaxPath_SM_xsd;
	}

	public String getJaxPath_SM_xml_GEN()
	{
		return JaxPath_SM_xml_GEN;
	}

	public void setJaxPath_SM_xml_GEN(String jaxPath_SM_xml_GEN)
	{
		JaxPath_SM_xml_GEN = jaxPath_SM_xml_GEN;
	}

	public String getJaxPath_SM_xml()
	{
		return JaxPath_SM_xml;
	}

	public void setJaxPath_SM_xml(String jaxPath_SM_xml)
	{
		JaxPath_SM_xml = jaxPath_SM_xml;
	}

	public PathsJAXBSM(String jaxPath_SM_xsd, String jaxPath_SM_xml_GEN, String jaxPath_SM_xml)
	{
		super();
		JaxPath_SM_xsd = jaxPath_SM_xsd;
		JaxPath_SM_xml_GEN = jaxPath_SM_xml_GEN;
		JaxPath_SM_xml = jaxPath_SM_xml;
	}

	public PathsJAXBSM()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
