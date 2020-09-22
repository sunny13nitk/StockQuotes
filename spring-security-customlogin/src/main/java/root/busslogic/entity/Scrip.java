package root.busslogic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(
        name = "dbo.ScripsH"
)
public class Scrip
{
	@Id
	@Column(
	        name = "scCode"
	)
	private String scCode;
	
	@Column(
	        name = "Description"
	)
	private String desc;
	
	@Column(
	        name = "Sector"
	)
	private String sector;
	
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
	
	public String getSector(
	)
	{
		return sector;
	}
	
	public void setSector(
	        String sector
	)
	{
		this.sector = sector;
	}
	
	public Scrip(
	        String scCode, String desc, String sector
	)
	{
		super();
		this.scCode = scCode;
		this.desc   = desc;
		this.sector = sector;
	}
	
	public Scrip(
	)
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
}
