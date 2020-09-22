package modelframework.JAXB.definitions.models;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Models")
@XmlAccessorType(XmlAccessType.FIELD)
public class Models
	{
		
		private ArrayList<Model> Model;
		
		public ArrayList<Model> getModels()
			{
				return Model;
			}
			
		public void setModels(ArrayList<Model> models)
			{
				Model = models;
			}
			
		public Models()
			{
				this.Model = new ArrayList<Model>();
			}
			
		public void Add_Model_to_Models(Model model)
			{
				this.getModels().add(model);
			}
	}
