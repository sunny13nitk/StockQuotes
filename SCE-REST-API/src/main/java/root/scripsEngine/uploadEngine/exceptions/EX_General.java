package root.scripsEngine.uploadEngine.exceptions;

import root.scripsEngine.uploadEngine.interfaces.IPropertyAwareMessage;

@SuppressWarnings(
    "serial"
)
public class EX_General extends Exception implements IPropertyAwareMessage
{
	private String Property_ID;
	
	private Object[] Args;
	
	@Override
	public void setProperty_ID(
	        String propertyID
	)
	{
		this.Property_ID = propertyID;
	}
	
	@Override
	public String getProperty_ID(
	)
	{
		// TODO Auto-generated method stub
		return Property_ID;
	}
	
	@Override
	public Object[] getArguments(
	)
	{
		// TODO Auto-generated method stub
		return Args;
	}
	
	@Override
	public boolean IS_Exception(
	)
	{
		// TODO Auto-generated method stub
		return true;
	}
	
	public EX_General(
	        Object[] args
	)
	{
		this.Args = args;
		this.setProperty_ID("SCRIPMDTERROR_BASEKEY");
	}
	
	public EX_General(
	        String propID, Object[] args
	)
	{
		this.Args = args;
		this.setProperty_ID(propID);
	}
}
