package scriptsengine.taxation.JAXB.implementations;

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
import scriptsengine.simulations.sell.definitions.TY_Sell_Proposal;
import scriptsengine.taxation.JAXB.definitions.Obj_ITaxableSrv_MapList;
import scriptsengine.taxation.JAXB.definitions.Obj_ITaxableSrv_Mapping;
import scriptsengine.taxation.JAXB.definitions.PathsJAXBTE;

public class Obj_ITaxableSrv_MappingConfig_JAXB implements IJaxable
{

	private PathsJAXBTE pathConstants;

	public PathsJAXBTE getPathConstants()
	{
		return pathConstants;
	}

	public void setPathConstants(PathsJAXBTE pathConstants)
	{
		this.pathConstants = pathConstants;
	}

	public Obj_ITaxableSrv_MappingConfig_JAXB(PathsJAXBTE pathConstants)
	{
		super();
		this.pathConstants = pathConstants;
	}

	public Obj_ITaxableSrv_MappingConfig_JAXB()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Generate_XSD() throws Exception
	{
		// Generate XSD from the ObjSchemas Class
		// grab the context

		JAXBContext context = JAXBContext.newInstance(Obj_ITaxableSrv_MapList.class);

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
				          File file1 = new File(getPathConstants().getJaxPath_TE_xsd());
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
		Obj_ITaxableSrv_MapList srvMapList = new Obj_ITaxableSrv_MapList();

		Obj_ITaxableSrv_Mapping srvMapping = new Obj_ITaxableSrv_Mapping(TY_Sell_Proposal.class.getName(), "SellProposaltoITaxableConvSrv");

		srvMapList.getObjTaxSrvMappings().add(srvMapping);

		// Marshal Here
		JAXBContext jaxbContext = JAXBContext.newInstance(Obj_ITaxableSrv_MapList.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		// Marshal the ObjectSchemas to file in OS Specific Location as
		// per properties file
		jaxbMarshaller.marshal(srvMapList, new File(this.getPathConstants().getJaxPath_TE_xml_GEN()));
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
			JAXBContext jaxbContext = JAXBContext.newInstance(Obj_ITaxableSrv_MapList.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			// We had written this file in marshalling example
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(this.getPathConstants().getJaxPath_TE_xml());
			Obj_ITaxableSrv_MapList srvMappings = (Obj_ITaxableSrv_MapList) jaxbUnmarshaller.unmarshal(is);
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

	@Override
	public <T> ArrayList<T> Load_XML_to_Objects(String extfile_path) throws Exception
	{
		ArrayList<T> list = new ArrayList<>();
		// Get Objects Back from XML
		try
		{
			JAXBContext jaxbContext = JAXBContext.newInstance(Obj_ITaxableSrv_MapList.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			// We had written this file in marshalling example
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(extfile_path);
			Obj_ITaxableSrv_MapList srvMappings = (Obj_ITaxableSrv_MapList) jaxbUnmarshaller.unmarshal(is);
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
