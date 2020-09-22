package scriptsengine.statistics.exceptions;

import modelframework.interfaces.IPropertyAwareMessage;

@SuppressWarnings("serial")
public class EX_StdDeviation extends Exception implements IPropertyAwareMessage
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

	/**
	 * @param property_ID
	 * @param args
	 */
	public EX_StdDeviation(String property_ID, Object[] args)
	{

		Property_ID = property_ID;
		Args = args;
	}

}
