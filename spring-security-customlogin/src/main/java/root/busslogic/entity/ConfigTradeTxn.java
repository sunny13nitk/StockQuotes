package root.busslogic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(
        name = "dbo.configTradeTxn"
)
public class ConfigTradeTxn
{
	@Id
	@Column(
	        name = "Ttxn_type"
	)
	private char code;
	
	@Column(
	        name = "Description"
	)
	private String desc;
	
	public char getCode(
	)
	{
		return code;
	}
	
	public void setCode(
	        char code
	)
	{
		this.code = code;
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
	
	public ConfigTradeTxn(
	        char code, String desc
	)
	{
		super();
		this.code = code;
		this.desc = desc;
	}
	
	public ConfigTradeTxn(
	)
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
}
