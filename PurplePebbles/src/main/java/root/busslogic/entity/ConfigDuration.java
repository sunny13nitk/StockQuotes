package root.busslogic.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(
        name = "configDuration"
)
public class ConfigDuration
{
	@Id
	@GeneratedValue
	private int Id;
	
	private String Duration;
	
	public int getId(
	)
	{
		return Id;
	}
	
	public void setId(
	        int id
	)
	{
		Id = id;
	}
	
	public String getDuration(
	)
	{
		return Duration;
	}
	
	public void setDuration(
	        String duration
	)
	{
		Duration = duration;
	}
	
	public ConfigDuration(
	        int id, String duration
	)
	{
		super();
		Id       = id;
		Duration = duration;
	}
	
	public ConfigDuration(
	)
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
}
