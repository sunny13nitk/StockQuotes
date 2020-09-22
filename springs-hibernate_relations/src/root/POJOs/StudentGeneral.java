package root.POJOs;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(
        name = "dbo.StudentGeneral"
)
public class StudentGeneral
{
	@Id
	@GeneratedValue(
	        strategy = GenerationType.IDENTITY
	)
	private int pdID;
	
	private String parentName;
	
	private String parentEmail;
	
	private boolean isSiblingAlumuni;
	
	private String siblingName;
	
	// Bi- directional Binding Set up - DO not Delete Student on Delete of Student General Info
	@OneToOne(
	        mappedBy = "studentGeneral", cascade =
			{ CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }
	)
	private Student student;
	
	public int getPdID(
	)
	{
		return pdID;
	}
	
	public void setPdID(
	        int pdID
	)
	{
		this.pdID = pdID;
	}
	
	public String getParentName(
	)
	{
		return parentName;
	}
	
	public void setParentName(
	        String parentName
	)
	{
		this.parentName = parentName;
	}
	
	public String getParentEmail(
	)
	{
		return parentEmail;
	}
	
	public void setParentEmail(
	        String parentEmail
	)
	{
		this.parentEmail = parentEmail;
	}
	
	public boolean isSiblingAlumuni(
	)
	{
		return isSiblingAlumuni;
	}
	
	public void setSiblingAlumuni(
	        boolean isSiblingAlumuni
	)
	{
		this.isSiblingAlumuni = isSiblingAlumuni;
	}
	
	public String getSiblingName(
	)
	{
		return siblingName;
	}
	
	public void setSiblingName(
	        String siblingName
	)
	{
		this.siblingName = siblingName;
	}
	
	public Student getStudent(
	)
	{
		return student;
	}
	
	public void setStudent(
	        Student student
	)
	{
		this.student = student;
	}
	
	public StudentGeneral(
	)
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
	public StudentGeneral(
	        String parentName, String parentEmail, boolean isSiblingAlumuni, String siblingName
	)
	{
		super();
		this.parentName       = parentName;
		this.parentEmail      = parentEmail;
		this.isSiblingAlumuni = isSiblingAlumuni;
		this.siblingName      = siblingName;
	}
	
	@Override
	public String toString(
	)
	{
		return "StudentGeneral [pdID=" + pdID + ", parentName=" + parentName + ", parentEmail=" + parentEmail
		        + ", isSiblingAlumuni=" + isSiblingAlumuni + ", siblingName=" + siblingName + "]";
	}
	
}
