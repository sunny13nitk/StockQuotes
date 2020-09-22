package scriptsengine.uploadengine.managers;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import modelframework.implementations.MessagesFormatter;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.services.interfaces.IScripDataService;

/**
 * Scrip Data Manager - Main Manager Class to provide ScipDataService bean instance that
 * futher invokes Scrip Creation and Updation in the system
 *
 */
@Service("ScripDataManager")
public class ScripDataManager implements ApplicationContextAware
{

	private static ApplicationContext	Context;

	@Autowired
	private static MessagesFormatter	msgFormatter;

	public static IScripDataService getScripDataService() throws EX_General
	{
		IScripDataService scripDataService = null;

		if (Context != null)
		{
			try
			{
				scripDataService = Context.getBean(IScripDataService.class);
				if (scripDataService != null)
				{
					if (scripDataService.getWBMetaData() == null)
					{
						/**
						 * Initialize Scrip data Service WB Metadata since it has not been initialized
						 * in
						 * context
						 */

						scripDataService.initialize();
					}
				}
			}
			catch (Exception e)
			{
				EX_General msgChgErr = new EX_General("UPLOAD_SRV_ERR", null);
				msgFormatter.generate_message_snippet(msgChgErr);
				throw msgChgErr;
			}
		}

		return scripDataService;
	}

	@Override
	public void setApplicationContext(ApplicationContext ctxt) throws BeansException
	{
		Context = ctxt;

	}

}
