package root.busslogic.SQLProc.entities;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

public class Rep_PFSS_TR
{
	private int pid;
	
	private String name;
	
	private String description;
	
	private String broker;
	
	private int flid;
	
	private String flName;
	
	@Temporal(
	    TemporalType.DATE
	)
	@DateTimeFormat(
	        pattern = "MM/dd/yyyy"
	)
	private Date fllastDDate;
	
	private int    numScrips;
	private String totalInv;
	private String top5Inv;
	private double top5Per;
	private String maxScrip;
	private String maxScInv;
	private double maxScPer;
	private String flBalance;
	private double flUtilization;
	private int    numSectors;
	private String maxSector;
	private String maxSectorInv;
	private double maxSectorPer;
	private int    numTrades;
	private int    numBTrades;
	private int    numSTrades;
	private String totalTradeAmnt;
	private String txnCost;
	
	@Temporal(
	    TemporalType.DATE
	)
	@DateTimeFormat(
	        pattern = "MM/dd/yyyy"
	)
	private Date lastTradeDate;
	
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
	
	public String getFlName(
	)
	{
		return flName;
	}
	
	public void setFlName(
	        String flName
	)
	{
		this.flName = flName;
	}
	
	public Date getFllastDDate(
	)
	{
		return fllastDDate;
	}
	
	public void setFllastDDate(
	        Date fllastDDate
	)
	{
		this.fllastDDate = fllastDDate;
	}
	
	public int getNumScrips(
	)
	{
		return numScrips;
	}
	
	public void setNumScrips(
	        int numScrips
	)
	{
		this.numScrips = numScrips;
	}
	
	public double getTop5Per(
	)
	{
		return top5Per;
	}
	
	public void setTop5Per(
	        double top5Per
	)
	{
		this.top5Per = top5Per;
	}
	
	public String getMaxScrip(
	)
	{
		return maxScrip;
	}
	
	public void setMaxScrip(
	        String maxScrip
	)
	{
		this.maxScrip = maxScrip;
	}
	
	public double getMaxScPer(
	)
	{
		return this.maxScPer;
	}
	
	public void setMaxScPer(
	        double maxScPer
	)
	{
		this.maxScPer = maxScPer;
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
	
	public int getNumSectors(
	)
	{
		return numSectors;
	}
	
	public void setNumSectors(
	        int numSectors
	)
	{
		this.numSectors = numSectors;
	}
	
	public String getMaxSector(
	)
	{
		return maxSector;
	}
	
	public void setMaxSector(
	        String maxSector
	)
	{
		this.maxSector = maxSector;
	}
	
	public double getMaxSectorPer(
	)
	{
		return maxSectorPer;
	}
	
	public void setMaxSectorPer(
	        double maxSectorPer
	)
	{
		this.maxSectorPer = maxSectorPer;
	}
	
	public int getNumTrades(
	)
	{
		return numTrades;
	}
	
	public void setNumTrades(
	        int numTrades
	)
	{
		this.numTrades = numTrades;
	}
	
	public int getNumBTrades(
	)
	{
		return numBTrades;
	}
	
	public void setNumBTrades(
	        int numBTrades
	)
	{
		this.numBTrades = numBTrades;
	}
	
	public int getNumSTrades(
	)
	{
		return numSTrades;
	}
	
	public void setNumSTrades(
	        int numSTrades
	)
	{
		this.numSTrades = numSTrades;
	}
	
	public Date getLastTradeDate(
	)
	{
		return lastTradeDate;
	}
	
	public void setLastTradeDate(
	        Date lastTradeDate
	)
	{
		this.lastTradeDate = lastTradeDate;
	}
	
	public String getBroker(
	)
	{
		return broker;
	}
	
	public void setBroker(
	        String broker
	)
	{
		this.broker = broker;
	}
	
	public String getTotalInv(
	)
	{
		return totalInv;
	}
	
	public void setTotalInv(
	        String totalInv
	)
	{
		this.totalInv = totalInv;
	}
	
	public String getTop5Inv(
	)
	{
		return top5Inv;
	}
	
	public void setTop5Inv(
	        String top5Inv
	)
	{
		this.top5Inv = top5Inv;
	}
	
	public String getMaxScInv(
	)
	{
		return maxScInv;
	}
	
	public void setMaxScInv(
	        String maxScInv
	)
	{
		this.maxScInv = maxScInv;
	}
	
	public String getFlBalance(
	)
	{
		return flBalance;
	}
	
	public void setFlBalance(
	        String flBalance
	)
	{
		this.flBalance = flBalance;
	}
	
	public String getMaxSectorInv(
	)
	{
		return maxSectorInv;
	}
	
	public void setMaxSectorInv(
	        String maxSectorInv
	)
	{
		this.maxSectorInv = maxSectorInv;
	}
	
	public String getTotalTradeAmnt(
	)
	{
		return totalTradeAmnt;
	}
	
	public void setTotalTradeAmnt(
	        String totalTradeAmnt
	)
	{
		this.totalTradeAmnt = totalTradeAmnt;
	}
	
	public String getTxnCost(
	)
	{
		return txnCost;
	}
	
	public void setTxnCost(
	        String txnCost
	)
	{
		this.txnCost = txnCost;
	}
	
	public Rep_PFSS_TR(
	)
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
}
