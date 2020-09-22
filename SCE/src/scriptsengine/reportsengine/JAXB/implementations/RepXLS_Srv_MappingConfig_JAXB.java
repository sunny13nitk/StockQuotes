package scriptsengine.reportsengine.JAXB.implementations;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import modelframework.interfaces.IJaxable;
import scriptsengine.reportsengine.JAXB.definitions.PathsJAXBRE;
import scriptsengine.reportsengine.JAXB.definitions.RepSrv_XLSSrv_MapList;
import scriptsengine.reportsengine.JAXB.definitions.RepSrv_XLSSrv_Mapping;

public class RepXLS_Srv_MappingConfig_JAXB implements IJaxable
{

	private PathsJAXBRE pathConstants;

	/**
	 * @return the pathConstants
	 */
	public PathsJAXBRE getPathConstants()
	{
		return pathConstants;
	}

	/**
	 * @param pathConstants
	 *             the pathConstants to set
	 */
	public void setPathConstants(PathsJAXBRE pathConstants)
	{
		this.pathConstants = pathConstants;
	}

	/**
	 * Blank Constructor
	 */
	public RepXLS_Srv_MappingConfig_JAXB()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param pathConstants
	 */
	public RepXLS_Srv_MappingConfig_JAXB(PathsJAXBRE pathConstants)
	{
		super();
		this.pathConstants = pathConstants;
	}

	public RepXLS_Srv_MappingConfig_JAXB(String xmlPath)
	{
		super();
		this.pathConstants.setJaxPath_RE_xml(xmlPath);
	}

	@Override
	public void Generate_XSD() throws Exception
	{
		// Generate XSD from the ObjSchemas Class
		// grab the context

		JAXBContext context = JAXBContext.newInstance(RepSrv_XLSSrv_MapList.class);

		context.generateSchema(
		          // need to define a SchemaOutputResolver to store to

		          new SchemaOutputResolver()
		          {
			          @Override
			          public Result createOutput(String ns, String file) throws IOException
			          {
				          // save the schema to the file at
		                    // specified path
		                    // create new file
				          File file1 = new File(getPathConstants().getJaxPath_RE_xsd());
				          // create stream result
				          StreamResult result = new StreamResult(file1);
				          // set system id
				          result.setSystemId(file1.toURI().toURL().toString());

				          // return result
				          return result;

			          }
		          });

	}

	@Override
	public void Generate_XML() throws Exception
	{
		// Genrate XML document with Dummy Objects

		// Create a Reporting Service XLS generation Service Metadata Object
		RepSrv_XLSSrv_MapList srvMapList = new RepSrv_XLSSrv_MapList();

		RepSrv_XLSSrv_Mapping srvMapping = new RepSrv_XLSSrv_Mapping("sA_ScripPriceProjectionService", "PriceForecastXlSSrv");

		srvMapList.getSrvMappings().add(srvMapping);

		// Marshal Here
		JAXBContext jaxbContext = JAXBContext.newInstance(RepSrv_XLSSrv_MapList.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		// Marshal the ObjectSchemas to file in OS Specific Location as
		// per properties file
		jaxbMarshaller.marshal(srvMapList, new File(this.getPathConstants().getJaxPath_RE_xml_GEN()));
		// Later copy the same file to your project

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> ArrayList<T> Load_XML_to_Objects() throws Exception
	{
		ArrayList<T> list = new ArrayList<>();
		// Get Objects Back from XML
		try
		{
			JAXBContext jaxbContext = JAXBContext.newInstance(RepSrv_XLSSrv_MapList.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			// We had written this file in marshalling example
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(this.getPathConstants().getJaxPath_RE_xml());
			RepSrv_XLSSrv_MapList srvMappings = (RepSrv_XLSSrv_MapList) jaxbUnmarshaller.unmarshal(is);
			// for ( Model model : models.getModels() )
			// {
			list.add((T) srvMappings);
			// }
		}
		catch (Exception Ex)
		{
			throw new Exception(Ex);
		}
		return list;

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> ArrayList<T> Load_XML_to_Objects(String extfile_path) throws Exception
	{
		ArrayList<T> list = new ArrayList<>();
		// Get Objects Back from XML
		try
		{
			JAXBContext jaxbContext = JAXBContext.newInstance(RepSrv_XLSSrv_MapList.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			// We had written this file in marshalling example
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(extfile_path);
			RepSrv_XLSSrv_MapList srvMappings = (RepSrv_XLSSrv_MapList) jaxbUnmarshaller.unmarshal(is);
			// for ( Model model : models.getModels() )
			// {
			list.add((T) srvMappings);
			// }
		}
		catch (Exception Ex)
		{
			throw new Exception(Ex);
		}
		return list;
	}

}
