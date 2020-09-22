package modelframework.interfaces;
/**
 * 
 */

/**
 * Enumable Interface: Defines method to get Int Value Back from Interface which
 * could be used for Insertion/Update Operations for an Enum Value
 *
 */
public interface IEnumable
	{
		// Get Int Value from Enum Object
		public int Get_Value_From_Enums(Object Enum);
		
		// Get the Value of Enum as Object back from It's Int Value
		public Object valueOf(int intValue);
	}
