/**
 * 
 */
package modelframework.exceptions;

import modelframework.interfaces.IPropertyAwareMessage;

/**
 * Primary Key Not Specified Exception
 * ERR_NOPKEY=Primary Key {0} must be set to Save Entity Instance of Type {1}
 */
@SuppressWarnings("serial")
public class EX_PKeynotSpecified extends Exception implements IPropertyAwareMessage
	{
		private String		Property_ID;
		
		private Object[]	Args;
		
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
			
		public EX_PKeynotSpecified(Object[] args)
			{
				this.Args = args;
				this.setProperty_ID("ERR_NOPKEY");
				
			}
	}
