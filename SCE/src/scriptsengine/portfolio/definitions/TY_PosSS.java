package scriptsengine.portfolio.definitions;

/**
 * 
 * Positions Snapshot POJO
 */
public class TY_PosSS
{
	private String	scCode;		// Reference Placeholder
	private int	holdings;		// Current Holdings
	private int	freeShares;	// FRee shares
	private double	ppu;			// Price/ Unit
	private double	investments;	// Investments
	private double	scPFContr;	// Scrip Portfolio Contribution
	private double	secPFContr;	// Current Scrip Sector's Contribution

	public int getFreeShares()
	{
		return freeShares;
	}

	public void setFreeShares(int freeShares)
	{
		this.freeShares = freeShares;
	}

	public String getScCode()
	{
		return scCode;
	}

	public void setScCode(String scCode)
	{
		this.scCode = scCode;
	}

	public int getHoldings()
	{
		return holdings;
	}

	public void setHoldings(int holdings)
	{
		this.holdings = holdings;
	}

	public double getPpu()
	{
		return ppu;
	}

	public void setPpu(double ppu)
	{
		this.ppu = ppu;
	}

	public double getInvestments()
	{
		return investments;
	}

	public void setInvestments(double investments)
	{
		this.investments = investments;
	}

	public double getScPFContr()
	{
		return scPFContr;
	}

	public void setScPFContr(double scPFContr)
	{
		this.scPFContr = scPFContr;
	}

	public double getSecPFContr()
	{
		return secPFContr;
	}

	public void setSecPFContr(double secPFContr)
	{
		this.secPFContr = secPFContr;
	}

	public TY_PosSS(String scCode, int holdings, int freeShares, double ppu, double investments, double scPFContr, double secPFContr)
	{
		super();
		this.scCode = scCode;
		this.holdings = holdings;
		this.freeShares = freeShares;
		this.ppu = ppu;
		this.investments = investments;
		this.scPFContr = scPFContr;
		this.secPFContr = secPFContr;
	}

	public TY_PosSS()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
