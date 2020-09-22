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

@Service("AlertProcessor_NettProfit")
public class AlertProcessor_NettProfit implements IAlertAware
{

	@Autowired
	private MessagesFormatter	msgFormatter;
	private final int			timePeriod	= 2;

	/**
	 * 1. If Delta is negative for the latest occurance i.e profit declined for latest year 2. If delta trend is
	 * negative for past 2 or more intervals. Alarming
	 */
	@Override
	public ArrayList<TY_Alert> processAlertsforAttribContainer(TY_Attr_Container attrContainer) throws EX_General
	{
		ArrayList<TY_Alert> alertMsgs = new ArrayList<TY_Alert>();
		boolean alert1Triggered = false;
		boolean alert2Triggered = false;
		boolean alertLterm = false;

		if (attrContainer != null)
		{
			if (attrContainer.getDeltaFigures() != null)
			{
				/**
				 * At Least 3 years data needed for Comparison
				 */
				int size = attrContainer.getDeltaFigures().size();
				if (attrContainer.getDeltaFigures().size() >= timePeriod)
				{

					int sizeto = size - timePeriod;

					for ( int x = (size - 1); x >= sizeto; x-- )
					{

						if (attrContainer.getDeltaFigures().get(x).getFigure() < 0)
						{
							if (alert1Triggered == true)
							{
								alert2Triggered = true;
							}
							alert1Triggered = true;
						}
						else
						{
							// Trigger False - So alert is not finally triggerred
							alert1Triggered = false;
						}
					}

					if (alert1Triggered == true)
					{
						/** Alert Messages **/

						GeneralMessage msgChgErr = new GeneralMessage("AL_NETTPRCURR", new Object[]
						{ attrContainer.getDeltaFigures().get(size - 1).getFigure()
						});
						alertMsgs.add(new TY_Alert(SCEenums.alertMode.AGAINST, msgFormatter.generate_message_snippet(msgChgErr)));
					}

					if (alert2Triggered == true)
					{
						/** Alert Messages **/

						GeneralMessage msgChgErr = new GeneralMessage("AL_NETTPRTREND", new Object[]
						{ attrContainer.getDeltaFigures().get(size - 1).getFigure(), attrContainer.getDeltaFigures().get(size - 2).getFigure()
						});
						alertMsgs.add(new TY_Alert(SCEenums.alertMode.AGAINST, msgFormatter.generate_message_snippet(msgChgErr)));
						;
					}

				}
			}
		}
		return alertMsgs;
	}

}
