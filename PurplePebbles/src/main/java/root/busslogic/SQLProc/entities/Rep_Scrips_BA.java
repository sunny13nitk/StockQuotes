package root.busslogic.SQLProc.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Rep_Scrips_BA
{
	@Id
	private String SCCode;
	private String Sector;
	private double CurrPE;
	private double UPH;
	private double PEG;
	private double MCapToSales;
	private int    SalesGR;
	private int    PATGR;
	private int    PEGR;
	private double DivYield;
	private double totalReturn;
	private double ROCEAvg;
	private double ROEAvg;
	private int    FCF_CFO_Avg;
	private int    OPM_Avg;
	private int    SSGRAvg;
	private int    INT_DEP_BY_SALES_Avg;
	private double WCCycle_Avg;
	private int    DivGR;
	private double DERatio;
	private double CPS;
	private double TTMSales;
	
	public String getSCCode(
	)
	{
		return SCCode;
	}
	
	public void setSCCode(
	        String sCCode
	)
	{
		SCCode = sCCode;
	}
	
	public double getTTMSales(
	)
	{
		return TTMSales;
	}
	
	public void setTTMSales(
	        double tTMSales
	)
	{
		TTMSales = tTMSales;
	}
	
	public int getOPM_Avg(
	)
	{
		return OPM_Avg;
	}
	
	public void setOPM_Avg(
	        int oPM_Avg
	)
	{
		OPM_Avg = oPM_Avg;
	}
	
	public int getSSGRAvg(
	)
	{
		return SSGRAvg;
	}
	
	public void setSSGRAvg(
	        int sSGRAvg
	)
	{
		SSGRAvg = sSGRAvg;
	}
	
	public int getINT_DEP_BY_SALES_Avg(
	)
	{
		return INT_DEP_BY_SALES_Avg;
	}
	
	public void setINT_DEP_BY_SALES_Avg(
	        int iNT_DEP_BY_SALES_Avg
	)
	{
		INT_DEP_BY_SALES_Avg = iNT_DEP_BY_SALES_Avg;
	}
	
	public double getWCCycle_Avg(
	)
	{
		return WCCycle_Avg;
	}
	
	public void setWCCycle_Avg(
	        double wCCycle_Avg
	)
	{
		WCCycle_Avg = wCCycle_Avg;
	}
	
	public int getDivGR(
	)
	{
		return DivGR;
	}
	
	public void setDivGR(
	        int divGR
	)
	{
		DivGR = divGR;
	}
	
	public double getDERatio(
	)
	{
		return DERatio;
	}
	
	public void setDERatio(
	        double dERatio
	)
	{
		DERatio = dERatio;
	}
	
	public String getSector(
	)
	{
		return Sector;
	}
	
	public void setSector(
	        String sector
	)
	{
		Sector = sector;
	}
	
	public double getCurrPE(
	)
	{
		return CurrPE;
	}
	
	public void setCurrPE(
	        double currPE
	)
	{
		CurrPE = currPE;
	}
	
	public double getUPH(
	)
	{
		return UPH;
	}
	
	public void setUPH(
	        double uPH
	)
	{
		UPH = uPH;
	}
	
	public double getPEG(
	)
	{
		return PEG;
	}
	
	public void setPEG(
	        double pEG
	)
	{
		PEG = pEG;
	}
	
	public double getMCapToSales(
	)
	{
		return MCapToSales;
	}
	
	public void setMCapToSales(
	        double mCapToSales
	)
	{
		MCapToSales = mCapToSales;
	}
	
	public int getSalesGR(
	)
	{
		return SalesGR;
	}
	
	public void setSalesGR(
	        int salesGR
	)
	{
		SalesGR = salesGR;
	}
	
	public int getPATGR(
	)
	{
		return PATGR;
	}
	
	public void setPATGR(
	        int pATGR
	)
	{
		PATGR = pATGR;
	}
	
	public int getPEGR(
	)
	{
		return PEGR;
	}
	
	public void setPEGR(
	        int pEGR
	)
	{
		PEGR = pEGR;
	}
	
	public double getDivYield(
	)
	{
		return DivYield;
	}
	
	public void setDivYield(
	        double divYield
	)
	{
		DivYield = divYield;
	}
	
	public double getTotalReturn(
	)
	{
		return totalReturn;
	}
	
	public void setTotalReturn(
	        double totalReturn
	)
	{
		this.totalReturn = totalReturn;
	}
	
	public double getROCEAvg(
	)
	{
		return ROCEAvg;
	}
	
	public void setROCEAvg(
	        double rOCEAvg
	)
	{
		ROCEAvg = rOCEAvg;
	}
	
	public double getROEAvg(
	)
	{
		return ROEAvg;
	}
	
	public void setROEAvg(
	        double rOEAvg
	)
	{
		ROEAvg = rOEAvg;
	}
	
	public int getFCF_CFO_Avg(
	)
	{
		return FCF_CFO_Avg;
	}
	
	public void setFCF_CFO_Avg(
	        int fCF_CFO_Avg
	)
	{
		FCF_CFO_Avg = fCF_CFO_Avg;
	}
	
	public double getCPS(
	)
	{
		return CPS;
	}
	
	public void setCPS(
	        double cPS
	)
	{
		CPS = cPS;
	}
	
	public Rep_Scrips_BA(
	)
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
}
