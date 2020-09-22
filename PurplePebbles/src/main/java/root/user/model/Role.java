package root.user.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(
        name = "role"
)
public class Role
{
	@Id
	@GeneratedValue(
	        strategy = GenerationType.IDENTITY
	)
	private int id;
	
	private String name;
	
	private boolean is_default;
	
	public boolean isIs_default(
	)
	{
		return is_default;
	}
	
	public void setIs_default(
	        boolean is_default
	)
	{
		this.is_default = is_default;
	}
	
	public int getId(
	)
	{
		return id;
	}
	
	public void setId(
	        int id
	)
	{
		this.id = id;
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
	
	public Role(
	        String name
	)
	{
		
		this.name = name;
	}
	
	public Role(
	)
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
}
