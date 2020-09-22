package scriptsengine.statistics.JAXB.implementations;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import modelframework.implementations.MessagesFormatter;
import scriptsengine.statistics.JAXB.definitions.PathsJAXBSS;
import scriptsengine.statistics.JAXB.definitions.StatsAttrDetails;
import scriptsengine.statistics.JAXB.definitions.StatsAttrList;
import scriptsengine.statistics.JAXB.interfaces.IStatsSrvConfigMetadata;
import scriptsengine.uploadengine.exceptions.EX_General;

@Service
public class StatsSrvConfigMetadata implements IStatsSrvConfigMetadata, ApplicationContextAware
{

	@Autowired
	private MessagesFormatter	msgFormatter;
	private ApplicationContext	appCtxt;
	private StatsAttrList		statAttrList;
	private String				XmlPath;

	@Override
	public void setApplicationContext(ApplicationContext ctxt) throws BeansException
	{
		this.appCtxt = ctxt;
		if (appCtxt != null)
		{
			PathsJAXBSS pathsSSMdt = ctxt.getBean(PathsJAXBSS.class);
			if (pathsSSMdt != null)
			{
				this.XmlPath = pathsSSMdt.getJaxPath_SS_xml();

			}
		}

	}

	/**
	 * @return the xmlPath
	 */
	public String getXmlPath()
	{
		return XmlPath;
	}

	@Override
	public StatsAttrList getStatAtrributesList() throws EX_General
	{
		if (statAttrList == null)
		{
			SSConfig_JAXB ssUnmarshal = new SSConfig_JAXB();
			try
			{
				this.statAttrList = (StatsAttrList) ssUnmarshal.Load_XML_to_Objects(this.getXmlPath()).get(0);
			}
			catch (Exception e)
			{
				EX_General msgChgErr = new EX_General("SS_SRV_ERR", null);
				msgFormatter.generate_message_snippet(msgChgErr);
				throw msgChgErr;
			}
		}

		return statAttrList;
	}

	@Override
	public String getFiguresSrvBeanName(String attrName) throws EX_General
	{
		String BeanName = null;
		if (attrName != null)
		{
			if (this.statAttrList == null)
			{
				this.getStatAtrributesList();
			}

			if (this.statAttrList != null)
			{
				try
				{
					BeanName = this.statAttrList.getStatAttrs().stream().filter(x -> x.getAttrName().equals(attrName)).findFirst().get()
					          .getFiguresSrvBean();
				}
				catch (NoSuchElementException e)
				{
					EX_General msgChgErr = new EX_General("ERR_NOFIG_SSSRV ", new Object[]
					{ attrName
					});
					msgFormatter.generate_message_snippet(msgChgErr);
					throw msgChgErr;

				}
			}
		}

		return BeanName;
	}

	@Override
	public String getAlertSrvBeanName(String attrName) throws EX_General
	{
		String BeanName = null;
		if (attrName != null)
		{
			if (this.statAttrList == null)
			{
				this.getStatAtrributesList();
			}

			if (this.statAttrList != null)
			{
				try
				{
					BeanName = this.statAttrList.getStatAttrs().stream().filter(x -> x.getAttrName().equals(attrName)).findFirst().get()
					          .getAlertDetSrvBean();
				}
				catch (NoSuchElementException e)
				{
					EX_General msgChgErr = new EX_General("ERR_NOALERT_SSSRV ", new Object[]
					{ attrName
					});
					msgFormatter.generate_message_snippet(msgChgErr);
					throw msgChgErr;

				}
			}
		}

		return BeanName;
	}

	@Override
	public StatsAttrDetails getattrDetailsbyAttrName(String attrName) throws EX_General
	{
		StatsAttrDetails attrMdt = null;

		if (attrName != null)
		{
			if (this.statAttrList == null)
			{
				this.getStatAtrributesList();
			}

			if (this.statAttrList != null)
			{
				try
				{
					attrMdt = this.statAttrList.getStatAttrs().stream().filter(x -> x.getAttrName().equals(attrName)).findFirst().get();
				}
				catch (NoSuchElementException e)
				{
					EX_General msgChgErr = new EX_General("ERR_INVALID_SSATTR", new Object[]
					{ attrName
					});
					msgFormatter.generate_message_snippet(msgChgErr);
					throw msgChgErr;

				}
			}
		}

		return attrMdt;
	}

	/**
	 * Get Market Share Analysis Relevant Attributes
	 */
	@Override
	public ArrayList<String> getmShareRelevant_AttrNames() throws EX_General
	{
		ArrayList<String> mShareAttrs = new ArrayList<String>();
		ArrayList<StatsAttrDetails> mShareAttrDetals = new ArrayList<StatsAttrDetails>();

		if (this.statAttrList == null)
		{
			this.getStatAtrributesList();
		}

		if (this.statAttrList != null)
		{
			mShareAttrDetals = this.statAttrList.getStatAttrs().stream().filter(x -> x.ismShareAnalysisON() == true)
			          .collect(Collectors.toCollection(ArrayList::new));

			if (mShareAttrDetals.size() > 0)
			{
				mShareAttrs = mShareAttrDetals.stream().map(StatsAttrDetails::getAttrName).collect(Collectors.toCollection(ArrayList::new));
			}
		}

		return mShareAttrs;
	}

}
