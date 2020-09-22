/**
 * 
 */
package modelframework.exceptions;

import modelframework.interfaces.IPropertyAwareMessage;

/**
 * 
 * Exception thrown when the Object in question is not Root Object but expected to be
 */
public class EX_NotRootException extends Exception implements IPropertyAwareMessage
	{
		private String		Property_ID;
		
		private Object[]	Args;
		
		@Override
		public void setProperty_ID(String propertyID)
			{
				this.Property_ID = propertyID;
			}
			
		@Override
		public String getProperty_ID()
			{
				// TODO Auto-generated method stub
				return Property_ID;
			}
			
		@Override
		public Object[] getArguments()
			{
				// TODO Auto-generated method stub
				return Args;
			}
			
		@Override
		public boolean IS_Exception()
			{
				// TODO Auto-generated method stub
				return true;
			}
			
		public EX_NotRootException(Object[] args)
			{
				this.Args = args;
				this.setProperty_ID("ERR_NOROOT");
				
			}
	}
