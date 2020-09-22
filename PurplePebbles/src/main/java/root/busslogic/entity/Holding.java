package root.busslogic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(
        name = "dbo.Holdings"
)
public class Holding
{
	@Id
	@GeneratedValue(
	        strategy = GenerationType.IDENTITY
	)
	private int hid;
	
	private int pid;
	
	private String scCode;
	
	private int numUnits;
	
	private double avgPPU;
	
	private double totalInvestment;
	
	private double totalDiv;
	
	private double adjPPU;
	
	@Column(
	        name = "PLBalance"
	)
	private double plBalance;
	
	public double getPlBalance(
	)
	{
		return plBalance;
	}
	
	public void setPlBalance(
	        double plBalance
	)
	{
		this.plBalance = plBalance;
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
	
	public Holding(
	        int pid, String scCode, int numUnits, double avgPPU, double totalInvestment, double totalDiv, double adjPPU
	)
	{
		super();
		this.pid             = pid;
		this.scCode          = scCode;
		this.numUnits        = numUnits;
		this.avgPPU          = avgPPU;
		this.totalInvestment = totalInvestment;
		this.totalDiv        = totalDiv;
		this.adjPPU          = adjPPU;
	}
	
	public Holding(
	)
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
}
