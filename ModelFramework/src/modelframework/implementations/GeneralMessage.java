/**
 * 
 */
package modelframework.implementations;

import modelframework.interfaces.IPropertyAwareMessage;

/**
 * General Message Class to hold System logging messages at different stages
 *
 */
public class GeneralMessage implements IPropertyAwareMessage
	{
		private Object[]	Args;
		
		private String		Property_ID;
		
		public Object[] getArguments()
			{
				return Args;
			}
			
		public void setProperty_ID(String propertyID)
			{
				this.Property_ID = propertyID;
			}
			
		public String getProperty_ID()
			{
				return Property_ID;
			}
			
		public boolean IS_Exception()
			{
				
				return false;
			}
			
		public GeneralMessage(String propID, Object[] Args)
			{
				this.setProperty_ID(propID);
				this.Args = Args;
			}
			
	}
