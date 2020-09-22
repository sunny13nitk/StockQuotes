package root.busslogic.SQLProc.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import root.busslogic.utilities.UtilDecimaltoMoneyString;

@Entity
public class Rep_PFSS
{
	@Id
	@GeneratedValue
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
	private double totalInv;
	private double top5Inv;
	private double top5Per;
	private String maxScrip;
	private double maxScInv;
	private double maxScPer;
	private double flBalance;
	private double flUtilization;
	private int    numSectors;
	private String maxSector;
	private double maxSectorInv;
	private double maxSectorPer;
	private int    numTrades;
	private int    numBTrades;
	private int    numSTrades;
	private double totalTradeAmnt;
	private double txnCost;
	
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
	
	public double getTotalInv(
	)
	{
		return totalInv;
	}
	
	public void setTotalInv(
	        double totalInv
	)
	{
		this.totalInv = totalInv;
	}
	
	public double getTop5Inv(
	)
	{
		return top5Inv;
	}
	
	public void setTop5Inv(
	        double top5Inv
	)
	{
		this.top5Inv = top5Inv;
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
	
	public double getMaxScInv(
	)
	{
		return maxScInv;
	}
	
	public void setMaxScInv(
	        double maxScInv
	)
	{
		this.maxScInv = maxScInv;
	}
	
	public double getMaxScPer(
	)
	{
		return maxScPer;
	}
	
	public void setMaxScPer(
	        double maxScPer
	)
	{
		this.maxScPer = maxScPer;
	}
	
	public double getFlBalance(
	)
	{
		return flBalance;
	}
	
	public void setFlBalance(
	        double flBalance
	)
	{
		this.flBalance = flBalance;
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
	
	public double getMaxSectorInv(
	)
	{
		return maxSectorInv;
	}
	
	public void setMaxSectorInv(
	        double maxSectorInv
	)
	{
		this.maxSectorInv = maxSectorInv;
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
	
	public double getTotalTradeAmnt(
	)
	{
		return totalTradeAmnt;
	}
	
	public void setTotalTradeAmnt(
	        double totalTradeAmnt
	)
	{
		this.totalTradeAmnt = totalTradeAmnt;
	}
	
	public double getTxnCost(
	)
	{
		return txnCost;
	}
	
	public void setTxnCost(
	        double txnCost
	)
	{
		this.txnCost = txnCost;
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
	
	public Rep_PFSS(
	)
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Rep_PFSS_TR transformToMoneyFormat(
	)
	{
		Rep_PFSS_TR pfStatTr = new Rep_PFSS_TR();
		
		pfStatTr.setPid(this.pid);
		pfStatTr.setBroker(broker);
		pfStatTr.setDescription(description);
		pfStatTr.setFlBalance(UtilDecimaltoMoneyString.getMoneyStringforDecimal(flBalance, 2));
		pfStatTr.setFlid(flid);
		pfStatTr.setFllastDDate(fllastDDate);
		pfStatTr.setFlName(flName);
		pfStatTr.setFlUtilization(flUtilization);
		pfStatTr.setLastTradeDate(lastTradeDate);
		pfStatTr.setMaxScInv(UtilDecimaltoMoneyString.getMoneyStringforDecimal(maxScInv, 0));
		pfStatTr.setMaxScPer(maxScPer);
		pfStatTr.setMaxScrip(maxScrip);
		pfStatTr.setMaxSector(maxSector);
		pfStatTr.setMaxSectorInv(UtilDecimaltoMoneyString.getMoneyStringforDecimal(maxSectorInv, 0));
		pfStatTr.setMaxSectorPer(maxSectorPer);
		pfStatTr.setName(name);
		pfStatTr.setNumBTrades(numBTrades);
		pfStatTr.setNumScrips(numScrips);
		pfStatTr.setNumSectors(numSectors);
		pfStatTr.setNumSTrades(numSTrades);
		pfStatTr.setNumTrades(numTrades);
		pfStatTr.setTop5Inv(UtilDecimaltoMoneyString.getMoneyStringforDecimal(top5Inv, 0));
		pfStatTr.setTop5Per(top5Per);
		pfStatTr.setTotalInv(UtilDecimaltoMoneyString.getMoneyStringforDecimal(totalInv, 2));
		pfStatTr.setTotalTradeAmnt(UtilDecimaltoMoneyString.getMoneyStringforDecimal(totalTradeAmnt, 2));
		pfStatTr.setTxnCost(UtilDecimaltoMoneyString.getMoneyStringforDecimal(txnCost, 1));
		
		return pfStatTr;
	}
}
