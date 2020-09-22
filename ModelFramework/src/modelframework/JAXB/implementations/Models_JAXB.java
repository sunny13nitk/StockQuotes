package modelframework.JAXB.implementations;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import modelframework.JAXB.definitions.models.Model;
import modelframework.JAXB.definitions.models.Model_Assembly;
import modelframework.JAXB.definitions.models.Models;
import modelframework.constants.Framework_Constants;
import modelframework.exceptions.EX_ModelLoadException;
import modelframework.interfaces.IJaxable;

// Model JAXB Class
public class Models_JAXB implements IJaxable
{

	private String	Model_Schema_Loc;

	private String	Model_Xml_Loc;

	private String	Model_Xml_Gen_Loc;

	public String getModel_Xml_Gen_Loc()
	{
		return Model_Xml_Gen_Loc;
	}

	public String getModel_Schema_Loc()
	{
		return Model_Schema_Loc;
	}

	public String getModel_Xml_Loc()
	{
		return Model_Xml_Loc;
	}

	// ********************************Constructors******************
	public Models_JAXB()
	{

	}

	public Models_JAXB(Framework_Constants Constants)
	{
		this.Model_Schema_Loc = Constants.getLoc_model_schema();
		this.Model_Xml_Loc = Constants.getLoc_model_xml();
		this.Model_Xml_Gen_Loc = Constants.getLoc_model_xml_gen();
	}

	public Models_JAXB(String model_Scheam_Loc, String model_Xml_Loc, String model_Xml_Gen_Loc)
	{

		Model_Schema_Loc = model_Scheam_Loc;
		Model_Xml_Loc = model_Xml_Loc;
		Model_Xml_Gen_Loc = model_Xml_Gen_Loc;
	}

	@Override
	public void Generate_XSD() throws Exception
	{
		// Generate XSD from the Models Class
		// grab the context

		JAXBContext context = JAXBContext.newInstance(Models.class);

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
				          File file1 = new File(getModel_Schema_Loc());
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

		// Create Models Object
		Models models = new Models();

		Model model = new Model();

		// Create two Model Assembly Objects
		Model_Assembly assm1 = new Model_Assembly();
		assm1.setObjName("OB_Txn_Catg");
		assm1.setAssembly("mypackage.OB_Txn_Catg.java");
		model.Add_Assembly_to_Model(assm1);

		Model_Assembly assm2 = new Model_Assembly();
		assm2.setObjName("OB_Category");
		assm2.setAssembly("mypackage.OB_Category.java");
		model.Add_Assembly_to_Model(assm2);

		model.setName("Category");

		// Set Back in Models
		models.Add_Model_to_Models(model);

		// Create another Model
		Model model1 = new Model();

		// Create two Model Assembly Objects
		Model_Assembly assm3 = new Model_Assembly();
		assm3.setObjName("Txn_Catg");
		assm3.setAssembly("/MFTest/src/OB_Txn_Catg.java");
		model1.Add_Assembly_to_Model(assm3);

		Model_Assembly assm4 = new Model_Assembly();
		assm4.setObjName("Categories");
		assm4.setAssembly("/MFTest/src/OB_Category.java");
		model1.Add_Assembly_to_Model(assm4);

		model1.setName("ALL");

		// Set Back in Models
		models.Add_Model_to_Models(model1);

		// Marshal Here
		JAXBContext jaxbContext = JAXBContext.newInstance(Models.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		// Marshal the Models to file in OS Specif Location as per
		// propertis file
		jaxbMarshaller.marshal(models, new File(this.getModel_Xml_Gen_Loc()));
		// Later copy the same file to your project
		// directory/src/config/models/models.xml
	}

	// Returns All Loaded Models from XM File Path
	@SuppressWarnings("unchecked")
	@Override
	public <T> ArrayList<T> Load_XML_to_Objects() throws Exception
	{
		ArrayList<T> list = new ArrayList<>();
		// Get Objects Back from XML
		try
		{
			JAXBContext jaxbContext = JAXBContext.newInstance(Models.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			// We had written this file in marshalling example
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(this.getModel_Xml_Loc());
			Models models = (Models) jaxbUnmarshaller.unmarshal(is);
			for ( Model model : models.getModels() )
			{
				list.add((T) model);
			}
		}
		catch (Exception Ex)
		{
			throw new Exception(Ex);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> ArrayList<T> Load_XML_to_Objects(String extfile_path) throws EX_ModelLoadException
	{
		ArrayList<T> list = new ArrayList<>();
		// Get Objects Back from XML
		try
		{
			if (extfile_path != null)
			{
				JAXBContext jaxbContext = JAXBContext.newInstance(Models.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

				// We had written this file in marshalling example
				InputStream is = new FileInputStream(new File(extfile_path));
				Models models = (Models) jaxbUnmarshaller.unmarshal(is);
				for ( Model model : models.getModels() )
				{
					list.add((T) model);
				}
			}
		}
		catch (Exception Ex)
		{
			throw new EX_ModelLoadException(this.getModel_Xml_Loc() + " - " + Ex.getMessage());
		}
		return list;
	}

	// Load Model by Model Name
	public Object Load_Model_by_Name(String ModelName) throws EX_ModelLoadException
	{
		Object sel_model = null;

		// Get Objects Back from XML
		try
		{
			JAXBContext jaxbContext = JAXBContext.newInstance(Models.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			InputStream is = this.getClass().getClassLoader().getResourceAsStream(this.getModel_Xml_Loc());

			// We had written this file in marshalling example
			Models models = (Models) jaxbUnmarshaller.unmarshal(is);
			try
			{
				sel_model = models.getModels().stream().filter(x -> x.getName().equals(ModelName)).findFirst().get();
			}
			catch (NoSuchElementException NE)
			{
				// Do Nothing
			}
		}

		catch (Exception Ex)
		{
			throw new EX_ModelLoadException(this.getModel_Xml_Loc() + " - " + Ex.getMessage());
		}
		return sel_model;
	}

	/**
	 * Load Model for Business Function
	 * 
	 * @param Modelloc
	 *             - Xml Location of Business Function Bean Model file
	 * @return
	 * @throws EX_ModelLoadException
	 */
	public Object Load_Model_forBussFxn(String ModelLoc) throws EX_ModelLoadException
	{
		Object sel_model = null;

		// Get Objects Back from XML
		try
		{
			JAXBContext jaxbContext = JAXBContext.newInstance(Models.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			InputStream is = this.getClass().getClassLoader().getResourceAsStream(ModelLoc);

			// We had written this file in marshalling example
			Models models = (Models) jaxbUnmarshaller.unmarshal(is);
			try
			{
				sel_model = models.getModels().get(0);
			}
			catch (NoSuchElementException NE)
			{
				// Do Nothing
			}
		}

		catch (Exception Ex)
		{
			throw new EX_ModelLoadException(this.getModel_Xml_Loc() + " - " + Ex.getMessage());
		}
		return sel_model;
	}

	public Object Load_Model_by_Assembly_ObjName(String ObjName) throws EX_ModelLoadException
	{
		Object sel_model = null;

		// Get Objects Back from XML
		try
		{
			JAXBContext jaxbContext = JAXBContext.newInstance(Models.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(this.getModel_Xml_Loc());

			// We had written this file in marshalling example
			Models models = (Models) jaxbUnmarshaller.unmarshal(is);
			try
			{
				for ( Model model : models.getModels() )
				{
					try
					{
						sel_model = model.getAssemblies().stream().filter(x -> x.getObjName().equals(ObjName)).findFirst().get();
					}
					catch (NoSuchElementException NE)
					{
						// Do Nothing
					}
				}

			}
			catch (NoSuchElementException NE)
			{
				// Do Nothing
			}
		}

		catch (Exception Ex)
		{
			throw new EX_ModelLoadException(this.getModel_Xml_Loc() + " - " + Ex.getMessage());
		}
		return sel_model;
	}

	public Object Load_Model_by_Assembly_ClassName(String Qual_ClassName) throws EX_ModelLoadException
	{
		Object sel_model = null;

		// Get Objects Back from XML
		try
		{
			JAXBContext jaxbContext = JAXBContext.newInstance(Models.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(this.getModel_Xml_Loc());
			// We had written this file in marshalling example
			Models models = (Models) jaxbUnmarshaller.unmarshal(is);
			try
			{
				for ( Model model : models.getModels() )
				{
					try
					{
						sel_model = model.getAssemblies().stream().filter(x -> x.getAssembly().equals(Qual_ClassName)).findFirst().get();
					}
					catch (NoSuchElementException NE)
					{
						// Do Nothing
					}
				}

			}
			catch (NoSuchElementException NE)
			{
				// Do Nothing
			}
		}

		catch (Exception Ex)
		{
			throw new EX_ModelLoadException(this.getModel_Xml_Loc() + " - " + Ex.getMessage());
		}
		return sel_model;
	}

}
