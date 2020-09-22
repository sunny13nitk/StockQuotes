/**
 * 
 */
package modelframework.usermanager.exceptions;

import modelframework.interfaces.IPropertyAwareMessage;

/**
 * 
 *
 */
public class EX_UserCreationException extends Exception implements IPropertyAwareMessage
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
			
		public EX_UserCreationException(String[] Innermsg)
			{
				this.setProperty_ID("ERR_USER_CREATE");
				this.Args = Innermsg;
			}
	}
