package modelframework.JAXB.definitions.objschemas;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ObjectSchemas")
@XmlAccessorType(XmlAccessType.FIELD)
public class ObjectSchemas
{
	private ArrayList<ObjectSchema> ObjectSchema;

	public ArrayList<ObjectSchema> getObjectSchemas()
	{
		return ObjectSchema;
	}

	public void setObjectSchemas(ArrayList<ObjectSchema> objectSchema)
	{
		ObjectSchema = objectSchema;
	}

	public ObjectSchemas(ArrayList<ObjectSchema> objectSchema)
	{

		this.ObjectSchema = objectSchema;
	}

	public ObjectSchemas()
	{
		this.ObjectSchema = new ArrayList<ObjectSchema>();
	}

	/*************************************************************************************************
	 * Get Root Object Metadata by Root Object Name
	 * 
	 * @param ObjName
	 *             - String Name of Object
	 * @return - Root Object Metadata Structure
	 *********************************************************************************************/
	public Root_Object_Defn get_Root_Metadata_byObjName(String ObjName)
	{
		Root_Object_Defn Root_Metadata = null;

		if (ObjName != null)
		{
			// Some Schemas Loaded
			if (this.ObjectSchema != null)
			{
				if (this.ObjectSchema.size() > 0)
				{
					try
					{
						Root_Metadata =

						          this.ObjectSchema.stream().filter(x -> x.getRootObjectMetadata().getObjectname().equals(ObjName)).findFirst()
						                    .get().getRootObjectMetadata();
					}
					catch (NoSuchElementException NE)
					{
						// Do Nothing
					}
				}
			}
		}

		return Root_Metadata;
	}

	/*************************************************************************************************
	 * Get Dependant Object Metadata by Dependant Object Name
	 * 
	 * @param ObjName
	 *             - String Name of Object
	 * @return - Dependant Object Metadata Structure
	 *********************************************************************************************/
	public Dependant_Object_Defn get_Dependant_Metadata_byObjName(String ObjName)
	{
		Dependant_Object_Defn Dependant_Metadata = null;

		if (ObjName != null)
		{
			// Some Schemas Loaded
			if (this.ObjectSchema != null)
			{
				if (this.ObjectSchema.size() > 0)
				{
					try
					{
						for ( ObjectSchema schema_obj : ObjectSchema )
						{
							ArrayList<Dependant_Object_Defn> relations = schema_obj.getRelations();
							if (relations != null)
							{
								if (relations.size() > 0)
								{
									for ( Dependant_Object_Defn relation : relations )
									{
										if (relation != null)
										{
											if (relation.getDepobjname().equals(ObjName))
											{
												Dependant_Metadata = relation;
												// Stop once object is found no further looping needed
												return Dependant_Metadata;
											}
										}
									}
								}
							}

						}
					}

					catch (NoSuchElementException NE)
					{
						// Do Nothing
					}
				}
			}
		}

		return Dependant_Metadata;
	}

	/*************************************************************************************************
	 * Get Root Object Schema by Root Object Name
	 * 
	 * @param ObjName
	 *             - String Name of Object
	 * @return - Root Object Schema Hierarichal Structure
	 *********************************************************************************************/
	public ObjectSchema Get_Schema_byRootObjName(String Objname)
	{
		ObjectSchema schema = null;
		if (Objname != null)
		{
			// Some Schemas Loaded
			if (this.ObjectSchema != null)
			{
				if (this.ObjectSchema.size() > 0)
				{
					try
					{
						schema =

						          this.ObjectSchema.stream().filter(x -> x.getRootObjectMetadata().getObjectname().equals(Objname)).findFirst()
						                    .get();
					}
					catch (NoSuchElementException NE)
					{
						// Do Nothing
					}
				}
			}
		}

		return schema;
	}

}
