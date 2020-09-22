package root.busslogic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(
        name = "dbo.Brokers"
)
public class Broker
{
	@Id
	@Column(
	        name = "brokerCode"
	)
	private String brokerCode;
	
	@Column(
	        name = "brokerage"
	)
	private double brokerage;
	
	@Column(
	        name = "minmBrokerage"
	)
	private double brokerageminm;
	
	@Column(
	        name = "STT"
	)
	private double STTcharges;
	
	@Column(
	        name = "txnCharges"
	)
	private double txncharges;
	
	@Column(
	        name = "GST"
	)
	private double GSTcharges;
	
	@Column(
	        name = "SEBI"
	)
	private double SEBIcharges;
	
	public String getBrokerCode(
	)
	{
		return brokerCode;
	}
	
	public void setBrokerCode(
	        String brokerCode
	)
	{
		this.brokerCode = brokerCode;
	}
	
	public double getBrokerage(
	)
	{
		return brokerage;
	}
	
	public void setBrokerage(
	        double brokerage
	)
	{
		this.brokerage = brokerage;
	}
	
	public double getSTTcharges(
	)
	{
		return STTcharges;
	}
	
	public void setSTTcharges(
	        double sTTcharges
	)
	{
		STTcharges = sTTcharges;
	}
	
	public double getGSTcharges(
	)
	{
		return GSTcharges;
	}
	
	public void setGSTcharges(
	        double gSTcharges
	)
	{
		GSTcharges = gSTcharges;
	}
	
	public double getSEBIcharges(
	)
	{
		return SEBIcharges;
	}
	
	public void setSEBIcharges(
	        double sEBIcharges
	)
	{
		SEBIcharges = sEBIcharges;
	}
	
	public double getBrokerageminm(
	)
	{
		return brokerageminm;
	}
	
	public void setBrokerageminm(
	        double brokerageminm
	)
	{
		this.brokerageminm = brokerageminm;
	}
	
	public double getTxncharges(
	)
	{
		return txncharges;
	}
	
	public void setTxncharges(
	        double txncharges
	)
	{
		this.txncharges = txncharges;
	}
	
	public Broker(
	        String brokerCode, double brokerage, double sTTcharges, double gSTcharges, double sEBIcharges
	)
	{
		super();
		this.brokerCode = brokerCode;
		this.brokerage  = brokerage;
		STTcharges      = sTTcharges;
		GSTcharges      = gSTcharges;
		SEBIcharges     = sEBIcharges;
	}
	
	public Broker(
	)
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
}
