package scriptsengine.uploadengineSC.appUtilities.defn;

public class AvgPEPricesPojo
{
	private String	ScCode;
	private double	EPS;
	private double	CMP;
	private double	PECurr;
	private double	PE3Yr;
	private double	Price3Yr;
	private double	PrDelta3Yr;
	private double	PE5Yr;
	private double	Price5Yr;
	private double	PrDelta5Yr;

	public String getScCode()
	{
		return ScCode;
	}

	public void setScCode(String scCode)
	{
		ScCode = scCode;
	}

	public double getEPS()
	{
		return EPS;
	}

	public void setEPS(double ePS)
	{
		EPS = ePS;
	}

	public double getCMP()
	{
		return CMP;
	}

	public void setCMP(double cMP)
	{
		CMP = cMP;
	}

	public double getPECurr()
	{
		return PECurr;
	}

	public void setPECurr(double pECurr)
	{
		PECurr = pECurr;
	}

	public double getPE3Yr()
	{
		return PE3Yr;
	}

	public void setPE3Yr(double pE3Yr)
	{
		PE3Yr = pE3Yr;
	}

	public double getPrice3Yr()
	{
		return Price3Yr;
	}

	public void setPrice3Yr(double price3Yr)
	{
		Price3Yr = price3Yr;
	}

	public double getPrDelta3Yr()
	{
		return PrDelta3Yr;
	}

	public void setPrDelta3Yr(double prDelta3Yr)
	{
		PrDelta3Yr = prDelta3Yr;
	}

	public double getPE5Yr()
	{
		return PE5Yr;
	}

	public void setPE5Yr(double pE5Yr)
	{
		PE5Yr = pE5Yr;
	}

	public double getPrice5Yr()
	{
		return Price5Yr;
	}

	public void setPrice5Yr(double price5Yr)
	{
		Price5Yr = price5Yr;
	}

	public double getPrDelta5Yr()
	{
		return PrDelta5Yr;
	}

	public void setPrDelta5Yr(double prDelta5Yr)
	{
		PrDelta5Yr = prDelta5Yr;
	}

	public AvgPEPricesPojo()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public AvgPEPricesPojo(String scCode, double ePS, double cMP, double pECurr, double pE3Yr, double price3Yr, double prDelta3Yr, double pE5Yr,
	          double price5Yr, double prDelta5Yr)
	{
		super();
		ScCode = scCode;
		EPS = ePS;
		CMP = cMP;
		PECurr = pECurr;
		PE3Yr = pE3Yr;
		Price3Yr = price3Yr;
		PrDelta3Yr = prDelta3Yr;
		PE5Yr = pE5Yr;
		Price5Yr = price5Yr;
		PrDelta5Yr = prDelta5Yr;
	}

}
