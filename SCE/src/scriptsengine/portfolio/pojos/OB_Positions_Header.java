package scriptsengine.portfolio.pojos;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import modelframework.annotations.BeforeSaveValidator;
import modelframework.annotations.UserAware;
import modelframework.implementations.RootObject;

/**
 * Scrips Positions Header - POJO - Root Object
 */
@Component("OB_Positions_Header") // Initialize as bean in Context with name as Class Name
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE) // Always create a new bean Instance
@UserAware
public class OB_Positions_Header extends RootObject
{

	private String	ScCode;

	private int	CurrHolding;		// Tradeable Qty

	private double	PPU;

	private double	CurrInvestment;

	private int	FreeHolding;

	private double	AmntRealized;

	private double	DividendEarnings;

	/**
	 * @return the scCode
	 */
	public String getScCode()
	{
		return ScCode;
	}

	/**
	 * @param scCode
	 *             the scCode to set
	 */
	public void setScCode(String scCode)
	{
		ScCode = scCode;
	}

	/**
	 * @return the currHolding
	 */
	public int getCurrHolding()
	{
		return CurrHolding;
	}

	/**
	 * @param currHolding
	 *             the currHolding to set
	 */
	public void setCurrHolding(int currHolding)
	{
		CurrHolding = currHolding;
	}

	/**
	 * @return the pPU
	 */
	public double getPPU()
	{
		return PPU;
	}

	/**
	 * @param pPU
	 *             the pPU to set
	 */
	public void setPPU(double pPU)
	{
		PPU = pPU;
	}

	/**
	 * @return the currInvestment
	 */
	public double getCurrInvestment()
	{
		return CurrInvestment;
	}

	/**
	 * @param currInvestment
	 *             the currInvestment to set
	 */
	public void setCurrInvestment(double currInvestment)
	{
		CurrInvestment = currInvestment;
	}

	/**
	 * @return the freeHolding
	 */
	public int getFreeHolding()
	{
		return FreeHolding;
	}

	/**
	 * @param freeHolding
	 *             the freeHolding to set
	 */
	public void setFreeHolding(int freeHolding)
	{
		FreeHolding = freeHolding;
	}

	/**
	 * @return the amntRealized
	 */
	public double getAmntRealized()
	{
		return AmntRealized;
	}

	/**
	 * @param amntRealized
	 *             the amntRealized to set
	 */
	public void setAmntRealized(double amntRealized)
	{
		AmntRealized = amntRealized;
	}

	/**
	 * @return the dividendEarnings
	 */
	public double getDividendEarnings()
	{
		return DividendEarnings;
	}

	/**
	 * @param dividendEarnings
	 *             the dividendEarnings to set
	 */
	public void setDividendEarnings(double dividendEarnings)
	{
		DividendEarnings = dividendEarnings;
	}

	/**
	 * 
	 */
	public OB_Positions_Header()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param posID
	 * @param scCode
	 * @param currHolding
	 * @param pPU
	 * @param currInvestment
	 * @param freeHolding
	 * @param amntRealized
	 * @param dividendEarnings
	 */
	public OB_Positions_Header(String scCode, int currHolding, double pPU, double currInvestment, int freeHolding, double amntRealized,
	          double dividendEarnings)
	{
		super();

		ScCode = scCode;
		CurrHolding = currHolding;
		PPU = pPU;
		CurrInvestment = currInvestment;
		FreeHolding = freeHolding;
		AmntRealized = amntRealized;
		DividendEarnings = dividendEarnings;
	}

	@BeforeSaveValidator
	public boolean validateBeforeSave()
	{
		boolean error = false;
		if (CurrHolding > 0 && PPU > 0)
		{
			// Holding and PPU exist but Investment < = 0
			if (CurrInvestment <= 0)
			{
				error = true;
			}

			// Investment is less than product of Holding and PPU. It can only be higher or equal considering
			// brokerage(A/C speific) and taxes(if any) - Error Pecentage - 1%
			if (((CurrInvestment - (CurrHolding * PPU)) / CurrInvestment) > .01)
			{
				error = true;
			}
		}

		return error;
	}

}
