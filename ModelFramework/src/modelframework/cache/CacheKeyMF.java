package modelframework.cache;

import java.util.ArrayList;

import modelframework.types.TY_NameValue;

/*
 * Object Name - Invoking Object Name
 ** Sql Where Condition
 ** Paremters -> Name, Value Arraylist
 */
public class CacheKeyMF
{

	public String					objName;

	public String					whereCondn;

	public ArrayList<TY_NameValue>	ParamsValues;

	/**
	 * @return the objName
	 */
	public String getObjName()
	{
		return objName;
	}

	/**
	 * @param objName
	 *             the objName to set
	 */
	public void setObjName(String objName)
	{
		this.objName = objName;
	}

	/**
	 * @return the whereCondn
	 */
	public String getWhereCondn()
	{
		return whereCondn;
	}

	/**
	 * @param whereCondn
	 *             the whereCondn to set
	 */
	public void setWhereCondn(String whereCondn)
	{
		this.whereCondn = whereCondn;
	}

	/**
	 * @return the paramsValues
	 */
	public ArrayList<TY_NameValue> getParamsValues()
	{
		return ParamsValues;
	}

	/**
	 * @param paramsValues
	 *             the paramsValues to set
	 */
	public void setParamsValues(ArrayList<TY_NameValue> paramsValues)
	{
		ParamsValues = paramsValues;
	}

	/**
	 * @param objName
	 * @param whereCondn
	 * @param paramsValues
	 */
	public CacheKeyMF(String objName, String whereCondn, ArrayList<TY_NameValue> paramsValues)
	{
		super();
		this.objName = objName;
		this.whereCondn = whereCondn;
		ParamsValues = paramsValues;
	}

	/**
	 * 
	 */
	public CacheKeyMF()
	{

	}

}
