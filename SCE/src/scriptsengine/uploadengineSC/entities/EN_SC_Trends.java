package scriptsengine.uploadengineSC.entities;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import modelframework.implementations.DependantObject;

@Component("EN_SC_Trends")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EN_SC_Trends extends DependantObject
{
	private String	SCCode;
	private String	Period;
	private int	SalesGR;
	private int	RecvTR;
	private int	OPMGR;
	private int	PATGR;
	private int	DivGR;
	private int	DebtGR;
	private int	BVGR;
	private double	AvgPE;
	private double	DivPayR;
	private double	ROCEGR;
	private int	FCF_CFO_GR;

	public String getSCCode()
	{
		return SCCode;
	}

	public void setSCCode(String sCCode)
	{
		SCCode = sCCode;
	}

	public String getPeriod()
	{
		return Period;
	}

	public void setPeriod(String period)
	{
		Period = period;
	}

	public int getSalesGR()
	{
		return SalesGR;
	}

	public int getRecvTR()
	{
		return RecvTR;
	}

	public void setRecvTR(int recvTR)
	{
		RecvTR = recvTR;
	}

	public void setSalesGR(int salesGR)
	{
		SalesGR = salesGR;
	}

	public int getOPMGR()
	{
		return OPMGR;
	}

	public void setOPMGR(int oPMGR)
	{
		OPMGR = oPMGR;
	}

	public int getPATGR()
	{
		return PATGR;
	}

	public void setPATGR(int pATGR)
	{
		PATGR = pATGR;
	}

	public int getDivGR()
	{
		return DivGR;
	}

	public void setDivGR(int divGR)
	{
		DivGR = divGR;
	}

	public int getDebtGR()
	{
		return DebtGR;
	}

	public void setDebtGR(int debtGR)
	{
		DebtGR = debtGR;
	}

	public int getBVGR()
	{
		return BVGR;
	}

	public void setBVGR(int bVGR)
	{
		BVGR = bVGR;
	}

	public double getAvgPE()
	{
		return AvgPE;
	}

	public void setAvgPE(double avgPE)
	{
		AvgPE = avgPE;
	}

	public double getDivPayR()
	{
		return DivPayR;
	}

	public void setDivPayR(double divPayR)
	{
		DivPayR = divPayR;
	}

	public double getROCEGR()
	{
		return ROCEGR;
	}

	public void setROCEGR(double rOCEGR)
	{
		ROCEGR = rOCEGR;
	}

	public int getFCF_CFO_GR()
	{
		return FCF_CFO_GR;
	}

	public void setFCF_CFO_GR(int fCF_CFO_GR)
	{
		FCF_CFO_GR = fCF_CFO_GR;
	}

	public EN_SC_Trends()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public EN_SC_Trends(String sCCode, String period, int salesGR, int recvTR, int oPMGR, int pATGR, int divGR, int debtGR, int bVGR, double avgPE,
	          double divPayR, double rOCEGR, int fCF_CFO_GR)
	{
		super();
		SCCode = sCCode;
		Period = period;
		SalesGR = salesGR;
		RecvTR = recvTR;
		OPMGR = oPMGR;
		PATGR = pATGR;
		DivGR = divGR;
		DebtGR = debtGR;
		BVGR = bVGR;
		AvgPE = avgPE;
		DivPayR = divPayR;
		ROCEGR = rOCEGR;
		FCF_CFO_GR = fCF_CFO_GR;
	}

}
