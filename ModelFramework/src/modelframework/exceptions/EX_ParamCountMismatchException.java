/**
 * 
 */
package modelframework.exceptions;

import modelframework.interfaces.IPropertyAwareMessage;

/**
 * 
 * ERR_PARAMCOUNT= Cannot Execute Query. Parameters Count Mismatch. Actual paraemters provided are {0} while
 * {1} are defined!
 */
@SuppressWarnings("serial")
public class EX_ParamCountMismatchException extends Exception implements IPropertyAwareMessage
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
			
		public EX_ParamCountMismatchException(Object[] args)
			{
				this.Args = args;
				this.setProperty_ID("ERR_PARAMCOUNT");
				
			}
			
	}
