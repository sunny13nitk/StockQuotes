package root.helperPOJO;

import org.hibernate.validator.constraints.Length;

import com.sun.istack.NotNull;

public class FilePathAttr
{
	@NotNull
	@Length(
	        min = 5
	)
	public String filePath;
	
	public String getFilePath(
	)
	{
		return filePath;
	}
	
	public void setFilePath(
	        String filePath
	)
	{
		this.filePath = filePath;
	}
	
	public FilePathAttr(
	        String filePath
	)
	{
		super();
		this.filePath = filePath;
	}
	
	public FilePathAttr(
	)
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
}
