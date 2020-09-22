package root.busslogic.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(
        name = "portfolios"
)
public class Portfolio
{
	@Id
	@GeneratedValue(
	        strategy = GenerationType.IDENTITY
	)
	private int pid;
	
	private int uid;
	
	@Column(
	        name = "Name"
	)
	@NotNull
	@Size(
	        min = 5
	)
	private String name;
	
	@Column(
	        name = "Description"
	)
	@NotNull
	@Size(
	        min = 5
	)
	private String desc;
	
	@Column(
	        name = "Broker"
	)
	@NotNull
	private String broker;
	
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
	
	/*
	 * A Portfolio funding can come from only one funding line at a time, but Many portfolios can derive funding from
	 * same Funding line
	 */
	@ManyToOne(
	        cascade =
			{ CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.LAZY
	)
	@JoinTable(
	        name = "fl_pf", joinColumns = @JoinColumn(
	                name = "pid"
	        ), inverseJoinColumns = @JoinColumn(
	                name = "flid"
	        )
	)
	private FundLine fundLine;
	
	@OneToMany(
	        cascade = CascadeType.ALL, fetch = FetchType.LAZY
	)
	@JoinColumn(
	        name = "pid"
	)
	private List<Trade> trades;
	
	@OneToMany(
	        cascade = CascadeType.ALL, fetch = FetchType.LAZY
	)
	@JoinColumn(
	        name = "pid"
	)
	private List<Holding> holdings;
	
	public List<Holding> getHoldings(
	)
	{
		return holdings;
	}
	
	public void setHoldings(
	        List<Holding> holdings
	)
	{
		this.holdings = holdings;
	}
	
	public List<Trade> getTrades(
	)
	{
		return trades;
	}
	
	public void setTrades(
	        List<Trade> trades
	)
	{
		this.trades = trades;
	}
	
	public int getUid(
	)
	{
		return uid;
	}
	
	public void setUid(
	        int uid
	)
	{
		this.uid = uid;
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
	
	public String getDesc(
	)
	{
		return desc;
	}
	
	public void setDesc(
	        String desc
	)
	{
		this.desc = desc;
	}
	
	public Portfolio(
	        int uid
	)
	{
		super();
		this.uid = uid;
	}
	
	public Portfolio(
	)
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
	public FundLine getFundLine(
	)
	{
		return fundLine;
	}
	
	public void setFundLine(
	        FundLine fundLine
	)
	{
		this.fundLine = fundLine;
	}
	
}
