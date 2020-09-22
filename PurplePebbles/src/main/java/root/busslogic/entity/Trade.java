package root.busslogic.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(
        name = "dbo.trades"
)
public class Trade
{
	
	@Id
	@GeneratedValue(
	        strategy = GenerationType.IDENTITY
	)
	private int tid;
	
	private int  pid;
	@NotNull(
	        message = "is required"
	)
	private char tCode;
	
	@Column(
	        name = "txnDate"
	)
	
	@Temporal(
	    TemporalType.DATE
	)
	@DateTimeFormat(
	        pattern = "yyyy-MM-dd"
	)
	
	private Date txnDate;
	
	@NotNull(
	        message = "is required"
	)
	private String scCode;
	
	@Min(
	        value = 1
	)
	private int numUnits;
	
	@Min(
	        value = 1
	)
	private double ppu;
	
	private double amount;
	
	public Date getTxnDate(
	)
	{
		return txnDate;
	}
	
	public void setTxnDate(
	        Date txnDate
	)
	{
		this.txnDate = txnDate;
	}
	
	public int getTid(
	)
	{
		return tid;
	}
	
	public void setTid(
	        int tid
	)
	{
		this.tid = tid;
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
	
	public char gettCode(
	)
	{
		return tCode;
	}
	
	public void settCode(
	        char tCode
	)
	{
		this.tCode = tCode;
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
	
	public double getPpu(
	)
	{
		return ppu;
	}
	
	public void setPpu(
	        double ppu
	)
	{
		this.ppu = ppu;
	}
	
	public double getAmount(
	)
	{
		return amount;
	}
	
	public void setAmount(
	        double amount
	)
	{
		this.amount = amount;
	}
	
	public Trade(
	        int pid, char tCode, String scCode, int numUnits, double ppu, double amount
	)
	{
		super();
		this.pid      = pid;
		this.tCode    = tCode;
		this.scCode   = scCode;
		this.numUnits = numUnits;
		this.ppu      = ppu;
		this.amount   = amount;
	}
	
	public Trade(
	)
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
}
