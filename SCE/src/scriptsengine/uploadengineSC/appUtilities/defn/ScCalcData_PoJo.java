package scriptsengine.uploadengineSC.appUtilities.defn;

public class ScCalcData_PoJo
{
	private double	EPS_CAGR;
	private double	DivP_Avg;
	private double	numShares;
	private double	DPS_Curr;
	private double	avgROE;
	private double	avgROCE;

	public double getEPS_CAGR()
	{
		return EPS_CAGR;
	}

	public void setEPS_CAGR(double ePS_CAGR)
	{
		EPS_CAGR = ePS_CAGR;
	}

	public double getDivP_Avg()
	{
		return DivP_Avg;
	}

	public void setDivP_Avg(double divP_CAGR)
	{
		DivP_Avg = divP_CAGR;
	}

	public double getNumShares()
	{
		return numShares;
	}

	public void setNumShares(double numShares)
	{
		this.numShares = numShares;
	}

	public double getDPS_Curr()
	{
		return DPS_Curr;
	}

	public void setDPS_Curr(double dPS_Curr)
	{
		DPS_Curr = dPS_Curr;
	}

	public double getAvgROE()
	{
		return avgROE;
	}

	public void setAvgROE(double avgROE)
	{
		this.avgROE = avgROE;
	}

	public double getAvgROCE()
	{
		return avgROCE;
	}

	public void setAvgROCE(double avgROCE)
	{
		this.avgROCE = avgROCE;
	}

	public ScCalcData_PoJo()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public ScCalcData_PoJo(double ePS_CAGR, double divP_Avg, double avgPE, double numShares, double dPS_Curr, double avgROE, double avgROCE,
	          double cFO_PAT_Ratio)
	{
		super();
		EPS_CAGR = ePS_CAGR;
		DivP_Avg = divP_Avg;
		this.numShares = numShares;
		DPS_Curr = dPS_Curr;
		this.avgROE = avgROE;
		this.avgROCE = avgROCE;

	}

}
