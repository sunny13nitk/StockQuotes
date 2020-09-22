package root.busslogic.SQLProc.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Rep_HoldingsTR implements Serializable
{
	@Id
	@GeneratedValue
	private int hid;
	
	private int pid;
	
	private String scCode;
	
	private int numUnits;
	
	private double avgPPU;
	
	private String totalInvestment;
	
	private String totalDiv;
	
	private double adjPPU;
	
	private double perPF;
	
	private double divY;
	
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
	
	public String getTotalInvestment(
	)
	{
		return totalInvestment;
	}
	
	public void setTotalInvestment(
	        String totalInvestment
	)
	{
		this.totalInvestment = totalInvestment;
	}
	
	public String getTotalDiv(
	)
	{
		return totalDiv;
	}
	
	public void setTotalDiv(
	        String totalDiv
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
	
	public Rep_HoldingsTR(
	)
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
}
