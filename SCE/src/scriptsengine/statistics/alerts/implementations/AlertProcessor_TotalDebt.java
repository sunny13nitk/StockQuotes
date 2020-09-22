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

@Service("AlertProcessor_TotalDebt")
public class AlertProcessor_TotalDebt implements IAlertAware
{
	@Autowired
	private MessagesFormatter	msgFormatter;
	private final double		debtDeltaTrigger		= 20;
	private final double		debtDeltaPenuTrigger	= 30;
	private final int			timePeriod			= 2;

	/**
	 * 1. If Delta is increasing by over 20% in last 3 years
	 * 2.If the debt delta has increased by over 30% in last year
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

					if (attrContainer.getDeltaFigures().get(size - 1).getFigure() >= debtDeltaPenuTrigger)
					{
						alertTriggered2 = true;
						penDebt = attrContainer.getDeltaFigures().get(size - 1).getFigure();
					}

					for ( int x = (size - 1); x >= sizeto; x-- )
					{
						if (attrContainer.getDeltaFigures().get(x).getFigure() >= debtDeltaTrigger)
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
						GeneralMessage msgChgErr = new GeneralMessage("AL_TOTALDEBT", new Object[]
						{ debtDeltaTrigger, timePeriod
						});
						alertMsgs.add(new TY_Alert(SCEenums.alertMode.AGAINST, msgFormatter.generate_message_snippet(msgChgErr)));
					}

					if (alertTriggered2 == true)
					{
						/** Alert Messages **/
						// AL_NETTSALES = Nett Sales has been falling for more than {0}% consecutively for past
						// {1} years!!
						GeneralMessage msgChgErr = new GeneralMessage("AL_TOTALDEBTPENU", new Object[]
						{ penDebt
						});
						alertMsgs.add(new TY_Alert(SCEenums.alertMode.AGAINST, msgFormatter.generate_message_snippet(msgChgErr)));
					}

				}
			}
		}
		return alertMsgs;
	}

}
