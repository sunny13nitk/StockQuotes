package scriptsengine.taxation.interfaces;

import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Interface to Convert Object to ITaxable Instance - To be consumed in Taxation Aspect Later for Tax computaitons
 */
public interface IObjITaxableConvSrv
{
	public ITaxable getTaxableObjfromObject(Object pojo) throws EX_General;
}
