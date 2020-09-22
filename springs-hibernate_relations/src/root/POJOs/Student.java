package root.POJOs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
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
	private int id;
	
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
	
	@OneToOne(
	        cascade = CascadeType.ALL
	)
	@JoinColumn(
	        name = "pdID"
	)
	private StudentGeneral studentGeneral;
	
	@ManyToMany(
	        fetch = FetchType.LAZY, cascade =
			{ CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }
	)
	@JoinTable(
	        name = "Course_Student", joinColumns = @JoinColumn(
	                name = "studentID"
	        ), inverseJoinColumns = @JoinColumn(
	                name = "courseID"
	        )
	)
	private List<Course> courses;
	
	public List<Course> getCourses(
	)
	{
		return courses;
	}
	
	public void setCourses(
	        List<Course> courses
	)
	{
		this.courses = courses;
	}
	
	public Student(
	)
	{
		super();
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
	
	public StudentGeneral getStudentGeneral(
	)
	{
		return studentGeneral;
	}
	
	public void setStudentGeneral(
	        StudentGeneral studentGeneral
	)
	{
		this.studentGeneral = studentGeneral;
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
	
	/**
	 * Conv. Method to Add Courses for a Student
	 */
	
	public void addCourse(
	        Course newCourse
	)
	{
		if (this.courses == null)
		{
			this.courses = new ArrayList<Course>();
		}
		this.courses.add(newCourse);
	}
}
