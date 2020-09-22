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

@Service("AlertProcessor_NPM")
public class AlertProcessor_NPM implements IAlertAware
{
	@Autowired
	private MessagesFormatter	msgFormatter;
	private final double		debtrsrvDeltaTrigger	= -20;
	private final double		debtDeltaPenuTrigger	= -30;
	private final int			timePeriod			= 2;

	/**
	 * 1. If Delta is decreasing by over 20% in last 2 years
	 * 2.If the ratio delta has decreased by over 30% in last year
	 * 
	 */
	@Override
	public ArrayList<TY_Alert> processAlertsforAttribContainer(TY_Attr_Container attrContainer) throws EX_General
	{
		ArrayList<TY_Alert> alertMsgs = new ArrayList<TY_Alert>();
		boolean alertTriggered = false;
		boolean alertTriggered2 = false;
		double penDebt = 0;
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

					if (attrContainer.getDeltaFigures().get(size - 1).getFigure() <= debtDeltaPenuTrigger)
					{
						alertTriggered2 = true;
						penDebt = attrContainer.getDeltaFigures().get(size - 1).getFigure();
					}

					for ( int x = (size - 1); x >= sizeto; x-- )
					{
						if (attrContainer.getDeltaFigures().get(x).getFigure() <= debtrsrvDeltaTrigger)
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

						GeneralMessage msgChgErr = new GeneralMessage("AL_NPMTREND", new Object[]
						{ timePeriod, attrContainer.getDeltaFigures().get(size - 1).getFigure(),
						          attrContainer.getDeltaFigures().get(size - 2).getFigure()
						});
						alertMsgs.add(new TY_Alert(SCEenums.alertMode.AGAINST, msgFormatter.generate_message_snippet(msgChgErr)));
					}

					if (alertTriggered2 == true)
					{
						/** Alert Messages **/
						GeneralMessage msgChgErr = new GeneralMessage("AL_NPMCURR", new Object[]
						{ penDebt, debtDeltaPenuTrigger
						});
						alertMsgs.add(new TY_Alert(SCEenums.alertMode.AGAINST, msgFormatter.generate_message_snippet(msgChgErr)));
					}

				}
			}
		}
		return alertMsgs;
	}
}
