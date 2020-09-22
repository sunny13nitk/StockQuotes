package scriptsengine.pojos;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import modelframework.implementations.DependantObject;

@Component("OB_Scrip_Expenses") // Initialize as bean in Context with name as Class Name
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE) // Always Create a New Instance
public class OB_Scrip_Expenses extends DependantObject
{
	private String	scCode;
	private int	Year;
	private double	CostRawMt;
	private double	CostPwFuel;
	private double	CostEmp;
	private double	CostMisc;

	/**
	 * @return the scCode
	 */
	public String getscCode()
	{
		return scCode;
	}

	/**
	 * @param scCode
	 *             the scCode to set
	 */
	public void setscCode(String scCode)
	{
		this.scCode = scCode;
	}

	/**
	 * @return the year
	 */
	public int getYear()
	{
		return Year;
	}

	/**
	 * @param year
	 *             the year to set
	 */
	public void setYear(int year)
	{
		this.Year = year;
	}

	/**
	 * @return the costRawMt
	 */
	public double getCostRawMt()
	{
		return CostRawMt;
	}

	/**
	 * @param costRawMt
	 *             the costRawMt to set
	 */
	public void setCostRawMt(double costRawMt)
	{
		CostRawMt = costRawMt;
	}

	/**
	 * @return the costPwFuel
	 */
	public double getCostPwFuel()
	{
		return CostPwFuel;
	}

	/**
	 * @param costPwFuel
	 *             the costPwFuel to set
	 */
	public void setCostPwFuel(double costPwFuel)
	{
		CostPwFuel = costPwFuel;
	}

	/**
	 * @return the costEmp
	 */
	public double getCostEmp()
	{
		return CostEmp;
	}

	/**
	 * @param costEmp
	 *             the costEmp to set
	 */
	public void setCostEmp(double costEmp)
	{
		CostEmp = costEmp;
	}

	/**
	 * @return the costMisc
	 */
	public double getCostMisc()
	{
		return CostMisc;
	}

	/**
	 * @param costMisc
	 *             the costMisc to set
	 */
	public void setCostMisc(double costMisc)
	{
		CostMisc = costMisc;
	}

	/**
	 * 
	 */
	public OB_Scrip_Expenses()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param scCode
	 * @param year
	 * @param costRawMt
	 * @param costPwFuel
	 * @param costEmp
	 * @param costMisc
	 */
	public OB_Scrip_Expenses(String scCode, int year, double costRawMt, double costPwFuel, double costEmp, double costMisc)
	{
		super();
		this.scCode = scCode;
		this.Year = year;
		CostRawMt = costRawMt;
		CostPwFuel = costPwFuel;
		CostEmp = costEmp;
		CostMisc = costMisc;
	}

}
