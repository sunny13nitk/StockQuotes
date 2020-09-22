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

@Service("AlertProcessor_CapexBwRatio")
public class AlertProcessor_CapexBwRatio implements IAlertAware
{
	@Autowired
	private MessagesFormatter	msgFormatter;
	private final double		capexBWDeltaTrigger	= -20;
	private final int			timePeriod		= 2;

	/**
	 * 1. If Delta is deccreasing by over 20% in last 2 years
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
						if (attrContainer.getDeltaFigures().get(x).getFigure() <= capexBWDeltaTrigger)
						{
							alertTriggered = true;
						}
						else
						{

							alertTriggered = false;
						}
					}

					if (alertTriggered == true)
					{
						/** Alert Messages **/

						GeneralMessage msgChgErr = new GeneralMessage("AL_CAPEXBWDEC", new Object[]
						{ (capexBWDeltaTrigger * -1), timePeriod
						});
						alertMsgs.add(new TY_Alert(SCEenums.alertMode.AGAINST, msgFormatter.generate_message_snippet(msgChgErr)));
					}

				}
			}
		}
		return alertMsgs;
	}

}
