package modelframework.JAXB.definitions.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

//Model Assembly Class : Can have 1:N cardinality with a Model
@XmlRootElement(name = "Assemblies")
@XmlAccessorType(XmlAccessType.FIELD)
public class Model_Assembly
	{
		
		private String	ObjName;
		
		private String	Assembly;
		
		public String getObjName()
			{
				return ObjName;
			}
			
		public void setObjName(String objName)
			{
				ObjName = objName;
			}
			
		public String getAssembly()
			{
				return Assembly;
			}
			
		public void setAssembly(String assembly)
			{
				Assembly = assembly;
			}
			
	}
