package root.POJOs;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(
        name = "dbo.Instructor"
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
	
	/*
	 * A single Instructor can teach Many Courses
	 */
	@OneToMany(
	        fetch = FetchType.LAZY, cascade =
			{ CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, mappedBy = "instructor"
			
	)
			
	private List<Course> courses;
	
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
	
	public Instructor(
	)
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Instructor(
	        String fullName, String email
	)
	{
		super();
		this.fullName = fullName;
		this.email    = email;
	}
	
	/**
	 * Utility method to create Association when adding a new course to set current Instructor Instance in the Course to
	 * be added
	 * 
	 * @param newCourse
	 */
	public void addCourse(
	        Course newCourse
	)
	{
		if (newCourse != null)
		{
			if (this.courses.size() == 0)
			{
				this.courses = new ArrayList<Course>();
			}
			
			this.courses.add(newCourse);
			newCourse.setInstructor(this);
			
		}
	}
	
	@Override
	public String toString(
	)
	{
		return "Instructor [instrId=" + instrId + ", fullName=" + fullName + ", email=" + email;
	}
	
}
