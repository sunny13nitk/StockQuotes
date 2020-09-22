package pp.springboot.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(
        name = "Instructor"
)
public class Instructor
{
	@Id
	@GeneratedValue(
	        strategy = GenerationType.IDENTITY
	)
	private int instrId;
	
	private String fullName;
	
	private String email;
	
	public int getInstrId(
	)
	{
		return instrId;
	}
	
	public void setInstrId(
	        int instrId
	)
	{
		this.instrId = instrId;
	}
	
	public String getFullName(
	)
	{
		return fullName;
	}
	
	public void setFullName(
	        String fullName
	)
	{
		this.fullName = fullName;
	}
	
	public String getEmail(
	)
	{
		return email;
	}
	
	public void setEmail(
	        String email
	)
	{
		this.email = email;
	}
	
	public Instructor(
	        String fullName, String email
	)
	{
		super();
		this.fullName = fullName;
		this.email    = email;
	}
	
	public Instructor(
	)
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString(
	)
	{
		return "Instructor [instrId=" + instrId + ", fullName=" + fullName + ", email=" + email + "]";
	}
	
}
