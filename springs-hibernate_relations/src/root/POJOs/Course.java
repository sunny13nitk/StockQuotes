package root.POJOs;

import java.util.ArrayList;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(
        name = "dbo.Courses"
)
public class Course
{
	@Id
	@GeneratedValue(
	        strategy = GenerationType.IDENTITY
	)
	@Column(
	        name = "course_id"
	)
	private int courseID;
	
	private String courseName;
	
	/*
	 * Many Courses can have the Same Instructor
	 */
	@ManyToOne(
	        cascade =
			{ CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }
	)
	@JoinColumn(
	        name = "instrId"
	)
	private Instructor instructor;
	
	@OneToMany(
	        cascade = CascadeType.ALL
	)
	@JoinColumn(
	        name = "course_id"
	)
	private List<Reviews> reviews;
	
	/*
	 * A course Can have Many Students - and Many Students Can have Same Course - Many to Many
	 */
	
	@ManyToMany(
	        fetch = FetchType.LAZY, cascade =
			{ CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }
	)
	@JoinTable(
	        name = "Course_Student", joinColumns = @JoinColumn(
	                name = "courseID"
	        ), inverseJoinColumns = @JoinColumn(
	                name = "studentID"
	        )
	)
	private List<Student> students;
	
	public List<Student> getStudents(
	)
	{
		return students;
	}
	
	public void setStudents(
	        List<Student> students
	)
	{
		this.students = students;
	}
	
	public Instructor getInstructor(
	)
	{
		return instructor;
	}
	
	public void setInstructor(
	        Instructor instructor
	)
	{
		this.instructor = instructor;
	}
	
	public int getCourseID(
	)
	{
		return courseID;
	}
	
	public void setCourseID(
	        int courseID
	)
	{
		this.courseID = courseID;
	}
	
	public String getCourseName(
	)
	{
		return courseName;
	}
	
	public void setCourseName(
	        String courseName
	)
	{
		this.courseName = courseName;
	}
	
	public List<Reviews> getReviews(
	)
	{
		return reviews;
	}
	
	public void setReviews(
	        List<Reviews> reviews
	)
	{
		this.reviews = reviews;
	}
	
	public Course(
	)
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Course(
	        String courseName
	)
	{
		super();
		this.courseName = courseName;
		
	}
	
	public Course(
	        String courseName, Instructor instructor
	)
	{
		super();
		this.courseName = courseName;
		this.instructor = instructor;
	}
	
	@Override
	public String toString(
	)
	{
		return "Course [courseID=" + courseID + ", courseName=" + courseName + ", instructor=" + instructor + "]";
	}
	
	/**
	 * Utility Method to add Review - Set up Course to REview Association
	 * 
	 * @param review
	 */
	public void addReview(
	        Reviews review
	)
	{
		if (this.reviews == null)
		{
			this.reviews = new ArrayList<Reviews>();
		}
		
		this.reviews.add(review); // No need to set Relation from Other (Reviews's) Direction
		
	}
	
	/*
	 * Utility Method to Add Students to Course
	 */
	
	public void addStudent(
	        Student newStudent
	)
	{
		if (this.students == null)
		{
			this.students = new ArrayList<Student>();
		}
		
		this.students.add(newStudent);
	}
}
