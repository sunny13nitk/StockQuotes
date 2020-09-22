package modelframework.definitions;

public class KeyEntity<T>
{
	private Object	key;

	private T		pojoEntity;

	/**
	 * @return the key
	 */
	public Object getKey()
	{
		return key;
	}

	/**
	 * @param key
	 *             the key to set
	 */
	public void setKey(Object key)
	{
		this.key = key;
	}

	/**
	 * @return the pojoEntity
	 */
	public T getPojoEntity()
	{
		return pojoEntity;
	}

	/**
	 * @param pojoEntity
	 *             the pojoEntity to set
	 */
	public void setPojoEntity(T pojoEntity)
	{
		this.pojoEntity = pojoEntity;
	}

	/**
	 * @param key
	 * @param pojoEntity
	 */
	public KeyEntity(Object key, T pojoEntity)
	{
		super();
		this.key = key;
		this.pojoEntity = pojoEntity;
	}

	/**
	 * 
	 */
	public KeyEntity()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
