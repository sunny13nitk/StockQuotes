/**
 * 
 */
package modelframework.events.declarations;

import org.springframework.context.ApplicationEvent;

/**
 * Logon event - It is triggered when the user is successfully authenticated for Login Triggers following
 * Actions - to be handled in Event Listener
 */

public class EV_Logon extends ApplicationEvent
{

	/**
	 * @param source
	 */
	public EV_Logon(Object source)
	{
		super(source);

	}

	@Override
	public String toString()
	{
		return "Logon";
	}

}
