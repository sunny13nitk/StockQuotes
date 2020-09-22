package root.entity;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import root.enums.Enums;
import root.enums.Enums.dbOperation;

@Entity
@Table(
        name = "dbo.SessionI"
)
public class SessionI
{
	@Id
	@GeneratedValue(
	        strategy = GenerationType.IDENTITY
	)
	@Column(
	        name = "sessionItemId"
	)
	private long sessItemId;
	
	@Column(
	        name = "sessionGuid"
	)
	private UUID sessGUID;
	
	private Timestamp timeStamp;
	
	@Column(
	        name = "AspectName"
	)
	private String aspectName;
	
	@Column(
	        name = "ArtifactName"
	)
	private String artifactName;
	
	@Column(
	        name = "MethodName"
	)
	private String methodName;
	
	@Column(
	        name = "Args"
	)
	private String args;
	
	@Column(
	        name = "OpType"
	)
	private Enums.dbOperation opType;
	
	@Column(
	        name = "Message"
	)
	private String message;
	
	private long timeElapsed;
	
	public long getTimeElapsed(
	)
	{
		return timeElapsed;
	}
	
	public void setTimeElapsed(
	        long timeElapsed
	)
	{
		this.timeElapsed = timeElapsed;
	}
	
	public long getSessItemId(
	)
	{
		return sessItemId;
	}
	
	public void setSessItemId(
	        long sessItemId
	)
	{
		this.sessItemId = sessItemId;
	}
	
	public UUID getSessGUID(
	)
	{
		return sessGUID;
	}
	
	public void setSessGUID(
	        UUID sessGUID
	)
	{
		this.sessGUID = sessGUID;
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
	
	public String getAspectName(
	)
	{
		return aspectName;
	}
	
	public void setAspectName(
	        String aspectName
	)
	{
		this.aspectName = aspectName;
	}
	
	public String getArtifactName(
	)
	{
		return artifactName;
	}
	
	public void setArtifactName(
	        String artifactName
	)
	{
		this.artifactName = artifactName;
	}
	
	public String getMethodName(
	)
	{
		return methodName;
	}
	
	public void setMethodName(
	        String methodName
	)
	{
		this.methodName = methodName;
	}
	
	public String getArgs(
	)
	{
		return args;
	}
	
	public void setArgs(
	        String args
	)
	{
		this.args = args;
	}
	
	public Enums.dbOperation getOpType(
	)
	{
		return opType;
	}
	
	public void setOpType(
	        Enums.dbOperation opType
	)
	{
		this.opType = opType;
	}
	
	public String getMessage(
	)
	{
		return message;
	}
	
	public void setMessage(
	        String message
	)
	{
		this.message = message;
	}
	
	public SessionI(
	)
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
	public SessionI(
	        long sessItemId, UUID sessGUID, Timestamp timeStamp, String aspectName, String artifactName,
	        String methodName, String args, dbOperation opType, String message, long timeElapsed
	)
	{
		super();
		this.sessItemId   = sessItemId;
		this.sessGUID     = sessGUID;
		this.timeStamp    = timeStamp;
		this.aspectName   = aspectName;
		this.artifactName = artifactName;
		this.methodName   = methodName;
		this.args         = args;
		this.opType       = opType;
		this.message      = message;
		this.timeElapsed  = timeElapsed;
	}
	
	public SessionI(
	        UUID sessGUID
	)
	{
		super();
		this.sessGUID = sessGUID;
	}
	
}
