package modelframework.types;

//Name Value Pair Class Type 
//System wide Global Types - Usually public properties since these classes objects are supposed to be used as types
public class TY_NameValue
	{
		public String	Name;
		
		public Object	Value;
		
		public TY_NameValue()
			{
				
			}
			
		public TY_NameValue(String name, Object value)
			{
				Name = name;
				Value = value;
			}
			
	}
