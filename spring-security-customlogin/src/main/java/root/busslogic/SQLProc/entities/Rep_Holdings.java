package root.busslogic.SQLProc.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import root.busslogic.utilities.UtilDecimaltoMoneyString;

@Entity
public class Rep_Holdings implements Serializable
{
	@Id
	@GeneratedValue
	private int hid;
	
	private int pid;
	
	private String scCode;
	
	private int numUnits;
	
	private double avgPPU;
	
	private double totalInvestment;
	
	private double totalDiv;
	
	private double adjPPU;
	
	private double perPF;
	
	private double divY;
	
	public double getDivY(
	)
	{
		return divY;
	}
	
	public void setDivY(
	        double divY
	)
	{
		this.divY = divY;
	}
	
	public int getHid(
	)
	{
		return hid;
	}
	
	public void setHid(
	        int hid
	)
	{
		this.hid = hid;
	}
	
	public int getPid(
	)
	{
		return pid;
	}
	
	public void setPid(
	        int pid
	)
	{
		this.pid = pid;
	}
	
	public String getScCode(
	)
	{
		return scCode;
	}
	
	public void setScCode(
	        String scCode
	)
	{
		this.scCode = scCode;
	}
	
	public int getNumUnits(
	)
	{
		return numUnits;
	}
	
	public void setNumUnits(
	        int numUnits
	)
	{
		this.numUnits = numUnits;
	}
	
	public double getAvgPPU(
	)
	{
		return avgPPU;
	}
	
	public void setAvgPPU(
	        double avgPPU
	)
	{
		this.avgPPU = avgPPU;
	}
	
	public double getTotalInvestment(
	)
	{
		return totalInvestment;
	}
	
	public void setTotalInvestment(
	        double totalInvestment
	)
	{
		this.totalInvestment = totalInvestment;
	}
	
	public double getTotalDiv(
	)
	{
		return totalDiv;
	}
	
	public void setTotalDiv(
	        double totalDiv
	)
	{
		this.totalDiv = totalDiv;
	}
	
	public double getAdjPPU(
	)
	{
		return adjPPU;
	}
	
	public void setAdjPPU(
	        double adjPPU
	)
	{
		this.adjPPU = adjPPU;
	}
	
	public double getPerPF(
	)
	{
		return perPF;
	}
	
	public void setPerPF(
	        double perPF
	)
	{
		this.perPF = perPF;
	}
	
	public Rep_Holdings(
	)
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Rep_HoldingsTR transformToMoneyFormat(
	)
	{
		Rep_HoldingsTR holdingTR = new Rep_HoldingsTR();
		holdingTR.setAdjPPU(adjPPU);
		holdingTR.setAvgPPU(avgPPU);
		holdingTR.setDivY(divY);
		holdingTR.setHid(hid);
		holdingTR.setNumUnits(numUnits);
		holdingTR.setPerPF(perPF);
		holdingTR.setPid(pid);
		holdingTR.setScCode(scCode);
		holdingTR.setTotalDiv(UtilDecimaltoMoneyString.getMoneyStringforDecimal(totalDiv, 1));
		holdingTR.setTotalInvestment(UtilDecimaltoMoneyString.getMoneyStringforDecimal(totalInvestment, 2));
		
		return holdingTR;
	}
	
}
