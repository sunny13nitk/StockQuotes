package modelframework.JAXB.implementations;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import modelframework.JAXB.definitions.objschemas.Dependant_Object_Defn;
import modelframework.JAXB.definitions.objschemas.ObjectSchema;
import modelframework.JAXB.definitions.objschemas.ObjectSchemas;
import modelframework.JAXB.definitions.objschemas.Root_Object_Defn;
import modelframework.constants.Framework_Constants;
import modelframework.exceptions.EX_SchemaLoadException;
import modelframework.interfaces.IJaxable;

public class ObjectSchemas_JAXB implements IJaxable
{

	private String	Schema_Loc;

	private String	Xml_Loc;

	private String	Xml_Gen_Loc;

	public String getSchema_Loc()
	{
		return Schema_Loc;
	}

	public String getXml_Loc()
	{
		return Xml_Loc;
	}

	public String getXml_Gen_Loc()
	{
		return Xml_Gen_Loc;
	}

	// ********************************Constructors******************

	public ObjectSchemas_JAXB(String schema_Loc, String xml_Loc, String xml_Gen_Loc)
	{

		Schema_Loc = schema_Loc;
		Xml_Loc = xml_Loc;
		Xml_Gen_Loc = xml_Gen_Loc;
	}

	public ObjectSchemas_JAXB()
	{

	}

	public ObjectSchemas_JAXB(Framework_Constants Constants)
	{
		this.Schema_Loc = Constants.getLoc_objschemas_schema();
		this.Xml_Loc = Constants.getLoc_objschemas_xml();
		this.Xml_Gen_Loc = Constants.getLoc_objschemas_xml_gen();
	}

	// Generate Schema
	@Override
	public void Generate_XSD() throws Exception
	{
		// Generate XSD from the ObjSchemas Class
		// grab the context

		JAXBContext context = JAXBContext.newInstance(ObjectSchemas.class);

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
				          File file1 = new File(getSchema_Loc());
				          // create stream result
				          StreamResult result = new StreamResult(file1);
				          // set system id
				          result.setSystemId(file1.toURI().toURL().toString());

				          // return result
				          return result;

			          }
		          });

	}

	// Generate XML
	@Override
	public void Generate_XML() throws Exception
	{
		ObjectSchemas Schemas = new ObjectSchemas();

		ObjectSchema Schema = new ObjectSchema();
		Root_Object_Defn ob_scrip = new Root_Object_Defn("OB_Scrip", "TB_Scrip", false, "Scrip_Code", null);

		ArrayList<Dependant_Object_Defn> Relations = new ArrayList<Dependant_Object_Defn>();
		Dependant_Object_Defn scrip_ba_det = new Dependant_Object_Defn("OB_Scrip_BADet", "OB_Scrip", null, "OB_Scrip_BADet_Rel", "TB_Scrip_BADet",
		          1, "Scrip_Code", true, null, "Det_ID");

		Dependant_Object_Defn scrip_fin_det = new Dependant_Object_Defn("OB_Scrip_FinDet", "OB_Scrip", null, "OB_Scrip_FinDet_Rel",
		          "TB_Scrip_FinDet", 1, "Scrip_Code", true, null, "Fin_ID");

		Relations.add(scrip_ba_det);
		Relations.add(scrip_fin_det);

		Schema.setRootObjectMetadata(ob_scrip);
		Schema.setRelations(Relations);

		ObjectSchema Schema2 = new ObjectSchema();
		Root_Object_Defn ob_txn_catg = new Root_Object_Defn("OB_Txn_Catg", "TB_Txn_Catg", true, null, "ID");
		Schema2.setRootObjectMetadata(ob_txn_catg);

		Schemas.getObjectSchemas().add(Schema);
		Schemas.getObjectSchemas().add(Schema2);

		// Marshal Here
		JAXBContext jaxbContext = JAXBContext.newInstance(ObjectSchemas.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		// Marshal the ObjectSchemas to file in OS Specific Location as
		// per properties file
		jaxbMarshaller.marshal(Schemas, new File(this.getXml_Loc()));
		// Later copy the same file to your project
		// directory/src/config/models/models.xml
	}

	// Load from System properties specified XML to ArrayList of Schema Objects
	@SuppressWarnings("unchecked")
	@Override
	public <T> ArrayList<T> Load_XML_to_Objects() throws EX_SchemaLoadException
	{
		ArrayList<T> list = new ArrayList<>();
		// Get Objects Back from XML
		try
		{
			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectSchemas.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			// We had written this file in marshalling example

			InputStream is = this.getClass().getClassLoader().getResourceAsStream(this.getXml_Loc());
			ObjectSchemas schemas = (ObjectSchemas) jaxbUnmarshaller.unmarshal(is);
			for ( ObjectSchema schema : schemas.getObjectSchemas() )
			{
				list.add((T) schema);
			}
		}
		catch (Exception Ex)
		{
			throw new EX_SchemaLoadException(this.getXml_Loc() + " - " + Ex.getMessage());
		}
		return list;

	}

	/**
	 * Method to get object Schemas from Business Functions to be called on from Blank Instance
	 * Schemas to be accomodated in the Object Factory of Framework Manger by Calling Buss. fxn. Aspect
	 */
	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> Load_XML_to_ObjectsforBussFxn(String xmlLoc) throws EX_SchemaLoadException
	{
		ArrayList<T> list = new ArrayList<>();
		// Get Objects Back from XML
		try
		{
			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectSchemas.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			// We had written this file in marshalling example

			InputStream is = this.getClass().getClassLoader().getResourceAsStream(xmlLoc);
			ObjectSchemas schemas = (ObjectSchemas) jaxbUnmarshaller.unmarshal(is);
			for ( ObjectSchema schema : schemas.getObjectSchemas() )
			{
				list.add((T) schema);
			}
		}
		catch (Exception Ex)
		{
			throw new EX_SchemaLoadException(this.getXml_Loc() + " - " + Ex.getMessage());
		}
		return list;

	}

	// Load from External XML to ArrayList of Schema Objects
	@SuppressWarnings("unchecked")
	@Override

	public <T> ArrayList<T> Load_XML_to_Objects(String extfile_path) throws EX_SchemaLoadException
	{
		ArrayList<T> list = new ArrayList<>();
		// Get Objects Back from XML
		try
		{
			if (extfile_path != null)
			{
				JAXBContext jaxbContext = JAXBContext.newInstance(ObjectSchemas.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

				// We had written this file in marshalling example
				InputStream is = new FileInputStream(new File(extfile_path));
				ObjectSchemas schemas = (ObjectSchemas) jaxbUnmarshaller.unmarshal(is);
				for ( ObjectSchema schema : schemas.getObjectSchemas() )
				{
					list.add((T) schema);
				}
			}
		}
		catch (Exception Ex)
		{
			throw new EX_SchemaLoadException("Error Loading Object Schemas from file " + this.getXml_Loc() + "Details - " + Ex.getMessage());
		}
		return list;
	}

	public ObjectSchemas Get_ObjectSchemas() throws EX_SchemaLoadException
	{
		ObjectSchemas Schemas = new ObjectSchemas();

		try
		{
			Schemas.setObjectSchemas(new ArrayList<ObjectSchema>(this.Load_XML_to_Objects()));
		}
		catch (Exception e)
		{
			throw new EX_SchemaLoadException("Error Loading Object Schemas from file " + this.getXml_Loc() + "Details - " + e.getMessage());
		}

		return Schemas;
	}
}
