package pp.springboot.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Student")
public class Student
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "DOB")
	@Temporal(TemporalType.DATE)
	private Date DOB;
	
	/*
	 * ------------------ Relations ------------------
	 */
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "pdID")
	private Student_Family familyInfo;
	
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
	
	public Student_Family getFamilyInfo(
	)
	{
		return familyInfo;
	}
	
	public void setFamilyInfo(
	        Student_Family familyInfo
	)
	{
		this.familyInfo = familyInfo;
	}
	
	public Student(
	        String firstName, String lastName, String email, Date dOB
	)
	{
		super();
		this.firstName = firstName;
		this.lastName  = lastName;
		this.email     = email;
		DOB            = dOB;
	}
	
	public Student(
	)
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString(
	)
	{
		return "Student [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
		        + ", DOB=" + DOB + "]";
	}
	
}
