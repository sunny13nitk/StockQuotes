package root.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(
        name = "dbo.Users"
)
public class User
{
	@Id
	@GeneratedValue(
	        strategy = GenerationType.IDENTITY
	)
	@Column(
	        name = "UserName"
	)
	private String userName;
	
	@Column(
	        name = "Password"
	)
	private String password;
	
	public String getUserName(
	)
	{
		return userName;
	}
	
	public void setUserName(
	        String userName
	)
	{
		this.userName = userName;
	}
	
	public String getPassword(
	)
	{
		return password;
	}
	
	public void setPassword(
	        String password
	)
	{
		this.password = password;
	}
	
	public User(
	)
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
	public User(
	        String userName, String password
	)
	{
		super();
		this.userName = userName;
		this.password = password;
	}
	
}
