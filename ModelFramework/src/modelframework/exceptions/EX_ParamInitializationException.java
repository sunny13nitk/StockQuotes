/**
 * 
 */
package modelframework.exceptions;

import modelframework.interfaces.IPropertyAwareMessage;

/**
 * ERR_PARAMBLANK= Parameters initilization error!! Parameters at Index {0} for Query for Object{1} has
 * null/blank Name/Value Pair!
 *
 */
@SuppressWarnings("serial")
public class EX_ParamInitializationException extends Exception implements IPropertyAwareMessage
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
			
		public EX_ParamInitializationException(Object[] args)
			{
				this.Args = args;
				this.setProperty_ID("ERR_PARAMBLANK");
				
			}
			
	}
