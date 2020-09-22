package modelframework.JAXB.definitions.models;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Model")
@XmlAccessorType(XmlAccessType.FIELD)
public class Model
	{
		
		private String						Name;
		
		private ArrayList<Model_Assembly>	Assemblies;
		
		public String getName()
			{
				return Name;
			}
			
		public void setName(String name)
			{
				Name = name;
			}
			
		public ArrayList<Model_Assembly> getAssemblies()
			{
				return Assemblies;
			}
			
		public void setAssemblies(ArrayList<Model_Assembly> assemblies)
			{
				Assemblies = assemblies;
			}
			
		public Model(String name, ArrayList<Model_Assembly> assemblies)
			{
				
				Name = name;
				Assemblies = assemblies;
			}
			
		public Model()
			{
				this.setAssemblies(new ArrayList<Model_Assembly>());
			}
			
		public void Add_Assembly_to_Model(Model_Assembly assm)
			{
				this.getAssemblies().add(assm);
			}
			
	}
