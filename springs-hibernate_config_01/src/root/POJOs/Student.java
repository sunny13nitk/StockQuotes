package root.POJOs;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(
        name = "dbo.Student"
)
public class Student
{
	@Id
	@GeneratedValue(
	        strategy = GenerationType.IDENTITY
	)
	private long id;
	
	@Column(
	        name = "first_name"
	)
	private String firstName;
	
	@Column(
	        name = "last_name"
	)
	private String lastName;
	
	@Column(
	        name = "email"
	)
	private String email;
	
	@Column(
	        name = "DOB"
	)
	@Temporal(
	    TemporalType.DATE
	)
	private Date DOB;
	
	public Student(
	)
	{
		super();
	}
	
	public long getId(
	)
	{
		return id;
	}
	
	public void setId(
	        long id
	)
	{
		this.id = id;
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
	
	public Date getDOB(
	)
	{
		return DOB;
	}
	
	public void setDOB(
	        Date dOB
	)
	{
		DOB = dOB;
	}
	
	public Student(
	        String firstName, String lastName, String email
	)
	{
		super();
		this.firstName = firstName;
		this.lastName  = lastName;
		this.email     = email;
		
	}
	
	@Override
	public String toString(
	)
	{
		return "Student [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + "]";
	}
	
}
