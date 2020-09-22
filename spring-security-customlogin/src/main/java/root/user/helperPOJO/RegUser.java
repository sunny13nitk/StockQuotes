package root.user.helperPOJO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import root.validation.annotations.FieldMatch;
import root.validation.annotations.ValidEmail;

/*
 * Registration User POJO - Used in Create Operation for New User Registration
 */
@FieldMatch(
        field1 = "password", field2 = "matchingPassword"
)
public class RegUser
{
	@NotNull(
	        message = "is required!"
	)
	@Size(
	        min = 5, max = 50, message = "Username between 5 - 50 char!"
	)
	private String userName;
	
	@NotNull(
	        message = "is required!"
	)
	@Size(
	        min = 6, max = 50, message = "Password between 6 - 50 char!"
	)
	private String password;
	
	@NotNull(
	        message = "is required!"
	)
	@Size(
	        min = 6, max = 50, message = "Password between 6 - 50 char!"
	)
	private String matchingPassword;
	
	@NotNull(
	        message = "is required!"
	)
	@Size(
	        min = 3, max = 50, message = "First Name between 3 - 50 char!"
	)
	private String firstName;
	
	@NotNull(
	        message = "is required!"
	)
	@Size(
	        min = 3, max = 50, message = "First Name between 3 - 50 char!"
	)
	private String lastName;
	
	@NotNull(
	        message = "is required!"
	)
	@Size(
	        min = 3, max = 50, message = "First Name between 3 - 50 char!"
	)
	@ValidEmail(
	        message = "Enter Valid Email Address!"
	)
	private String email;
	
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
	
	public String getMatchingPassword(
	)
	{
		return matchingPassword;
	}
	
	public void setMatchingPassword(
	        String matchingPassword
	)
	{
		this.matchingPassword = matchingPassword;
	}
	
	public String getFirstName(
	)
	{
		return firstName;
	}
	
	public void setFirstName(
	        String firstName
	)
	{
		this.firstName = firstName;
	}
	
	public String getLastName(
	)
	{
		return lastName;
	}
	
	public void setLastName(
	        String lastName
	)
	{
		this.lastName = lastName;
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
	
	public RegUser(
	)
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
}
