package root.POJOs;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(
        name = "dbo.Reviews"
)
public class Reviews
{
	@Id
	@GeneratedValue(
	        strategy = GenerationType.IDENTITY
	)
	private int reviewID;
	
	private String comment;
	
	public int getReviewID(
	)
	{
		return reviewID;
	}
	
	public void setReviewID(
	        int reviewID
	)
	{
		this.reviewID = reviewID;
	}
	
	public String getComment(
	)
	{
		return comment;
	}
	
	public void setComment(
	        String comment
	)
	{
		this.comment = comment;
	}
	
	public Reviews(
	        String comment
	)
	{
		super();
		
		this.comment = comment;
	}
	
	public Reviews(
	)
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
}
