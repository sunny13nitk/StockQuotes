/**
 * 
 */
package modelframework.usermanager.exceptions;

import modelframework.interfaces.IPropertyAwareMessage;

/**
 * 
 *
 */
@SuppressWarnings("serial")
public class EX_InvalidLogonException extends Exception implements IPropertyAwareMessage
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
				
				return true;
			}
			
		public EX_InvalidLogonException(String User)
			{
				this.setProperty_ID("ERR_INVALID_PW");
				this.Args = new Object[]
					{ User };
			}
	}
