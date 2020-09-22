package modelframework.exceptions;

import modelframework.interfaces.IPropertyAwareMessage;

public class EX_CHANGEMODE_KEYVIOLATION extends Exception implements IPropertyAwareMessage
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

	public EX_CHANGEMODE_KEYVIOLATION(Object[] args)
	{
		this.Args = args;
		this.setProperty_ID("ERR_CHANGEMODE_KEYVIOLATION");

	}

}
