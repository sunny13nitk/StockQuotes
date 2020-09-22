package modelframework.types;

import java.util.ArrayList;

/**
 * 
 * Filter Class to hold Where Condition & List of Name value pairs for filter parameters
 *
 */
public class TY_Filter
{
	private String					whereCondn;

	private ArrayList<TY_NameValue>	filterParams;

	/**
	 * @return the whereCondn
	 */
	public String getWhereCondn()
	{
		return whereCondn;
	}

	/**
	 * @param whereCondn
	 *             the whereCondn to set
	 */
	public void setWhereCondn(String whereCondn)
	{
		this.whereCondn = whereCondn;
	}

	/**
	 * @return the filterParams
	 */
	public ArrayList<TY_NameValue> getFilterParams()
	{
		return filterParams;
	}

	/**
	 * @param filterParams
	 *             the filterParams to set
	 */
	public void setFilterParams(ArrayList<TY_NameValue> filterParams)
	{
		this.filterParams = filterParams;
	}

	/**
	 * @param whereCondn
	 * @param filterParams
	 */
	public TY_Filter(String whereCondn, ArrayList<TY_NameValue> filterParams)
	{
		super();
		this.whereCondn = whereCondn;
		this.filterParams = filterParams;
	}

	/**
	 * 
	 */
	public TY_Filter()
	{
		super();

	}

}
