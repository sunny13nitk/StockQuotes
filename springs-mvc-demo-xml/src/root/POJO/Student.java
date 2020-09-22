package root.POJO;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import root.validations.defn.CourseCode;

public class Student
{
	private String firstName;
	
	@NotNull(
	        message = "is required!"
	)
	@Size(
	        min = 2, message = "min'm 2 Char!"
	)
	private String lastName;
	
	private String country;
	
	private String prLanguage;
	
	private String[] opSys;
	
	@NotNull(
	        message = "is required !"
	)
	@Min(
	        value = 0, message = "Minimum value expected is 0 !"
	)
	@Max(
	        value = 10, message = "Maximum value that can be entered is 10 !"
	)
	private Integer freePasses;
	
	@Pattern(
	        regexp = "^[a-zA-Z0-9]{6}", message = "only 6 char/digits"
	)
	private String postalCode;
	
	@CourseCode(
	        value = "ENG_"
	)
	private String courseCode;
	
	public String getCourseCode(
	)
	{
		return courseCode;
	}
	
	public void setCourseCode(
	        String courseCode
	)
	{
		this.courseCode = courseCode;
	}
	
	public String[] getOpSys(
	)
	{
		return opSys;
	}
	
	public void setOpSys(
	        String[] opSys
	)
	{
		this.opSys = opSys;
	}
	
	public String getPrLanguage(
	)
	{
		return prLanguage;
	}
	
	public void setPrLanguage(
	        String prLanguage
	)
	{
		this.prLanguage = prLanguage;
	}
	
	public String getCountry(
	)
	{
		return country;
	}
	
	public void setCountry(
	        String country
	)
	{
		this.country = country;
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
	
	public Integer getFreePasses(
	)
	{
		return freePasses;
	}
	
	public void setFreePasses(
	        Integer freePasses
	)
	{
		this.freePasses = freePasses;
	}
	
	public String getPostalCode(
	)
	{
		return postalCode;
	}
	
	public void setPostalCode(
	        String postalCode
	)
	{
		this.postalCode = postalCode;
	}
	
	public Student(
	)
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Student(
	        String firstName, String lastName, String country
	)
	{
		super();
		this.firstName = firstName;
		this.lastName  = lastName;
		this.country   = country;
	}
	
}
