package root.busslogic.SQLProc.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import root.busslogic.utilities.UtilDecimaltoMoneyString;

@Entity
public class Rep_FL
{
	@Id
	@GeneratedValue
	private int flid;
	
	private String name;
	
	private String description;
	
	private double balance;
	
	private int numPf;
	
	private double totalDeposits;
	
	private double totalCredits;
	
	private double flD2C;
	
	private double flUtilization;
	
	public int getFlid(
	)
	{
		return flid;
	}
	
	public void setFlid(
	        int flid
	)
	{
		this.flid = flid;
	}
	
	public String getName(
	)
	{
		return name;
	}
	
	public void setName(
	        String name
	)
	{
		this.name = name;
	}
	
	public String getDescription(
	)
	{
		return description;
	}
	
	public void setDescription(
	        String description
	)
	{
		this.description = description;
	}
	
	public double getBalance(
	)
	{
		return balance;
	}
	
	public void setBalance(
	        double balance
	)
	{
		this.balance = balance;
	}
	
	public int getNumPf(
	)
	{
		return numPf;
	}
	
	public void setNumPf(
	        int numPf
	)
	{
		this.numPf = numPf;
	}
	
	public double getTotalDeposits(
	)
	{
		return totalDeposits;
	}
	
	public void setTotalDeposits(
	        double totalDeposits
	)
	{
		this.totalDeposits = totalDeposits;
	}
	
	public double getTotalCredits(
	)
	{
		return totalCredits;
	}
	
	public void setTotalCredits(
	        double totalCredits
	)
	{
		this.totalCredits = totalCredits;
	}
	
	public double getFlD2C(
	)
	{
		return flD2C;
	}
	
	public void setFlD2C(
	        double flD2C
	)
	{
		this.flD2C = flD2C;
	}
	
	public double getFlUtilization(
	)
	{
		return flUtilization;
	}
	
	public void setFlUtilization(
	        double flUtilization
	)
	{
		this.flUtilization = flUtilization;
	}
	
	public Rep_FL(
	        int flid, String name, String description, double balance, int numPf, double totalDeposits,
	        double totalCredits, double flD2C, double flUtilization
	)
	{
		super();
		this.flid          = flid;
		this.name          = name;
		this.description   = description;
		this.balance       = balance;
		this.numPf         = numPf;
		this.totalDeposits = totalDeposits;
		this.totalCredits  = totalCredits;
		this.flD2C         = flD2C;
		this.flUtilization = flUtilization;
	}
	
	public Rep_FL(
	)
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Rep_FL_TR transformToMoneyFormat(
	)
	{
		Rep_FL_TR repTR = new Rep_FL_TR();
		
		repTR.setFlid(flid);
		repTR.setDescription(description);
		repTR.setName(name);
		repTR.setBalance(UtilDecimaltoMoneyString.getMoneyStringforDecimal(balance, 1));
		repTR.setFlD2C(flD2C);
		repTR.setNumPf(numPf);
		repTR.setTotalCredits(totalCredits);
		repTR.setTotalDeposits(totalDeposits);
		repTR.setFlUtilization(flUtilization);
		
		return repTR;
	}
	
}
