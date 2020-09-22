package scriptsengine.bussfxn;

import modelframework.interfaces.IBussFxn;

public class SCEBean implements IBussFxn
{
	private String	SchemaLoc;

	private String	ModelLoc;

	/**
	 * @param schemaLoc
	 * @param modelLoc
	 */
	public SCEBean(String schemaLoc, String modelLoc)
	{
		super();
		SchemaLoc = schemaLoc;
		ModelLoc = modelLoc;
	}

	/**
	 * 
	 */
	public SCEBean()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param schemaLoc
	 *             the schemaLoc to set
	 */
	public void setSchemaLoc(String schemaLoc)
	{
		SchemaLoc = schemaLoc;
	}

	/**
	 * @param modelLoc
	 *             the modelLoc to set
	 */
	public void setModelLoc(String modelLoc)
	{
		ModelLoc = modelLoc;
	}

	@Override
	public String getSchemaLoc()
	{
		// TODO Auto-generated method stub
		return SchemaLoc;
	}

	@Override
	public String getModelLoc()
	{
		// TODO Auto-generated method stub
		return ModelLoc;
	}

	@Override
	/**
	 * Will be dealt with an Advice in an Aspect in Model Framework
	 */
	public boolean activateBussFxn()
	{

		return false;
	}

}
