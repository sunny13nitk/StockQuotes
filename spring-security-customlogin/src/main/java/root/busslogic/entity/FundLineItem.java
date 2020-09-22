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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(
        name = "dbo.fundline_items"
)
public class FundLineItem
{
	@Id
	@GeneratedValue(
	        strategy = GenerationType.IDENTITY
	)
	private int fl_i_id;
	
	private int flid;
	
	private char type;
	
	@Column(
	        name = "Date"
	)
	
	@Temporal(
	    TemporalType.DATE
	)
	@DateTimeFormat(
	        pattern = "MM/dd/yyyy"
	)
	
	private Date date;
	
	@Column(
	        name = "Description"
	)
	
	@NotNull(
	        message = "is required"
	)
	@Size(
	        min = 3, message = "name should be at least 3 char"
	)
	private String desc;
	
	@Column(
	        name = "Amount"
	)
	
	private double amount;
	
	public int getFl_i_id(
	)
	{
		return fl_i_id;
	}
	
	public void setFl_i_id(
	        int fl_i_id
	)
	{
		this.fl_i_id = fl_i_id;
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
	
	public char getType(
	)
	{
		return type;
	}
	
	public void setType(
	        char type
	)
	{
		this.type = type;
	}
	
	public Date getDate(
	)
	{
		return date;
	}
	
	public void setDate(
	        Date date
	)
	{
		this.date = date;
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
	
	public FundLineItem(
	        char type, java.util.Date date, @NotNull(
	                message = "is required"
	        ) @Size(
	                min = 3, message = "name should be at least 3 char"
	        ) String description, double amount
	)
	{
		super();
		this.type   = type;
		this.date   = date;
		this.desc   = description;
		this.amount = amount;
	}
	
	public FundLineItem(
	)
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
}
