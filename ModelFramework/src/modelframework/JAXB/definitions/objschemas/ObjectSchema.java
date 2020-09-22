package modelframework.JAXB.definitions.objschemas;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ObjectSchema")
@XmlAccessorType(XmlAccessType.FIELD)
public class ObjectSchema
	{
		private Root_Object_Defn					RootObjectMetadata;
		
		private ArrayList<Dependant_Object_Defn>	Relations;
		
		public Root_Object_Defn getRootObjectMetadata()
			{
				return RootObjectMetadata;
			}
			
		public void setRootObjectMetadata(Root_Object_Defn rootObjectMetadata)
			{
				RootObjectMetadata = rootObjectMetadata;
			}
			
		public ArrayList<Dependant_Object_Defn> getRelations()
			{
				return Relations;
			}
			
		public void setRelations(ArrayList<Dependant_Object_Defn> relations)
			{
				Relations = relations;
			}
			
		public ObjectSchema(Root_Object_Defn rootObjectMetadata, ArrayList<Dependant_Object_Defn> relations)
			{
				super();
				RootObjectMetadata = rootObjectMetadata;
				Relations = relations;
			}
			
		public ObjectSchema()
			{
				
			}
			
		/*************************************************************************************************
		 * Get Relations details as Dependant Object Definition from Current Schema by Relation Name
		 * 
		 * @param Relationname
		 *            - String Name of Realtion as per config Xml
		 * @return - Dependant Object Definition/Relation Details
		 **************************************************************************************************/
		public Dependant_Object_Defn getRelationDetails(String Relationname)
			{
				Dependant_Object_Defn relDefn = null;
				
				if (Relationname != null)
					{
						try
							{
								return this.getRelations().stream()
								        .filter(x -> x.getRelationname().equals(Relationname)).findFirst().get();
							}
							
						catch (NoSuchElementException NE)
							{
								//Do nothing
							}
					}
					
				return relDefn;
			}
			
	}
