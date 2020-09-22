package modelframework.managers;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import modelframework.definitions.BussFxn;
import modelframework.definitions.BussFxnList;
import modelframework.implementations.GeneralMessage;
import modelframework.implementations.MessagesFormatter;
import modelframework.interfaces.IBussFxn;

/**
 * Business Function Manager
 * Processes the requested business functions and triggers their load via call to aspect
 */
@Service("BussFxnManager")
public class BussFxnManager implements ApplicationContextAware
{

	@Autowired
	private MessagesFormatter	msgFormatter;
	private ApplicationContext	context;

	/**
	 * load the Business Functions as per the request
	 * 
	 * @param reqBussFxns
	 *             - requested Business Functions
	 */
	public void loadBusinessFunctions(BussFxnList reqBussFxns)
	{
		@SuppressWarnings("unused")
		String msg = null;
		if (reqBussFxns != null && context != null)
		{
			for ( BussFxn bussFxn : reqBussFxns.getBussFxns() )
			{
				if (bussFxn != null)
				{
					// Bean Name specified
					if (!StringUtils.isEmpty(bussFxn.getBeanName()))
					{
						/**
						 * Try to instantiate the bean from Application Context
						 */
						IBussFxn bfBean = (IBussFxn) context.getBean(bussFxn.getBeanName());
						if (bfBean != null)
						{
							try
							{
								bfBean.activateBussFxn(); // Handled via Aspect
								// Populate Message in Message Container
								GeneralMessage msgReset = new GeneralMessage("BFX_LOADED", new Object[]
								{ bussFxn.getBussFxnName(), bussFxn.getBeanName()
								});
								msg = msgFormatter.generate_message_snippet(msgReset);
							}
							catch (Exception e)
							{
								GeneralMessage msgReset = new GeneralMessage("BFX_INST_FAILED", new Object[]
								{ bussFxn.getBeanName(), e.getMessage()
								});
								msg = msgFormatter.generate_message_snippet(msgReset);

							}

						}
						else
						{
							// Populate Message in Message Container
							GeneralMessage msgReset = new GeneralMessage("BFX_LOAD_FAILED", new Object[]
							{ bussFxn.getBeanName(), bussFxn.getBussFxnName()
							});
							msg = msgFormatter.generate_message_snippet(msgReset);
						}
					}
				}
			}
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext ctxt) throws BeansException
	{
		if (ctxt != null)
		{
			this.context = ctxt;
		}
	}

}
