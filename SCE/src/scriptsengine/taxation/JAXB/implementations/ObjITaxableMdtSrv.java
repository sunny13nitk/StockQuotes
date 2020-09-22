package scriptsengine.taxation.JAXB.implementations;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import modelframework.implementations.MessagesFormatter;
import scriptsengine.taxation.JAXB.definitions.Obj_ITaxableSrv_MapList;
import scriptsengine.taxation.JAXB.definitions.PathsJAXBTE;
import scriptsengine.taxation.JAXB.interfaces.IObjITaxableMdtSrv;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * SingleTon context Scope Service to fetch Object ITaxable Conversion Service Metadata
 */
@Service
public class ObjITaxableMdtSrv implements IObjITaxableMdtSrv, ApplicationContextAware
{

	@Autowired
	private static MessagesFormatter	msgFormatter;
	private ApplicationContext		appCtxt;
	private Obj_ITaxableSrv_MapList	objSrvMappingList;
	private String					XmlPath;

	public static MessagesFormatter getMsgFormatter()
	{
		return msgFormatter;
	}

	public static void setMsgFormatter(MessagesFormatter msgFormatter)
	{
		ObjITaxableMdtSrv.msgFormatter = msgFormatter;
	}

	public Obj_ITaxableSrv_MapList getObjSrvMappingList()
	{
		return objSrvMappingList;
	}

	public void setObjSrvMappingList(Obj_ITaxableSrv_MapList objSrvMappingList)
	{
		this.objSrvMappingList = objSrvMappingList;
	}

	public String getXmlPath()
	{
		return XmlPath;
	}

	public void setXmlPath(String xmlPath)
	{
		XmlPath = xmlPath;
	}

	/**
	 * ------------------------- Get the ObjName Service Mapping List ------------------
	 * 
	 * @return
	 * @throws EX_General
	 */
	@Override
	public Obj_ITaxableSrv_MapList getObjITaxableConvSrvList() throws EX_General
	{
		if (objSrvMappingList == null)
		{
			Obj_ITaxableSrv_MappingConfig_JAXB reUnmarshal = new Obj_ITaxableSrv_MappingConfig_JAXB();
			try
			{
				this.objSrvMappingList = (Obj_ITaxableSrv_MapList) reUnmarshal.Load_XML_to_Objects(this.getXmlPath()).get(0);
			}
			catch (Exception e)
			{
				EX_General msgChgErr = new EX_General("TE_SRV_ERR", null);
				msgFormatter.generate_message_snippet(msgChgErr);
				throw msgChgErr;
			}
		}

		return objSrvMappingList;
	}

	/**
	 * ------------------- Get the ITaxable Conversion Service for Current Object Instance --------
	 * 
	 * @param obj
	 * @return
	 * @throws EX_General
	 */
	@Override
	public String getITaxableConvSrvforObject(Object obj) throws EX_General
	{
		String convSrvBeanName = null;
		if (obj != null)
		{
			String objName = obj.getClass().getName();
			if (objName != null)
			{
				if (this.objSrvMappingList == null)
				{
					getObjITaxableConvSrvList();
				}
				if (this.objSrvMappingList != null)
				{
					if (this.objSrvMappingList.getObjTaxSrvMappings() != null)
					{
						if (this.objSrvMappingList.getObjTaxSrvMappings().size() > 0)
						{
							try
							{
								convSrvBeanName = objSrvMappingList.getObjTaxSrvMappings().stream()
								          .filter(x -> x.getObjName().equals(objName)).findFirst().get().getITaxableSrvName();
							}
							catch (Exception e)
							{
								EX_General msgChgErr = new EX_General("ERR_TAXCONV_BEANNOTFOUND", new Object[]
								{ objName
								});
								msgFormatter.generate_message_snippet(msgChgErr);
								throw msgChgErr;
							}
						}
					}
				}
			}
		}
		return convSrvBeanName;
	}

	/**
	 * App context Initialization to set Tax Engine JAXB Path to read through XML
	 */
	@Override
	public void setApplicationContext(ApplicationContext ctxt) throws BeansException
	{
		this.appCtxt = ctxt;
		if (appCtxt != null)
		{
			PathsJAXBTE pathsTEMdt = ctxt.getBean(PathsJAXBTE.class);
			if (pathsTEMdt != null)
			{
				this.XmlPath = pathsTEMdt.getJaxPath_TE_xml();

			}
		}

	}

}
