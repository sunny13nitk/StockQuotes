package scriptsengine.uploadengineSC.appUtilities.defn;

import java.util.ArrayList;

public class HEGPOJO
{
	private String					ScCode;
	private double					expected_Return	= 20;
	private double					EPS_CAGR;
	private double					DivP_Avg;
	private double					avgPE5Yr;

	private ArrayList<YearEpsDps_PoJo>	forecastItems;

	private double					sumDiv;
	private double					projEPS;
	private double					projPrice;
	private double					totalGain;
	private double					CMP;
	private double					CMP_CAGR;
	private double					expRet_BuyPrice;

	public String getScCode()
	{
		return ScCode;
	}

	public void setScCode(String scCode)
	{
		ScCode = scCode;
	}

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

	public double getAvgPE5Yr()
	{
		return avgPE5Yr;
	}

	public void setAvgPE5Yr(double avgPE5Yr)
	{
		this.avgPE5Yr = avgPE5Yr;
	}

	public ArrayList<YearEpsDps_PoJo> getForecastItems()
	{
		return forecastItems;
	}

	public void setForecastItems(ArrayList<YearEpsDps_PoJo> forecastItems)
	{
		this.forecastItems = forecastItems;
	}

	public double getSumDiv()
	{
		return sumDiv;
	}

	public void setSumDiv(double sumDiv)
	{
		this.sumDiv = sumDiv;
	}

	public double getProjEPS()
	{
		return projEPS;
	}

	public void setProjEPS(double projEPS)
	{
		this.projEPS = projEPS;
	}

	public double getProjPrice()
	{
		return projPrice;
	}

	public void setProjPrice(double projPrice)
	{
		this.projPrice = projPrice;
	}

	public double getTotalGain()
	{
		return totalGain;
	}

	public void setTotalGain(double totalGain)
	{
		this.totalGain = totalGain;
	}

	public double getCMP()
	{
		return CMP;
	}

	public void setCMP(double cMP)
	{
		CMP = cMP;
	}

	public double getCMP_CAGR()
	{
		return CMP_CAGR;
	}

	public void setCMP_CAGR(double cMP_CAGR)
	{
		CMP_CAGR = cMP_CAGR;
	}

	public double getExpRet_BuyPrice()
	{
		return expRet_BuyPrice;
	}

	public void setExpRet_BuyPrice(double expRet_BuyPrice)
	{
		this.expRet_BuyPrice = expRet_BuyPrice;
	}

	public double getExpected_Return()
	{
		return expected_Return;
	}

	public HEGPOJO()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public HEGPOJO(String scCode, double expected_Return, double ePS_CAGR, double divP_CAGR, double avgPE5Yr,
	          ArrayList<YearEpsDps_PoJo> forecastItems, double sumDiv, double projEPS, double projPrice, double totalGain, double cMP,
	          double cMP_CAGR, double expRet_BuyPrice)
	{
		super();
		ScCode = scCode;
		this.expected_Return = expected_Return;
		EPS_CAGR = ePS_CAGR;
		DivP_Avg = divP_CAGR;
		this.avgPE5Yr = avgPE5Yr;
		this.forecastItems = forecastItems;
		this.sumDiv = sumDiv;
		this.projEPS = projEPS;
		this.projPrice = projPrice;
		this.totalGain = totalGain;
		CMP = cMP;
		CMP_CAGR = cMP_CAGR;
		this.expRet_BuyPrice = expRet_BuyPrice;
	}

}
