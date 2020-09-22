package root.busslogic.entity;

import java.util.ArrayList;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(
        name = "dbo.fundline"
)
public class FundLine
{
	@Id
	@GeneratedValue(
	        strategy = GenerationType.IDENTITY
	)
	private int flid;
	
	private int uid;
	
	@NotNull(
	        message = "is required"
	)
	@Size(
	        min = 3, message = "name should be at least 3 char"
	)
	@Column(
	        name = "Name"
	)
	private String name;
	
	@NotNull(
	        message = "is required"
	)
	@Size(
	        min = 3, message = "description should be at least 3 char"
	)
	@Column(
	        name = "Description"
	)
	private String desc;
	
	@Column(
	        name = "balance"
	)
	private double balance;
	
	@OneToMany(
	        cascade = CascadeType.ALL, fetch = FetchType.EAGER
	)
	@JoinColumn(
	        name = "flid"
	)
	private List<FundLineItem> flItems;
	
	/*
	 * A Single Fund line can support multiple Portfolios
	 */
	
	@OneToMany(
	        cascade =
			{ CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.LAZY
	)
	@JoinTable(
	        name = "fl_pf", joinColumns = @JoinColumn(
	                name = "flid"
	        ), inverseJoinColumns = @JoinColumn(
	                name = "pid"
	        )
	)
	private List<Portfolio> portfolios;
	
	public List<Portfolio> getPortfolios(
	)
	{
		return portfolios;
	}
	
	public void setPortfolios(
	        List<Portfolio> portfolios
	)
	{
		this.portfolios = portfolios;
	}
	
	public List<FundLineItem> getFlItems(
	)
	{
		return flItems;
	}
	
	public void setFlItems(
	        List<FundLineItem> flItems
	)
	{
		this.flItems = flItems;
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
	
	public FundLine(
	        String name, String description
	)
	{
		super();
		this.name    = name;
		desc         = description;
		this.balance = 0;
		this.flItems = new ArrayList<FundLineItem>();
	}
	
	public FundLine(
	)
	{
		super();
		this.flItems = new ArrayList<FundLineItem>();
	}
	
	public void addFundLineItem(
	        FundLineItem flItem
	)
	{
		this.getFlItems().add(flItem);
	}
	
}
