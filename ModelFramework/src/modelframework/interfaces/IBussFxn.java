package modelframework.interfaces;

/**
 * 
 * Interface for Business Function
 * Implemented as a bean in Business Function library beans.xml
 * - Getters will correspond to fields while activation will be dealt with an advice in aspect
 */
public interface IBussFxn
{
	public String getSchemaLoc();

	public String getModelLoc();

	public boolean activateBussFxn();

}
