package scriptsengine.taxation.JAXB.interfaces;

import scriptsengine.taxation.JAXB.definitions.Obj_ITaxableSrv_MapList;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Object to Taxable Config Metadata Service Interface
 *
 */
public interface IObjITaxableMdtSrv
{
	/**
	 * ------------------------- Get the ObjName Service Mapping List ------------------
	 * 
	 * @return
	 * @throws EX_General
	 */
	public Obj_ITaxableSrv_MapList getObjITaxableConvSrvList() throws EX_General;

	/**
	 * ------------------- Get the ITaxable Conversion Service for Current Object Instance --------
	 * 
	 * @param obj
	 * @return
	 * @throws EX_General
	 */
	public String getITaxableConvSrvforObject(Object obj) throws EX_General;

}
