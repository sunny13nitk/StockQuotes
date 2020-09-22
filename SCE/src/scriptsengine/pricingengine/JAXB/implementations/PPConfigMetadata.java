package scriptsengine.pricingengine.JAXB.implementations;

import java.util.NoSuchElementException;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import modelframework.implementations.MessagesFormatter;
import scriptsengine.pricingengine.JAXB.definitions.PathsJAXBPP;
import scriptsengine.pricingengine.JAXB.definitions.PricesProjectionMetadata;
import scriptsengine.pricingengine.JAXB.interfaces.IPPConfigMetadata;
import scriptsengine.uploadengine.exceptions.EX_General;

@Service
public class PPConfigMetadata implements IPPConfigMetadata, ApplicationContextAware
{

	@Autowired
	private static MessagesFormatter	msgFormatter;
	private ApplicationContext		appCtxt;
	private PricesProjectionMetadata	ppMdt;
	private String					XmlPath;

	@Override
	public void setApplicationContext(ApplicationContext ctxt) throws BeansException
	{
		this.appCtxt = ctxt;
		if (appCtxt != null)
		{
			PathsJAXBPP pathsPPMdt = ctxt.getBean(PathsJAXBPP.class);
			if (pathsPPMdt != null)
			{
				this.XmlPath = pathsPPMdt.getJaxPath_PP_xml();

			}
		}

	}

	@Override
	public String getDefaultPricingBeanName() throws EX_General
	{
		String defBeanName = null;

		if (this.ppMdt == null)
		{
			this.getPPMetadata();
		}

		if (this.ppMdt != null)
		{
			try
			{
				defBeanName = this.ppMdt.getPricingFactorsConfig().stream().filter(x -> x.isBase() == true).findFirst().get().getBeanName();
			}
			catch (NoSuchElementException e)
			{
				EX_General msgChgErr = new EX_General("ERR_NODEF_PPSRV", null);
				msgFormatter.generate_message_snippet(msgChgErr);
				throw msgChgErr;

			}
		}

		return defBeanName;
	}

	@Override
	public PricesProjectionMetadata getPPMetadata() throws EX_General
	{
		if (ppMdt == null)
		{
			PriceProjectionConfig_JAXB ppUnmarshal = new PriceProjectionConfig_JAXB();
			try
			{
				this.ppMdt = (PricesProjectionMetadata) ppUnmarshal.Load_XML_to_Objects(this.getXmlPath()).get(0);
			}
			catch (Exception e)
			{
				EX_General msgChgErr = new EX_General("UPLOAD_SRV_ERR", null);
				msgFormatter.generate_message_snippet(msgChgErr);
				throw msgChgErr;
			}
		}

		return ppMdt;
	}

	/**
	 * @return the xmlPath
	 */
	public String getXmlPath()
	{
		return XmlPath;
	}

	@Override
	public String getProjectionFactorBeanName(String projectionFactorName) throws EX_General
	{
		String pfBeanName = null;
		if (projectionFactorName != null)
		{
			if (this.ppMdt == null)
			{
				this.getPPMetadata();
			}

			if (this.ppMdt != null)
			{
				try
				{
					pfBeanName = this.ppMdt.getPricingFactorsConfig().stream().filter(x -> x.getPpFactor().equals(projectionFactorName))
					          .findFirst().get().getBeanName();
				}
				catch (NoSuchElementException e)
				{
					EX_General msgChgErr = new EX_General("ERR_NOPRF_PPSRV", new Object[]
					{ projectionFactorName
					});
					msgFormatter.generate_message_snippet(msgChgErr);
					throw msgChgErr;

				}
			}
		}

		return pfBeanName;
	}

}
