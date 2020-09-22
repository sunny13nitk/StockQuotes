/**
 * 
 */
package modelframework.exceptions;

import modelframework.interfaces.IPropertyAwareMessage;

/**
 * Null Parameters Exception
 * ERR_NULLPARAM=No parameters specified in Sql Query for Object {0} while they are needed as per Query
 * definition!
 *
 */
@SuppressWarnings("serial")
public class EX_NullParamsException extends Exception implements IPropertyAwareMessage
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
			
		public EX_NullParamsException(Object[] args)
			{
				this.Args = args;
				this.setProperty_ID("ERR_NULLPARAM");
				
			}
	}
