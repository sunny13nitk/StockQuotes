package modelframework.usermanager.exceptions;

import modelframework.interfaces.IPropertyAwareMessage;

@SuppressWarnings("serial")

public class EX_DuplicateUserException extends Exception implements IPropertyAwareMessage
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
			
		public EX_DuplicateUserException(String user)
			{
				this.setProperty_ID("DUPLICATE_USER_CREATION");
				this.Args = new Object[] { user };
			}
			
		@Override
		public boolean IS_Exception()
			{
				
				return true;
			}
			
	}
