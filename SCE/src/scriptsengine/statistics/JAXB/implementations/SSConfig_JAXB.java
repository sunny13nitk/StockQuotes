package scriptsengine.statistics.JAXB.implementations;

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
import scriptsengine.statistics.JAXB.definitions.PathsJAXBSS;
import scriptsengine.statistics.JAXB.definitions.StatsAttrDetails;
import scriptsengine.statistics.JAXB.definitions.StatsAttrList;

public class SSConfig_JAXB implements IJaxable
{

	private PathsJAXBSS pathConstants;

	/**
	 * @return the pathConstants
	 */
	public PathsJAXBSS getPathConstants()
	{
		return pathConstants;
	}

	/**
	 * @param pathConstants
	 *             the pathConstants to set
	 */
	public void setPathConstants(PathsJAXBSS pathConstants)
	{
		this.pathConstants = pathConstants;
	}

	/**
	 * Blank Constructor
	 */
	public SSConfig_JAXB()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param pathConstants
	 */
	public SSConfig_JAXB(PathsJAXBSS pathConstants)
	{
		super();
		this.pathConstants = pathConstants;
	}

	public SSConfig_JAXB(String xmlPath)
	{
		super();
		this.pathConstants.setJaxPath_SS_xml(xmlPath);
	}

	@Override
	public void Generate_XSD() throws Exception
	{
		// Generate XSD from the ObjSchemas Class
		// grab the context

		JAXBContext context = JAXBContext.newInstance(StatsAttrList.class);

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
				          File file1 = new File(getPathConstants().getJaxPath_SS_xsd());
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

		// Create a Prices Projection Metadata Object
		StatsAttrList statList = new StatsAttrList();

		StatsAttrDetails attrdet = new StatsAttrDetails("Nett. Sales", "NA", "BalanceSheet", "NettSales", "year", false, false, true, false, true,
		          false, false, true, true, true, true, false, "", true);

		statList.getStatAttrs().add(attrdet);

		// Marshal Here
		JAXBContext jaxbContext = JAXBContext.newInstance(StatsAttrList.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		// Marshal the ObjectSchemas to file in OS Specific Location as
		// per properties file
		jaxbMarshaller.marshal(statList, new File(this.getPathConstants().getJaxPath_SS_xml_GEN()));
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
			JAXBContext jaxbContext = JAXBContext.newInstance(StatsSrvConfigMetadata.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			// We had written this file in marshalling example
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(this.getPathConstants().getJaxPath_SS_xml());
			StatsSrvConfigMetadata ssMetadata = (StatsSrvConfigMetadata) jaxbUnmarshaller.unmarshal(is);
			// for ( Model model : models.getModels() )
			// {
			list.add((T) ssMetadata);
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
			JAXBContext jaxbContext = JAXBContext.newInstance(StatsAttrList.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			// We had written this file in marshalling example
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(extfile_path);
			StatsAttrList ssMetadata = (StatsAttrList) jaxbUnmarshaller.unmarshal(is);
			// for ( Model model : models.getModels() )
			// {
			list.add((T) ssMetadata);
			// }
		}
		catch (Exception Ex)
		{
			throw new Exception(Ex);
		}
		return list;
	}

}
