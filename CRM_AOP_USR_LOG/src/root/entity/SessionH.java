package root.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(
        name = "dbo.SessionH"
)
public class SessionH
{
	@Id
	@Column(
	        name = "sessionGuid"
	)
	private UUID sessionGuid;
	
	@Column(
	        name = "userName"
	)
	private String userName;
	
	@Column(
	        name = "DateTime"
	)
	private Timestamp timeStamp;
	
	public UUID getSessionGuid(
	)
	{
		return sessionGuid;
	}
	
	public void setSessionGuid(
	        UUID sessionGuid
	)
	{
		this.sessionGuid = sessionGuid;
	}
	
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
	
	public Timestamp getTimeStamp(
	)
	{
		return timeStamp;
	}
	
	public void setTimeStamp(
	        Timestamp timeStamp
	)
	{
		this.timeStamp = timeStamp;
	}
	
	@OneToMany(
	        cascade = CascadeType.ALL
	)
	@JoinColumn(
	        name = "sessionGuid"
	)
	private List<SessionI> sessItems;
	
	public List<SessionI> getSessItems(
	)
	{
		return sessItems;
	}
	
	public void setSessItems(
	        List<SessionI> sessItems
	)
	{
		this.sessItems = sessItems;
	}
	
	public SessionH(
	)
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
	public SessionH(
	        String userName
	)
	{
		super();
		this.userName = userName;
	}
	
	/*
	 * UTILITY METHOD - ADD Session Item
	 */
	
	public void addSessionItem(
	        SessionI sessI
	)
	{
		if (sessI != null)
		{
			if (this.sessItems == null)
			{
				this.sessItems = new ArrayList<SessionI>();
			}
			sessItems.add(sessI);
		}
	}
}
