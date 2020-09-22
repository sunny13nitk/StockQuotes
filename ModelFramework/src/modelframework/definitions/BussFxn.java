package modelframework.definitions;

/**
 * Business Function Class - Holds Business Function Name and Corresponding Bean
 * The business function definition bean though will be instantiated in the XML of the calling application
 * The real business fxn bean will be instantiated after the successful login and would be defined in library of the
 * business function
 * - Will follow an Interface for Business Function
 * - Instantiated via a Manager defined in Model Framework
 */

public class BussFxn
{
	private String	bussFxnName;

	private String	beanName;

	/**
	 * @param bussFxnName
	 * @param beanName
	 */
	public BussFxn(String bussFxnName, String beanName)
	{
		super();
		this.bussFxnName = bussFxnName;
		this.beanName = beanName;
	}

	/**
	 * 
	 */
	public BussFxn()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the bussFxnName
	 */
	public String getBussFxnName()
	{
		return bussFxnName;
	}

	/**
	 * @param bussFxnName
	 *             the bussFxnName to set
	 */
	public void setBussFxnName(String bussFxnName)
	{
		this.bussFxnName = bussFxnName;
	}

	/**
	 * @return the beanName
	 */
	public String getBeanName()
	{
		return beanName;
	}

	/**
	 * @param beanName
	 *             the beanName to set
	 */
	public void setBeanName(String beanName)
	{
		this.beanName = beanName;
	}

}
