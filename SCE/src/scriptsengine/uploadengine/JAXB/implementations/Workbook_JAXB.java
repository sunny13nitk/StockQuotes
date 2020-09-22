package scriptsengine.uploadengine.JAXB.implementations;

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

import com.sun.imageio.plugins.wbmp.WBMPMetadata;

import modelframework.interfaces.IJaxable;
import scriptsengine.uploadengine.JAXB.definitions.FieldsMetadata;
import scriptsengine.uploadengine.JAXB.definitions.PathsJAXB;
import scriptsengine.uploadengine.JAXB.definitions.SheetMetadata;
import scriptsengine.uploadengine.JAXB.definitions.WorkbookMetadata;

public class Workbook_JAXB implements IJaxable
{

	private PathsJAXB pathConstants;

	/**
	 * @return the pathConstants
	 */
	public PathsJAXB getPathConstants()
	{
		return pathConstants;
	}

	/**
	 * @param pathConstants
	 *             the pathConstants to set
	 */
	public void setPathConstants(PathsJAXB pathConstants)
	{
		this.pathConstants = pathConstants;
	}

	@Override
	public void Generate_XSD() throws Exception
	{
		// Generate XSD from the ObjSchemas Class
		// grab the context

		JAXBContext context = JAXBContext.newInstance(WorkbookMetadata.class);

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
				          File file1 = new File(getPathConstants().getJaxPath_WB_xsd());
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

		// Create a WorkBook Metadata Object
		WorkbookMetadata wbMetadata = new WorkbookMetadata();

		// Create fields in Sheet1 - General
		ArrayList<FieldsMetadata> fieldsdetails1 = new ArrayList<FieldsMetadata>();

		FieldsMetadata fieldScCode = new FieldsMetadata("Scrip Code", true, true, false, false, false, "scCode");
		fieldsdetails1.add(fieldScCode);
		FieldsMetadata fieldScName = new FieldsMetadata("Description", false, true, false, false, false, "scName");
		fieldsdetails1.add(fieldScName);
		FieldsMetadata fieldScSector = new FieldsMetadata("Sector", false, true, false, false, false, "scSector");
		fieldsdetails1.add(fieldScSector);
		FieldsMetadata fieldScMcap = new FieldsMetadata("Market Cap", false, true, true, true, false, "scmCap");
		fieldsdetails1.add(fieldScMcap);

		// Create Sheet Metadata Object and Add to WbMetadata
		SheetMetadata sheetMetadata1 = new SheetMetadata("General", "OB_Scrip_General", true, true, false, "GENERAL", null, null, fieldsdetails1);
		wbMetadata.getSheetsMetadata().add(sheetMetadata1);

		// Create fields in Sheet1 - General
		ArrayList<FieldsMetadata> fieldsdetails2 = new ArrayList<FieldsMetadata>();

		FieldsMetadata fieldprom = new FieldsMetadata("Promoter Holding", false, true, true, true, false, "promoter");
		fieldsdetails2.add(fieldprom);
		FieldsMetadata fieldfii = new FieldsMetadata("FII Holding", false, true, true, false, false, "fii");
		fieldsdetails2.add(fieldfii);
		FieldsMetadata fielddii = new FieldsMetadata("DII Holding", false, true, true, false, false, "dii");
		fieldsdetails2.add(fielddii);
		FieldsMetadata fieldgeneral = new FieldsMetadata("General Public Holding", false, true, true, true, false, "general");
		fieldsdetails2.add(fieldgeneral);
		FieldsMetadata fieldpledged = new FieldsMetadata("Pledged Promoter Holding", false, true, true, false, false, "pledged");
		fieldsdetails2.add(fieldpledged);

		// Create Sheet Metadata Object and Add to WbMetadata
		SheetMetadata sheetMetadata2 = new SheetMetadata("Shareholding", "OB_Scrip_Shareholding", true, false, false, "GENERAL", null, null,
		          fieldsdetails2);
		wbMetadata.getSheetsMetadata().add(sheetMetadata2);

		// Marshal Here
		JAXBContext jaxbContext = JAXBContext.newInstance(WorkbookMetadata.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		// Marshal the ObjectSchemas to file in OS Specific Location as
		// per properties file
		jaxbMarshaller.marshal(wbMetadata, new File(this.getPathConstants().getJaxPath_WB_xml_GEN()));
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
			JAXBContext jaxbContext = JAXBContext.newInstance(WorkbookMetadata.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			// We had written this file in marshalling example
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(this.getPathConstants().getJaxPath_WB_xml());
			WBMPMetadata wbMetadata = (WBMPMetadata) jaxbUnmarshaller.unmarshal(is);
			// for ( Model model : models.getModels() )
			// {
			list.add((T) wbMetadata);
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
			JAXBContext jaxbContext = JAXBContext.newInstance(WorkbookMetadata.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			// We had written this file in marshalling example
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(extfile_path);
			WorkbookMetadata wbMetadata = (WorkbookMetadata) jaxbUnmarshaller.unmarshal(is);
			// for ( Model model : models.getModels() )
			// {
			list.add((T) wbMetadata);
			// }
		}
		catch (Exception Ex)
		{
			throw new Exception(Ex);
		}
		return list;
	}

	/**
	 * Blank Constructor
	 */
	public Workbook_JAXB()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param pathConstants
	 */
	public Workbook_JAXB(PathsJAXB pathConstants)
	{
		super();
		this.pathConstants = pathConstants;
	}

	public Workbook_JAXB(String xmlPath)
	{
		super();
		this.pathConstants.setJaxPath_WB_xml(xmlPath);
	}

}
