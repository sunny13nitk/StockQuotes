package scriptsengine.reportsengine.JAXB.implementations;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import modelframework.implementations.MessagesFormatter;
import scriptsengine.reportsengine.JAXB.definitions.PathsJAXBRE;
import scriptsengine.reportsengine.JAXB.definitions.RepSrv_XLSSrv_MapList;
import scriptsengine.reportsengine.JAXB.interfaces.IRepSrvConfigMetadata;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * Report Service Config Metadata Service
 *
 */

@Service
public class RepSrvConfigMetadata implements IRepSrvConfigMetadata, ApplicationContextAware
{

	@Autowired
	private static MessagesFormatter	msgFormatter;
	private ApplicationContext		appCtxt;
	private RepSrv_XLSSrv_MapList		srvMappingList;
	private String					XmlPath;

	/**
	 * @return the msgFormatter
	 */
	public static MessagesFormatter getMsgFormatter()
	{
		return msgFormatter;
	}

	/**
	 * @param msgFormatter
	 *             the msgFormatter to set
	 */
	public static void setMsgFormatter(MessagesFormatter msgFormatter)
	{
		RepSrvConfigMetadata.msgFormatter = msgFormatter;
	}

	/**
	 * @return the srvMappingList
	 */
	public RepSrv_XLSSrv_MapList getSrvMappingList()
	{
		return srvMappingList;
	}

	/**
	 * @param srvMappingList
	 *             the srvMappingList to set
	 */
	public void setSrvMappingList(RepSrv_XLSSrv_MapList srvMappingList)
	{
		this.srvMappingList = srvMappingList;
	}

	/**
	 * @return the xmlPath
	 */
	public String getXmlPath()
	{
		return XmlPath;
	}

	/**
	 * @param xmlPath
	 *             the xmlPath to set
	 */
	public void setXmlPath(String xmlPath)
	{
		XmlPath = xmlPath;
	}

	@Override
	public RepSrv_XLSSrv_MapList getRepSrvMetadata() throws EX_General
	{
		if (srvMappingList == null)
		{
			RepXLS_Srv_MappingConfig_JAXB reUnmarshal = new RepXLS_Srv_MappingConfig_JAXB();
			try
			{
				this.srvMappingList = (RepSrv_XLSSrv_MapList) reUnmarshal.Load_XML_to_Objects(this.getXmlPath()).get(0);
			}
			catch (Exception e)
			{
				EX_General msgChgErr = new EX_General("RE_SRV_ERR", null);
				msgFormatter.generate_message_snippet(msgChgErr);
				throw msgChgErr;
			}
		}

		return srvMappingList;
	}

	@Override
	public String getRepSrvforSrvBeanName(String srvBeanName) throws EX_General
	{
		String repSrvBeanName = null;
		if (srvBeanName != null)
		{
			if (this.srvMappingList == null)
			{
				getRepSrvMetadata();
			}
			if (this.srvMappingList != null)
			{
				if (this.srvMappingList.getSrvMappings() != null)
				{
					if (this.srvMappingList.getSrvMappings().size() > 0)
					{
						try
						{
							repSrvBeanName = srvMappingList.getSrvMappings().stream().filter(x -> x.getRepSrvName().equals(srvBeanName))
							          .findFirst().get().getXLSSrvName();
						}
						catch (Exception e)
						{
							EX_General msgChgErr = new EX_General("ERR_XLS_BEANNOTFOUND", new Object[]
							{ srvBeanName
							});
							msgFormatter.generate_message_snippet(msgChgErr);
							throw msgChgErr;
						}
					}
				}
			}
		}
		return repSrvBeanName;

	}

	@Override
	public void setApplicationContext(ApplicationContext ctxt) throws BeansException
	{
		this.appCtxt = ctxt;
		if (appCtxt != null)
		{
			PathsJAXBRE pathsREMdt = ctxt.getBean(PathsJAXBRE.class);
			if (pathsREMdt != null)
			{
				this.XmlPath = pathsREMdt.getJaxPath_RE_xml();

			}
		}

	}

}
