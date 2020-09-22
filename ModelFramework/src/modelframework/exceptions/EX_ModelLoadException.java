package modelframework.exceptions;

import modelframework.interfaces.IPropertyAwareMessage;

public class EX_ModelLoadException extends Exception implements IPropertyAwareMessage
	{
		private String		Property_ID;
		
		private Object[]	Args;
		
		@Override
		public void setProperty_ID(String propertyID)
			{
				// TODO Auto-generated method stub
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
			
		public EX_ModelLoadException(String InnerEx)
			{
				this.setProperty_ID("ERR_LOADING_MODELS");
				this.Args = new Object[] { InnerEx };
			}
			
	}
