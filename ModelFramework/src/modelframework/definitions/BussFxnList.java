package modelframework.definitions;

import java.util.ArrayList;

/**
 * List of Business Functions that need to be activated for implementation
 * Bean will hold individual business functions that need to be activated
 */
public class BussFxnList
{

	private ArrayList<BussFxn> bussFxns;

	/**
	 * @param bussFxns
	 */
	public BussFxnList(ArrayList<BussFxn> bussFxns)
	{
		super();
		this.bussFxns = bussFxns;
	}

	/**
	 * 
	 */
	public BussFxnList()
	{
		this.bussFxns = new ArrayList<BussFxn>();
	}

	/**
	 * @return the bussFxns
	 */
	public ArrayList<BussFxn> getBussFxns()
	{
		return bussFxns;
	}

	/**
	 * @param bussFxns
	 *             the bussFxns to set
	 */
	public void setBussFxns(ArrayList<BussFxn> bussFxns)
	{
		this.bussFxns = bussFxns;
	}

}
