package modelframework.exceptions;

import modelframework.interfaces.IPropertyAwareMessage;

public class EX_SchemaLoadException extends Exception implements IPropertyAwareMessage
	{
		
		private Object[]	Args;
		
		private String		Property_ID;
		
		@Override
		public void setProperty_ID(String propertyID)
			{
				this.Property_ID = propertyID;
				
			}
			
		@Override
		public String getProperty_ID()
			{
				// TODO Auto-generated method stub
				return this.Property_ID;
			}
			
		@Override
		public Object[] getArguments()
			{
				// TODO Auto-generated method stub
				return this.Args;
			}
			
		@Override
		public boolean IS_Exception()
			{
				// TODO Auto-generated method stub
				return true;
			}
			
		public EX_SchemaLoadException(String InnerExceptionmsg)
			{
				this.Args = new Object[] { InnerExceptionmsg };
				this.setProperty_ID("ERR_LOADING_OBJSCHEMAS");
				
			}
			
	}
