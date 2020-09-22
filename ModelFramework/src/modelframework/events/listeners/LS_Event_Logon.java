/**
 * 
 */
package modelframework.events.listeners;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import modelframework.definitions.BussFxnList;
import modelframework.events.declarations.EV_Logon;
import modelframework.exposed.FrameworkManager;
import modelframework.implementations.GeneralMessage;
import modelframework.managers.BussFxnManager;
import modelframework.usermanager.services.LogonService;

/**
 * Event Handler for Logon event
 *
 */
@Component
public class LS_Event_Logon implements ApplicationListener<EV_Logon>
{
	@Autowired
	private FrameworkManager	FrameworkManager;

	@Autowired
	private BussFxnManager	bussFxnManager;

	@Autowired
	private BussFxnList		bussFxns;

	/**
	 * Handle the Custom events
	 */
	@Override
	public void onApplicationEvent(EV_Logon event)
	{
		switch (event.toString())
		{
		case "Logon":
			if (event.getSource() instanceof LogonService)
			{
				LogonService logonService = (LogonService) event.getSource();
				if (logonService != null)
				{

					processlogon(logonService);
				}
			}
		}
	}

	/**
	 * Process the Logon Event Handling
	 * 
	 * @param logonService
	 * @throws Exception
	 */
	private void processlogon(LogonService logonService)
	{
		if (logonService.getUsername() != null && logonService.IS_Authenticated())
		{
			// User Authenticated
			// Load User Specific Permissions and authorization in User Manager - TBI <to be Implemeneted>

			// Initialize Framework Manager
			try
			{
				FrameworkManager.Initialize_Framework();
				// Log User Logon Message

				if (FrameworkManager.getMessageFormatter() != null)
				{

					DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
					Calendar cal = Calendar.getInstance();
					String date_time = (dateFormat.format(cal.getTime()));
					// Prepare message as below
					// SUCC_LOGON=User {0} Logged in system at {1}. Framework Initalized successfully. Model Loaded
					// {2}
					FrameworkManager.getMessageFormatter().setMessage_snippet(new GeneralMessage("SUCC_LOGON", new Object[]
					{ logonService.getUsername(), date_time, FrameworkManager.getConstants().getModel_default()
					}));
					FrameworkManager.getMessageFormatter().generate_formatted_message();

				}

				// Initialize business Functions if any defined
				if (bussFxns != null)
				{
					/**
					 * Delegate Business Functions to Business functions Manager and the corresponding BF beans
					 * will be initialized in the Context
					 */
					if (this.bussFxnManager != null)
					{
						bussFxnManager.loadBusinessFunctions(bussFxns);
					}

				}

			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
