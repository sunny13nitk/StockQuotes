package scriptsengine.taxation.implementations;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import scriptsengine.taxation.JAXB.definitions.Obj_ITaxableSrv_MapList;
import scriptsengine.taxation.JAXB.interfaces.IObjITaxableMdtSrv;
import scriptsengine.taxation.interfaces.IObjITaxableConvSrv;
import scriptsengine.taxation.interfaces.ITaxable;
import scriptsengine.uploadengine.exceptions.EX_General;

@Service
public class TaxManager implements ApplicationContextAware
{
	@Autowired
	private IObjITaxableMdtSrv		objTaxMdtSrv;

	private Obj_ITaxableSrv_MapList	objSrvMapList;

	private ApplicationContext		appCtxt;

	@Override
	public void setApplicationContext(ApplicationContext ctxt) throws BeansException
	{
		if (ctxt != null)
		{
			appCtxt = ctxt;

		}

	}

	public void Initialize() throws EX_General
	{
		if (objTaxMdtSrv != null && objSrvMapList == null)
		{
			this.objSrvMapList = objTaxMdtSrv.getObjITaxableConvSrvList();
		}
	}

	public ITaxable getTaxableObjfromObject(Object pojo) throws EX_General
	{
		ITaxable taxableObj = null;
		String convSrvName = null;
		if (appCtxt != null)
		{
			if (this.objSrvMapList == null)
			{
				Initialize();
			}

			/**
			 * Get Service Name by Object
			 */
			if (pojo != null)
			{
				convSrvName = objTaxMdtSrv.getITaxableConvSrvforObject(pojo);
				if (convSrvName != null && appCtxt != null)
				{
					// Instantiate the Conversion Service Bean
					IObjITaxableConvSrv convSrv = (IObjITaxableConvSrv) appCtxt.getBean(convSrvName);
					if (convSrv != null)
					{
						taxableObj = convSrv.getTaxableObjfromObject(pojo);
					}
				}
			}
		}

		return taxableObj;
	}

}
