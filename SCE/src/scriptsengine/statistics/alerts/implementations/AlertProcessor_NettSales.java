package scriptsengine.statistics.alerts.implementations;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import modelframework.implementations.GeneralMessage;
import modelframework.implementations.MessagesFormatter;
import scriptsengine.enums.SCEenums;
import scriptsengine.reportsengine.repDS.definitions.TY_Attr_Container;
import scriptsengine.statistics.alerts.definitions.TY_Alert;
import scriptsengine.statistics.alerts.interfaces.IAlertAware;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Alert Processor For Nett Sales attribute - App context Specific Implementation
 *
 */
@Service("AlertProcessor_NettSales")
public class AlertProcessor_NettSales implements IAlertAware
{
	@Autowired
	private MessagesFormatter	msgFormatter;
	private final double		salesDeltaTrigger	= -10;
	private final int			timePeriod		= 3;

	/**
	 * If Delta is continously decreasing more than 10%. For last 3 years i.e sales are decreasing continously for more
	 * than 10 %
	 * 
	 */
	@Override
	public ArrayList<TY_Alert> processAlertsforAttribContainer(TY_Attr_Container attrContainer) throws EX_General
	{
		ArrayList<TY_Alert> alertMsgs = new ArrayList<TY_Alert>();
		boolean alertTriggered = false;
		if (attrContainer != null)
		{
			if (attrContainer.getDeltaFigures() != null)
			{
				/**
				 * At Least 3 years data needed for Comparison
				 */
				if (attrContainer.getDeltaFigures().size() >= timePeriod)
				{
					int size = attrContainer.getDeltaFigures().size(); // 4
					int sizeto = size - timePeriod; // 4,3,2

					for ( int x = (size - 1); x >= sizeto; x-- )
					{
						if (attrContainer.getDeltaFigures().get(x).getFigure() <= salesDeltaTrigger)
						{
							alertTriggered = true;
						}
						else
						{
							// Trigger False if for any of the years delta in last 3 years sales not fell more
							// than allowance limit - So alert is not finally triggerred
							alertTriggered = false;
						}
					}

					if (alertTriggered == true)
					{
						/** Alert Messages **/
						// AL_NETTSALES = Nett Sales has been falling for more than {0}% consecutively for past
						// {1} years!!
						GeneralMessage msgChgErr = new GeneralMessage("AL_NETTSALES", new Object[]
						{ (salesDeltaTrigger * -1), timePeriod
						});
						alertMsgs.add(new TY_Alert(SCEenums.alertMode.AGAINST, msgFormatter.generate_message_snippet(msgChgErr)));
					}

				}
			}
		}
		return alertMsgs;
	}

}
