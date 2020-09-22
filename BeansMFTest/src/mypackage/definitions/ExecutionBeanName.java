package mypackage.definitions;

/**
 * 
 * POJO to define Execution Bean Name - TO Identify Unique BEan which implemants IExecute and Currently needs to be
 * trigeered
 */
public class ExecutionBeanName
{
	private String exeBeanName;

	public String getExeBeanName()
	{
		return exeBeanName;
	}

	public void setExeBeanName(String exeBeanName)
	{
		this.exeBeanName = exeBeanName;
	}

	public ExecutionBeanName(String exeBeanName)
	{
		super();
		this.exeBeanName = exeBeanName;
	}

	public ExecutionBeanName()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
